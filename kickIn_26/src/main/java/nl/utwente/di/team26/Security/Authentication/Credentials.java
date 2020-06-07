package nl.utwente.di.team26.Security.Authentication;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Credentials {

    String email;
    String googleToken;

    public Credentials() {

    }

    public Credentials(String email, String googleToken) {
        this.email = email;
        this.googleToken = googleToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }
}
