package br.com.springboot.tgs.controllers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.springboot.tgs.entities.ConsultPlain;
import br.com.springboot.tgs.entities.Schedule;
import br.com.springboot.tgs.entities.models.Consult;
import br.com.springboot.tgs.interfaces.RestControllerModel;
import br.com.springboot.tgs.repositories.ConsultRepository;

@RestController
@RequestMapping("/consults")
@CrossOrigin
public class ConsultController implements RestControllerModel<Consult, Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultController.class);

    @Autowired
    private ConsultRepository consultRepository;

    /**
     * Busca uma consulta pelo id
     * 
     * @param id
     * @return - Retorna as informações da consulta correspondente
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable("id") Integer id) {
        try {
            Optional<Consult> consultFind = consultRepository.findById(id);

            if (consultFind.isPresent()) {
                LOGGER.info("Search consult - " + id);

                return ResponseEntity.status(HttpStatus.OK).body(consultFind.get());
            } else {
                throw new IllegalArgumentException("Consulta não encontrada");
            }
        } catch (Exception e) {
            LOGGER.info("Consult not found - " + e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Busca todas as consultas
     * 
     * @param status - Recebe o status da consulta [true|false]
     * @return - Busca a lista de consultas referentes ao status recebido
     */
    @Override
    @GetMapping("/list/{status}")
    public List<Consult> findByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search consults by status - " + status);

        return this.consultRepository.findAllByStatus(status);
    }

    /**
     * Buscar dados de consultas para o grafico de linhas
     * 
     * @return - os dados de consultas do ultimo dos ultimos 12 meses
     */
    @GetMapping("/chart/line")
    public ResponseEntity<Object> getChartLine() {
        try {
            List<Object> lineChartData = this.consultRepository.findLineChartData();

            return ResponseEntity.status(HttpStatus.OK).body(lineChartData);
        } catch (Exception e) {
            LOGGER.info("Unable to create the line chart - " + e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Buscar dados de consultas para o grafico de pizza
     * 
     * @return - os dados referentes aos procedimentos realizados no mês corrente
     */
    @GetMapping("/chart/pie")
    public ResponseEntity<Object> getChartPie() {
        try {
            List<Object> pieChartData = this.consultRepository.findPieChartData();

            return ResponseEntity.status(HttpStatus.OK).body(pieChartData);
        } catch (Exception e) {
            LOGGER.info("Unable to create the pie chart - " + e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Abrir agenda de consultas
     * 
     * @param schedule - Recebe os dados para abrir agenda
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/schedule/open")
    public ResponseEntity<Object> scheduleOpen(@RequestBody Schedule schedule) {
        try {
            while (!schedule.getStartDate().isAfter(schedule.getFinalDate())) {
                LocalTime currentWorkTime = schedule.getStartWorkHour();

                while (!currentWorkTime
                        .isAfter(schedule.getStartLunchHour().minusHours(schedule.getConsultDuration().getHour())
                                .minusMinutes(schedule.getConsultDuration().getMinute()))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime currentDateTime = LocalDateTime
                            .parse(schedule.getStartDate().toString() + " " + currentWorkTime.toString(), formatter);

                    Consult consult = new Consult(schedule.getDentist(), currentDateTime, schedule.getEmployee(),
                            false);

                    createAndUpdate(consult);

                    currentWorkTime = currentWorkTime.plusHours(schedule.getConsultDuration().getHour())
                            .plusMinutes(schedule.getConsultDuration().getMinute());
                }

                currentWorkTime = schedule.getFinalLunchHour();

                while (!currentWorkTime
                        .isAfter(schedule.getFinalWorkHour().minusHours(schedule.getConsultDuration().getHour())
                                .minusMinutes(schedule.getConsultDuration().getMinute()))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime currentDateTime = LocalDateTime
                            .parse(schedule.getStartDate().toString() + " " + currentWorkTime.toString(), formatter);

                    Consult consult = new Consult(schedule.getDentist(), currentDateTime, schedule.getEmployee(),
                            false);

                    createAndUpdate(consult);

                    currentWorkTime = currentWorkTime.plusHours(schedule.getConsultDuration().getHour())
                            .plusMinutes(schedule.getConsultDuration().getMinute());
                }

                schedule.setStartDate(schedule.getStartDate().plusDays(1));
            }

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Realiza um agendamento de consulta
     * 
     * @param consultPlain - Recebe uma consulta para ser agendada
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/")
    public ResponseEntity<Object> scheduleAppointment(@RequestBody ConsultPlain consultPlain) {
        try {
            consultPlain.setStatus(true);

            validateConsult(consultPlain);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime currentDateTime = LocalDateTime
                    .parse(consultPlain.getDate().toString() + " " + consultPlain.getHour().toString(), formatter);

            Consult consult = new Consult(consultPlain.getId(), consultPlain.getPatient(), consultPlain.getDentist(),
                    currentDateTime, consultPlain.getProcedure(), consultPlain.getEmployee(), true);

            createAndUpdate(consult);
            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Registra|Atualiza uma consulta
     * 
     * @param consult - Recebe uma consulta para cadastrar ou atualizar
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @Override
    public ResponseEntity<Object> createAndUpdate(Consult consult) {
        try {
            this.consultRepository.save(consult);

            LOGGER.warn("Create consult - " + consult.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Create consult fail - ", e);
            throw new IllegalArgumentException("Falha ao criar consulta - " + e);
        }
    }

    /**
     * Cancela|Remove uma consulta
     * 
     * @param consult - Recebe uma consulta para ser cancelada ou removida
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/remove")
    public ResponseEntity<Object> removeAppointment(@RequestBody ConsultPlain consultPlain) {
        try {            
            Consult consult = this.consultRepository.findById(consultPlain.getId()).get();

            remove(consult);
            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

    /**
     * Cancela|Remove uma consulta
     * 
     * @param consult - Recebe a consulta a ser cancelada ou removida
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @Override
    public ResponseEntity<Object> remove(Consult consult) {
        try {
            if (consult.getStatus() == true) {
                consult.setPatient(null);
                consult.setProcedure(null);
                consult.setStatus(false);

                this.consultRepository.save(consult);
            } else {
                this.consultRepository.delete(consult);
            }

            LOGGER.warn("Remove consult - " + consult.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Remove consult fail - ", e);

            throw new IllegalArgumentException("Falha ao remover consulta - " + e);
        }
    }

    /**
     * Valida os dados recebidos
     * 
     * @param consult - Recebe uma consulta para ser validada
     */
    private void validateConsult(ConsultPlain consult) {
        if (consult.getPatient().getCpf() == null || consult.getPatient().getCpf().isEmpty()
                || !ValidateController.validateCPF(consult.getPatient().getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }

        try {
            validateDentistAndEmployee(consult.getDentist().getUserId(), consult.getEmployee().getUserId());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }

        if (consult.getStatus() == null) {
            throw new IllegalArgumentException("Status inválido");
        }
    }

    /**
     * Valida os dados do dentista e do funcionário
     * 
     * @param dentistId  - Recebe o id do dentista
     * @param employeeId - Recebe o id do funcionário
     */
    private void validateDentistAndEmployee(String dentistId, String employeeId) {
        String[] dentistIdSplited = dentistId.split("-");
        String[] employeeIdSplited = employeeId.split("-");

        if (dentistId == null || dentistId.isEmpty()
                || !DentistController.PREFIX_DENTIST_USER_ID.equals(dentistIdSplited[0] + "-")
                || !ValidateController.validateCRO(dentistIdSplited[1])) {
            throw new IllegalArgumentException("Dentista inválido");
        }

        if ((employeeId == null || employeeId.isEmpty()
                || !EmployeeController.PREFIX_EMPLOYEE_USER_ID.equals(employeeIdSplited[0] + "-")
                || !ValidateController.validateCPF(employeeIdSplited[1]))
                && (dentistId == null || dentistId.isEmpty()
                        || !DentistController.PREFIX_DENTIST_USER_ID.equals(dentistIdSplited[0] + "-")
                        || !ValidateController.validateCRO(dentistIdSplited[1]))) {
            throw new IllegalArgumentException("Funcionário inválido");
        }
    }
}