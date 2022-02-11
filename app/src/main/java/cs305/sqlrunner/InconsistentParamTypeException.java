package cs305.sqlrunner;

public class InconsistentParamTypeException extends Exception {

    public InconsistentParamTypeException(String paramTypeInXml, String paramTypeInSql) {
        super("Got " + paramTypeInXml + " from XML and " + paramTypeInSql + " from SQL");
    }
}
