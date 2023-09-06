package com.egg.noticia.servicios;

import com.egg.noticia.entidades.Periodista;
import com.egg.noticia.entidades.Imagen;
import com.egg.noticia.entidades.Noticia;

import com.egg.noticia.excepciones.MiExcepcion;

import com.egg.noticia.repositorios.NoticiaRepositorio;
import com.egg.noticia.repositorios.PeriodistaRepositorio;
import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticiaServicio {
    
    @Autowired
    public NoticiaRepositorio noticiaRepositorio;
    
    @Autowired
    public PeriodistaRepositorio periodistaRepositorio;
    
    @Autowired
    public PeriodistaServicio periodistaServicios;
    
    @Autowired
    public ImagenServicio imagenServicio;
    
    @Transactional
    public void guardarNoticia(String titulo, String cuerpo, Integer idPeriodista, MultipartFile archivo) throws MiExcepcion {
        
        validar(titulo, cuerpo, idPeriodista);
        
        Noticia noticia = new Noticia();
        Periodista periodista = new Periodista();
        
        Optional<Periodista> respuesta = periodistaRepositorio.findById(idPeriodista);
        
        if (respuesta.isPresent()) {
            periodista = respuesta.get();
            
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setCreador(periodista);
            noticia.setFechaDePublicacion(new Date());
            
            Imagen imagen = imagenServicio.guardar(archivo);
            noticia.setImagen(imagen);
            
            noticiaRepositorio.save(noticia);
        }
    }
    
    @Transactional
    public void actualizarNoticia(Integer idNoticia, String titulo, String cuerpo, 
            Integer idPeriodista, MultipartFile archivo) throws MiExcepcion {
        if (idNoticia == null) {
            throw new MiExcepcion("El id de la noticia está vacío");
        }
        validar(titulo, cuerpo, idPeriodista);
        Noticia noticia = new Noticia();
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        Periodista periodista = new Periodista();
        Optional<Periodista> periodistaRespuesta = periodistaRepositorio.findById(idPeriodista);
        if (respuesta.isPresent() && periodistaRespuesta.isPresent()) {
            noticia = respuesta.get();
            periodista = periodistaRespuesta.get();
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);
            noticia.setCreador(periodista);
            
            Imagen imagen = imagenServicio.actualizar(archivo, noticia.getImagen().getId());
            noticia.setImagen(imagen);
            
            noticiaRepositorio.save(noticia);
        }
    }
    
    @Transactional
    public void borrarNoticia(Integer idNoticia) throws MiExcepcion {
        if (idNoticia == null) {
            throw new MiExcepcion("La noticia no se encuentra.");
        }
        
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        
        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            
            noticiaRepositorio.delete(noticia);
        }
    }
    
    public Noticia getOne(Integer id) {
        return noticiaRepositorio.getOne(id);
    }
    
    public List<Noticia> listarNoticia() {
        List<Noticia> noticia = new ArrayList();
        noticia = noticiaRepositorio.findAll();
        return noticia;
    }
   

      
    public void validar(String titulo, String cuerpo, Integer idPeriodista) throws MiExcepcion {
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
