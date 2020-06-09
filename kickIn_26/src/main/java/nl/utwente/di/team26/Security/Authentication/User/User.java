package nl.utwente.di.team26.Security.Authentication.User;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    int userId;
    String email;
    String password;
    int clarificationLevel;

    public User() {

    }

    public User(String email, String password, int clarificationLevel) {
        this.email = email;
        this.password = password;
        this.clarificationLevel = clarificationLevel;
    }

    public User(int userId, String email, String password, int clarificationLevel) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.clarificationLevel = clarificationLevel;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getClarificationLevel() {
        return clarificationLevel;
    }

    public void setClarificationLevel(int clarificationLevel) {
        this.clarificationLevel = clarificationLevel;
    }
}
