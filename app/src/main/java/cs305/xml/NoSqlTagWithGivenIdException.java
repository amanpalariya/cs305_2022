package cs305.xml;

public class NoSqlTagWithGivenIdException extends Exception {

    public NoSqlTagWithGivenIdException(String queryId) {
        super("No SQL tag with \"" + queryId + "\" found");
    }
}
