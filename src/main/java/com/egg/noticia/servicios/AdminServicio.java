package com.egg.noticia.servicios;

import com.egg.noticia.entidades.Imagen;
import com.egg.noticia.entidades.Noticia;
import com.egg.noticia.entidades.Periodista;
import com.egg.noticia.excepciones.MiExcepcion;
import com.egg.noticia.repositorios.NoticiaRepositorio;
import com.egg.noticia.repositorios.PeriodistaRepositorio;
import java.util.Date;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdminServicio {

    @Autowired
    private NoticiaRepositorio noticiaRepositorio;

    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void crearNoticia(String titulo, String cuerpo, Integer idPeriodista, MultipartFile archivo) throws MiExcepcion {
        validarNoticia(titulo, cuerpo, idPeriodista);

        Noticia noticia = new Noticia();
        Optional<Periodista> optionalPeriodista = periodistaRepositorio.findById(idPeriodista);
        if (optionalPeriodista.isPresent()) {
            Periodista periodista = optionalPeriodista.get();
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setCreador(periodista);
            noticia.setFechaDePublicacion(new Date());
            Imagen imagen = imagenServicio.guardar(archivo);
            noticia.setImagen(imagen);
            noticiaRepositorio.save(noticia);
        } else {
            throw new MiExcepcion("El id del periodista no es válido.");
        }
    }

    @Transactional
    public void modificarNoticia(Integer idNoticia, String titulo, String cuerpo, Integer idPeriodista, MultipartFile archivo) throws MiExcepcion {
        validarNoticia(titulo, cuerpo, idPeriodista);

        Optional<Noticia> optionalNoticia = noticiaRepositorio.findById(idNoticia);
        if (optionalNoticia.isPresent()) {
            Noticia noticia = optionalNoticia.get();
            Optional<Periodista> optionalPeriodista = periodistaRepositorio.findById(idPeriodista);
            if (optionalPeriodista.isPresent()) {
                Periodista periodista = optionalPeriodista.get();
                noticia.setTitulo(titulo);
                noticia.setCuerpo(cuerpo);
                noticia.setCreador(periodista);
                Imagen imagen = imagenServicio.actualizar(archivo, noticia.getImagen().getId());
                noticia.setImagen(imagen);
                noticiaRepositorio.save(noticia);
            } else {
                throw new MiExcepcion("El id del periodista no es válido.");
            }
        } else {
            throw new MiExcepcion("El id de la noticia no es válido.");
        }
    }

    @Transactional
    public void eliminarNoticia(Integer idNoticia) throws MiExcepcion {
        Optional<Noticia> optionalNoticia = noticiaRepositorio.findById(idNoticia);
        if (optionalNoticia.isPresent()) {
            Noticia noticia = optionalNoticia.get();
            noticiaRepositorio.delete(noticia);
        } else {
            throw new MiExcepcion("El id de la noticia no es válido.");
        }
    }

    public void darAltaPeriodista(Integer idPeriodista) throws MiExcepcion {
        Optional<Periodista> optionalPeriodista = periodistaRepositorio.findById(idPeriodista);
        if (optionalPeriodista.isPresent()) {
            Periodista periodista = optionalPeriodista.get();
            periodista.setActivo(true);
            periodistaRepositorio.save(periodista);
        } else {
            throw new MiExcepcion("El id del periodista no es válido.");
        }
    }

    public void darBajaPeriodista(Integer idPeriodista) throws MiExcepcion {
        Optional<Periodista> optionalPeriodista = periodistaRepositorio.findById(idPeriodista);

    }

    private void validarNoticia(String titulo, String cuerpo, Integer idPeriodista) throws MiExcepcion {
        if (titulo == null || titulo.isEmpty()) {
            throw new MiExcepcion("El título no debe estar vacío.");
        }
        if (cuerpo == null || cuerpo.isEmpty()) {
            throw new MiExcepcion("El cuerpo de la noticia no debe estar vacío.");
        }
        if (idPeriodista == null) {
            throw new MiExcepcion("El id del periodista no se reconoce.");
        } 
    }
}