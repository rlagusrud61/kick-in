package nl.utwente.di.team26.Exceptions;

public class DataSourceNotFoundException extends Exception {

    /**
     * Constructor for DataSourceNotFoundException. The input message is
     * returned in toString() message.
     */
    public DataSourceNotFoundException(String msg) {
        super(msg);
    }
}
