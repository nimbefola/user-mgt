package com.pentspace.accountmgtservice.security.securityServices;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pentspace.accountmgtservice.entities.Account;
import com.pentspace.accountmgtservice.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import javax.persistence.Column;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
@NoArgsConstructor
public class ApplicationUser implements UserDetails {

    private String id;

    private String firstName;

    private String lastName;
    @JsonIgnore
    private  String email;

    private String phoneNumber;

    @JsonIgnore
    private  String password;


    private Collection<? extends GrantedAuthority> authorities;

    private Map<String, Object> attributes;



    public static ApplicationUser create(User user) {
        //user entity has no role. if you need a role, add field role

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles().toString())); //user entity does not have role. create one
        return new ApplicationUser(
                String.valueOf(user.getId()),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getPassword(),
                authorities
        );
    }

    public ApplicationUser(String id, String firstName, String lastName,  String email, String phoneNumber, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.authorities = authorities;
    }

    public static ApplicationUser create(User user, Map<String, Object> attributes) {
        ApplicationUser applicationUser = ApplicationUser.create(user);
        applicationUser.setAttributes(attributes);
        return applicationUser;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

}
