package com.aluraclases.litratura.Repository;

import com.aluraclases.litratura.estrucutradelJSON.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    @Query("SELECT a FROM Autor a WHERE a.birth_year <= :birth_year AND a.death_year >= :death_year")
    List<Autor> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqual(@Param("birth_year") int birthYear, @Param("death_year") int deathYear);

    @Query("SELECT a FROM Autor a WHERE a.birth_year <= :birth_year AND a.death_year IS NULL")
    List<Autor> findByBirthYearLessThanEqualAndDeathYearIsNull(@Param("birth_year") int birthYear);


}
