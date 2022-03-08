from typing import List

from entities.image import Image

class FaceImage:
    __features: List[float]
    __image: Image

    def __init__(self, image: Image, features: List[float]) -> None:
        self.__image = image
        self.__features = features

    def getFeatures(self) -> List[float]:
        return self.__features
    
    def getImage(self) -> Image:
        return self.__image