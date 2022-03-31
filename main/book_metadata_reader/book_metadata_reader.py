from abc import ABC, abstractmethod

from main.entities.book_metadata import BookMetadata
from main.entities.image import Image

class BookMetadataReader(ABC):
    @abstractmethod
    def get_metadata_from_image(self, image: Image) -> BookMetadata:
        pass