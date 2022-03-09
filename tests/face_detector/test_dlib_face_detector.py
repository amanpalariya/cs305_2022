from tkinter import image_names
from venv import create
import pytest
from scipy.misc import face
from main.entities.image import Image
from main.face_detector.dlib_face_detector import DlibFaceDetector
from main.face_detector.face_detector import FaceNotFoundError

@pytest.fixture
def kitten_image_filepath():
    return 'tests/resources/kitten.jpg'

@pytest.fixture
def kitten_image(kitten_image_filepath):
    return Image.from_filepath(kitten_image_filepath)

@pytest.fixture
def selfie_image_filepath():
    return 'tests/resources/selfie.png'

@pytest.fixture
def selfie_image(selfie_image_filepath):
    return Image.from_filepath(selfie_image_filepath)

def test_create_face_image(selfie_image):
    face_detector = DlibFaceDetector()
    face_image = face_detector.create_face_image_from_image(selfie_image)
    assert face_image is not None
    assert face_image.getImage() is not None
    assert face_image.getFeatures() is not None

def test_no_face_found(kitten_image):
    face_detector = DlibFaceDetector()
    with pytest.raises(FaceNotFoundError):
        face_detector.create_face_image_from_image(kitten_image)

def test_get_similarity():
    face_detector = DlibFaceDetector()
    assert face_detector.get_similarity_of_features([2, 2], [2, 2])==1

def test_get_all_face_images(selfie_image):
    face_detector = DlibFaceDetector()
    images = face_detector.get_all_face_images_from_image(selfie_image)
    assert len(images)==2