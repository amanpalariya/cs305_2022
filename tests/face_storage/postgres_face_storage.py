import pytest
from scipy.misc import face

from main.database_reader.postgres_database_reader import PostgresDatabaseReader
from main.entities.face_record import FaceRecord
from main.entities.image import Image
from main.entities.person import Person
from main.face_detector.dlib_face_detector import DlibFaceDetector
from main.face_detector.face_detector import FaceDetector
from main.face_storage.no_entry_with_given_id_error import NoEntryWithGivenIdError
from main.face_storage.postgres_face_storage import PostgresFaceStorage

from tests.config import DatabaseConfig

@pytest.fixture
def table_name():
    return 'test_faces'

@pytest.fixture
def selfie_image_filepath():
    return 'tests/resources/selfie.png'

@pytest.fixture
def person_name():
    return "Test"

@pytest.fixture
def selfie_image(selfie_image_filepath):
    return Image.from_filepath(selfie_image_filepath)

@pytest.fixture
def db_reader():
    return PostgresDatabaseReader(DatabaseConfig.Database, DatabaseConfig.Username, DatabaseConfig.Password)

@pytest.fixture
def face_storage(db_reader, table_name) -> PostgresFaceStorage:
    return PostgresFaceStorage(db_reader, table_name)

@pytest.fixture
def face_detector() -> FaceDetector:
    return DlibFaceDetector()

def test_add_new_face_record_and_get_match(face_storage: PostgresFaceStorage, face_detector: FaceDetector, selfie_image: Image, person_name: str):
    face_image = face_detector.create_face_image_from_image(selfie_image)
    person = Person(person_name)
    face_storage.add_new_face_record(FaceRecord(face_image, person))
    assert face_storage.get_top_k_matches(face_image, 1, 0, None)[0][1].getName()==person_name
    face_storage.close()

def test_get_person_by_id(face_storage: PostgresFaceStorage, face_detector: FaceDetector, selfie_image: Image, person_name: str):
    for id in range(0, 10):
        try:
            person = face_storage.get_person_by_id(id)
            assert person.getName() == person_name
        except NoEntryWithGivenIdError:
            assert True
    face_storage.close()