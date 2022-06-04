package br.com.springboot.tgs.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.springboot.tgs.entities.models.Consult;
import br.com.springboot.tgs.interfaces.RepositoriesModel;

public interface ConsultRepository extends RepositoriesModel<Consult>, JpaRepository<Consult, Integer>{  
  /**
   * 
   * @param status - Recebe o status por parametro
   * @return - Retorna a lista de consultas referentes ao status recebido
   */
  @Query("SELECT u from Consult u where u.status = :status")
  public List<Consult> findAllByStatus(@Param("status") Boolean status);

  /**
   * Busca no banco os dados para o grafico de linhas
   * @return - os dados
   */
  @Query("SELECT datename(mm, u.dateTime) as label, count(*) as value from Consult u where datediff(yy, u.dateTime, getdate()) in (0, 1) and u.status = 'true' group by datename(mm, dateTime), month(u.dateTime)")
  public List<Object> findLineChartData();

  /**
   * Busca no banco os dados para o grafico de pizza
   * @return - os dados
   */
  @Query("SELECT p.title, count(p.title) as total from Consult u inner join u.procedure p where u.status = 'true' and month(u.dateTime) = month(getdate()) group by p.title")
  public List<Object> findPieChartData();
}