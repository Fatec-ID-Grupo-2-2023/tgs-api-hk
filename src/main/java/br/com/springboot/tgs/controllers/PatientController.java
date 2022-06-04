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

import br.com.springboot.tgs.entities.Patient;
import br.com.springboot.tgs.repositories.PatientRepository;

@RestController
@RequestMapping("/patients")
public class PatientController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureController.class);

  @Autowired
  private PatientRepository patientRepository;

  /**
   * 
   * @param cpf - Recebe o cpf do paciente por parametro
   * @return - Retorna as informações do paciente correspondente
   */
  @GetMapping("/{cpf}")
  public Object findByCpf(@PathVariable("cpf") String cpf) {
    try {
      Optional<Patient> patientFind = patientRepository.findById(cpf);

      if (patientFind.isPresent()) {
        LOGGER.info("Search patient - " + cpf);

        return ResponseEntity.status(HttpStatus.OK).body(patientFind.get());
      }
    } catch (Exception e) {
      LOGGER.info("Patient - " + cpf + " not found");
    }

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
  }

  /**
   * 
   * @param status - Recebe o status do paciente
   * @return - Busca a lista de pacientes referentes ao status recebido
   */
  @GetMapping("/list/{status}")
  public List<Patient> findByStatus(@PathVariable("status") Boolean status) {
    LOGGER.info("Search patients by status - " + status);

    return this.patientRepository.findAllByStatus(status);
  }

  /**
   * 
   * @param patient - Recebe um pacientes para cadastrar ou atualizar no banco
   * @return - Retorna uma mensagem de sucesso ou erro
   */
  @PostMapping("/")
  public ResponseEntity<Object> createAndUpdate(@RequestBody Patient patient) {
    try {
      patient.setStatus(true);

      this.patientRepository.save(patient);

      LOGGER.warn("Create patient - " + patient.getCpf());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    } catch (Exception e) {
      LOGGER.error("Create patient fail - ", e.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }
  }

  /**
   * 
   * @param patient - Recebe um pacientes para remover do banco
   * @return - Retorna uma mensagem de sucesso ou erro 
   */
  @PostMapping("/remove")
  public ResponseEntity<Object> remove(@RequestBody Patient patient) {
    try {
      patient.setStatus(false);

      this.patientRepository.save(patient);

      LOGGER.warn("Remove patient - " + patient.getCpf());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    } catch (Exception e) {
      LOGGER.error("Remove patient fail - ", e.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }
  }
}