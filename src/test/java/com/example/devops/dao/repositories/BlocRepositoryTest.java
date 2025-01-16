package com.example.devops.dao.repositories;

import com.example.devops.dao.entities.Bloc;
import com.example.devops.dao.entities.Foyer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BlocRepositoryTest {

    @Autowired
    private BlocRepository blocRepository;

    @BeforeEach
    void setUp() {
        Foyer foyer = Foyer.builder().idFoyer(1L).build();
        System.out.println("this is generated foyer " + foyer.getIdFoyer());

        Bloc bloc1 = Bloc.builder()
                .nomBloc("Bloc A")
                .capaciteBloc(5)
                .build();
        Bloc bloc2 = Bloc.builder()
                .nomBloc("Bloc B")
                .capaciteBloc(20)
                .build();

        blocRepository.save(bloc1);
        blocRepository.save(bloc2);
    }

    @AfterEach
    void tearDown() {
        blocRepository.deleteAll();
    }

    @Test
    @Order(1)
    void selectByNomBJPQL1_ShouldReturnBloc() {
        Bloc result = blocRepository.selectByNomBJPQL1("Bloc A");
        assertNotNull(result);
        System.out.println("Retrieved Bloc: " + result);
        assertEquals("Bloc A", result.getNomBloc());
    }

    @Test
    @Order(2)
    void updateBlocJPQL_ShouldUpdateNomBlocForCapacityLessThan10() {
        blocRepository.updateBlocJPQL("Updated Bloc");
        Bloc updatedBloc = blocRepository.findByNomBloc("Updated Bloc");
        assertNotNull(updatedBloc);
        assertEquals(5, updatedBloc.getCapaciteBloc());
    }

    @Test
    @Order(3)
    void findByCapaciteBlocGreaterThan_ShouldReturnBlocs() {
        List<Bloc> blocs = blocRepository.findByCapaciteBlocGreaterThan(10);
        assertNotNull(blocs);
        assertEquals(1, blocs.size());
        assertEquals("Bloc B", blocs.get(0).getNomBloc());
    }

    @Test
    @Order(4)
    void findByNomBlocIgnoreCase_ShouldReturnBloc() {
        List<Bloc> blocs = blocRepository.findByNomBlocIgnoreCase("bloc a");
        assertNotNull(blocs);
        assertEquals(1, blocs.size());
        assertEquals("Bloc A", blocs.get(0).getNomBloc());
    }
}
