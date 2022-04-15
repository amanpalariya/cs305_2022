from typing import List
from abc import ABC, abstractmethod


class TableWriter(ABC):
    def __init__(self, header: List[str]) -> None:
        self.__header = header

    def get_header(self) -> List[str]:
        return self.__header

    @abstractmethod
    def add_row(self, row: List[str]):
        assert len(row) == len(self.get_header()), "Row does not match header"
        pass

    @abstractmethod
    def get_number_of_rows_added(self) -> int:
        pass

    @abstractmethod
    def close(self):
        pass
