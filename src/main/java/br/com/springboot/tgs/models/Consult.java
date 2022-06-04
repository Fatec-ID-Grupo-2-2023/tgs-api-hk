package br.com.springboot.tgs.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "procedure_id")
    private Procedure procedure;

    // @Column(nullable = false)
    // private Integer procedure;

    // @Column(nullable = false)
    // private String employee;

    private Boolean status;

    // ID
    public Integer getId() {
        return id;
    }

    // PROCEDURE
    @JsonBackReference
    public Procedure getProcedure() {
        return procedure;
    }

    public void setProcedure(Procedure procedure) {
        this.procedure = procedure;
    }

    // STATUS
    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}