package br.com.springboot.tgs.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consults")
public class Consult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // @Column(nullable = false)
    // private String patient;


    // @Column(nullable = false)
    // private String dentist;

    // @Column(nullable = false)
    // private String dateTime;


    // @Column(nullable = false)
    // private Integer procedure;

    // @Column(nullable = false)
    // private String employee;

    private Boolean status;

    // ID
    public Integer getId() {
        return id;
    }


    // STATUS
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}