package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureController.class);

    @Autowired
    private ProcedureRepository procedureRepository;

    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Integer id) {
        try {
            Optional<Procedure> procedureFind = procedureRepository.findById(id);

            if (procedureFind.isPresent()) {
                LOGGER.info("Search procedure - " + id);

                return ResponseEntity.status(HttpStatus.OK).body(procedureFind.get());
            }
        } catch (Exception e) {            
            LOGGER.info("Procedure - " + id + " not found");
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }

    @GetMapping("/list/{status}")
    public List<Procedure> findByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search procedures by status - " + status);

        return this.procedureRepository.findAllByStatus(status);
    }

    @PostMapping("/")
    public ResponseEntity<Object> createAndUpdate(@RequestBody Procedure procedure) {
        try {
            procedure.setStatus(true);

            this.procedureRepository.save(procedure);

            LOGGER.warn("Create procedure - " + procedure.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Create procedure fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@RequestBody Procedure procedure) {
        try {
            procedure.setStatus(false);

            this.procedureRepository.save(procedure);

            LOGGER.warn("Remove procedure - " + procedure.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Remove procedure fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
        }
    }
}