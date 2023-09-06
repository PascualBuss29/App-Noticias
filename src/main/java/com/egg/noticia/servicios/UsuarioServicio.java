package com.egg.noticia.servicios;

import com.egg.noticia.entidades.Imagen;
import com.egg.noticia.entidades.Usuario;
import com.egg.noticia.enumeraciones.Rol;
import com.egg.noticia.excepciones.MiExcepcion;
import com.egg.noticia.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    public UsuarioRepositorio usuarioRepositorio;

    @Autowired
    public ImagenServicio imagenServicio;

    @Transactional
    public void crearUsuario(String nombre, String email, String password,
            String password2, MultipartFile archivo) throws MiExcepcion {

        validar(nombre, email, password);
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas deben coincidir.");
        }
        if (archivo == null) {
            throw new MiExcepcion("La imagen está vacía.");
        }

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setAlta(new Date());

        if (nombre == "admin") {
            usuario.setRol(Rol.ADMIN);
        } else {
            usuario.setRol(Rol.USER);
        }
        usuario.setActivo(true);

        Imagen imagen = imagenServicio.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void actualizarUsuario(Integer id, String nombre, String email,
            String password, MultipartFile archivo) throws MiExcepcion {
        validar(nombre, email, password);
        if (id == null) {
            throw new MiExcepcion("El id de usuario está vacío.");
        }
        if (archivo == null) {
            throw new MiExcepcion("La imagen está vacía.");
        }

        Optional<Usuario> optionalUsuario = usuarioRepositorio.findById(id);

        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();

            usuario.setNombre(nombre);
            usuario.setEmail(email);

            if (password != null && !password.isEmpty()) {
                usuario.setPassword(password);
            }

            Integer idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }

    }

    @Transactional
    public void borrarUsuario(Integer id) throws MiExcepcion {
        if (id == null) {
            throw new MiExcepcion("El id está vacío.");
        }
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            usuarioRepositorio.deleteById(id);
        }
    }

    public Usuario getOne(Integer id) {
        return usuarioRepositorio.getOne(id);
    }

    public List<Usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public void validar(String nombre, String email, String password) throws MiExcepcion {
        if (nombre == null || nombre.isEmpty()) {
            throw new MiExcepcion("El nombre no debe estar vacío.");
        }
        if (email == null || email.isEmpty()) {
            throw new MiExcepcion("El email no debe estar vacío.");
        }
        if (password == null || password.isEmpty() || password.length() <= 5) {
            throw new MiExcepcion("La contraseña no debe estar vacía y debe ser mayor a 5 caracteres.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
    }

    @Transactional
    public void cambiarRol(Integer id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();

            if (usuario.getRol().equals(Rol.USER)) {
                usuario.setRol(Rol.ADMIN);
            } else {
                if (usuario.getRol().equals(Rol.ADMIN)) {
                    usuario.setRol(Rol.PER);
                } else {
                    if (usuario.getRol().equals(Rol.PER)) {
                        usuario.setRol(Rol.USER);
                    }

                }
            }
        }
    }
}
