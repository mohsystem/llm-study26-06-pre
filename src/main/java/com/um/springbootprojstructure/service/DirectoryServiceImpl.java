package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.config.LdapProperties;
import com.um.springbootprojstructure.dto.LdapUserResponse;
import com.um.springbootprojstructure.service.exception.RejectedOperationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attributes;
import javax.naming.directory.Attribute;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    private final LdapTemplate ldapTemplate;
    private final LdapProperties props;

    public DirectoryServiceImpl(LdapTemplate ldapTemplate, LdapProperties props) {
        this.ldapTemplate = ldapTemplate;
        this.props = props;
    }

    @Override
    public List<LdapUserResponse> searchUser(String dc, String username) {
        if (dc == null || dc.isBlank()) {
            throw new RejectedOperationException("LDAP_DC_REQUIRED", "dc is required");
        }
        if (username == null || username.isBlank()) {
            throw new RejectedOperationException("LDAP_USERNAME_REQUIRED", "username is required");
        }

        // Basic hardening: allow only alphanum and hyphen in dc to prevent DN injection
        if (!dc.matches("[A-Za-z0-9-]+")) {
            throw new RejectedOperationException("LDAP_DC_INVALID", "dc contains invalid characters");
        }

        // Search base: ou=people,dc=<dc>
        // NOTE: baseDn is already set on the context source; so we search relative to it.
        String searchBase = props.getUserSearchBase() + ",dc=" + dc;

        LdapQuery q = query()
                .base(searchBase)
                .searchScope(org.springframework.ldap.query.SearchScope.SUBTREE)
                .where(props.getUsernameAttribute()).is(username);

        return ldapTemplate.search(q, (AttributesMapper<LdapUserResponse>) attrs -> map(attrs, username));
    }

    private LdapUserResponse map(Attributes attrs, String username) throws javax.naming.NamingException {
        // Some LDAP servers provide "distinguishedName"; otherwise dn can be reconstructed differently.
        String dn = getString(attrs, "distinguishedName");
        if (dn == null) dn = getString(attrs, "entryDN");

        String displayName = firstNonNull(getString(attrs, "displayName"), getString(attrs, "cn"));
        String email = getString(attrs, "mail");

        List<String> groups = getMulti(attrs, "memberOf");

        return new LdapUserResponse(dn, username, displayName, email, groups);
    }

    private String getString(Attributes attrs, String name) throws javax.naming.NamingException {
        Attribute a = attrs.get(name);
        if (a == null) return null;
        Object v = a.get();
        return v == null ? null : v.toString();
    }

    private List<String> getMulti(Attributes attrs, String name) throws javax.naming.NamingException {
        Attribute a = attrs.get(name);
        if (a == null) return List.of();
        List<String> out = new ArrayList<>();
        NamingEnumeration<?> all = a.getAll();
        while (all.hasMore()) {
            Object v = all.next();
            if (v != null) out.add(v.toString());
        }
        return out;
    }

    private String firstNonNull(String a, String b) {
        return (a != null && !a.isBlank()) ? a : b;
    }
}
