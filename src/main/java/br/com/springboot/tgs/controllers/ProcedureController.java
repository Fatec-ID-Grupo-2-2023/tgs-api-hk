package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.tgs.models.Procedure;
import br.com.springboot.tgs.repositories.ProcedureRepository;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {

  @Autowired
  private ProcedureRepository procedureRepository;

  @GetMapping("/{id}")
  public Object findById(@PathVariable("id") Integer id) {
    Optional<Procedure> procedureFind = procedureRepository.findById(id);

    if (procedureFind.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(procedureFind.get());
    }

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
  }

  @GetMapping("/list")
  public List<Procedure> findAll() {
    return this.procedureRepository.findAllByStatus(true);
  }

  @GetMapping("/list/{status}")
  public List<Procedure> findByStatus(@PathVariable("status") Boolean status) {
    return this.procedureRepository.findAllByStatus(status);
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> createAndUpdate(@RequestBody Procedure procedure) {
    try {
      procedure.setStatus(true);
      this.procedureRepository.save(procedure);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> remove(@RequestBody Procedure procedure) {
    try {
      procedure.setStatus(false);
      this.procedureRepository.save(procedure);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}