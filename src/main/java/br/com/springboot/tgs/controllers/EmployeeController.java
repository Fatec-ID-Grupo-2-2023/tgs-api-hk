package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

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
  private final String PREFIX_EMPLOYEE_USER_ID = "EPL";

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping("/{user}")
  public Object findByUser(@PathVariable("user") String user) {

    Optional<User> employeeFind = userRepository.findById(user);

    if (employeeFind.isPresent()) {
      return ResponseEntity.status(HttpStatus.OK).body(employeeFind.get());      
    }

    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
  }      

  @GetMapping("/list")
  public List<User> employees() {
    return this.userRepository.findAllByStatus(true, PREFIX_EMPLOYEE_USER_ID);
  }

  @GetMapping("/list/{status}")
  public List<User> listByStatus(@PathVariable("status") Boolean status) {
    return this.userRepository.findAllByStatus(status, PREFIX_EMPLOYEE_USER_ID);
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> createAndUpdate(@RequestBody User employee) {
    try {
      employee.setUserId(PREFIX_EMPLOYEE_USER_ID + employee.getDocument());
      employee.setPassword(encoder.encode(employee.getPassword()));
      employee.setStatus(true);
      
      this.userRepository.save(employee);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);        
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }

  @PostMapping("/remove")
  public ResponseEntity<HttpStatus> remove(@RequestBody User employee) {
    try {
      employee.setStatus(false);
      this.userRepository.save(employee);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}