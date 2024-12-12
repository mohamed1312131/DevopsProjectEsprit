package com.example.devops.dao.repositories;


import com.example.devops.dao.entities.Bloc;
import com.example.devops.dao.entities.Chambre;
import com.example.devops.dao.entities.TypeChambre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ChambreRepositoryTest {

    @Autowired
    private ChambreRepository chambreRepository;

    private Bloc testBloc;
    private Chambre testChambre;

    @BeforeEach
    void setUp() {
        // Initialize test data
        testBloc = Bloc.builder().nomBloc("Test Bloc").build();
        testChambre = Chambre.builder()
                .numeroChambre(101L)
                .typeC(TypeChambre.SIMPLE)
                .bloc(testBloc)
                .build();

        chambreRepository.save(testChambre);
    }

    @Test
    void save_ShouldPersistChambre() {
        Chambre chambre = Chambre.builder()
                .numeroChambre(102L)
                .typeC(TypeChambre.DOUBLE)
                .bloc(testBloc)
                .build();

        Chambre savedChambre = chambreRepository.save(testChambre);
        Long savedId = savedChambre.getIdChambre();
        Optional<Chambre> chambreOptional = chambreRepository.findById(savedId);
        assertTrue(chambreOptional.isPresent());
    }


    @Test
    void findById_ShouldReturnEmpty_WhenIdDoesNotExist() {
        Optional<Chambre> foundChambre = chambreRepository.findById(999L);

        assertFalse(foundChambre.isPresent());
    }

    @Test
    void findAll_ShouldReturnListOfChambres() {
        List<Chambre> chambres = chambreRepository.findAll();

        assertNotNull(chambres);
        assertEquals(1, chambres.size());
    }

    @Test
    void deleteById_ShouldRemoveChambre() {
        chambreRepository.deleteById(testChambre.getNumeroChambre());

        Optional<Chambre> foundChambre = chambreRepository.findById(testChambre.getNumeroChambre());

        assertFalse(foundChambre.isPresent());
    }

    @Test
    void count_ShouldReturnNumberOfChambres() {
        long count = chambreRepository.count();

        assertEquals(1, count);
    }
}
