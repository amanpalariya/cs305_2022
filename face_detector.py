from abc import ABC, abstractmethod
from face_image import FaceImage
from image import Image

class FaceDetector(ABC):
    @abstractmethod
    def create_face_image_from_image(self, image: Image) -> FaceImage:
        pass
