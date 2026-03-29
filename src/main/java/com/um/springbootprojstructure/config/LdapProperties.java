package com.um.springbootprojstructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ldap")
public class LdapProperties {
    private String url;
    private String baseDn;
    private String bindDn;
    private String bindPassword;

    private String userSearchBase = "ou=people";
    private String usernameAttribute = "uid";

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getBaseDn() { return baseDn; }
    public void setBaseDn(String baseDn) { this.baseDn = baseDn; }

    public String getBindDn() { return bindDn; }
    public void setBindDn(String bindDn) { this.bindDn = bindDn; }

    public String getBindPassword() { return bindPassword; }
    public void setBindPassword(String bindPassword) { this.bindPassword = bindPassword; }

    public String getUserSearchBase() { return userSearchBase; }
    public void setUserSearchBase(String userSearchBase) { this.userSearchBase = userSearchBase; }

    public String getUsernameAttribute() { return usernameAttribute; }
    public void setUsernameAttribute(String usernameAttribute) { this.usernameAttribute = usernameAttribute; }
}
