package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.tgs.models.Patient;
import br.com.springboot.tgs.repositories.PatientRepository;

@RestController
@RequestMapping("/patients")
public class PatientController {

  @Autowired
  private PatientRepository patientRepository;

  @GetMapping("/{cpf}")
  public Object findByCpf(@PathVariable("cpf") String cpf) {
    Optional<Patient> patientFind = patientRepository.findById(cpf);

    if (patientFind.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(patientFind.get());
    }

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
  }

  @GetMapping("/list")
  public List<Patient> findAll() {
    return this.patientRepository.findAllByStatus(true);
  }

  @GetMapping("/list/{status}")
  public List<Patient> findByStatus(@PathVariable("status") Boolean status) {
    return this.patientRepository.findAllByStatus(status);
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> createAndUpdate(@RequestBody Patient patient) {
    try {
      patient.setStatus(true);

      this.patientRepository.save(patient);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }
  
  @PostMapping("/remove")
  public ResponseEntity<HttpStatus> remove(@RequestBody Patient patient) {
    try {
      patient.setStatus(false);

      this.patientRepository.save(patient);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}