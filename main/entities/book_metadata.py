from typing import List
from dataclasses import dataclass

@dataclass
class BookMetadata:
    title: str
    authors: List[str]
    publisher: str
    isbn: str

