package by.heap.security.provider;

import by.heap.security.HeapUserDetails;
import com.google.common.collect.ImmutableSet;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Objects;

public class JwtAuthenticationToken implements Authentication {

    private final HeapUserDetails userDetails;

    private final Object credentials;

    private boolean isAuthenticated;

    private final Collection<? extends GrantedAuthority> grantedAuthorities;

    /**
     * Constructor for creating authentication request.
     */
    public JwtAuthenticationToken(final String token) {
        this.credentials = token;
        this.userDetails = null;
        this.grantedAuthorities = null;
    }

    /**
     * Constructor for creating authenticated user.
     */
    public JwtAuthenticationToken(final HeapUserDetails userDetails) {
        this.credentials = null;
        this.userDetails = userDetails;
        this.grantedAuthorities = ImmutableSet.copyOf(userDetails.getAuthorities());
        this.isAuthenticated = true;
    }

    @Override
    public String getName() {
        return Objects.isNull(this.userDetails) ? null : this.userDetails.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    @Override
    public void setAuthenticated(final boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Once created you cannot set this token to authenticated.");
        }
        this.isAuthenticated = false;
    }
}