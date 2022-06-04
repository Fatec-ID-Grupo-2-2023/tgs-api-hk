package br.com.springboot.tgs.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Schedule {
    private User dentist;
    private User employee;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime consultDuration;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate finalDate;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime startWorkHour;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime finalWorkHour;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime startLunchHour;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime finalLunchHour;

    
    // DENTIST
    public User getDentist() {
        return dentist;
    }

    public void setDentist(User dentist) {
        this.dentist = dentist;
    }
    
    // EMPLOYEE
    public User getEmployee() {
        return employee;
    }

    public void setEmployee(User employee) {
        this.employee = employee;
    }

    // CONSULT DURATION
    public LocalTime getConsultDuration() {
        return consultDuration;
    }

    public void setConsultDuration(LocalTime consultDuration) {
        this.consultDuration = consultDuration;
    }

    // START DATE
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // FINAL DATE
    public LocalDate getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(LocalDate finalDate) {
        this.finalDate = finalDate;
    }

    // START WORK HOUR
    public LocalTime getStartWorkHour() {
        return startWorkHour;
    }

    public void setStartWorkHour(LocalTime startWorkHour) {
        this.startWorkHour = startWorkHour;
    }

    // FINAL WORK HOUR
    public LocalTime getFinalWorkHour() {
        return finalWorkHour;
    }

    public void setFinalWorkHour(LocalTime finalWorkHour) {
        this.finalWorkHour = finalWorkHour;
    }

    // START LUNCH HOUR
    public LocalTime getStartLunchHour() {
        return startLunchHour;
    }

    public void setStartLunchHour(LocalTime startLunchHour) {
        this.startLunchHour = startLunchHour;
    }

    // FINAL LUNCH HOUR
    public LocalTime getFinalLunchHour() {
        return finalLunchHour;
    }

    public void setFinalLunchHour(LocalTime finalLunchHour) {
        this.finalLunchHour = finalLunchHour;
    }
}
