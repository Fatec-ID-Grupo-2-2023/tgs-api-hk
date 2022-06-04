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

import br.com.springboot.tgs.entities.Consult;
import br.com.springboot.tgs.entities.LineChart;
import br.com.springboot.tgs.entities.Schedule;
import br.com.springboot.tgs.models.RestControllerModel;
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
    public ResponseEntity<HttpStatus> scheduleOpen(@RequestBody Schedule schedule) {
        try {           
            while(!schedule.getStartDate().isAfter(schedule.getFinalDate())){
                LocalTime currentWorkTime = schedule.getStartWorkHour();

                while(!currentWorkTime.isAfter(schedule.getStartLunchHour().minusHours(schedule.getConsultDuration().getHour()).minusMinutes(schedule.getConsultDuration().getMinute()))){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime currentDateTime = LocalDateTime.parse(schedule.getStartDate().toString() + " " + currentWorkTime.toString(), formatter);
                    
                    Consult consult = new Consult(schedule.getDentist(), currentDateTime, schedule.getEmployee(), true);
                                        
                    createAndUpdate(consult);

                    currentWorkTime = currentWorkTime.plusHours(schedule.getConsultDuration().getHour()).plusMinutes(schedule.getConsultDuration().getMinute());
                }
                
                currentWorkTime = schedule.getFinalLunchHour();

                while(!currentWorkTime.isAfter(schedule.getFinalWorkHour().minusHours(schedule.getConsultDuration().getHour()).minusMinutes(schedule.getConsultDuration().getMinute()))){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    LocalDateTime currentDateTime = LocalDateTime.parse(schedule.getStartDate().toString() + " " + currentWorkTime.toString(), formatter);
                    
                    Consult consult = new Consult(schedule.getDentist(), currentDateTime, schedule.getEmployee(), true);
                                        
                    createAndUpdate(consult);

                    currentWorkTime = currentWorkTime.plusHours(schedule.getConsultDuration().getHour()).plusMinutes(schedule.getConsultDuration().getMinute());
                }

                schedule.setStartDate(schedule.getStartDate().plusDays(1));
            }

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
    @PostMapping("/")
    public ResponseEntity<Object> createAndUpdate(@RequestBody Consult consult) {
        try {
            consult.setStatus(true);

            this.consultRepository.save(consult);

            LOGGER.warn("Create consult - " + consult.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Create consult fail - ", e.getMessage());

            // return
            // ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(consult);
        }
    }

    /**
     * 
     * @param consult - Recebe a consulta a ser removida do banco
     * @return - Retorna uma mensagem de sucesso ou erro
     */
    @Override
    @PostMapping("/remove")
    public ResponseEntity<Object> remove(@RequestBody Consult consult) {
        try {
            consult.setPatient(null);
            consult.setProcedure(null);

            this.consultRepository.save(consult);

            LOGGER.warn("Remove consult - " + consult.getId());

            return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
        } catch (Exception e) {
            LOGGER.error("Remove consult fail - ", e.getMessage());

            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(HttpStatus.NOT_ACCEPTABLE.toString());
        }
    }
}