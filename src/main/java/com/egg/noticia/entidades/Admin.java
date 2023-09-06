package com.egg.noticia.entidades;


import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Administrador")
public class Admin extends Usuario {

  
    private String cargo;
    private Double sueldoMensual;
    

  
}
