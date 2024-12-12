package com.example.devops.dao.repositories;

import com.example.devops.dao.entities.Etudiant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EtudiantRepositoryTest {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @BeforeEach
    void setUp() {
        Etudiant etudiant1 = Etudiant.builder()
                .nomEt("Doe")
                .prenomEt("John")
                .cin(12345678L)
                .ecole("Ecole1")
                .dateNaissance(LocalDate.of(2000, 1, 1))
                .build();

        Etudiant etudiant2 = Etudiant.builder()
                .nomEt("Smith")
                .prenomEt("Jane")
                .cin(87654321L)
                .ecole("Ecole2")
                .dateNaissance(LocalDate.of(1998, 5, 15))
                .build();

        etudiantRepository.save(etudiant1);
        etudiantRepository.save(etudiant2);
    }

    @Test
    void shouldFindByNomEt() {
        List<Etudiant> etudiants = etudiantRepository.findByNomEt("Doe");
        assertThat(etudiants).hasSize(1);
        assertThat(etudiants.get(0).getPrenomEt()).isEqualTo("John");
    }

    @Test
    void shouldFindByNomEtAndPrenomEt() {
        List<Etudiant> etudiants = etudiantRepository.findByNomEtAndPrenomEt("Smith", "Jane");
        assertThat(etudiants).hasSize(1);
        assertThat(etudiants.get(0).getCin()).isEqualTo(87654321L);
    }

    @Test
    void shouldGetByDateNaissanceBetween() {
        List<Etudiant> etudiants = etudiantRepository.getByDateNaissanceBetween(
                LocalDate.of(1990, 1, 1), LocalDate.of(2005, 1, 1));
        assertThat(etudiants).hasSize(2);
    }
}
