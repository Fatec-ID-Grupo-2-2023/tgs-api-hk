package br.com.springboot.tgs.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.springboot.tgs.entities.Dentist;

public class UserDetailData implements UserDetails {
    private Optional<Dentist> dentist;

    public UserDetailData(Optional<Dentist> dentist){
        this.dentist = dentist;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return dentist.orElse(new Dentist()).getPassword();
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return dentist.orElse(new Dentist()).getCro();
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return true;
    }
    
}
