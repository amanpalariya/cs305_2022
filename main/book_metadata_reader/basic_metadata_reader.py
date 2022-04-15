from main.book_metadata_reader.book_metadata_reader import BookMetadataReader
from main.book_metadata_reader.image_to_metadata import ImageToMetadata
from main.entities.book_metadata import BookMetadata
from main.entities.image import Image


class BasicBookMetadataReader(BookMetadataReader):
    def __init__(self) -> None:
        super().__init__()
        self.__image_to_metadata = ImageToMetadata()
    
    def get_metadata_from_image(self, image: Image) -> BookMetadata:
        super().get_metadata_from_image(image)
        return self.__image_to_metadata.get_metadata(image)