from abc import ABC, abstractmethod
from typing import Dict, List, Any


class DatabaseRecord:
    __column_dict: Dict[str, int]
    __data: List[Any]

    def __init__(self, column_names: List[str], data: List[Any]) -> None:
        self.__column_dict = {column_name: i for i,
                              column_name in enumerate(column_names)}
        self.__column_names = column_names
        self.__data = data

    def get_value_by_column_index(self, index: int) -> Any:
        return self.__data[index]

    def get_value_by_column_name(self, name: str) -> Any:
        return self.__data[self.__column_dict[name]]

    def __repr__(self) -> str:
        return repr(list(map(lambda name, value: f"{name}: {value}", self.__column_names, self.__data)))


class SqlDatabaseReader(ABC):

    @abstractmethod
    def execute_select(self, query: str) -> List[DatabaseRecord]:
        pass

    @abstractmethod
    def execute(self, query: str) -> int:
        pass
