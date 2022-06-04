package br.com.springboot.tgs.services;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String _user) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(_user);

        if(!user.isPresent()){
            LOGGER.warn("User [" + user + "] not found");
            throw new UsernameNotFoundException("User [" + user + "] not found");
        }

        LOGGER.info("User loaded [" + user + "]");

        return new UserDetailData(user);
    }   
}