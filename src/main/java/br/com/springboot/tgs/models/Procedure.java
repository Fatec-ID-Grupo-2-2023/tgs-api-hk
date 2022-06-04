package br.com.springboot.tgs.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "procedures")
public class Procedure {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  @Column(nullable = false, unique = true)
  private String title;
  private String description;
  private Boolean status;

  @OneToMany(
    mappedBy = "procedure",
    cascade = CascadeType.ALL,
    orphanRemoval = true
  )
  private List<Consult> consults = new ArrayList<>();

  // ID
  public Integer getId() {
    return id;
  }

  // TITLE
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  // DESCRIPTION
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  // STATUS
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  //
  public List<Consult> getConsults() {
    return consults;
  }

  public void setConsults(List<Consult> consults) {
    this.consults = consults;
  }
}