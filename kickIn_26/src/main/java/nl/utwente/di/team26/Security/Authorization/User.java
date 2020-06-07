package nl.utwente.di.team26.Security.Authorization;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

    int userId;
    String email;
    int clarificationLevel;

    public User() {

    }

    public User(String email, int clarificationLevel) {
        this.email = email;
        this.clarificationLevel = clarificationLevel;
    }

    public User(int userId, String email, int clarificationLevel) {
        this.userId = userId;
        this.email = email;
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

    public int getClarificationLevel() {
        return clarificationLevel;
    }

    public void setClarificationLevel(int clarificationLevel) {
        this.clarificationLevel = clarificationLevel;
    }
}
