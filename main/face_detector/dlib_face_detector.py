from typing import List

from pynvim import encoding

from face_detector.face_detector import FaceDetector, FaceNotFoundError
from entities.face_image import FaceImage
from entities.image import Image
import face_recognition

class DlibFaceDetector(FaceDetector):
    def create_face_image_from_image(self, image: Image) -> FaceImage:
        super().create_face_image_from_image(image)
        encodings = face_recognition.face_encodings(image.get_array())
        if len(encodings)>0:
            return FaceImage(image, encodings[0])
        else:
            raise FaceNotFoundError
    
    def get_similarity_of_features(self, feature1: List[float], feature2: List[float]) -> float:
        super().get_similarity_of_features(feature1, feature2)
        return 1 - face_recognition.face_distance([feature1], feature2)[0]