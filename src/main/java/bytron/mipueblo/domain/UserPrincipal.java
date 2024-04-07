package bytron.mipueblo.domain;

import bytron.mipueblo.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

import static bytron.mipueblo.dtomapper.UserDTOMapper.fromUser;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class UserPrincipal implements UserDetails {

    private final User user;
//    private final String permissions; //USER:READ,CUSTOMER:READ
    private final Role role; //passing in the entire role instead of just permissions

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return stream(permissions.split(",")).map(SimpleGrantedAuthority::new).collect(toList());
        //changed
//        return stream(permissions.split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
        //works og
        //return stream(this.role.getPermission().split(",".trim())).map(SimpleGrantedAuthority::new).collect(toList());
        return AuthorityUtils.commaSeparatedStringToAuthorityList(role.getPermission());
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isNotLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    ///for logging
//    public User getUser(){
//        return this.user;
//    }

    public UserDTO getUser(){
        return fromUser(this.user, role);
    }
}
