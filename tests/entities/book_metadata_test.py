import pytest

from main.entities.book_metadata import BookMetadata


@pytest.fixture
def title():
    return "Title"

@pytest.fixture
def authors():
    return ["Author 1", "Author 2"]

@pytest.fixture
def publishers():
    return ["Publisher 1", "Publisher 2"]

@pytest.fixture
def isbn():
    return "1234567890123"

def test_init(title, authors, publishers, isbn):
    metadata = BookMetadata(title, authors, publishers, isbn)
    assert metadata.title == title
    assert metadata.authors == authors
    assert metadata.publishers == publishers
    assert metadata.isbn == isbn