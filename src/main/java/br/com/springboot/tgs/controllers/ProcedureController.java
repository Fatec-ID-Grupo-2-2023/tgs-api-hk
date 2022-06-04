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

import br.com.springboot.tgs.entities.Procedure;
import br.com.springboot.tgs.repository.ProcedureRepository;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {
  // GET
  // POST
  // PUT
  // DELETE
  // PATCH

  @Autowired
  private ProcedureRepository procedureRepository;

  @GetMapping("/{id}")
  public Procedure procedure(@PathVariable("id") Integer id) {

    Optional<Procedure> procedureFind = procedureRepository.findById(id);

    if (procedureFind.isPresent()) {
      return procedureFind.get();
    }

    return null;
  }

  @GetMapping("/{title}")
  public List<Procedure> findByTitle(@PathVariable("title") String title) {
    return this.procedureRepository.findByTitleIgnoreCase(title);
  }

  @GetMapping("/list")
  public List<Procedure> procedures() {
    return this.procedureRepository.findAll();
  }

  @GetMapping("/list/{status}")
  public List<Procedure> listMoreThan(@PathVariable("status") Boolean status) {
    return this.procedureRepository.findAllByStatus(status);
  }

  @PostMapping("/")
  public Procedure procedure(@RequestBody Procedure procedure) {
    return this.procedureRepository.save(procedure);
  }
}
