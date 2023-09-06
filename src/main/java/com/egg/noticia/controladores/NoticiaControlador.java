package com.egg.noticia.controladores;

import com.egg.noticia.entidades.Periodista;
import com.egg.noticia.entidades.Noticia;
import com.egg.noticia.excepciones.MiExcepcion;
import com.egg.noticia.servicios.AdminServicio;

import com.egg.noticia.servicios.NoticiaServicio;
import com.egg.noticia.servicios.PeriodistaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PER', 'ROLE_ADMIN')")
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    NoticiaServicio noticiaServicio;

    @Autowired
    PeriodistaServicio periodistaServicios;

  
    @GetMapping("/lista")
    public String lista_noticia(ModelMap modelo) {

        List<Noticia> noticia = noticiaServicio.listarNoticia();

        modelo.put("noticias", noticia);

        return "noticia_list.html";
    }

    @Autowired
    private AdminServicio adminServicio;

    @PostMapping
    public ResponseEntity<?> crearNoticia(@RequestParam("titulo") String titulo,
            @RequestParam("cuerpo") String cuerpo,
            @RequestParam("idPeriodista") Integer idPeriodista,
            @RequestParam("archivo") MultipartFile archivo) {
        try {
            adminServicio.crearNoticia(titulo, cuerpo, idPeriodista, archivo);
            return ResponseEntity.ok().build();
        } catch (MiExcepcion e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarNoticia(@PathVariable("id") Integer idNoticia,
            @RequestParam("titulo") String titulo,
            @RequestParam("cuerpo") String cuerpo,
            @RequestParam("idPeriodista") Integer idPeriodista,
            @RequestParam(value = "archivo", required = false) MultipartFile archivo) {
        try {
            adminServicio.modificarNoticia(idNoticia, titulo, cuerpo, idPeriodista, archivo);
            return ResponseEntity.ok().build();
        } catch (MiExcepcion e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarNoticia(@PathVariable("id") Integer idNoticia) {
        try {
            adminServicio.eliminarNoticia(idNoticia);
            return ResponseEntity.ok().build();
        } catch (MiExcepcion e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/periodistas/{id}/alta")
    public ResponseEntity<?> darAltaPeriodista(@PathVariable("id") Integer idPeriodista) {
        try {
            adminServicio.darAltaPeriodista(idPeriodista);
            return ResponseEntity.ok().build();
        } catch (MiExcepcion e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/periodistas/{id}/baja")
    public ResponseEntity<?> darBajaPeriodista(@PathVariable("id") Integer idPeriodista) {
        try {
            adminServicio.darBajaPeriodista(idPeriodista);
            return ResponseEntity.ok().build();
        } catch (MiExcepcion e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/escribir-noticia")
    public String registrarNoticia(ModelMap modelo) {

        List<Periodista> periodistas = periodistaServicios.listarPeriodistas();

        modelo.addAttribute("periodistas", periodistas);

        return "noticia_form.html";
    }

    @PostMapping("/registro")
    public String guardarNoticia(@RequestParam String titulo, @RequestParam String cuerpo,
            @RequestParam(required = false) Integer idPeriodista, @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            noticiaServicio.guardarNoticia(titulo, cuerpo, idPeriodista, archivo);
            modelo.put("exito", "Se guardó la noticia correctamente.");
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("cuerpo", cuerpo);
            modelo.put("idPeriodista", idPeriodista);
            return "noticia_form.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_PER', 'ROLE_ADMIN')")
    @GetMapping("/modificar/{id}")
    public String modificarNoticia(@PathVariable Integer id, ModelMap modelo) {
        List<Periodista> periodistas = periodistaServicios.listarPeriodistas();
        modelo.put("periodistas", periodistas);
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_modificar.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_PER', 'ROLE_ADMIN')")
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Integer id, @RequestParam String titulo,
            @RequestParam String cuerpo, @RequestParam Integer idPeriodista, @RequestParam MultipartFile archivo, ModelMap modelo) {
        try {
            noticiaServicio.actualizarNoticia(id, titulo, cuerpo, idPeriodista, archivo);
            modelo.put("exito", "Se actualizó la noticia correctamente.");
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put(("error"), ex.getMessage());
            modelo.put("titulo", titulo);
            modelo.put("cuerpo", cuerpo);
            modelo.put("idPeriodista", idPeriodista);
            return "noticia_modificar.html";
        }
    }

    @GetMapping("/leer/{id}")
    public String leerNoticia(@PathVariable Integer id, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_leer.html";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarNoticia(@PathVariable Integer id, ModelMap modelo) {

        Noticia noticia = noticiaServicio.getOne(id);

        try {
            noticiaServicio.borrarNoticia(noticia.getId());
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:../lista";
        }
    }
}
