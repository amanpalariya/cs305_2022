from typing import List

import sqlite3

from database_reader.sql_database_reader import SqlDatabaseReader, DatabaseRecord

class SqliteDatabaseReader(SqlDatabaseReader):
    __conn: sqlite3.Connection

    def __init__(self, filepath: str) -> None:
        super().__init__()
        self.__conn = sqlite3.connect(filepath)

    def __get_columns_from_result(self, result: sqlite3.Cursor) -> List[str]:
        return list(map(lambda x: x[0], result.description))

    def execute_select(self, query: str) -> List[DatabaseRecord]:
        super().execute_select(query)
        result = self.__conn.execute(query)
        data = result.fetchall()
        column_names = self.__get_columns_from_result(result)
        return list(map(lambda datarow: DatabaseRecord(column_names, datarow), data))

    def execute(self, query: str) -> int:
        super().execute(query)
        cursor = self.__conn.cursor()
        cursor.execute(query)
        self.__conn.commit()
        return cursor.rowcount

