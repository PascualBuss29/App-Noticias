package com.egg.noticia.repositorios;

import com.egg.noticia.entidades.Noticia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, Integer> {

    @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    public Noticia buscarPorTitulo(@Param("titulo") String titulo);
    
    @Query("SELECT n FROM Noticia n WHERE n.creador.nombre = :nombre")
    public Noticia buscarPorNombreDePeriodista(@Param("nombre")String nombre);

}
