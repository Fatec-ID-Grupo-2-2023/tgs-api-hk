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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping("/{user}")
  public User user(@PathVariable("user") String user) {

    Optional<User> employeeFind = userRepository.findById(user);

    if (employeeFind.isPresent()) {
      return employeeFind.get();
    }

    return null;
  }

  @GetMapping("/list")
  public List<User> employees() {
    return this.userRepository.findAll();
  }
  
  @GetMapping("/list/document/{document}")
  public List<User> findByDocument(@PathVariable("document") String document) {
    return this.userRepository.findByDocument(document);
  }

  @GetMapping("/list/name/{name}")
  public List<User> findByName(@PathVariable("name") String name) {
    return this.userRepository.findByNameIgnoreCase(name);
  }

  @GetMapping("/list/status/{status}")
  public List<User> listByStatus(@PathVariable("status") Boolean status) {
    return this.userRepository.findAllByStatus(status);
  }

  @GetMapping("/validarSenha")
  public ResponseEntity<Boolean> login(@RequestBody User user) {
    Optional<User> optEmployee = userRepository.findById(user.getUserId());
    if(!optEmployee.isPresent()){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
    }

    User _employee = optEmployee.get();
    boolean valid = encoder.matches(user.getPassword(), _employee.getPassword());

    HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
    return ResponseEntity.status(status).body(valid);
  }

  @PostMapping("/")
  public ResponseEntity<HttpStatus> employee(@RequestBody User employee) {
    try {
      employee.setPassword(encoder.encode(employee.getPassword()));
      this.userRepository.save(employee);
      return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);        
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }
  }
}