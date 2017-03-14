package com.sergeybochkov.bookshelf.fx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sergeybochkov.bookshelf.fx.model.SearchQuery;
import com.sergeybochkov.bookshelf.fx.model.Volume;
import com.sergeybochkov.bookshelf.fx.model.VolumeList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.List;

public final class Client {

    private final HttpClient client;
    private final Gson gson;
    private final BooleanProperty connectedProperty;
    private final String host;
    private final int port;

    public Client(String host, int port) {
        connectedProperty = new SimpleBooleanProperty(false);
        client = HttpClients.createDefault();
        gson = new GsonBuilder().create();
        this.host = host;
        this.port = port;
    }

    public ObservableBooleanValue connectedProperty() {
        return connectedProperty;
    }

    private String get(String url) throws IOException {
        try {
            String answer = client.execute(new HttpHost(host, port),
                    new HttpGet(url),
                    httpResponse -> IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"));
            connectedProperty.setValue(true);
            return answer;
        }
        catch (IOException ex) {
            connectedProperty.setValue(false);
            throw ex;
        }
    }

    private String post(String url, String json) throws IOException {
        try {
            HttpPost method = new HttpPost(url);
            method.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
            String answer = client.execute(new HttpHost(host, port),
                    method,
                    httpResponse -> IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8"));
            connectedProperty.setValue(true);
            return answer;
        }
        catch (IOException ex) {
            connectedProperty.setValue(false);
            throw ex;
        }
    }

    public List<Volume> findAll() throws IOException {
        return gson.fromJson(
                get("/api/list/"),
                new TypeToken<List<Volume>>() {}.getType());
    }

    public Volume save(Volume volume) throws IOException {
        return gson.fromJson(
                post("/api/save/", gson.toJson(volume)),
                Volume.class);
    }

    public List<Volume> find(String query) throws IOException {
        return gson.fromJson(
                post("/api/search/", gson.toJson(new SearchQuery(query))),
                new TypeToken<List<Volume>>() {}.getType());
    }

    public List<Volume> delete(List<Volume> volumes) throws IOException {
        return gson.fromJson(
                post("/api/delete/", gson.toJson(new VolumeList(volumes))),
                new TypeToken<List<Volume>>() {}.getType());
    }
}
