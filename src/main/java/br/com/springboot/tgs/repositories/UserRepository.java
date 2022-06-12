package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.entities.models.User;
import br.com.springboot.tgs.interfaces.RepositoriesModel;

public interface UserRepository extends RepositoriesModel<User>, JpaRepository<User, String> {
  /**
   * Busca todos os dentistas|funcionarios
   * 
   * @param status - Recebe o status do dentista|funcionario
   * @return - Busca a lista de dentistas|funcionarios referentes ao status recebido
   */
  @Query("SELECT u from User u where u.status = :status and u.userId like :prefixId%")
  public List<User> findAllByStatus(@Param("status") Boolean status, @Param("prefixId") String prefixId);
}
