package com.keycloak.keycloakdemo.security;

import com.nimbusds.oauth2.sdk.util.MapUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Map<String, Object> realmAccess = source.getClaimAsMap("realm_access");
        Map<String, Object> resourceAccess = source.getClaimAsMap("resource_access");

        if (MapUtils.isNotEmpty(realmAccess) || MapUtils.isNotEmpty(resourceAccess)) {
            List<String> realmRoles = (List<String>) realmAccess.get("roles");
            Map<String, List<String>> clientMap = (Map<String, List<String>>) resourceAccess.get("spring-client");
            realmRoles.addAll(clientMap.get("roles"));


            return realmRoles.stream()
                    .map(rn -> new SimpleGrantedAuthority("ROLE_" + rn))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

}