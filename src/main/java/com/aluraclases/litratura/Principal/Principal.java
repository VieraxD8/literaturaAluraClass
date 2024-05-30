package com.aluraclases.litratura.Principal;

import com.aluraclases.litratura.API.ApiRequest;
import com.aluraclases.litratura.Service.LiteraturaService;
import com.aluraclases.litratura.estrucutradelJSON.Autor;
import com.aluraclases.litratura.estrucutradelJSON.Libro;
import com.aluraclases.litratura.estrucutradelJSON.Resultado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


import java.io.IOException;

@Component
public class Principal {

    @Autowired
    private LiteraturaService literaturaService;
    private Scanner teclado = new Scanner(System.in);
    ApiRequest apiRequest = new ApiRequest();


    public void muestraElMenu() {

        var opcion = -1;
        while(opcion != 0){
            var menu = """
                1- GurdarDB
                2- Buscar libros por titulos
                3- Listar libros registrados
                4- Listar Autores Registrados
                5- Listar Autores vivos en un determinado año
                6- Listar libros por idiomas
             
                0 - Salir
             
             """;

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion){

                case 1: GuardarDB(apiRequest);
                    break;
                case 2:
                    buscarlibrostitulos(apiRequest, teclado);
                    break;
                case 3:
                    librosRegistrados(apiRequest);
                    break;
                case 4:
                    buscarRegistrosAutores(apiRequest);
                    break;
                case 5:
                    autoresVivosAnho();
                    break;
                case 6: CuantosLibrosHayPorIdiomas();
                    break;



                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }


        }

    }

    private void GuardarDB(ApiRequest apiRequest){

        Resultado resultado = null;
        try {
            resultado = apiRequest.urlLibro("https://gutendex.com/books/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Libro libro: resultado.getResults()){
            System.out.println("ID: " + libro.getId());
            System.out.println("Titulo: " + libro.getTitle());
            System.out.println("Autor: " + libro.getAuthors().get(0).getName() );
            System.out.println("Idioma: " + libro.getLanguages().get(0));
            System.out.println("Numero de Descargas : " + libro.getDownload_count());
            literaturaService.saveLibro(libro);
            System.out.println("Libro guardado: " + libro.getTitle());
            System.out.println();
        }

    }


    private void buscarlibrostitulos(ApiRequest apiRequest, Scanner teclado) {

        System.out.println("Escribe el nombre del libro que deseas buscar");
        var nombreLibro = teclado.nextLine();

        Resultado resultado = null;
        try {
            resultado = apiRequest.urlLibro("https://gutendex.com/books/?search="+nombreLibro.replace(" ","+"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Libro libro : resultado.getResults()) {
            System.out.println("********************** LIBROS *************************");
            System.out.println("Título: " + libro.getTitle());
            System.out.println("Autor: " + libro.getAuthors().get(0).getName());
            System.out.println("Idiomas: " + String.join(", ", libro.getLanguages()));
            System.out.println("Número de Descargas: " + libro.getDownload_count());

            System.out.println("********************************************************");


        }

    }

    private void librosRegistrados(ApiRequest apiRequest){

        Resultado resultado = null;
        try {
            resultado = apiRequest.urlLibro("https://gutendex.com/books/");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for (Libro libro: resultado.getResults()){
            System.out.println("********************** LIBROS  REGISTRADOS **********************");
            System.out.println("Titulo: " + libro.getTitle());
            System.out.println("Autor: " + libro.getAuthors().get(0).getName() );
            System.out.println("Idioma: " + libro.getLanguages().get(0));
            System.out.println("Numero de Descargas : " + libro.getDownload_count());
            System.out.println("*****************************************************************");

        }

    }



    private void buscarRegistrosAutores(ApiRequest apiRequest){

        Resultado resultado = null;
        try {
            resultado = apiRequest.urlLibro("https://gutendex.com/books/");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Map<String, List<String>> librosPorAutor = new HashMap<>();
        Map<String, Integer> birthYearPorAutor = new HashMap<>();
        Map<String, Integer> deathYearPorAutor = new HashMap<>();

        for (Libro libro : resultado.getResults()) {
            String autor = libro.getAuthors().get(0).getName();
            int birthYear = libro.getAuthors().get(0).getBirth_year();
            int deathYear = libro.getAuthors().get(0).getDeath_year();

            if (!librosPorAutor.containsKey(autor)) {
                librosPorAutor.put(autor, new ArrayList<>());
                birthYearPorAutor.put(autor, birthYear);
                deathYearPorAutor.put(autor, deathYear);
            }
            librosPorAutor.get(autor).add(libro.getTitle());
        }

        for (Map.Entry<String, List<String>> entry : librosPorAutor.entrySet()) {
            String autor = entry.getKey();
            System.out.println("Autor: " + autor);
            System.out.println("Fecha de nacimiento: " + birthYearPorAutor.get(autor));
            System.out.println("Fecha de fallecimiento: " + deathYearPorAutor.get(autor));
            System.out.println("Libros:");
            for (String tituloLibro : entry.getValue()) {
                System.out.println("  - " + tituloLibro);
            }
            System.out.println();
        }
    }

    private void autoresVivosAnho() {

        System.out.println("Ingrese el año para buscar autores vivos:");
        int anho;
        try {
            anho = teclado.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, ingrese un número.");
            teclado.nextLine(); // limpiar el buffer
            return;
        }

        List<Autor> autoresVivos = literaturaService.getAutoresVivosEnAno(anho);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anho);
        } else {
            for (Autor autor : autoresVivos) {
                System.out.println("Autor: " + autor.getName());
                System.out.println("Fecha de nacimiento: " + autor.getBirth_year());
                System.out.println("Fecha de fallecimiento: " + (autor.getDeath_year() != 0 ? autor.getDeath_year() : "N/A"));
                System.out.println();
            }
        }
        

    }


    public void CuantosLibrosHayPorIdiomas() {

        System.out.println("Escribe el idioma que deseas buscar: Inglés(en), Español(es) o Tagalo(tl)");
        String idiomaIngresado = teclado.nextLine().toLowerCase();

        List<String> idiomasDisponibles = Arrays.asList("es", "en", "tl");

        if (!idiomasDisponibles.contains(idiomaIngresado)) {
            System.out.println("Idioma no válido. Por favor, ingresa un idioma válido.");
            return;
        }

        List<Libro> libros = literaturaService.getAllLibros();

        long cantidadLibros = libros.stream()
                .filter(libro -> libro.getLanguages().contains(idiomaIngresado))
                .count();

        String idiomaMostrado = idiomaIngresado.equals("es") ? "Español" : idiomaIngresado.equals("en") ? "Inglés" : "Tagalo";

        System.out.println("Idioma: " + idiomaMostrado + ", Cantidad de Libros: " + cantidadLibros);


    }

}
