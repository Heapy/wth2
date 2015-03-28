package by.heap.security;

import by.heap.entity.User;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

/**
 * Spring Security User Details Interface Implementation.
 *
 * @author Ibragimov Ruslan
 */
public class HeapUserDetails implements UserDetails {

    private String secret;

    private String password;

    private final Long userId;

    private final String username;

    private final Set<GrantedAuthority> authorities;

    /**
     * Creates an instance of spring security user class which implements UserDetails interface.
     */
    public HeapUserDetails(final User user) {
        Preconditions.checkNotNull(user.getId(), "User Id should be set.");
        Preconditions.checkNotNull(user.getUsername(), "User email should be set.");

        this.authorities = ImmutableSet.of(new SimpleGrantedAuthority("ROLE_USER"));
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.secret = user.getSecret();
        this.userId = user.getId();
    }

    public String getSecret() {
        return this.secret;
    }

    public Long getUserId() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void eraseCredentials() {
        this.secret = null;
        this.password = null;
    }
}
