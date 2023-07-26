package com.itinerariosdeaprendizaje.practicum.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Mapeo de properties de LDAP para simplificar su uso e injección
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.ldap")
@Configuration
public class LdapProperties {

    String[] urls;
    String username;
    String password;
    String referral;
    String searchbase;
    String searchfilter;
}

