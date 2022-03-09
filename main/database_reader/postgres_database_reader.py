from asyncio import futures
from typing import List

import psycopg2

from database_reader.sql_database_reader import SqlDatabaseReader, DatabaseRecord


class PostgresDatabaseReader(SqlDatabaseReader):

    def __init__(self, database: str, username: str, password: str, hostname: str = 'localhost', port: int = 5432) -> None:
        super().__init__()
        self.__conn = psycopg2.connect(host=hostname, database=database, user=username, password=password, port=port)

    def __get_columns_from_cursor(self, cursor) -> List[str]:
        return list(map(lambda x: x[0], cursor.description))

    def execute_select(self, query: str) -> List[DatabaseRecord]:
        super().execute_select(query)
        cursor = self.__conn.cursor()
        cursor.execute(query)
        data = cursor.fetchall()
        column_names = self.__get_columns_from_cursor(cursor)
        return list(map(lambda datarow: DatabaseRecord(column_names, datarow), data))

    def execute(self, query: str) -> int:
        super().execute(query)
        cursor = self.__conn.cursor()
        cursor.execute(query)
        self.__conn.commit()
        return cursor.rowcount

    def close(self):
        super().close()
        self.__conn.close()