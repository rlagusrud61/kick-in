package nl.utwente.di.team26.Security.User;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", clarificationLevel=" + clarificationLevel +
                '}';
    }
}
