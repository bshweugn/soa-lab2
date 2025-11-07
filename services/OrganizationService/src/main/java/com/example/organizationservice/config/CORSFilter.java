package com.example.organizationservice.config;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Provider
public class CORSFilter implements ContainerResponseFilter {

    private static final Set<String> ALLOWED_ORIGINS = new HashSet<>(Arrays.asList(
       "http://localhost:3000",
       "https://localhost:23223"
    ));

    private static final  String ALLOWED_METHODS = "GET, POST, PUT, PATCH, DELETE";
    private static final String ALLOWED_HEADERS = "Origin, Content-Type, Accept";
    private static final boolean ALLOW_CREDENTIALS = false;
    private static final String MAX_AGE = "3600";

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        String origin = req.getHeaderString("Origin");

        if(!ALLOWED_ORIGINS.contains(origin)){
            return;
        }

        res.getHeaders().putSingle("Access-Control-Allow-Origin", origin);
        res.getHeaders().add("Vary", "Origin");
        res.getHeaders().putSingle("Access-Control-Allow-Methods", ALLOWED_METHODS);
        res.getHeaders().putSingle("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        res.getHeaders().putSingle("Access-Control-Allow-Credentials", String.valueOf(ALLOW_CREDENTIALS));
        res.getHeaders().putSingle("Access-Control-Max-Age", MAX_AGE);
    }
}