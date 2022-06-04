package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.models.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {
  /**
   * 
   * @param status - Recebe o status por parametro
   * @return - Retorna a lista de pacientes referentes ao status recebido
   */
  @Query("SELECT u from Patient u where u.status = :status")
  public List<Patient> findAllByStatus(@Param("status") Boolean status);
}
