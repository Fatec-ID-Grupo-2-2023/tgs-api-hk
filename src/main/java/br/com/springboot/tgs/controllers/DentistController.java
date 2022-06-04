package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

  @PostMapping("/")
  public Dentist dentist(@RequestBody Dentist dentist) {
    return this.dentistRepository.save(dentist);
  }
}