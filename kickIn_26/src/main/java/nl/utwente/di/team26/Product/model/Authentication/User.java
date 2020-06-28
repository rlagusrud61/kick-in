package nl.utwente.di.team26.Product.model.Authentication;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@XmlRootElement
public class User {

    long userId;
    String email;
    String password;
    String nickname;
    int clearanceLevel;

    public User() {

    }

    public User(String email, String password, String nickname, int clearanceLevel) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.clearanceLevel = clearanceLevel;
    }

    public User(String email, int clearanceLevel, String nickname) {
        this.email = email;
        this.nickname = nickname;
        this.clearanceLevel = clearanceLevel;
    }

    public User(long userId, String email, String nickname, int clearanceLevel) {
        this.userId = userId;
        this.email = email;
        this.nickname = nickname;
        this.clearanceLevel = clearanceLevel;
    }

    @Deprecated
    public User(String email, String password, int clearanceLevel) {
        this.email = email;
        this.password = password;
        this.clearanceLevel = clearanceLevel;
    }

    @Deprecated
    public User(long userId, String email, String password, String nickname, int clearanceLevel) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.clearanceLevel = clearanceLevel;
    }

    public User(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getClearanceLevel() {
        return clearanceLevel;
    }

    public void setClearanceLevel(int clearanceLevel) {
        this.clearanceLevel = clearanceLevel;
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
        return "UserDao{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", clearanceLevel=" + clearanceLevel +
                '}';
    }
}
