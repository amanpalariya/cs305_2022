from abc import ABC, abstractmethod
from typing import List
from entities.face_image import FaceImage
from entities.image import Image

class FaceDetector(ABC):
    @abstractmethod
    def create_face_image_from_image(self, image: Image) -> FaceImage:
        pass

    @abstractmethod
    def get_similarity_of_features(self, feature1: List[float], feature2: List[float]) -> float:
        pass

class FaceNotFoundError(Exception):
    def __init__(self) -> None:
        super().__init__("Could not find a face in image")