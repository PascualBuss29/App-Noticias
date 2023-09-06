package com.egg.noticia.controladores;


import com.egg.noticia.entidades.Imagen;
import com.egg.noticia.entidades.Noticia;
import com.egg.noticia.entidades.Periodista;
import com.egg.noticia.entidades.Usuario;
import com.egg.noticia.servicios.ImagenServicio;
import com.egg.noticia.servicios.NoticiaServicio;
import com.egg.noticia.servicios.PeriodistaServicio;
import com.egg.noticia.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @Autowired
    PeriodistaServicio periodistaServicios;
    
    @Autowired
    ImagenServicio imagenServicio;
    
    //RECIBIR ID DE IMAGEN
    
    @GetMapping("/perfil/{id}")
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable Integer id) {
        Usuario usuario = usuarioServicio.getOne(id);
        
        byte[] imagen = usuario.getImagen().getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
    
    @GetMapping("/noticia/{id}")
    public ResponseEntity<byte[]> imagenNoticia(@PathVariable Integer id) {
        
        Noticia noticia = noticiaServicio.getOne(id);
        
        Imagen foto = imagenServicio.traerImagen(noticia.getImagen().getId());
        
        byte[] imagen = foto.getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
    
    @GetMapping("/periodista/{id}")
    public ResponseEntity<byte[]> imagenPeriodista(@PathVariable Integer id) {
        
        Periodista periodista = periodistaServicios.getOne(id);
        
        Imagen foto = imagenServicio.traerImagen(periodista.getImagen().getId());
        
        byte[] imagen = foto.getContenido();
        
        HttpHeaders headers = new HttpHeaders();
        
        headers.setContentType(MediaType.IMAGE_JPEG);
        
        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
    }
}