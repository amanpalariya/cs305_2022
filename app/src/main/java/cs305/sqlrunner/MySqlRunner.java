package cs305.sqlrunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cs305.dbms.DatabaseReader;
import cs305.dbms.IllFormedParamException;
import cs305.dbms.PrimitiveNotImplementedException;
import cs305.dbms.SqlQuery;
import cs305.xml.NoSqlTagWithGivenIdException;
import cs305.xml.SqlTag;
import cs305.xml.XmlParsingException;
import cs305.xml.XmlReader;

public class MySqlRunner {

    private DatabaseReader dbReader;
    private XmlReader xmlReader;

    public MySqlRunner(String databaseName, String username, String password,
            String xmlFilePath) throws SQLException, IOException, XmlParsingException {
        this.xmlReader = new XmlReader(xmlFilePath);
        this.dbReader = new DatabaseReader(databaseName, username, password);
    }

    private void throwExceptionIfParamTypesMismatch(String paramTypeInXml, String paramTypeInSql)
            throws InconsistentParamTypeException {
        if (!paramTypeInXml.contentEquals(paramTypeInSql)) {
            throw new InconsistentParamTypeException(paramTypeInXml, paramTypeInSql);
        }
    }

    private <R> R mapResultSet(ResultSet resultSet, Class<R> cls)
            throws InvalidReturnTypeException {
        try {
            R r = cls.getDeclaredConstructor().newInstance();
            for (Field field : cls.getDeclaredFields()) {
                field.set(r, resultSet.getObject(field.getName()));
            }
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            throw new InvalidReturnTypeException(cls);
        }
    }

    public <T, R> R selectOne(String queryId, T queryParam, Class<R> resultType)
            throws InconsistentParamTypeException, NoSqlTagWithGivenIdException, InvalidReturnTypeException,
            SQLException, IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException {
        SqlTag sqlTag = this.xmlReader.getTagWithId(queryId);

        throwExceptionIfParamTypesMismatch(sqlTag.getParamType(), queryParam.getClass().getName());

        SqlQuery<T> sqlQuery = new SqlQuery<>(sqlTag.getQuery());
        ResultSet resultSet = this.dbReader.executeQuery(sqlQuery, queryParam);
        resultSet.next();
        return mapResultSet(resultSet, resultType);
    }

    public <T, R> List<R> selectMany(String queryId, T queryParam, Class<R> resultType)
            throws SQLException, InvalidReturnTypeException, NoSqlTagWithGivenIdException, IllFormedParamException,
            InconsistentParamTypeException, IllegalArgumentException, PrimitiveNotImplementedException {
        SqlTag sqlTag = this.xmlReader.getTagWithId(queryId);

        throwExceptionIfParamTypesMismatch(sqlTag.getParamType(), queryParam.getClass().getName());

        List<R> results = new ArrayList<>();
        SqlQuery<T> sqlQuery = new SqlQuery<>(sqlTag.getQuery());
        ResultSet resultSet = this.dbReader.executeQuery(sqlQuery, queryParam);
        while (resultSet.next()) {
            results.add(mapResultSet(resultSet, resultType));
        }
        return results;
    }

    private <T> int handleUpdateTypeQueries(String queryId, T queryParam)
            throws InconsistentParamTypeException, NoSqlTagWithGivenIdException, SQLException, IllFormedParamException,
            IllegalArgumentException, PrimitiveNotImplementedException {
        SqlTag sqlTag = this.xmlReader.getTagWithId(queryId);

        throwExceptionIfParamTypesMismatch(sqlTag.getParamType(), queryParam.getClass().getName());

        SqlQuery<T> sqlQuery = new SqlQuery<>(sqlTag.getQuery());
        return this.dbReader.executeUpdate(sqlQuery, queryParam);
    }

    public <T> int update(String queryId, T queryParam)
            throws InconsistentParamTypeException, NoSqlTagWithGivenIdException, SQLException, IllFormedParamException,
            IllegalArgumentException, PrimitiveNotImplementedException {
        return this.handleUpdateTypeQueries(queryId, queryParam);
    }

    public <T> int insert(String queryId, T queryParam)
            throws InconsistentParamTypeException, NoSqlTagWithGivenIdException, SQLException, IllFormedParamException,
            IllegalArgumentException, PrimitiveNotImplementedException {
        return this.handleUpdateTypeQueries(queryId, queryParam);
    }

    public <T> int delete(String queryId, T queryParam)
            throws NoSqlTagWithGivenIdException, InconsistentParamTypeException, SQLException, IllFormedParamException,
            IllegalArgumentException, PrimitiveNotImplementedException {
        return this.handleUpdateTypeQueries(queryId, queryParam);
    }
}
