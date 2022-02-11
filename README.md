# Assignment 1
**CS305 - Software Engineering**

- **Name**: Aman Palariya
- **Entry number**: 2019csb1068

## Description
SqlRunner is an interface for querying the database.
SqlRunner fetches the queries from XML file, interpolates the variables in the query and runs them on the SQL server.
In case of SELECT queries, it converts the output from the server to the specified object.
In all other cases, it returns the number of rows affected by the query.

This project contains a Java implementation of SqlRunner, namely MySqlRunner.
All the modules are tested using JUnit Jupiter.

## Working
The code is organized in 3 packages - `xml`, `dbms`, and `sqlrunner`.

### `xml`

The `xml` package contains `XmlReader`.  `XmlReader` does the sole job of reading an XML file and finding `<sql/>` tags with given ID.  The function `getTagWithId(String queryId)` returns an `SqlTag` if one was found with given ID, throws `NoSqlTagWithGivenIdExcecption` otherwise.

`SqlTag` is a simple dataclass that stores the text content and the attribute `paramType` of the `<sql/>` tag.

### `dbms`

The `dbms` package contains `DatabaseReader`.
`DatabaseReader` connects to a MySQL database using JDBC.
After connection, it can be used to run a query on the database.
The queries are stored in `SqlQuery<T>` object.
It stores the raw query that was read from the XML and using the `getQuery(T param)` function, interpolates the query with parameters from `param`. 
Only a subset of param can be handled e.g. `Integer`, `String`, `Double`, `ArrayList`. But the code can be modified easily to incorporate other data types. 

**Note**: Strings are not escaped before substitution and therefore SQL injection is possible.

### `sqlrunner`

Finally, the `sqlrunner` package contains the `SqlRunner` and its implementation `MySqlRunner`.
`MySqlRunner` makes use of `XmlReader` and `DatabaseReader` to fetch queries and execute them.
It then, maps the result of SELECT queries to appropriate object.

The file `App.java` can be used to experiment with the usage of `MySqlRunner`.

The automated tests are present in `app/src/test` folder. Each file is mirrored with a corresponding test file.

## Compilation
In the root of the project, there is a `gradlew` script (`gradlew.bat` for Windows).

To run `App.java`, execute
```sh
./gradlew run
```

To run tests,
1. Setup [the MySQL Sakila sample database](https://dev.mysql.com/doc/sakila/en/)
1. Change the configuration in `app/src/test/java/cs305/dbms/DatabaseConfig.java`
1. Then, execute the following

```sh
./gradlew test
```
