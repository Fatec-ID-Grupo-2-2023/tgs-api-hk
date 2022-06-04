package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.entities.Procedure;
import br.com.springboot.tgs.models.RepositoriesModel;

public interface ProcedureRepository extends RepositoriesModel<Procedure>, JpaRepository<Procedure, Integer> {
  /**
   * 
   * @param status - Recebe o status por parametro
   * @return - Retorna a lista de procedimentos referentes ao status recebido
   */
  @Query("SELECT u from Procedure u where u.status = :status")
  public List<Procedure> findAllByStatus(@Param("status") Boolean status);
}
