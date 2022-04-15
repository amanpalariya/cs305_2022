import pytest
from main.book_metadata_reader.basic_metadata_reader import BasicBookMetadataReader

from main.entities.book_metadata import BookMetadata
from main.entities.image import Image


@pytest.fixture
def book_cover_path():
    return "tests/resources/test-book-cover.jpg"

@pytest.fixture
def test_image_metadata():
    return BookMetadata("Test Title", ["John Doe"], [], "1234567891011")

def test_metadata_extract(book_cover_path, test_image_metadata):
    image = Image.from_filepath(book_cover_path)
    reader = BasicBookMetadataReader()
    metadata = reader.get_metadata_from_image(image)
    assert metadata == test_image_metadata

