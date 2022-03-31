from __future__ import annotations
import numpy
from PIL import Image as PILImage

ArrayImage = numpy.ndarray

class Image:
    __img: PILImage

    def __init__(self, img: PILImage) -> None:
        self.__img = img.convert('RGB')

    @staticmethod
    def from_filepath(filepath: str) -> Image:
        return Image(PILImage.open(filepath))
    
    @staticmethod
    def from_file(file) -> Image:
        return Image(PILImage.open(file))

    @staticmethod
    def from_array(array: ArrayImage) -> Image:
        return Image(PILImage.fromarray(array))

    def get_array(self) -> ArrayImage:
        return numpy.array(self.__img)
    
    def save(self, filepath):
        self.__img.save(filepath)
