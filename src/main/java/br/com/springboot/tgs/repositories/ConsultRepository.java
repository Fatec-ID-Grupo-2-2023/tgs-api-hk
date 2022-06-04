package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.entities.Consult;
import br.com.springboot.tgs.models.RepositoriesModel;

public interface ConsultRepository extends RepositoriesModel<Consult>, JpaRepository<Consult, Integer>{  
  /**
   * 
   * @param status - Recebe o status por parametro
   * @return - Retorna a lista de consultas referentes ao status recebido
   */
  @Query("SELECT u from Consult u where u.status = :status")
  public List<Consult> findAllByStatus(@Param("status") Boolean status);
}