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

import br.com.springboot.tgs.models.User;
import br.com.springboot.tgs.repositories.UserRepository;

@RestController
@RequestMapping("/employees") 
public class EmployeeController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureController.class);

  private final String PREFIX_EMPLOYEE_USER_ID = "EPL";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping("/{user}")
  public Object findByUser(@PathVariable("user") String user) {

    Optional<User> employeeFind = userRepository.findById(user);

    if (employeeFind.isPresent()) {
      LOGGER.info("Search employee - " + employeeFind.get().getUserId());

      return ResponseEntity.status(HttpStatus.OK).body(employeeFind.get());      
    }
    LOGGER.info("Employee - " + employeeFind.get().getUserId() + " not found");

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
  }

  @GetMapping("/list/{status}")
  public List<User> listByStatus(@PathVariable("status") Boolean status) {
    LOGGER.info("Search employees by status - " + status);

    return this.userRepository.findAllByStatus(status, PREFIX_EMPLOYEE_USER_ID);
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> createAndUpdate(@RequestBody User employee) {
    try {
      employee.setUserId(PREFIX_EMPLOYEE_USER_ID + employee.getDocument());
      employee.setPassword(encoder.encode(employee.getPassword()));
      employee.setStatus(true);
      
      this.userRepository.save(employee);

      LOGGER.warn("Create employee - " + employee.getUserId());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);        
    } catch (Exception e) {
      LOGGER.error("Create employee fail - ", e.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PostMapping("/remove")
  public ResponseEntity<HttpStatus> remove(@RequestBody User employee) {
    try {
      employee.setUserId(PREFIX_EMPLOYEE_USER_ID + employee.getDocument());
      employee.setStatus(false);

      this.userRepository.save(employee);

      LOGGER.warn("Remove employee - " + employee.getUserId());

      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      LOGGER.error("Remove employee fail - ", e.getMessage());

      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}