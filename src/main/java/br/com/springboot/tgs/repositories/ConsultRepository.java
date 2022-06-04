package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.models.Consult;

public interface ConsultRepository extends JpaRepository<Consult, Integer> {  
  @Query("SELECT u from Consult u where u.status = :status")
  public List<Consult> findAllByStatus(@Param("status") Boolean status);
}