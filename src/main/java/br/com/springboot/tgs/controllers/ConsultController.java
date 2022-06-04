package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.tgs.models.Consult;
import br.com.springboot.tgs.repositories.ConsultRepository;

@RestController
@RequestMapping("/consults")
@CrossOrigin
public class ConsultController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureController.class);

    @Autowired
    private ConsultRepository consultRepository;

    /**
     * 
     * @param id - Recebe o id da consulta por parametro
     * @return - Retorna as informações da consulta correspondente
     */
    @GetMapping("/{id}")
    public Object findById(@PathVariable("id") Integer id) {
        try {
            Optional<Consult> consultFind = consultRepository.findById(id);

            if (consultFind.isPresent()) {
                LOGGER.info("Search consult - " + id);

                return ResponseEntity.status(HttpStatus.OK).body(consultFind.get());
            }
        } catch (Exception e) {
            LOGGER.info("Consult - " + id + " not found");
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }

    /**
     * 
     * @param status - Recebe o status da consulta
     * @return - Busca a lista de consultas referentes ao status recebido
     */
    @GetMapping("/list/{status}")
    public List<Consult> findByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search consults by status - " + status);

        return this.consultRepository.findAllByStatus(status);
    }

    /**
     * 
     * @param consult - Recebe uma consulta para cadastrar ou atualizar no banco
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/")
    public ResponseEntity<Object> createAndUpdate(@RequestBody Consult consult) {
        try {
            consult.setStatus(true);

            this.consultRepository.save(consult);

            LOGGER.warn("Create consult - " + consult.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Create consult fail - ", e.getMessage());

            // return
            // ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(consult);
        }
    }

    /**
     * 
     * @param consult - Recebe uma consulta para remover do banco
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@RequestBody Consult consult) {
        try {
            consult.setStatus(false);

            this.consultRepository.save(consult);

            LOGGER.warn("Remove consult - " + consult.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Remove consult fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
        }
    }
}