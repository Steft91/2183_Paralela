/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package ec.edu.espe.concurrentefotos;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author andrespillajo, Stefany Díaz, 
 * Pablo Campoverde, Diego Delgado
 */
public class Imagenes {

    public static void main(String[] args) {
        try {
            File imagenesEntrada = new File("imagenes");
            File imagenesSalida = new File("imagenes_grises_concurrente");
                    
            if (!imagenesSalida.exists()){
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
                
                int altura = imagen.getHeight();
                int ancho = imagen.getWidth();
                System.out.println("Procesando " + imagenEntrada.getName() + " de " + ancho + "x" + altura);
                
                // Crear y asignar hilos
                int numeroHilos = 4; // Dividir en 4 partes
                Thread[] hilos = new Thread[numeroHilos];

                int filasPorHilo = altura / numeroHilos;
                int finFila;
                
                long inicio = System.nanoTime(); // Registrar tiempo inicial
            
                for (int i = 0; i < numeroHilos; i++) {
                    int inicioFila = i * filasPorHilo;

                    if(i == numeroHilos - 1){
                        finFila = altura;
                    }else{
                        finFila = inicioFila + filasPorHilo;
                    }

                    hilos[i] = new Thread(new FiltroGris(imagen, inicioFila, finFila));
                    hilos[i].start();
                }
                // Esperar a que todos los hilos terminen
                for (Thread hilo : hilos) {
                    hilo.join();
                }
                long fin = System.nanoTime(); // Tiempo final por imagen
                
                // Guardar la nueva imagen
                File imagenSalida = new File(imagenesSalida, "gris_" + imagenEntrada.getName() + ".");
                ImageIO.write(imagen, "png", imagenSalida);

                System.out.println("Imagen " + imagenEntrada.getName() + " procesada y guardada como " + imagenSalida.getName() + ".");
                System.out.println("Tiempo de ejecución para esta imagen: " + (fin - inicio) / 1_000_000 + " ms");
            }
            long finTotal = System.nanoTime(); // Tiempo final total
            System.out.println("Todas las imágenes procesadas.");
            System.out.println("Tiempo total de ejecución: " + (finTotal - inicioTotal) / 1_000_000 + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
