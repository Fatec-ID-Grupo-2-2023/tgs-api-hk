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
@RequestMapping("/dentists")
public class DentistController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureController.class);

    private final String PREFIX_DENTIST_USER_ID = "DTN";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @GetMapping("/{user}")
    public Object findByUser(@PathVariable("user") String user) {
        Optional<User> dentistFind = userRepository.findById(user);

        if (dentistFind.isPresent()) {
            LOGGER.info("Search dentist - " + dentistFind.get().getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(dentistFind.get());
        }

        LOGGER.info("Dentist - " + dentistFind.get().getUserId() + " not found");

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }

    @GetMapping("/list/{status}")
    public List<User> listByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search dentists by status - " + status);

        return this.userRepository.findAllByStatus(status, PREFIX_DENTIST_USER_ID);
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> createAndUpdate(@RequestBody User dentist) {
        try {
            dentist.setUserId(PREFIX_DENTIST_USER_ID + dentist.getDocument());
            dentist.setPassword(encoder.encode(dentist.getPassword()));
            dentist.setStatus(true);

            this.userRepository.save(dentist);

            LOGGER.warn("Create dentist - " + dentist.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Create dentist fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/remove")
    public ResponseEntity<HttpStatus> remove(@RequestBody User dentist) {
        try {
            dentist.setUserId(PREFIX_DENTIST_USER_ID + dentist.getDocument());
            dentist.setStatus(false);

            this.userRepository.save(dentist);

            LOGGER.warn("Remove dentist - " + dentist.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Remove dentist fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}