package nl.utwente.di.team26.Product.model.Authentication;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Session {

    private long tokenId;
    private String token;
    private long userId;

    public Session() {

    }

    public Session(int userId) {
        this.userId = userId;
    }

    public Session(String token, long userId) {
        this.token = token;
        this.userId = userId;
    }

    public Session(long tokenId, String token, long userId) {
        this.tokenId = tokenId;
        this.token = token;
        this.userId = userId;
    }

    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
