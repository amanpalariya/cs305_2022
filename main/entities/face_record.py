from entities.face_image import FaceImage
from entities.person import Person

class FaceRecord:
    __face_image: FaceImage
    __person: Person
    
    def __init__(self, face_image: FaceImage, person: Person) -> None:
        self.__face_image = face_image
        self.__person = person

    def getFaceImage(self):
        return self.__face_image
    
    def getPerson(self):
        return self.__person
