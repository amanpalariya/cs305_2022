from typing import List, Callable
from abc import ABC, abstractmethod
from entities.face_record import FaceRecord
from entities.face_image import FaceImage
from entities.person import Person

class FaceStorage(ABC):

    @abstractmethod
    def add_new_face_record(self, face_record: FaceRecord):
        pass

    @abstractmethod
    def get_top_k_matches(self, face_image: FaceImage, k: int, get_similarity_of_features: Callable[[List[float], List[float]], float]) -> List[Person]:
        assert k>0, f"Number of matches 'k' must be positive integer (got {k})"
        pass
