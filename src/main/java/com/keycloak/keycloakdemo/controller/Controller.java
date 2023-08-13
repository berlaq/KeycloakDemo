package com.keycloak.keycloakdemo.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.keycloak.keycloakdemo.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController()
public class Controller {

    @Value("${token-endpoint}")
    private String tokenEndpoint;
    @Value("${client-id}")
    private String clientId;
    @Value("${client-secret}")
    private String clientSecret;

    @GetMapping("/login")
    public ResponseEntity<TokenResponse> getLoggedInUser(@RequestParam("username") String user, @RequestParam("pwd") String pwd) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> request = new LinkedMultiValueMap<>();
        request.add("grant_type", "password");
        request.add("client_id", clientId);
        request.add("username", user);
        request.add("password", pwd);
        request.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders(request);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<TokenResponse> tokenResponse = restTemplate.postForEntity(tokenEndpoint, entity, TokenResponse.class);
        return ResponseEntity.ok(tokenResponse.getBody());
    }

    @GetMapping("/text")
    public ResponseEntity<String> getCustomer() {
        return ResponseEntity.ok("Spring security worked!");
    }
}
