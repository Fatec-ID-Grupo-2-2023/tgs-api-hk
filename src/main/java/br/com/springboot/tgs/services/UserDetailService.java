package br.com.springboot.tgs.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.springboot.tgs.data.UserDetailData;
import br.com.springboot.tgs.entities.Dentist;
import br.com.springboot.tgs.repositories.DentistRepository;

@Component
public class UserDetailService implements UserDetailsService{
    @Autowired
    private DentistRepository dentistRepository;

    @Override
    public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
        // TODO Auto-generated method stub
        Optional<Dentist> dentist = dentistRepository.findById(arg0);
        if(!dentist.isPresent()){
            throw new UsernameNotFoundException("Dentista [" + dentist + "] não encontrado");
        }

        return new UserDetailData(dentist);
    }
    
}
