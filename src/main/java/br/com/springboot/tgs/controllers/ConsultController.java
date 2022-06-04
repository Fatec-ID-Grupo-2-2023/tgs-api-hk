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
import org.springframework.web.server.ResponseStatusException;

import br.com.springboot.tgs.entities.ConsultPlain;
import br.com.springboot.tgs.entities.LineChart;
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
     * 
     * @param id - Recebe o id da consulta por parametro
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
            }
        } catch (Exception e) {
            LOGGER.info("Consult - " + id + " not found");
        }

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
    }

    /**
     *
     * @param status - Recebe o status da consulta
     * @return - Busca a lista de consultas referentes ao status recebido
     */
    @Override
    @GetMapping("/list/{status}")
    public List<Consult> findByStatus(@PathVariable("status") Boolean status) {
        LOGGER.info("Search consults by status - " + status);

        return this.consultRepository.findAllByStatus(status);
    }

    /**
     * Buscar dados para o grafico de linhas
     * 
     * @return - os dados
     */
    @GetMapping("/chart/line")
    public ResponseEntity<Object> getChartLine() {
        try {
            String[] labels = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
            int[] data = { 0, 10000, 5000, 15000, 10000, 20000, 15000, 25000, 20000, 30000, 25000, 40000 };
            LineChart lc = new LineChart(labels, data);
            return ResponseEntity.status(HttpStatus.OK).body(lc);
        } catch (Exception e) {
            LOGGER.info("Unable to create the line chart");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
    }

    /**
     * 
     * @param schedule - Recebe os dados para abrir agenda
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/schedule/open")
    public ResponseEntity<Object> scheduleOpen(@RequestBody Schedule schedule) {
        try {
            Consult test = null;
            while (!schedule.getStartDate().isAfter(schedule.getFinalDate())) {
                LocalTime currentWorkTime = schedule.getStartWorkHour();

                while (!currentWorkTime
                        .isAfter(schedule.getStartLunchHour().minusHours(schedule.getConsultDuration().getHour())
                                .minusMinutes(schedule.getConsultDuration().getMinute()))) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime currentDateTime = LocalDateTime
                            .parse(schedule.getStartDate().toString() + " " + currentWorkTime.toString(), formatter);

                    Consult consult = new Consult(schedule.getDentist(), currentDateTime, schedule.getEmployee(), true);

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

                    Consult consult = new Consult(schedule.getDentist(), currentDateTime, schedule.getEmployee(), true);
                    test = consult;
                    createAndUpdate(consult);

                    currentWorkTime = currentWorkTime.plusHours(schedule.getConsultDuration().getHour())
                            .plusMinutes(schedule.getConsultDuration().getMinute());
                }

                schedule.setStartDate(schedule.getStartDate().plusDays(1));
            }

            return ResponseEntity.status(HttpStatus.OK).body(test);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 
     * @param consultPlain - Recebe uma consulta para ser agendada
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/")
    public ResponseEntity<HttpStatus> scheduleAppointment(@RequestBody ConsultPlain consultPlain) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime currentDateTime = LocalDateTime
                    .parse(consultPlain.getDate().toString() + " " + consultPlain.getHour().toString(), formatter);

            Consult consult = new Consult(consultPlain.getId(), consultPlain.getPatient(), consultPlain.getDentist(),
                    currentDateTime, consultPlain.getProcedure(), consultPlain.getEmployee(), true);

            createAndUpdate(consult);
            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 
     * @param consult - Recebe uma consulta para cadastrar ou atualizar no banco
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @Override
    public ResponseEntity<Object> createAndUpdate(Consult consult) {
        try {
            consult.setStatus(true);

            this.consultRepository.save(consult);

            LOGGER.warn("Create consult - " + consult.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("Create consult fail - ", e);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Create consult fail - " + e);
        }
    }

    /**
     * 
     * @param consult - Recebe uma consulta para ser cancelada
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @PostMapping("/remove")
    public ResponseEntity<HttpStatus> removeAppointment(@RequestBody ConsultPlain consultPlain) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime currentDateTime = LocalDateTime
                    .parse(consultPlain.getDate().toString() + " " + consultPlain.getHour().toString(), formatter);

            Consult consult = new Consult(consultPlain.getId(), consultPlain.getPatient(), consultPlain.getDentist(),
                    currentDateTime, consultPlain.getProcedure(), consultPlain.getEmployee(), consultPlain.getStatus());

            remove(consult);
            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    /**
     * 
     * @param consult - Recebe a consulta a ser removida do banco
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @Override
    public ResponseEntity<Object> remove(Consult consult) {
        try {                    
            if(consult.getStatus() == true){
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

            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Remove consult fail - " + e);
        }
    }
}