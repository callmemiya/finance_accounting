package ru.nirs.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * Реализация аутентификационного токена для хранения JWT
 *
 * @author Igor Zuykov
 */
public class JwtTokenAuthentication extends AbstractAuthenticationToken {

    private final String token;

    private final String uid;

    public JwtTokenAuthentication(String token, String uid, Collection<? extends GrantedAuthority> authorities,
                                  boolean authenticated) {
        super(authorities);
        this.token = token;
        this.uid = uid;
        setAuthenticated(authenticated);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return uid;
    }

    @Override
    public boolean implies(Subject subject) {
        return true;
    }

}
