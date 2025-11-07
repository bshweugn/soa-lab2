package com.example.orgdirectoryservice.config;

import com.example.common.dto.OrganizationDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class OrganizationClient {

    private OkHttpClient httpClient;
    private final Gson gson = GsonConfig.createGson();

    @Value("${organization.service.url}")
    private String orgServiceUrl;

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration READ_TIMEOUT = Duration.ofSeconds(30);
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @PostConstruct
    public void initClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) { }
                        public void checkServerTrusted(X509Certificate[] chain, String authType) { }
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(CONNECT_TIMEOUT)
                    .readTimeout(READ_TIMEOUT)
                    .callTimeout(READ_TIMEOUT.plusSeconds(5))
                    .build();
        } catch (Exception e) {
            log.error("Failed to init OkHttpClient for OrganizationClient", e);
            httpClient = new OkHttpClient();
        }
    }

    public List<OrganizationDto> searchOrganizations(Map<String, Object> request) {
        try {
            String url = trimTrailingSlash(orgServiceUrl) + "/organizations/search";
            String jsonRequest = gson.toJson(request);
            RequestBody body = RequestBody.create(jsonRequest, JSON);

            Request req = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .build();

            try (Response response = httpClient.newCall(req).execute()) {
                if (!response.isSuccessful()) {
                    log.warn("OrganizationClient.searchOrganizations — HTTP {} for url {}", response.code(), url);
                    return Collections.emptyList();
                }

                String respBody = response.body() != null ? response.body().string() : null;
                if (respBody == null || respBody.isBlank()) return Collections.emptyList();

                JsonElement root = JsonParser.parseString(respBody);
                Type listType = new TypeToken<List<OrganizationDto>>() {}.getType();

                if (root.isJsonObject() && root.getAsJsonObject().has("content")) {
                    JsonElement content = root.getAsJsonObject().get("content");
                    return gson.fromJson(content, listType);
                } else if (root.isJsonArray()) {
                    return gson.fromJson(root, listType);
                } else if (root.isJsonObject()) {
                    OrganizationDto single = gson.fromJson(root, OrganizationDto.class);
                    return single != null ? List.of(single) : Collections.emptyList();
                } else {
                    return Collections.emptyList();
                }
            }
        } catch (Exception e) {
            log.error("Error while calling OrganizationService /organizations/search", e);
            return Collections.emptyList();
        }
    }

    public OrganizationDto getById(Long id) {
        try {
            String url = trimTrailingSlash(orgServiceUrl) + "/organizations/" + id;
            Request req = new Request.Builder()
                    .url(url)
                    .get()
                    .header("Accept", "application/json")
                    .build();

            try (Response response = httpClient.newCall(req).execute()) {
                if (!response.isSuccessful()) {
                    log.warn("OrganizationClient.getById — HTTP {} for url {}", response.code(), url);
                    return null;
                }

                String body = response.body() != null ? response.body().string() : null;
                if (body == null || body.isBlank()) return null;

                return gson.fromJson(body, OrganizationDto.class);
            }
        } catch (Exception e) {
            log.error("Error while calling OrganizationService GET /organizations/{id}", e);
            return null;
        }
    }

    private static String trimTrailingSlash(String url) {
        if (url == null) return "";
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }
}
