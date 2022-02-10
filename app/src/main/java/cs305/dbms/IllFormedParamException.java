package cs305.dbms;

public class IllFormedParamException extends Exception {

    public IllFormedParamException(String field, String className) {
        super("Exception occurred when accessing \"" + field + "\" of " + className);
    }

}
