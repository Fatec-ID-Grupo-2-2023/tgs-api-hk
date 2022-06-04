package br.com.springboot.tgs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.springboot.tgs.data.UserDetailData;
import br.com.springboot.tgs.models.User;
import br.com.springboot.tgs.repositories.UserRepository;

@Component
public class UserDetailService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String _user) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        Optional<User> user = userRepository.findById(_user);
        if(!user.isPresent()){
            throw new UsernameNotFoundException("Dentista [" + user + "] não encontrado");
        }

        return new UserDetailData(user);
    }
    
}
