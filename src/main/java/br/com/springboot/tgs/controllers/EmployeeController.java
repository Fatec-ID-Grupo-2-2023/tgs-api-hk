package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import br.com.springboot.tgs.entities.models.User;
import br.com.springboot.tgs.interfaces.RestControllerModel;
import br.com.springboot.tgs.repositories.UserRepository;

@RestController
@RequestMapping("/employees")
public class EmployeeController implements RestControllerModel<User, String> {
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

  public static final String PREFIX_EMPLOYEE_USER_ID = "EPL-";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  /**
   * Busca um funcionario pelo id
   * 
   * @param document - Recebe o documento do funcionario
   * @return - Retorna as informações do funcionario correspondente
   */
  @Override
  @GetMapping("/{document}")
  public ResponseEntity<Object> findById(@PathVariable("document") String document) {
    try {
      if (document == null || document.isEmpty() || !ValidateController.validateCPF(document)) {
        throw new IllegalArgumentException("Documento inválido");
      }

      String userId = PREFIX_EMPLOYEE_USER_ID + document;

      Optional<User> employeeFind = userRepository.findById(userId);

      if (employeeFind.isPresent()) {
        LOGGER.info("Search employee - " + userId);

        return ResponseEntity.status(HttpStatus.OK).body(employeeFind.get());
      } else {
        throw new IllegalArgumentException("Funcionario não encontrado");
      }
    } catch (Exception e) {
      LOGGER.info("Employee not found - " + e);
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
  }

  /**
   * Busca todos os funcionarios
   * 
   * @param status - Recebe o status do funcionario
   * @return - Busca a lista de funcionarios referentes ao status recebido
   */
  @Override
  @GetMapping("/list/{status}")
  public List<User> findByStatus(@PathVariable("status") Boolean status) {
    LOGGER.info("Search employees by status - " + status);

    return this.userRepository.findAllByStatus(status, PREFIX_EMPLOYEE_USER_ID);
  }

  /**
   * Registra|Atualiza um funcionario
   * 
   * @param employee - Recebe um funcionario para cadastrar ou atualizar
   * @return - Retorna uma mensagem de sucesso ou erro
   */
  @Override
  @PostMapping("/")
  public ResponseEntity<Object> createAndUpdate(@RequestBody User employee) {
    try {
      employee.setStatus(true);

      validateEmployee(employee);

      employee.setUserId(PREFIX_EMPLOYEE_USER_ID + employee.getDocument());

      Optional<User> user = userRepository.findById(employee.getUserId());

      if (user.isPresent()) {
        employee.setPassword(user.get().getPassword());
      } else {
        employee.setPassword(encoder.encode(employee.getPassword()));
      }

      this.userRepository.save(employee);

      LOGGER.warn("Create employee - " + employee.getUserId());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("Create employee fail - ", e);

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
  }

  /**
   * Desativa um funcionario
   * 
   * @param u - Recebe o documento do funcionario para desativar
   * @return - Retorna uma mensagem de sucesso ou erro
   */
  @Override
  @PostMapping("/remove")
  public ResponseEntity<Object> remove(@RequestBody User u) {
    try {
      if (u.getDocument() == null || u.getDocument().isEmpty() || !ValidateController.validateCPF(u.getDocument())) {
        throw new IllegalArgumentException("Documento inválido");
      }

      u.setUserId(PREFIX_EMPLOYEE_USER_ID + u.getDocument());

      User employee = userRepository.findById(u.getUserId()).get();

      employee.setStatus(false);

      this.userRepository.save(employee);

      LOGGER.warn("Remove employee - " + employee.getUserId());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("Remove employee fail - ", e);

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
  }

  /**
   * Valida os dados do funcionario
   * 
   * @param employee - Recebe o funcionario para validar
   */
  private void validateEmployee(User employee) {
    if (employee.getDocument() == null || employee.getDocument().isEmpty()
        || !ValidateController.validateCPF(employee.getDocument())) {
      throw new IllegalArgumentException("Documento inválido");
    }

    if (employee.getName() == null || employee.getName().isEmpty()
        || !ValidateController.validateText(employee.getName())) {
      throw new IllegalArgumentException("Nome inválido");
    }

    if (employee.getSurname() == null || employee.getSurname().isEmpty()
        || !ValidateController.validateText(employee.getSurname())) {
      throw new IllegalArgumentException("Sobrenome inválido");
    }

    if (employee.getEmail() == null || employee.getEmail().isEmpty()
        || !ValidateController.validateEmail(employee.getEmail())) {
      throw new IllegalArgumentException("Email inválido");
    }

    if (employee.getTelephone() != null && !employee.getTelephone().isEmpty()
        && !ValidateController.validateTelephone(employee.getTelephone())) {
      throw new IllegalArgumentException("Telefone inválido");
    }

    if (employee.getExpertise() != null && !employee.getExpertise().isEmpty()
        && !ValidateController.validateText(employee.getExpertise())) {
      throw new IllegalArgumentException("Especialidade inválida");
    }

    if (employee.getStatus() == null) {
      throw new IllegalArgumentException("Status inválido");
    }
  }
}