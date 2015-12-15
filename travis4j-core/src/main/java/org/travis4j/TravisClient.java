package org.travis4j;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.travis4j.api.BuildsResource;
import org.travis4j.api.RepositoriesResource;
import org.travis4j.api.Travis;
import org.travis4j.api.UsersResource;
import org.travis4j.model.Build;
import org.travis4j.model.EntityFactory;
import org.travis4j.model.Repository;
import org.travis4j.model.User;
import org.travis4j.model.json.JsonEntityFactory;
import org.travis4j.rest.JsonResponse;
import org.travis4j.rest.SimpleRestClient;

/**
 * TravisClient.
 */
public class TravisClient implements Closeable, Travis,
        RepositoriesResource,
        UsersResource,
        BuildsResource {

    private static final Logger LOG = LoggerFactory.getLogger(TravisClient.class);

    public static final URI DEFAULT_API = URI.create("https://api.travis-ci.org/");

    private URI api;
    private String token;
    private SimpleRestClient client;
    private EntityFactory factory;

    public TravisClient() {
        this(null);
    }

    public TravisClient(String token) {
        this.api = DEFAULT_API;
        this.token = token;
        this.client = new SimpleRestClient(createHttpClient(), api);
        this.factory = new JsonEntityFactory();
    }

    @Override
    public RepositoriesResource repositories() {
        return this;
    }

    @Override
    public UsersResource users() {
        return this;
    }

    @Override
    public Repository getRepository(long id) {
        JsonResponse response = client.query("repos/" + id);
        return factory.createRepository(response);
    }

    @Override
    public Repository getRepository(String ownerName, String repositoryName) {
        JsonResponse response = client.query(String.format("repos/%s/%s", ownerName, repositoryName));
        return factory.createRepository(response);
    }

    @Override
    public List<Repository> getRepositories(String slug) {
        return null;
    }

    @Override
    public List<User> getUsers() {
        JsonResponse response = client.query("users");
        return factory.createUserList(response);
    }

    @Override
    public User getUser(long id) {
        JsonResponse response = client.query("users/" + id);
        return factory.createUser(response);
    }

    @Override
    public Build getBuild(long buildId) {
        JsonResponse response = client.query("builds/" + buildId);
        return factory.createBuild(response);
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    private HttpClient createHttpClient() {
        return HttpClientBuilder.create()
                .setDefaultHeaders(createDefaultHeaders())
                .build();
    }

    private List<Header> createDefaultHeaders() {
        List<Header> headers = new ArrayList<>();

        headers.add(new BasicHeader("Accept", "application/vnd.travis-ci.2+json"));
        headers.add(new BasicHeader("Content-Type", "application/json"));

        if (token == null || token.isEmpty()) {
            LOG.warn("No Authorization token provided, won't be able to access all resources");
        } else {
            headers.add(new BasicHeader("Authorization", "token \"" + token + "\""));
        }

        return headers;
    }
}