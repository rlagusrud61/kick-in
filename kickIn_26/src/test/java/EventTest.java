import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;


public class EventTest {

    private final int HTTP_OK = Status.OK.getStatusCode();
    private final int HTTP_NOT_FOUND = Status.NOT_FOUND.getStatusCode();
    private final int HTTP_UNAUTHORIZED = Status.UNAUTHORIZED.getStatusCode();
    private final int HTTP_INTERNAL_SERVER_ERROR = Status.INTERNAL_SERVER_ERROR.getStatusCode();
    private final int HTTP_BAD_REQUEST = Status.BAD_REQUEST.getStatusCode();

    @Test
    public void getAllEvents() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(getBaseURI().toString())
                .method("GET", null)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/kickInTeam26").build();
    }

}
