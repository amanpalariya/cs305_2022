import argparse
import pathlib
import os

from main.book_metadata_reader.basic_metadata_reader import BasicBookMetadataReader
from main.book_metadata_writer.book_metadata_writer import BookMetadataWriter
from main.entities.image import Image

from main.table_writer.excel_table_writer import ExcelTableWriter


class Cli:
    SUPPORTED_INPUT_FILE_EXTENSIONS = ['.jpg', '.png', '']
    SUPPORTED_OUTPUT_FILE_EXTENSIONS = ['.xlsx']

    def __init__(self) -> None:
        self.parser = argparse.ArgumentParser(description='Extract metadata from images of book covers and saves them')
        self.parser.add_argument('input_path',
                                 metavar='input',
                                 type=self.get_input_file_type(),
                                 help=f'the path to an image file ({", ".join(self.SUPPORTED_INPUT_FILE_EXTENSIONS)}) or a directory containing image files'
                                 )
        self.parser.add_argument('output_path',
                                 metavar='output',
                                 type=self.get_output_file_type(),
                                 help=f'the path to file ({", ".join(self.SUPPORTED_OUTPUT_FILE_EXTENSIONS)}) where the output will be stored'
                                 )

    def get_input_file_type(self):
        return self.get_supported_file_type('input', self.SUPPORTED_INPUT_FILE_EXTENSIONS)

    def get_output_file_type(self):
        return self.get_supported_file_type('output', self.SUPPORTED_OUTPUT_FILE_EXTENSIONS)

    def get_supported_file_type(self, type, supported_extensions):
        """
        Creates a new type for argument parser allowing only `supported_extensions`
        """
        def supported_file_type(filename):
            extension = pathlib.Path(filename).suffix
            if extension.lower() not in supported_extensions:
                if extension == '':
                    self.parser.error(f'{type} file must have a supported extension ({", ".join(supported_extensions)})')
                self.parser.error(f'{type} file extension ({extension}) is not supported')
            else:
                return filename
        return supported_file_type

    def run(self, logger=print):
        args = self.parser.parse_args()

        input_filename = args.input_path
        output_filename = args.output_path

        self.__extract_and_export_metadata(input_filename, output_filename, logger)

    def __get_input_file_names(self, input_filename):
        if pathlib.Path(input_filename).is_dir():
            return map(lambda f: pathlib.Path(input_filename)/f, os.listdir(input_filename))
        else:
            return [input_filename]

    def __get_metadata_from_file(self, filename, book_metadata_reader):
        IMAGE_EXTENSIONS = ['.jpg', '.png', '.jpeg', '.bmp']
        if pathlib.Path(filename).suffix in IMAGE_EXTENSIONS:
            return book_metadata_reader.get_metadata_from_image(Image.from_filepath(filename))
        raise Exception(f'No metadata reader found for "{filename}"')

    def __get_metadata_writer(self, output_filename):
        if pathlib.Path(output_filename).suffix.lower() == '.xlsx':
            return BookMetadataWriter(ExcelTableWriter, output_filename)
        raise Exception(f'No metadata writer found for "{output_filename}"')

    def __extract_and_export_metadata(self, input_filename, output_filename, logger):
        book_metadata_reader = BasicBookMetadataReader()
        book_metadata_writer = self.__get_metadata_writer(output_filename)

        for filename in self.__get_input_file_names(input_filename):
            logger(filename)
            metadata = self.__get_metadata_from_file(filename, book_metadata_reader)
            book_metadata_writer.add_metadata(filename, metadata)

        book_metadata_writer.close()


if __name__ == '__main__':
    try:
        Cli().run(
            logger = lambda filename: print(f'Processing "{filename}"')
        )
    except Exception as e:
        print(e)
