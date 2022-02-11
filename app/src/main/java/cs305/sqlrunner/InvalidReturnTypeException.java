package cs305.sqlrunner;

public class InvalidReturnTypeException extends Exception {

    public InvalidReturnTypeException(Class<?> cls) {
        super("The class " + cls.getName() + " does not have empty constructor");
    }

}
