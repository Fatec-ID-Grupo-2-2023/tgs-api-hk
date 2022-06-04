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
public class EmployeeController implements RestControllerModel<User, String>{
  private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

  private final String PREFIX_EMPLOYEE_USER_ID = "EPL";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  /**
   * 
   * @param user - Recebe o usuario do funcionario por parametro
   * @return - Retorna as informações do funcionario correspondente
   */
  @Override
  @GetMapping("/{user}")
  public ResponseEntity<Object> findById(@PathVariable("user") String user) {
    try {
      Optional<User> employeeFind = userRepository.findById(user);

      if (employeeFind.isPresent()) {
        LOGGER.info("Search employee - " + user);

        return ResponseEntity.status(HttpStatus.OK).body(employeeFind.get());
      }
    } catch (Exception e) {
      LOGGER.info("Employee - " + user + " not found");
    }

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
  }

  /**
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
   * 
   * @param employee - Recebe um funcionario para cadastrar ou atualizar no banco
   * @return - Retorna uma mensagem de sucesso ou erro
   */
  @Override
  @PostMapping("/")
  public ResponseEntity<Object> createAndUpdate(@RequestBody User employee) {
    try {
      employee.setUserId(PREFIX_EMPLOYEE_USER_ID + employee.getDocument());
      employee.setPassword(encoder.encode(employee.getPassword()));
      employee.setStatus(true);

      this.userRepository.save(employee);

      LOGGER.warn("Create employee - " + employee.getUserId());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    } catch (Exception e) {
      LOGGER.error("Create employee fail - ", e.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }
  }

  /**
   * 
   * @param employee - Recebe um funcionario para remover do banco
   * @return - Retorna uma mensagem de sucesso ou erro 
   */
  @Override
  @PostMapping("/remove")
  public ResponseEntity<Object> remove(@RequestBody User employee) {
    try {
      employee.setUserId(PREFIX_EMPLOYEE_USER_ID + employee.getDocument());
      employee.setStatus(false);

      this.userRepository.save(employee);

      LOGGER.warn("Remove employee - " + employee.getUserId());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    } catch (Exception e) {
      LOGGER.error("Remove employee fail - ", e.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }
  }
}