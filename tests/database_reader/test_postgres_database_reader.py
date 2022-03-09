from h11 import Data
from main.database_reader.postgres_database_reader import PostgresDatabaseReader
from tests.config import DatabaseConfig

def test_execute():
    db_reader = PostgresDatabaseReader(DatabaseConfig.Database, DatabaseConfig.Username, DatabaseConfig.Password)
    db_reader.execute('CREATE TABLE IF NOT EXISTS test(id INTEGER)')
    db_reader.execute('DELETE FROM test')
    db_reader.execute('INSERT INTO test VALUES(1)')
    db_reader.close()

def test_execute_select():
    db_reader = PostgresDatabaseReader(DatabaseConfig.Database, DatabaseConfig.Username, DatabaseConfig.Password)
    db_reader.execute('INSERT INTO test VALUES(2)')
    records = db_reader.execute_select('SELECT id AS new_col FROM test WHERE id=2')
    assert records[0].get_value_by_column_index(0)==2
    assert records[0].get_value_by_column_name('new_col')==2
    assert repr(records).count("2")>=1
    db_reader.close()