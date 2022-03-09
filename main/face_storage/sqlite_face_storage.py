from datetime import datetime
from typing import Callable, List, Tuple

from entities.image import Image
from entities.person import Person
from face_storage.face_storage import FaceStorage
from entities.face_record import FaceRecord
from entities.face_image import FaceImage
from database_reader.sql_database_reader import DatabaseRecord, SqlDatabaseReader
import os

from face_storage.no_entry_with_given_id_error import NoEntryWithGivenIdError

class Columns:
    Id = "id"
    Name = "name"
    Features = "features"
    Filepath = "filepath"


class SqliteFaceStorage(FaceStorage):
    __database_reader: SqlDatabaseReader
    __TABLE_NAME: str = "faces"
    __images_folder: str
    __image_format: str

    def __init__(self, sql_database_reader: SqlDatabaseReader, images_folder: str, image_format: str = 'jpg') -> None:
        super().__init__()
        self.__database_reader = sql_database_reader
        self.__images_folder = images_folder
        self.__image_format = image_format
        self.__create_folder_if_not_exists()
        self.__create_table_if_not_exists()

    def __create_folder_if_not_exists(self):
        try:
            os.mkdir(self.__images_folder)
        except FileExistsError:
            pass

    def __create_table_if_not_exists(self):
        self.__database_reader.execute(self.__get_create_table_if_not_exist_query())

    def __get_create_table_if_not_exist_query(self) -> str:
        return f"CREATE TABLE IF NOT EXISTS {self.__TABLE_NAME}({Columns.Id} INTEGER PRIMARY KEY AUTOINCREMENT, {Columns.Name} TEXT NOT NULL, {Columns.Features} TEXT NOT NULL, {Columns.Filepath} TEXT NOT NULL)"

    def __get_sql_string_literal(self, string: str) -> str:
        return f'"{string}"'

    def __convert_features_to_sql_literal(self, features: List[float]) -> str:
        space_separated_string_features = " ".join(map(str, features))
        return self.__get_sql_string_literal(space_separated_string_features)

    def __save_image_and_get_path(self, image: Image, person: Person) -> str:
        time_in_micros = int(datetime.now().timestamp()*1e6)
        filename = f"{person.getName()} {time_in_micros}.{self.__image_format}"
        filepath = f"{self.__images_folder}{os.sep}{filename}"
        abs_filepath = os.path.abspath(filepath)
        image.save(abs_filepath)
        return abs_filepath
    
    def __save_image_and_get_sql_filepath(self, image: Image, person: Person) -> str:
        path = self.__save_image_and_get_path(image, person)
        return self.__get_sql_string_literal(path)

    def __get_insert_face_record_query(self, face_record: FaceRecord) -> str:
        name_literal = self.__get_sql_string_literal(face_record.getPerson().getName())
        features_literal = self.__convert_features_to_sql_literal(face_record.getFaceImage().getFeatures())
        filepath_literal = self.__save_image_and_get_sql_filepath(face_record.getFaceImage().getImage(), face_record.getPerson())
        return f"INSERT INTO {self.__TABLE_NAME}({Columns.Name}, {Columns.Features}, {Columns.Filepath}) VALUES({name_literal}, {features_literal}, {filepath_literal})"

    def add_new_face_record(self, face_record: FaceRecord):
        super().add_new_face_record(face_record)
        query = self.__get_insert_face_record_query(face_record)
        self.__database_reader.execute(query)

    def __get_all_persons_query(self) -> str:
        return f"SELECT DISTINCT {Columns.Name} FROM {self.__TABLE_NAME}"

    def __get_all_persons(self) -> List[Person]:
        result = self.__database_reader.execute_select(self.__get_all_persons_query())
        record_to_person = lambda record: Person(record.get_value_by_column_name(Columns.Name))
        return list(map(record_to_person, result))
    
    def __get_all_features_of_a_person_query(self, person: Person) -> str:
        name_literal = self.__get_sql_string_literal(person.getName())
        return f"SELECT {Columns.Id} {Columns.Features} FROM {self.__TABLE_NAME} WHERE {Columns.Name} IS {name_literal}"

    def __get_all_features_of_a_person(self, person: Person) -> List[Tuple[float, List[float]]]:
        result = self.__database_reader.execute_select(self.__get_all_features_of_a_person_query(person))
        record_to_tuple = lambda record: (record.get_value_by_column_name(Columns.Id), list(map(float, record.get_value_by_column_name(Columns.Features).split(' '))))
        return list(map(record_to_tuple, result))
    
    def __get_similarity_with_person(self, feature: List[float], person: Person, get_similarity_of_features: Callable[[List[float], List[float]], float]) -> Tuple[float, float]:
        features = self.__get_all_features_of_a_person(person)
        feature_to_similarity_with_person = lambda f: (f[0], get_similarity_of_features(f[1], feature))
        return max(map(feature_to_similarity_with_person, features), key=lambda x: x[1])

    def get_top_k_matches(self, face_image: FaceImage, k: int, confidence: float, get_similarity_of_features: Callable[[List[float], List[float]], float]) -> List[Tuple[float, Person, float]]:
        super().get_top_k_matches(face_image, k, confidence, get_similarity_of_features)
        all_persons = self.__get_all_persons()
        similarity = []
        ids = []
        for person in all_persons:
            id, sim = self.__get_similarity_with_person(face_image.getFeatures(), person, get_similarity_of_features)
            similarity.append(sim)
            ids.append(id)
        top_k_matches = list(sorted(filter(lambda x: x[2]>=confidence, zip(id, all_persons, similarity)), key= lambda x: -x[2]))[:k] # Sort in order of decreasing similarity
        return top_k_matches

    def __get_person_by_id_query(self, id) -> str:
        return f"SELECT {Columns.Name} FROM {self.__TABLE_NAME} WHERE {Columns.Id}={id}"

    def get_person_by_id(self, id: int) -> Person:
        super().get_person_by_id(id)
        records = self.__database_reader.execute_select(self.__get_person_by_id_query(id))
        if len(records)>0:
            return Person(records[0].get_value_by_column_name(Columns.Name))
        else:
            raise NoEntryWithGivenIdError(id)