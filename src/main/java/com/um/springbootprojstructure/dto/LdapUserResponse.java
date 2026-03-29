package com.um.springbootprojstructure.dto;

import java.util.List;

public class LdapUserResponse {
    private String dn;
    private String username;
    private String displayName;
    private String email;
    private List<String> groups;

    public LdapUserResponse() {}

    public LdapUserResponse(String dn, String username, String displayName, String email, List<String> groups) {
        this.dn = dn;
        this.username = username;
        this.displayName = displayName;
        this.email = email;
        this.groups = groups;
    }

    public String getDn() { return dn; }
    public void setDn(String dn) { this.dn = dn; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getGroups() { return groups; }
    public void setGroups(List<String> groups) { this.groups = groups; }
}
