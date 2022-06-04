package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.entities.User;

public interface UserRepository extends JpaRepository<User, String> { 
  /**
   * 
   * @param status - Recebe o status por parametro
   * @param prefixId - Recebe o prefixo do id de usuario por parametro
   * @return - Retorna a lista de procedimentos referentes ao status e prefixo recebidos
   */  
  @Query("SELECT u from User u where u.status = :status and u.userId like :prefixId%")
  public List<User> findAllByStatus(@Param("status") Boolean status, @Param("prefixId") String prefixId);
}
