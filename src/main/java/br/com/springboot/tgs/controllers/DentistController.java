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

import br.com.springboot.tgs.entities.Dentist;
import br.com.springboot.tgs.repositories.DentistRepository;

@RestController
@RequestMapping("/dentists") 
public class DentistController {

  @Autowired
  private DentistRepository dentistRepository;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping("/{cro}")
  public Dentist dentist(@PathVariable("cro") String cro) {

    Optional<Dentist> dentistFind = dentistRepository.findById(cro);

    if (dentistFind.isPresent()) {
      return dentistFind.get();
    }

    return null;
  }

  
  @GetMapping("/list")
  public List<Dentist> dentists() {
    return this.dentistRepository.findAll();
  }
  
  @GetMapping("/list/name/{name}")
  public List<Dentist> findByTitle(@PathVariable("name") String name) {
    return this.dentistRepository.findByNameIgnoreCase(name);
  }

  @GetMapping("/list/status/{status}")
  public List<Dentist> listMoreThan(@PathVariable("status") Boolean status) {
    return this.dentistRepository.findAllByStatus(status);
  }

  @GetMapping("/validarSenha")
  public ResponseEntity<Boolean> login(@RequestBody Dentist dentist) {
    Optional<Dentist> optDentist = dentistRepository.findById(dentist.getCro());
    if(!optDentist.isPresent()){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    Dentist _dentist = optDentist.get();
    boolean valid = encoder.matches(dentist.getPassword(), _dentist.getPassword());

    HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
    return ResponseEntity.status(status).body(valid);
  }

  @PostMapping("/")
  public Dentist dentist(@RequestBody Dentist dentist) {
    dentist.setPassword(encoder.encode(dentist.getPassword()));
    return this.dentistRepository.save(dentist);
  }
}