package nl.utwente.di.team26.Security.Authentication;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Session {

    int tokenId;
    String token;
    int userId;

    public Session() {

    }

    public Session(String token, int userId) {
        this.token = token;
        this.userId = userId;
    }

    public Session(int tokenId, String token, int userId) {
        this.tokenId = tokenId;
        this.token = token;
        this.userId = userId;
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(int tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
