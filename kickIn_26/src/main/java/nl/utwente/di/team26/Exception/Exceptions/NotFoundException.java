package nl.utwente.di.team26.Exception.Exceptions;

public class NotFoundException extends Exception {

    /**
     * Constructor for NotFoundException. The input message is
     * returned in toString() message.
     */
    public NotFoundException(String msg) {
        super(msg);
    }

}
