
package com.egg.noticia.repositorios;

import com.egg.noticia.entidades.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagenRepositorio extends JpaRepository<Imagen, Integer> {
    
}
