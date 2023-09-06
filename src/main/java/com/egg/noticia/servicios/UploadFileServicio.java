
package com.egg.noticia.servicios;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServicio {
    
    private String folder="img//";
    
    public String guardarImagen(MultipartFile archivo) throws IOException{
        if (!archivo.isEmpty()) {
            byte[] foto = archivo.getBytes();
            Path path = Paths.get(folder + archivo.getOriginalFilename());
            Files.write(path, foto);
            return archivo.getOriginalFilename();
        }
        return "default.jpg";
    }    
    
    public void borrarImagen(String nombre) {
        String ruta = "img//";
        File file = new File(ruta + nombre);
        file.delete();
    }
    
}
