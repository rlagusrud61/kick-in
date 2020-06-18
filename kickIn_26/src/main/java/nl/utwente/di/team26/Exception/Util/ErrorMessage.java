package nl.utwente.di.team26.Exception.Util;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorMessage {

    private Status errorCode;
    private String errorMessage;

    public ErrorMessage() {

    }

    public ErrorMessage(Status errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public Status getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Status errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setAll(Status errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
