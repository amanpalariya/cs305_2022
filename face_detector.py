from abc import ABC, abstractmethod
from face_image import FaceImage

class FaceDetector(ABC):
    @abstractmethod
    def create_face_image_from_image(self, image) -> FaceImage:
        pass
