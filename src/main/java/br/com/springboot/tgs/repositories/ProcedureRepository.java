package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.models.Procedure;

public interface ProcedureRepository extends JpaRepository<Procedure, Integer> {
  @Query("SELECT u from Procedure u where u.id > :id")
  public List<Procedure> findAllMoreThan(@Param("id") Integer id);

  @Query("SELECT u from Procedure u where u.status = :status")
  public List<Procedure> findAllByStatus(@Param("status") Boolean status);
}
