package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.models.User;

public interface UserRepository extends JpaRepository<User, String> {
  public List<User> findByUserId(String userId);

  public List<User> findByDocument(String document);

  public List<User> findByNameIgnoreCase(String name);
  
  @Query("SELECT u from User u where u.status = :status")
  public List<User> findAllByStatus(@Param("status") Boolean status);
}
