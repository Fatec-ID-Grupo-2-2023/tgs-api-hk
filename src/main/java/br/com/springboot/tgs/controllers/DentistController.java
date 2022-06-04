package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.tgs.models.User;
import br.com.springboot.tgs.repositories.UserRepository;

@RestController
@RequestMapping("/dentists") 
public class DentistController {
  private final String PREFIX_DENTIST_USER_ID = "DTN";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping("/{user}")
  public Object findByUser(@PathVariable("user") String user) {
    Optional<User> dentistFind = userRepository.findById(user);

    if (dentistFind.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(dentistFind.get());
    }

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
  }

  @GetMapping("/list")
  public List<User> dentists() {
    return this.userRepository.findAllByStatus(true, PREFIX_DENTIST_USER_ID);
  }

  @GetMapping("/list/{status}")
  public List<User> listByStatus(@PathVariable("status") Boolean status) {
    return this.userRepository.findAllByStatus(status, PREFIX_DENTIST_USER_ID);
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> createAndUpdate(@RequestBody User dentist) {    
    try {
      dentist.setUserId(PREFIX_DENTIST_USER_ID + dentist.getDocument());
      dentist.setPassword(encoder.encode(dentist.getPassword()));
      dentist.setStatus(true);

      this.userRepository.save(dentist);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);        
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PostMapping("/remove")
  public ResponseEntity<HttpStatus> remove(@RequestBody User dentist) {
    try {
      dentist.setStatus(false);
      this.userRepository.save(dentist);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}