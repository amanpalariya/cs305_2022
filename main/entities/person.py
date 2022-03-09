class Person:
    __name: str

    def __init__(self, name: str) -> None:
        self.__name = name

    def getName(self) -> str:
        return self.__name
    
    def __hash__(self) -> int:
        return hash(self.__name)
    
    def __eq__(self, __o: object) -> bool:
        return type(__o)==Person and __o.getName()==self.getName()

    def __repr__(self) -> str:
        return f"Person(\"{self.__name}\")"