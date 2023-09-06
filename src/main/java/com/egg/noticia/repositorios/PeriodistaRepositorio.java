
package com.egg.noticia.repositorios;

import com.egg.noticia.entidades.Periodista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodistaRepositorio extends JpaRepository<Periodista, Integer> {
    
    @Query("SELECT p FROM Periodista p WHERE p.email = :email")
    public Periodista buscarPeriodistaPorEmail(@Param("email") String email);
    
    @Query("SELECT p FROM Periodista p WHERE p.activo = :activo")
    public List<Periodista> listarPeriodistasActivos(@Param("activo") boolean activo);
    
    @Query("SELECT p FROM Periodista p ORDER BY sueldo")
    public List<Periodista> listarPeriodistasPorSueldo();
    
    @Query("SELECT p FROM Periodista p WHERE p.id =:id")
    Periodista findById(@Param("id")int id);
    
}
