package com.egg.noticia.entidades;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;



@Entity
@lombok.Getter
@lombok.Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "periodista")
public class Periodista extends Usuario {

    private ArrayList<Noticia> misNoticias;
    private Double sueldo;
    private byte[] archivo;
    
    public void setArchivo(byte[] bytesArchivo) {
         this.archivo = bytesArchivo;
    }
 
}
