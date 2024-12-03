package com.example.devops.services.bloc;

import com.example.devops.dao.entities.Bloc;
import com.example.devops.dao.repositories.BlocRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlocServiceTest {

    @Mock private BlocRepository blocRepository;


    @InjectMocks
    private BlocService blocService;
    @Test
    @Disabled
    void addOrUpdate2() {
    }

    @Test
    @Disabled
    void addOrUpdate() {
    }

    @Test
    void TestIfFindAllRetunAllBloc() {
        List<Bloc> mockBlocs = List.of(
                new Bloc(1L, "Bloc A", 100L, null, new ArrayList<>()),
                new Bloc(2L, "Bloc B", 150L, null, new ArrayList<>())
        );
        when(blocRepository.findAll()).thenReturn(mockBlocs);

        List<Bloc> result = blocService.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bloc A", result.get(0).getNomBloc());
        assertEquals("Bloc B", result.get(1).getNomBloc());

        verify(blocRepository, times(1)).findAll();
    }


    @Test
    @Disabled
    void findById() {
    }

    @Test
    @Disabled
    void deleteById() {
    }

    @Test
    @Disabled
    void delete() {
    }

    @Test
    @Disabled
    void affecterChambresABloc() {
    }

    @Test
    @Disabled
    void affecterBlocAFoyer() {
    }

    @Test
    @Disabled
    void ajouterBlocEtSesChambres() {
    }

    @Test
    @Disabled
    void ajouterBlocEtAffecterAFoyer() {
    }
}