package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.entities.Dentist;

public interface DentistRepository extends JpaRepository<Dentist, String> {
  public List<Dentist> findByNameIgnoreCase(String name);

  @Query("SELECT u from Dentist u where u.cro = :cro")
  public List<Dentist> findAllMoreThan(@Param("cro") String cro);

  @Query("SELECT u from Dentist u where u.status = :status")
  public List<Dentist> findAllByStatus(@Param("status") Boolean status);
}
