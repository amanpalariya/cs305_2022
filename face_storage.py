from typing import List
from abc import ABC, abstractmethod
from face_record import FaceRecord
from face_image import FaceImage
from person import Person

class FaceStorage(ABC):

    @abstractmethod
    def add_new_face_record(self, face_record: FaceRecord):
        pass

    @abstractmethod
    def get_top_k_matches(self, face_image: FaceImage, k: int, confidence: float) -> List[Person]:
        assert k>0, f"Number of matches 'k' must be positive integer (got {k})"
        assert 0<=confidence<=1, f"Confidence must be between 0 and 1 (got {confidence})"
        pass
