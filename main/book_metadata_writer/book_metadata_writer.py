from main.entities.book_metadata import BookMetadata
from main.table_writer.table_writer import TableWriter


class BookMetadataWriter:
    TABLE_HEADER = ['', 'Filename', 'Title', 'Authors', 'Publishers', 'ISBN']

    def __init__(self, table_writer_class, filename: str) -> None:
        self.__table_writer: TableWriter = table_writer_class(self.TABLE_HEADER, filename, 'Main')
    
    def add_metadata(self, filename, metadata: BookMetadata):
        row_number = self.__table_writer.get_number_of_rows_added() + 1
        title = metadata.title
        authors = ', '.join(metadata.authors)
        publishers = ', '.join(metadata.publishers)
        isbn = metadata.isbn
        self.__table_writer.add_row([row_number, str(filename), title, authors, publishers, isbn])
    
    def close(self):
        self.__table_writer.close()