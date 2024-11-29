/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ec.edu.espe.secuencialfotos;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Stefany Díaz, 
 * Pablo Campoverde, Diego Delgado
 * SECUENCIAL
 */
public class ImagenesSinHilos {

    public static void main(String[] args) {
        try {
            File imagenesEntrada = new File("imagenes"); 
            File imagenesSalida = new File("imagenes_grises_secuencial");
            
            if (!imagenesSalida.exists()) {
                imagenesSalida.mkdirs();
            }
            
            File[] archivos = imagenesEntrada.listFiles((dir, name) -> name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith("jpeg"));
            
            if (archivos == null || archivos.length == 0){
                System.out.println("No se encontraron imágenes");
                return;
            }
            
            System.out.println("Procesando " + archivos.length + " imágenes...");
           
            long inicioTotal = System.nanoTime(); // Tiempo inicial total
            

            for (File imagenEntrada : archivos){
                BufferedImage imagen = ImageIO.read(imagenEntrada);
                
                if (imagen == null){
                    System.out.println("No se pudo cargar la imagen: " + imagenEntrada.getName());
                    continue;
                }
                 // Obtener dimensiones de la imagen
                int ancho = imagen.getWidth();
                int alto = imagen.getHeight();
            
                System.out.println("Procesando " + imagenEntrada.getName() + " de " + ancho + " x " + alto + " píxeles.");

                long inicio = System.nanoTime(); // Registrar tiempo inicial
                
                // Recorrer cada píxel de la imagen
                for (int y = 0; y < alto; y++) {
                    for (int x = 0; x < ancho; x++) {
                        // Obtener el valor ARGB del píxel
                        int pixel = imagen.getRGB(x, y);

                        // Extraer componentes de color
                        int alpha = (pixel >> 24) & 0xff; // Componente Alpha
                        int red = (pixel >> 16) & 0xff;   // Componente Rojo
                        int green = (pixel >> 8) & 0xff;  // Componente Verde
                        int blue = pixel & 0xff;          // Componente Azul

                        // Calcular el promedio para escala de grises
                        int gris = (red + green + blue) / 3;

                        // Crear el nuevo color en escala de grises
                        int nuevoPixel = (alpha << 24) | (gris << 16) | (gris << 8) | gris;

                        // Asignar el nuevo color al píxel
                        imagen.setRGB(x, y, nuevoPixel);
                    }
                }
                
                long fin = System.nanoTime(); // Registrar tiempo final
                
                // Guardar la imagen resultante
                File imagenSalida = new File(imagenesSalida, "gris_" + imagenEntrada.getName()); // Cambia "imagen_gris.png" por la ruta de tu imagen
                ImageIO.write(imagen, "jpg", imagenSalida);
                
                System.out.println("Imagen " + imagenEntrada.getName() + " procesada y guardada com o" + imagenSalida.getName() + ".");
                System.out.println("Tiempo de ejecución: " + (fin - inicio) / 1_000_000 + " ms");
            }
            
            long finTotal = System.nanoTime();
            System.out.println("Todas las imágenes procesadas.");
            System.out.println("Tiempo total de ejecución: " + (finTotal - inicioTotal)/ 1_000_000 + "ms");    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
