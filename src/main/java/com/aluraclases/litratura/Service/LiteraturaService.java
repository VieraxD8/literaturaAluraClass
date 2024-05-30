package com.aluraclases.litratura.Service;

import com.aluraclases.litratura.Repository.AutorRepository;
import com.aluraclases.litratura.Repository.LibroRepository;
import com.aluraclases.litratura.estrucutradelJSON.Autor;
import com.aluraclases.litratura.estrucutradelJSON.Libro;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


import java.util.ArrayList;


@Service
public class LiteraturaService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void saveLibro(Libro libro) {

        List<Autor> autoresGuardados = new ArrayList<>();

        for (Autor autor : libro.getAuthors()) {
            if (autor.getId() == null) {
                Autor autorGuardado = autorRepository.save(autor);
                autor.setId(autorGuardado.getId());
                autoresGuardados.add(autorGuardado);
            } else {
                autoresGuardados.add(autor);
            }
        }
        libro.setAuthors(autoresGuardados);
        libroRepository.save(libro);
    }

    @Transactional(readOnly = true)
    public List<Libro> getAllLibros() {
        List<Libro> libros = libroRepository.findAll();
        for (Libro libro : libros) {
            Hibernate.initialize(libro.getLanguages());
        }
        return libros;
    }

    public List<Autor> getAutoresVivosEnAno(int anho) {
        List<Autor> autoresVivos = autorRepository .findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(anho, anho);
        autoresVivos.addAll(autorRepository.findByBirthYearLessThanEqualAndDeathYearIsNull(anho));
        return autoresVivos;
    }
}
