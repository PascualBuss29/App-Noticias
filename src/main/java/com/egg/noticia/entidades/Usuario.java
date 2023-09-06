package com.egg.noticia.entidades;

import com.egg.noticia.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
@lombok.Getter
@lombok.Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nombre;
    private String email;
    private String password;
   

    @Temporal(TemporalType.DATE)
    protected Date alta;

    @Enumerated(EnumType.STRING)
    private Rol rol;
    
    protected boolean activo;

    @OneToOne
    private Imagen imagen;

}
