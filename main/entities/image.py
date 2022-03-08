from __future__ import annotations
import numpy
from PIL import Image as PILImage

ArrayImage = numpy.ndarray

class Image:
    __image: PILImage

    def __init__(self, image: PILImage) -> None:
        self.__image = image

    @staticmethod
    def from_filepath(filepath: str) -> Image:
        return Image(PILImage.open(filepath))

    @staticmethod
    def from_array(array: ArrayImage) -> Image:
        return PILImage.fromarray(array)

    def get_array(self) -> ArrayImage:
        return numpy.array(self.__image)
    
    def save(self, filepath):
        self.__image.save(filepath)