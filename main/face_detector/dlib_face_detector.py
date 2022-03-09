from typing import List

from main.face_detector.face_detector import FaceDetector, FaceNotFoundError
from main.entities.face_image import FaceImage
from main.entities.image import Image
import face_recognition
import numpy as np

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
        return 1 - face_recognition.face_distance([np.array(feature1)], np.array(feature2))[0]
    
    def get_all_face_images_from_image(self, image: Image) -> List[FaceImage]:
        super().get_all_face_images_from_image(image)
        encodings = face_recognition.face_encodings(image.get_array())
        encoding_to_face_image = lambda encoding: FaceImage(image, encoding)
        return list(map(encoding_to_face_image, encodings))