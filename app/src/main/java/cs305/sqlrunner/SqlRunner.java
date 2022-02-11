package cs305.sqlrunner;

import java.sql.SQLException;
import java.util.List;

import cs305.dbms.IllFormedParamException;
import cs305.dbms.PrimitiveNotImplementedException;
import cs305.xml.NoSqlTagWithGivenIdException;

public interface SqlRunner {
	public <T, R> R selectOne(String queryId, T queryParam, Class<R> resultType)
			throws InconsistentParamTypeException, NoSqlTagWithGivenIdException, InvalidReturnTypeException,
			SQLException, IllFormedParamException, IllegalArgumentException, PrimitiveNotImplementedException;

	public <T, R> List<R> selectMany(String queryId, T queryParam, Class<R> resultType)
			throws SQLException, InvalidReturnTypeException, NoSqlTagWithGivenIdException, IllFormedParamException,
			InconsistentParamTypeException, IllegalArgumentException, PrimitiveNotImplementedException;

	public <T> int update(String queryId, T queryParam)
			throws InconsistentParamTypeException, NoSqlTagWithGivenIdException, SQLException, IllFormedParamException,
			IllegalArgumentException, PrimitiveNotImplementedException;

	public <T> int insert(String queryId, T queryParam)
			throws InconsistentParamTypeException, NoSqlTagWithGivenIdException, SQLException, IllFormedParamException,
			IllegalArgumentException, PrimitiveNotImplementedException;

	public <T> int delete(String queryId, T queryParam)
			throws NoSqlTagWithGivenIdException, InconsistentParamTypeException, SQLException, IllFormedParamException,
			IllegalArgumentException, PrimitiveNotImplementedException;
}
