package cs305.dbms;

public class PrimitiveNotImplementedException extends Exception {

    public PrimitiveNotImplementedException(String className) {
        super("Class " + className + " not implemented");
    }

}
