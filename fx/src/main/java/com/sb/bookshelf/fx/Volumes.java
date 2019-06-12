package com.sb.bookshelf.fx;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcabi.http.Request;
import com.jcabi.http.request.JdkRequest;
import com.jcabi.http.response.JacksonResponse;
import com.jcabi.http.wire.BasicAuthWire;
import com.jcabi.http.wire.RetryWire;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class Volumes {

    private final String host;
    private final int port;
    private final String user;
    private final String pass;

    public Volumes(String host, int port, String user, String pass) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
    }

    public Long count() throws IOException {
        return new ObjectMapper()
                .readValue(
                        new JdkRequest(String.format("http://%s:%s/api/count", host, port))
                                .through(RetryWire.class)
                                .fetch()
                                .body(),
                        Long.class
                );
    }

    public List<Volume> findAll() throws IOException {
        return Arrays.asList(
                new ObjectMapper()
                        .readValue(
                                new JdkRequest(String.format("http://%s:%s/api/list", host, port))
                                        .through(RetryWire.class)
                                        .fetch()
                                        .as(JacksonResponse.class)
                                        .json()
                                        .readArray()
                                        .toString(),
                                Volume[].class
                        )
        );
    }

    @SuppressWarnings("Duplicates")
    public List<Volume> find(String query) throws IOException {
        return Arrays.asList(
                new ObjectMapper()
                        .readValue(
                                new JdkRequest(String.format("http://%s:%s@%s:%s/api/search", user, pass, host, port))
                                        .through(RetryWire.class)
                                        .method(Request.POST)
                                        .body()
                                        .set(query == null ? "" : query)
                                        .back()
                                        .header("Content-Type", "application/json")
                                        .fetch()
                                        .as(JacksonResponse.class)
                                        .json()
                                        .readArray()
                                        .toString(),
                                Volume[].class
                        )
        );
    }

    public Volume save(Volume volume) throws IOException {
        return new ObjectMapper()
                .readValue(
                        new JdkRequest(String.format("http://%s:%s@%s:%s/api/save", user, pass, host, port))
                                .through(RetryWire.class)
                                .through(BasicAuthWire.class)
                                .method(Request.POST)
                                .body()
                                .set(new ObjectMapper().writeValueAsString(volume))
                                .back()
                                .header("Content-Type", "application/json")
                                .fetch()
                                .as(JacksonResponse.class)
                                .json()
                                .readObject()
                                .toString(),
                        Volume.class
                );
    }

    public void delete(List<Volume> volumes) throws IOException {
        new JdkRequest(String.format("http://%s:%s@%s:%s/api/delete", user, pass, host, port))
                .through(RetryWire.class)
                .through(BasicAuthWire.class)
                .method(Request.POST)
                .body()
                .set(new ObjectMapper().writeValueAsString(volumes))
                .back()
                .header("Content-Type", "application/json")
                .fetch();
    }
}
