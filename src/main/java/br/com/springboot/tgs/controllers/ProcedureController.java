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

import br.com.springboot.tgs.entities.models.Procedure;
import br.com.springboot.tgs.interfaces.RestControllerModel;
import br.com.springboot.tgs.repositories.ProcedureRepository;

@RestController
@RequestMapping("/procedures")
@CrossOrigin
public class ProcedureController implements RestControllerModel<Procedure, Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureController.class);

    @Autowired
    private ProcedureRepository procedureRepository;

    /**
     * Busca um procedimento pelo id
     * 
     * @param id - Recebe o id do procedimento
     * @return - Retorna as informações do procedimento correspondente
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Integer id) {
        try {
            Optional<Procedure> procedureFind = procedureRepository.findById(id);

            if (procedureFind.isPresent()) {
                LOGGER.info("Search procedure - " + id);

                return ResponseEntity.status(HttpStatus.OK).body(procedureFind.get());
            } else {
                throw new IllegalArgumentException("Procedimento não encontrado");
            }
        } catch (Exception e) {
            LOGGER.info("Procedure not found - " + e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Busca todos os procedimentos
     * 
     * @param status - Recebe o status do procedimento
     * @return - Busca a lista de procedimentos referentes ao status recebido
     */
    @GetMapping("/list/{status}")
    public List<Procedure> findByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search procedures by status - " + status);

        return this.procedureRepository.findAllByStatus(status);
    }

    /**
     * Registra|Atualiza um procedimento
     * 
     * @param procedure - Recebe um procedimento para cadastrar ou atualizar
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/")
    public ResponseEntity<Object> createAndUpdate(@RequestBody Procedure procedure) {
        try {
            procedure.setStatus(true);

            validateProcedure(procedure);

            this.procedureRepository.save(procedure);

            LOGGER.warn("Create procedure - " + procedure.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Create procedure fail - ", e);

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Desativa um procedimento
     * 
     * @param procedure - Recebe um procedimento para desativar
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@RequestBody Procedure procedure) {
        try {
            Procedure procedureFind = this.procedureRepository.findById(procedure.getId()).get();            

            procedureFind.setStatus(false);
            
            this.procedureRepository.save(procedureFind);

            LOGGER.warn("Remove procedure - " + procedure.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Remove procedure fail - ", e);

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Valida os dados do procedimento
     * 
     * @param procedure - Recebe um procedimento para validar
     */
    private void validateProcedure(Procedure procedure) {
        if (procedure.getTitle() == null || procedure.getTitle().isEmpty()
                || !ValidateController.validateText(procedure.getTitle())) {
            throw new IllegalArgumentException("Título inválido");
        }

        if (procedure.getDescription() != null && !procedure.getDescription().isEmpty()
                && !ValidateController.validateText(procedure.getDescription())) {
            throw new IllegalArgumentException("Descrição inválida");
        }

        if (procedure.getStatus() == null) {
            throw new IllegalArgumentException("Status inválido");
        }
    }
}