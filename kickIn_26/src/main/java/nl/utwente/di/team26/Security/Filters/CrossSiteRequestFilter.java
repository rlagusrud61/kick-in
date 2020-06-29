package nl.utwente.di.team26.Security.Filters;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.apache.commons.codec.Charsets;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.safety.Whitelist;
import org.owasp.encoder.Encode;
import org.owasp.esapi.ESAPI;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Provider
@PreMatching
public class CrossSiteRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext request) {
        cleanQueryParams(request);
        cleanHeaders(request.getHeaders());
        cleanCookies(request.getCookies());
        cleanJSONBody(request);
    }

    private void cleanJSONBody(ContainerRequestContext request) {
        if (isJson(request)) {
            try {
                String json = IOUtils.toString(request.getEntityStream(), Charsets.UTF_8);
                if (json.equals("")) {
                    return;
                }

                JsonElement jsonElement = new JsonParser().parse(json);
                System.out.println("Before: " + json);
                json = clean(jsonElement).toString();
                System.out.println("After: " + json);

                // replace input stream for Jersey as we've already read it
                InputStream in = IOUtils.toInputStream(json, Charsets.UTF_8);
                request.setEntityStream(in);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    boolean isJson(ContainerRequestContext request) {
        // define rules when to read body
        return request.getMediaType().toString().contains("application/json");
    }

    private static JsonElement clean(JsonElement elem) {
        if (elem.isJsonPrimitive()) { // Base case - we have a Number, Boolean or String
            JsonPrimitive primitive = elem.getAsJsonPrimitive();
            if(primitive.isString()) {
                // Escape all String values
                return new JsonPrimitive(Encode.forHtml(primitive.getAsString()));
            } else {
                return primitive;
            }
        } else if (elem.isJsonArray()) { // We have an array - GSON requires handling this separately
            JsonArray cleanArray = new JsonArray();
            for(JsonElement arrayElement: elem.getAsJsonArray()) {
                cleanArray.add(clean(arrayElement));
            }
            return cleanArray;
        } else { // Recursive case - iterate over JSON object entries
            JsonObject obj = elem.getAsJsonObject();
            JsonObject clean = new JsonObject();
            for(Map.Entry<String, JsonElement> entry :  obj.entrySet()) {
                // Encode the key right away and encode the value recursively
                clean.add(Encode.forHtml(entry.getKey()), clean(entry.getValue()));
            }
            return clean;
        }
    }

    private void cleanCookies(Map<String, Cookie> cookies) {
        for(Map.Entry<String, Cookie> cookieEntry : cookies.entrySet()) {
            String key = cookieEntry.getKey();
            Cookie dirtyCookie = cookieEntry.getValue();

            Cookie cleanCookie = new Cookie(
                    escapeXSS(dirtyCookie.getName()),
                    escapeXSS(dirtyCookie.getValue()),
                    escapeXSS(dirtyCookie.getPath()),
                    escapeXSS(dirtyCookie.getDomain())
            );
            cookies.put(key, cleanCookie);
        }
    }

    private void cleanQueryParams( ContainerRequestContext request ) {
        UriBuilder builder = request.getUriInfo().getRequestUriBuilder();
        MultivaluedMap<String, String> queries = request.getUriInfo().getQueryParameters();

        for(Map.Entry<String, List<String>> query : queries.entrySet()) {
            String key = query.getKey();
            List<String> values = query.getValue();

            List<String> dirtyValues = new ArrayList<>();
            for(String value : values) {
                dirtyValues.add(escapeXSS(value));
            }

            int size = CollectionUtils.size(dirtyValues);
            builder.replaceQueryParam(key);

            if(size == 1) {
                builder.replaceQueryParam(key, dirtyValues.get(0));
            } else if(size > 1) {
                builder.replaceQueryParam(key, dirtyValues.toArray());
            }
        }

        request.setRequestUri(builder.build());
    }

    private void cleanHeaders(MultivaluedMap<String, String> headers) {
        for(Map.Entry<String, List<String>> header : headers.entrySet()) {
            String key = header.getKey();
            List<String> values = header.getValue();

            List<String> cleanValues = new ArrayList<>();
            for(String value : values) {
                cleanValues.add(escapeXSS(value));
            }

            headers.put(key, cleanValues);
        }
    }

    public String escapeXSS(String value) {
        if(value == null) {
            return null;
        }

        // Use the ESAPI library to avoid encoded attacks.
        value = ESAPI.encoder().canonicalize(value);

        // Avoid null characters
        value = value.replaceAll("\0", "");

        // Clean out HTML
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.escapeMode(Entities.EscapeMode.xhtml);
        outputSettings.prettyPrint(false);
        value = Jsoup.clean(value, "", Whitelist.none(), outputSettings);
        return value;
    }
}