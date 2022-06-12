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

import br.com.springboot.tgs.entities.models.User;
import br.com.springboot.tgs.interfaces.RestControllerModel;
import br.com.springboot.tgs.repositories.UserRepository;

@RestController
@RequestMapping("/dentists")
@CrossOrigin
public class DentistController implements RestControllerModel<User, String> {
    private static final Logger LOGGER = LoggerFactory.getLogger(DentistController.class);

    public static final String PREFIX_DENTIST_USER_ID = "DTN-";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * Busca um dentista pelo id
     * 
     * @param document - Recebe o documento do dentista
     * @return - Retorna as informações do dentista correspondente
     */
    @Override
    @GetMapping("/{document}")
    public ResponseEntity<Object> findById(@PathVariable("document") String document) {
        try {
            if (document == null || document.isEmpty()
                    || !ValidateController.validateCRO(document)) {
                throw new IllegalArgumentException("Documento inválido");
            }

            String userId = PREFIX_DENTIST_USER_ID + document;

            Optional<User> dentistFind = userRepository.findById(userId);

            if (dentistFind.isPresent()) {
                LOGGER.info("Search dentist - " + userId);

                return ResponseEntity.status(HttpStatus.OK).body(dentistFind.get());
            } else {
                throw new IllegalArgumentException("Dentista não encontrado");
            }
        } catch (Exception e) {
            LOGGER.info("Dentist not found - " + e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Busca todos os dentistas
     * 
     * @param status - Recebe o status do dentista
     * @return - Busca a lista de dentistas referentes ao status recebido
     */
    @Override
    @GetMapping("/list/{status}")
    public List<User> findByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search dentists by status - " + status);

        return this.userRepository.findAllByStatus(status, PREFIX_DENTIST_USER_ID);
    }

    /**
     * Registra|Atualiza um dentista
     * 
     * @param dentist - Recebe um dentista para cadastrar ou atualizar
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @Override
    @PostMapping("/")
    public ResponseEntity<Object> createAndUpdate(@RequestBody User dentist) {
        try {
            dentist.setStatus(true);

            validateDentist(dentist);

            dentist.setUserId(PREFIX_DENTIST_USER_ID + dentist.getDocument());

            Optional<User> user = userRepository.findById(dentist.getUserId());

            if (user.isPresent()) {
                dentist.setPassword(user.get().getPassword());
            } else {
                dentist.setPassword(encoder.encode(dentist.getPassword()));
            }

            this.userRepository.save(dentist);

            LOGGER.warn("Create dentist - " + dentist.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Create dentist fail - ", e);

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Desativa um dentista
     * 
     * @param u - Recebe o documento do dentista para desativar
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @Override
    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@RequestBody User u) {
        try {        
            if (u.getDocument() == null || u.getDocument().isEmpty()
                    || !ValidateController.validateCRO(u.getDocument())) {
                throw new IllegalArgumentException("Documento inválido");
            }

            u.setUserId(PREFIX_DENTIST_USER_ID + u.getDocument());

            User dentist = userRepository.findById(u.getUserId()).get();

            validateDentist(dentist);

            dentist.setStatus(false);

            this.userRepository.save(dentist);

            LOGGER.warn("Remove dentist - " + dentist.getUserId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Remove dentist fail - ", e);

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Valida os dados do dentista
     * 
     * @param dentist - Recebe o dentista para validar
     */
    private void validateDentist(User dentist) {
        if (dentist.getDocument() == null || dentist.getDocument().isEmpty()
                || !ValidateController.validateCRO(dentist.getDocument())) {
            throw new IllegalArgumentException("Documento inválido");
        }

        if (dentist.getName() == null || dentist.getName().isEmpty()
                || !ValidateController.validateText(dentist.getName())) {
            throw new IllegalArgumentException("Nome inválido");
        }

        if (dentist.getSurname() == null || dentist.getSurname().isEmpty()
                || !ValidateController.validateText(dentist.getSurname())) {
            throw new IllegalArgumentException("Sobrenome inválido");
        }

        if (dentist.getEmail() == null || dentist.getEmail().isEmpty()
                || !ValidateController.validateEmail(dentist.getEmail())) {
            throw new IllegalArgumentException("Email inválido");
        }

        if (dentist.getTelephone() != null && !dentist.getTelephone().isEmpty()
                && !ValidateController.validateTelephone(dentist.getTelephone())) {
            throw new IllegalArgumentException("Telefone inválido");
        }

        if (dentist.getExpertise() != null && !dentist.getExpertise().isEmpty()
                && !ValidateController.validateText(dentist.getExpertise())) {
            throw new IllegalArgumentException("Especialidade inválida");
        }

        if (dentist.getStatus() == null) {
            throw new IllegalArgumentException("Status inválido");
        }
    }
}