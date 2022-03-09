from datetime import datetime
from typing import Callable, List, Tuple

from main.entities.image import Image
from main.entities.person import Person
from main.face_storage.face_storage import FaceStorage
from main.entities.face_record import FaceRecord
from main.entities.face_image import FaceImage

from main.database_reader.postgres_database_reader import PostgresDatabaseReader
from main.face_storage.no_entry_with_given_id_error import NoEntryWithGivenIdError

class Columns:
    Id = "id"
    Name = "name"
    Features = "features"
    Similarity = "similarity"

class PostgresFaceStorage(FaceStorage):
    __TABLE_NAME: str
    __GET_DISTANCE_FUNCTION_NAME = "get_distance"

    def __init__(self, sql_database_reader: PostgresDatabaseReader, table_name: str = "faces") -> None:
        super().__init__()
        self.__database_reader = sql_database_reader
        self.__TABLE_NAME = table_name
        self.__create_table_if_not_exists()
        self.__create_or_replace_get_distance_function()

    def __get_create_table_if_not_exist_query(self) -> str:
        return f"CREATE TABLE IF NOT EXISTS {self.__TABLE_NAME}({Columns.Id} SERIAL, {Columns.Name} VARCHAR(50), {Columns.Features} FLOAT[])"

    def __create_table_if_not_exists(self):
        self.__database_reader.execute(self.__get_create_table_if_not_exist_query())
    
    def __get_create_or_replace_get_distance_function_query(self) -> str:
        return f"""CREATE OR REPLACE FUNCTION {self.__GET_DISTANCE_FUNCTION_NAME}(array1 FLOAT[], array2 FLOAT[])
                RETURNS FLOAT
                LANGUAGE plpgsql
                AS
                $$
                DECLARE
                    distance FLOAT;
                BEGIN
                    SELECT sqrt(pow(cube(array1[:64]) <-> cube(array2[:64]), 2) + pow(cube(array1[64:]) <-> cube(array2[64:]), 2))
                    INTO distance;
                    RETURN distance;
                END;
                $$"""
    
    def __create_or_replace_get_distance_function(self):
        self.__database_reader.execute(self.__get_create_or_replace_get_distance_function_query())

    def __get_sql_string_literal(self, string: str) -> str:
        return f"'{string}'"
    
    def __get_sql_float_array_literal(self, array: List[float]) -> str:
        return f"ARRAY[{','.join(map(str, array))}]"

    def __get_add_new_face_record_query(self, face_record: FaceRecord) -> str:
        name_literal = self.__get_sql_string_literal(face_record.getPerson().getName())
        features_literal = self.__get_sql_float_array_literal(face_record.getFaceImage().getFeatures())
        return f"INSERT INTO {self.__TABLE_NAME}({Columns.Name}, {Columns.Features}) VALUES({name_literal}, {features_literal})"
    
    def add_new_face_record(self, face_record: FaceRecord):
        super().add_new_face_record(face_record)
        self.__database_reader.execute(self.__get_add_new_face_record_query(face_record))
    
    def __get_top_k_matches_query(self, features: List[float], k: int, confidence: float, get_similarity_of_features: Callable[[List[float], List[float]], float]):
        features_literal = self.__get_sql_float_array_literal(features)
        return f"""
        SELECT MIN(z.{Columns.Id}) AS {Columns.Id}, z.{Columns.Name} AS {Columns.Name}, y.max_similarity AS {Columns.Similarity}
        FROM
            (
                SELECT x.{Columns.Name} as {Columns.Name}, MAX(x.{Columns.Similarity}) AS max_similarity
                FROM 
                (
                    SELECT {Columns.Name}, 1-{self.__GET_DISTANCE_FUNCTION_NAME}({Columns.Features}::FLOAT[], {features_literal}::FLOAT[]) AS {Columns.Similarity}
                    FROM {self.__TABLE_NAME}
                ) x
                WHERE x.{Columns.Similarity}>={confidence}
                GROUP BY x.{Columns.Name}
            ) y,
            (
                SELECT {Columns.Id}, {Columns.Name}, 1-{self.__GET_DISTANCE_FUNCTION_NAME}({Columns.Features}::FLOAT[], {features_literal}::FLOAT[]) AS {Columns.Similarity}
                FROM {self.__TABLE_NAME}
            ) z
        WHERE y.{Columns.Name}=z.{Columns.Name} AND y.max_similarity=z.{Columns.Similarity}
        GROUP BY (z.{Columns.Name}, y.max_similarity)
        ORDER BY y.max_similarity DESC
        LIMIT {k}
        """

    def get_top_k_matches(self, face_image: FaceImage, k: int, confidence: float, get_similarity_of_features: Callable[[List[float], List[float]], float]) -> List[Tuple[int, Person, float]]:
        super().get_top_k_matches(face_image, k, confidence, get_similarity_of_features)
        records = self.__database_reader.execute_select(self.__get_top_k_matches_query(face_image.getFeatures(), k, confidence, get_similarity_of_features))
        record_to_tuple = lambda record: (record.get_value_by_column_name(Columns.Id), Person(record.get_value_by_column_name(Columns.Name)), record.get_value_by_column_name(Columns.Similarity))
        return list(map(record_to_tuple, records))
        
    def __get_person_by_id_query(self, id) -> str:
        return f"SELECT {Columns.Name} FROM {self.__TABLE_NAME} WHERE {Columns.Id}={id}"

    def get_person_by_id(self, id: int) -> Person:
        super().get_person_by_id(id)
        records = self.__database_reader.execute_select(self.__get_person_by_id_query(id))
        if len(records)>0:
            return Person(records[0].get_value_by_column_name(Columns.Name))
        else:
            raise NoEntryWithGivenIdError(id)
        
    def close(self):
        super().close()
        self.__database_reader.close()