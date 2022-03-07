class Person:
    __name: str

    def __init__(self, name: str) -> None:
        self.__name = name

    def getName(self) -> str:
        return self.__name
    
    def __hash__(self) -> int:
        return hash(self.__name)
