
package com.egg.noticia.servicios;

import com.egg.noticia.entidades.Periodista;
import com.egg.noticia.enumeraciones.Rol;
import com.egg.noticia.excepciones.MiExcepcion;
import com.egg.noticia.repositorios.PeriodistaRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PeriodistaServicio {
    @Autowired
    public PeriodistaRepositorio periodistaRepositorio;

    @Transactional
    public void crearPeriodista(String nombre, String email, String password,
            String password2, Double sueldoMensual) throws MiExcepcion {
        validar(nombre, email, password);
        if (!password.equals(password2)) {
            throw new MiExcepcion("Las contraseñas deben coincidir.");
        }
        if (sueldoMensual == null) {
            throw new MiExcepcion("Debe ingresar el sueldo.");
        }

        Periodista periodista = new Periodista();

        periodista.setNombre(nombre);
        periodista.setEmail(email);
        periodista.setPassword(new BCryptPasswordEncoder().encode(password));
        periodista.setAlta(new Date());
        periodista.setRol(Rol.PER);
        periodista.setActivo(true);
        periodista.setSueldo(sueldoMensual);

        periodistaRepositorio.save(periodista);
    }

    @Transactional
    public void modificarPeriodista(Integer id, String nombre, String email,
            String password, Double sueldoMensual) throws MiExcepcion {

        validar(nombre, email, password);

        if (id == null) {
            throw new MiExcepcion("El id está vacío.");
        }
        if (sueldoMensual == null) {
            throw new MiExcepcion("Debe ingresar el sueldo.");
        }

        Periodista periodista = new Periodista();

        Optional<Periodista> respuesta = periodistaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            periodista = respuesta.get();

            periodista.setNombre(nombre);
            periodista.setEmail(email);
            periodista.setPassword(password);
            periodista.setSueldo(sueldoMensual);

            periodistaRepositorio.save(periodista);
        }
    }

    @Transactional
    public void borrarPeriodista(Integer id) throws MiExcepcion {
        if (id == null) {
            throw new MiExcepcion("El id está vacío.");
        }
        periodistaRepositorio.deleteById(id);
    }

    public Periodista getOne(Integer id) {
        return periodistaRepositorio.getOne(id);
    }

    public List<Periodista> listarPeriodistas() {
        return periodistaRepositorio.findAll();
    }

    @Transactional
    public void modificarAltaPeriodista(Integer idPeriodista, boolean activo) throws MiExcepcion {
        if (idPeriodista == null) {
            throw new MiExcepcion("El id del periodista está vacío.");
        }
        Periodista periodista = new Periodista();

        Optional<Periodista> respuesta = periodistaRepositorio.findById(idPeriodista);

        if (respuesta.isPresent()) {
            periodista = respuesta.get();

            periodista.setActivo(activo);

            periodistaRepositorio.save(periodista);
        }

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

     
    
}
