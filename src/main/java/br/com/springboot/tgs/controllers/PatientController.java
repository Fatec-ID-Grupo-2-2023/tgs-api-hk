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

import br.com.springboot.tgs.entities.models.Patient;
import br.com.springboot.tgs.interfaces.RestControllerModel;
import br.com.springboot.tgs.repositories.PatientRepository;

@RestController
@RequestMapping("/patients")
public class PatientController implements RestControllerModel<Patient, String> {
  private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

  @Autowired
  private PatientRepository patientRepository;

  /**
   * Busca um paciente pelo id
   * 
   * @param cpf - Recebe o cpf do paciente
   * @return - Retorna as informações do paciente correspondente
   */
  @Override
  @GetMapping("/{cpf}")
  public ResponseEntity<Object> findById(@PathVariable("cpf") String cpf) {
    try {
      if (cpf == null || cpf.isEmpty() || !ValidateController.validateCPF(cpf)) {
        throw new IllegalArgumentException("CPF inválido");
      }

      Optional<Patient> patientFind = patientRepository.findById(cpf);

      if (patientFind.isPresent()) {
        LOGGER.info("Search patient - " + cpf);

        return ResponseEntity.status(HttpStatus.OK).body(patientFind.get());
      } else {
        throw new IllegalArgumentException("Paciente não encontrado");
      }
    } catch (Exception e) {
      LOGGER.info("Patient not found - " + e);
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
  }

  /**
   * Busca todos os pacientes
   * 
   * @param status - Recebe o status do paciente
   * @return - Busca a lista de pacientes referentes ao status recebido
   */
  @Override
  @GetMapping("/list/{status}")
  public List<Patient> findByStatus(@PathVariable("status") Boolean status) {
    LOGGER.info("Search patients by status - " + status);

    return this.patientRepository.findAllByStatus(status);
  }

  /**
   * Registra|Atualiza um paciente
   * 
   * @param patient - Recebe um paciente para cadastrar ou atualizar
   * @return - Retorna uma mensagem de sucesso ou erro
   */
  @Override
  @PostMapping("/")
  public ResponseEntity<Object> createAndUpdate(@RequestBody Patient patient) {
    try {
      patient.setStatus(true);

      validatePatient(patient);

      this.patientRepository.save(patient);

      LOGGER.warn("Create patient - " + patient.getCpf());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("Create patient fail - ", e);

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
  }

  /**
   * Desativa um dentista
   * 
   * @param p - Recebe um paciente para desativar
   * @return - Retorna uma mensagem de sucesso ou erro
   */
  @Override
  @PostMapping("/remove")
  public ResponseEntity<Object> remove(@RequestBody Patient p) {
    try {
      if (p.getCpf() == null || p.getCpf().isEmpty() || !ValidateController.validateCPF(p.getCpf())) {
        throw new IllegalArgumentException("CPF inválido");
      }

      Patient patient = patientRepository.findById(p.getCpf()).get();
      patient.setStatus(false);

      validatePatient(patient);

      this.patientRepository.save(patient);

      LOGGER.warn("Remove patient - " + patient.getCpf());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("Remove patient fail - ", e);

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
  }

  /**
   * Valida os dados do paciente
   * 
   * @param dentist - Recebe o paciente para validar
   */
  private void validatePatient(Patient patient) {
    if (patient.getCpf() == null || patient.getCpf().isEmpty() || !ValidateController.validateCPF(patient.getCpf())) {
      throw new IllegalArgumentException("CPF inválido");
    }

    if (patient.getRg() == null || patient.getRg().isEmpty() || !ValidateController.validateRG(patient.getRg())) {
      throw new IllegalArgumentException("RG inválido");
    }

    if (patient.getName() == null || patient.getName().isEmpty()
        || !ValidateController.validateText(patient.getName())) {
      throw new IllegalArgumentException("Nome inválido");
    }

    if (patient.getSurname() == null || patient.getSurname().isEmpty()
        || !ValidateController.validateText(patient.getSurname())) {
      throw new IllegalArgumentException("Sobrenome inválido");
    }

    if (patient.getNickname() != null && !patient.getNickname().isEmpty()
        && !ValidateController.validateText(patient.getNickname())) {
      throw new IllegalArgumentException("Apelido inválido");
    }

    if (patient.getEmail() != null && !patient.getEmail().isEmpty()
        && !ValidateController.validateEmail(patient.getEmail())) {
      throw new IllegalArgumentException("Email inválido");
    }

    if (patient.getTelephone() != null && !patient.getTelephone().isEmpty()
        && !ValidateController.validateTelephone(patient.getTelephone())) {
      throw new IllegalArgumentException("Telefone inválido");
    }

    if (patient.getCellphone() == null || patient.getCellphone().isEmpty()
        || !ValidateController.validateCellphone(patient.getCellphone())) {
      throw new IllegalArgumentException("Celular inválido");
    }

    if (patient.getStreet() == null || patient.getStreet().isEmpty()
        || !ValidateController.validateText(patient.getStreet())) {
      throw new IllegalArgumentException("Rua inválida");
    }

    if (patient.getNeighborhood() == null || patient.getNeighborhood().isEmpty()
        || !ValidateController.validateText(patient.getNeighborhood())) {
      throw new IllegalArgumentException("Bairro inválido");
    }

    if (patient.getCity() == null || patient.getCity().isEmpty()
        || !ValidateController.validateText(patient.getCity())) {
      throw new IllegalArgumentException("Cidade inválida");
    }

    if (patient.getDistrict() == null || patient.getDistrict().isEmpty() || patient.getDistrict().length() > 2
        || !ValidateController.validateText(patient.getDistrict())) {
      throw new IllegalArgumentException("Estado inválido");
    }

    if (patient.getCep() == null || patient.getCep().isEmpty() || !ValidateController.validateCEP(patient.getCep())) {
      throw new IllegalArgumentException("CEP inválido");
    }

    if (patient.getNumber() == null) {
      throw new IllegalArgumentException("Número inválido");
    }

    if (patient.getComplement() != null && !patient.getComplement().isEmpty()
        && !ValidateController.validateTextAndNumber(patient.getComplement())) {
      throw new IllegalArgumentException("Complemento inválido");
    }
  }
}