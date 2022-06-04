package br.com.springboot.tgs.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
public class DentistController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcedureController.class);

    private final String PREFIX_DENTIST_USER_ID = "DTN";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * 
     * @param user - Recebe o usuario do dentista por parametro
     * @return - Retorna as informações do dentista correspondente
     */
    @GetMapping("/{user}")
    public Object findByUser(@PathVariable("user") String user) {
        try {
            Optional<User> dentistFind = userRepository.findById(user);

        if (dentistFind.isPresent()) {
            LOGGER.info("Search dentist - " + user);

            return ResponseEntity.status(HttpStatus.OK).body(dentistFind.get());
        }
        } catch (Exception e) {
            LOGGER.info("Dentist - " + user + " not found");
        }
        
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }

    /**
     * 
     * @param status - Recebe o status do dentista
     * @return - Busca a lista de dentistas referentes ao status recebido
     */
    @GetMapping("/list/{status}")
    public List<User> listByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search dentists by status - " + status);

        return this.userRepository.findAllByStatus(status, PREFIX_DENTIST_USER_ID);
    }

    /**
     * 
     * @param dentist - Recebe um dentista para cadastrar ou atualizar no banco
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/")
    public ResponseEntity<Object> createAndUpdate(@RequestBody User dentist) {
        try {
            dentist.setUserId(PREFIX_DENTIST_USER_ID + dentist.getDocument());
            dentist.setPassword(encoder.encode(dentist.getPassword()));
            dentist.setStatus(true);

            this.userRepository.save(dentist);

            LOGGER.warn("Create dentist - " + dentist.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Create dentist fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
        }
    }

    /**
     * 
     * @param dentist - Recebe um dentista para remover do banco
     * @return - Retorna uma mensagem de sucesso ou erro 
     */
    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@RequestBody User dentist) {
        try {
            dentist.setUserId(PREFIX_DENTIST_USER_ID + dentist.getDocument());
            dentist.setStatus(false);

            this.userRepository.save(dentist);

            LOGGER.warn("Remove dentist - " + dentist.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Remove dentist fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
        }
    }
}