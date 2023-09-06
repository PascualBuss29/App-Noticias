package com.egg.noticia.controladores;

import com.egg.noticia.entidades.Noticia;
import com.egg.noticia.entidades.Periodista;
import com.egg.noticia.entidades.Usuario;
import com.egg.noticia.enumeraciones.Rol;
import com.egg.noticia.excepciones.MiExcepcion;
import com.egg.noticia.servicios.NoticiaServicio;
import com.egg.noticia.servicios.PeriodistaServicio;
import com.egg.noticia.servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    UsuarioServicio usuarioServicio;
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @Autowired
    PeriodistaServicio periodistaServicios;
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticia();
        
        Noticia not1 = noticias.get(0);
        Noticia not2 = noticias.get(1);
        Noticia not3 = noticias.get(2);
        
        modelo.addAttribute("not1", not1);
        modelo.addAttribute("not2", not2);
        modelo.addAttribute("not3", not3);
        return "panelAdmin.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/registro-periodista")
    public String registroPeriodista() {
        return "periodista_form.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/registro-periodista")
    public String registrarPeriodista(@RequestParam String nombre, @RequestParam String email, @RequestParam String password,
            @RequestParam String password2, @RequestParam MultipartFile archivo, @RequestParam Double sueldo, ModelMap modelo) {
        try {
            periodistaServicios.crearPeriodista(nombre, email, password, password2, sueldo);
            modelo.put("exito", "Se registró correctamente al Periodista");
            return "periodista_form.html";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "periodista_form.html";
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/lista-periodistas")
    public String listarPeriodistas(ModelMap modelo) {
        
        List<Periodista> periodistas = periodistaServicios.listarPeriodistas();
        
        modelo.addAttribute("periodistas", periodistas);
        
        return "periodista_list.html";
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/lista-admin")
    public String listarAdmins(ModelMap modelo) {
        
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        
        List<Usuario> administradores = new ArrayList<>();
        
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() == Rol.ADMIN) {
                administradores.add(usuario);
            }
        }
        
        modelo.addAttribute("administradores", administradores);
        
        return "admin_list.html";
    }
    
    @GetMapping("/actualizarPeriodista/{id}")
    public String actualizarPeriodista(@PathVariable Integer id, ModelMap modelo) {
        
        Periodista periodista = periodistaServicios.getOne(id);
        
        modelo.addAttribute("periodista", periodista);
        
        return "periodista_modificar.html";
    }
    
    @PostMapping("/actualizarPeriodista/{id}")
    public String actualizar(@PathVariable Integer id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam(required = false) MultipartFile archivo,
            @RequestParam Integer sueldo, ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(id, nombre, email, password, archivo);
            modelo.put("exito", "Se actualizaron los datos.");
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar.html";
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("admin/eliminar/{id}")
    public String eliminarPeriodista(@PathVariable Integer id, ModelMap modelo) {
        
        Periodista periodista = periodistaServicios.getOne(id);
        
        try {
            periodistaServicios.borrarPeriodista(id);
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:../lista";
        }
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        
        return "usuario_list";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable Integer id) {
        usuarioServicio.cambiarRol(id);
        
        return "redirect:/admin/usuarios";
    }
    
    
    @GetMapping("/modificar/{id}")
    public String modificarUsuario(@PathVariable Integer id, ModelMap modelo) {
        
        Usuario usuario = usuarioServicio.getOne(id);
        
        modelo.addAttribute("usuarios", usuario);
        
        return "usuario_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable Integer id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam(required = false) MultipartFile archivo,
            @RequestParam Integer sueldo, ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(id, nombre, email, password, archivo);
            modelo.put("exito", "Se actualizaron los datos.");
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar.html";
        }
    }
   
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id, ModelMap modelo) {
        
        Usuario usuario = usuarioServicio.getOne(id);
        
        try {
            usuarioServicio.borrarUsuario(id);
            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "redirect:../lista";
        }
    }
}
