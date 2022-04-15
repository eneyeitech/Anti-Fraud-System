package antifraud.business;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfo implements UserDetails {
    private final String username;
    private final String password;
    private final List<GrantedAuthority> rolesAndAuthorities;
    private final boolean isLocked;

    public UserInfo(UserEntity user) {
        username = user.getUsername();
        password = user.getPassword();
        rolesAndAuthorities = List.of(new SimpleGrantedAuthority(user.getRole().withPrefix()));
        isLocked = user.isLocked();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return rolesAndAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 4 remaining methods that just return true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}