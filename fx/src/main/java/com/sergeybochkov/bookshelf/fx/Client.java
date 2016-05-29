package com.sergeybochkov.bookshelf.fx;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Client {

    private HttpClient client;
    private HttpHost hostConfiguration;

    private Gson gson = new Gson();

    public Client(String host, int port) {
        client = HttpClients.createDefault();
        hostConfiguration = new HttpHost(host, port);
    }

    public List<Book> findAll() {
        try {
            String response = get("/api/list/");
            Type type = new TypeToken<List<Book>>() {}.getType();
            return gson.fromJson(response, type);
        }
        catch (IOException ex) {
            return Lists.newArrayList();
        }
    }

    public Book save(Book book) {
        try {
            String json = gson.toJson(book);
            String response = post("/api/save/", json);
            return gson.fromJson(response, Book.class);
        }
        catch (IOException ex) {
            return null;
        }
    }

    public List<Book> find(String query) {
        try {
            SearchQuery q = new SearchQuery();
            q.setRequest(query);
            String json = gson.toJson(q);
            String response = post("/api/search/", json);
            Type type = new TypeToken<List<Book>>() {}.getType();
            return gson.fromJson(response, type);
        }
        catch (IOException ex) {
            return null;
        }
    }

    public List<Book> delete(List<Book> books) {
        try {
            BookWrapper wrapper = new BookWrapper();
            wrapper.setBooks(books);
            String json = gson.toJson(wrapper);

            String response = post("/api/delete/", json);
            Type type = new TypeToken<List<Book>>() {}.getType();
            return gson.fromJson(response, type);
        }
        catch (IOException ex) {
            return null;
        }
    }

    private String get(String url) throws IOException {
        HttpGet method = new HttpGet(url);
        return client.execute(hostConfiguration, method, httpResponse -> {
            return IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        });
    }

    private String post(String url, String json) throws IOException {
        HttpPost method = new HttpPost(url);
        HttpEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        method.setEntity(entity);
        return client.execute(hostConfiguration, method, httpResponse -> {
            return IOUtils.toString(httpResponse.getEntity().getContent(), "UTF-8");
        });
    }
}
