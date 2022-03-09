from typing import List, Callable, Tuple
from abc import ABC, abstractmethod
from main.entities.face_record import FaceRecord
from main.entities.face_image import FaceImage
from main.entities.person import Person

class FaceStorage(ABC):

    @abstractmethod
    def add_new_face_record(self, face_record: FaceRecord):
        pass

    @abstractmethod
    def get_top_k_matches(self, face_image: FaceImage, k: int, confidence: float, get_similarity_of_features: Callable[[List[float], List[float]], float]) -> List[Tuple[int, Person, float]]:
        assert k>0, f"Number of matches 'k' must be positive integer (got {k})"
        assert 0<=confidence<=1, f"Confidence must lie in [0, 1] (got {confidence}"
        pass

    @abstractmethod
    def get_person_by_id(self, id: int) -> Person:
        pass
