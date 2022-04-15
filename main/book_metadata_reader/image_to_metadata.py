from main.book_metadata_reader.book_metadata_reader import BookMetadataReader
from main.entities.book_metadata import BookMetadata
from main.entities.image import Image
import re
import pytesseract
from io import StringIO
import csv
import en_core_web_sm


class ImageToMetadata:
    def __init__(self) -> None:
        self.__nlp = en_core_web_sm.load()

    def __get_title(self, image, range_ext=10):
        """
        Steps
            - Extract width and height of each word from image
            - For each word
                - Ignore if the word is whitespace, or is short (<3) non alphanumeric string
                - Ignore if the word falls below the current height range
                - If the words falls above the height range, remove previous data and call this word's range the new range
                - If the words falls within the height range, include it in data and update the range
        """
        data = pytesseract.image_to_data(image)
        reader = csv.DictReader(StringIO(data), delimiter='\t', lineterminator='\n')

        height_range = None
        height_data = []
        for row in reader:
            height = int(row['height'])
            text = row['text']
            if text == '' or text.isspace() or (len(text.strip()) < 3 and not text.isalpha()):  # Ignore useless words
                continue
            elif height_range is None or (height > height_range[1]):  # Refresh range if a bigger height word found
                height_range = (height - range_ext/2, height + range_ext/2)
                height_data = [text]
            elif height_range[0] <= height <= height_range[1]:  # Update data if words lies in given range
                l, h = height_range
                l2, h2 = (height - range_ext/2, height + range_ext/2)
                height_range = (min(l, l2), max(h, h2))
                height_data.append(text)
        return ' '.join(height_data)

    def __get_authors(self, text):
        doc = self.__nlp(text)

        authors = []
        current_author_words = []
        for word in doc:
            if word.ent_type_ == 'PERSON':
                # Add word to current author
                current_author_words.append(word.text)
            else:
                if current_author_words:
                    # Add author to list of authors
                    authors.append(' '.join(current_author_words))
        if current_author_words:
            # Add author to list of authors
            authors.append(' '.join(current_author_words))

        return list(set(authors))

    def __get_publishers(self, text):
        doc = self.__nlp(text)

        publishers = []
        current_publisher_words = []
        for word in doc:
            if word.ent_type_ == 'ORG':
                # Add word to current publisher
                current_publisher_words.append(word.text)
            else:
                if current_publisher_words:
                    # Add publisher to list of publishers
                    publishers.append(' '.join(current_publisher_words))
        if current_publisher_words:
            # Add publisher to list of publishers
            publishers.append(' '.join(current_publisher_words))

        return list(set(publishers))

    def __get_isbn(self, text):
        text = text.replace('-', '').replace('\n', '')

        pat = re.compile(r'[0-9]{13}')  # A pattern of 13 digits
        match = pat.search(text)

        if match:
            return match.group()

    def get_metadata(self, image: Image) -> BookMetadata:
        image = image.get_array()
        text = pytesseract.image_to_string(image)

        title = self.__get_title(image)
        authors = self.__get_authors(text)
        publishers = self.__get_publishers(text)
        isbn = self.__get_isbn(text)
        return BookMetadata(title, authors, publishers, isbn)
