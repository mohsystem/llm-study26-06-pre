package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@EnableConfigurationProperties(LdapProperties.class)
public class LdapConfig {

    @Bean
    public LdapContextSource contextSource(LdapProperties props) {
        LdapContextSource cs = new LdapContextSource();
        cs.setUrl(props.getUrl());
        cs.setBase(props.getBaseDn());
        cs.setUserDn(props.getBindDn());
        cs.setPassword(props.getBindPassword());
        cs.afterPropertiesSet();
        return cs;
    }

    @Bean
    public LdapTemplate ldapTemplate(LdapContextSource contextSource) {
        return new LdapTemplate(contextSource);
    }
}
