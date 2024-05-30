package com.aluraclases.litratura.Repository;

import com.aluraclases.litratura.estrucutradelJSON.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LibroRepository extends JpaRepository<Libro, Long> {


}
