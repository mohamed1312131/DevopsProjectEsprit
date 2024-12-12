package com.example.devops.services.bloc;


import com.example.devops.dao.entities.Bloc;
import com.example.devops.dao.entities.Chambre;
import com.example.devops.dao.entities.TypeChambre;
import com.example.devops.dao.repositories.ChambreRepository;
import com.example.devops.dao.repositories.BlocRepository;
import com.example.devops.services.chambre.ChambreService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private ChambreService chambreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void addOrUpdate_ShouldSaveChambre() {
        Chambre chambre = Chambre.builder().numeroChambre(101L).typeC(TypeChambre.SIMPLE).build();
        when(chambreRepository.save(chambre)).thenReturn(chambre);

        Chambre result = chambreService.addOrUpdate(chambre);

        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    @Order(2)
    void findAll_ShouldReturnListOfChambres() {
        Chambre chambre1 = Chambre.builder().numeroChambre(101L).typeC(TypeChambre.SIMPLE).build();
        Chambre chambre2 = Chambre.builder().numeroChambre(102L).typeC(TypeChambre.DOUBLE).build();

        when(chambreRepository.findAll()).thenReturn(Arrays.asList(chambre1, chambre2));

        List<Chambre> result = chambreService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(chambreRepository, times(1)).findAll();
    }

    @Test
    @Order(3)
    void findById_ShouldReturnChambre_WhenIdExists() {
        Chambre chambre = Chambre.builder().numeroChambre(101L).typeC(TypeChambre.SIMPLE).build();
        when(chambreRepository.findById(1L)).thenReturn(Optional.of(chambre));

        Chambre result = chambreService.findById(1L);

        assertNotNull(result);
        assertEquals(101L, result.getNumeroChambre());
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    @Order(4)
    void findById_ShouldThrowException_WhenIdDoesNotExist() {
        when(chambreRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> chambreService.findById(1L));

        assertEquals("Chambre not found with id: 1", exception.getMessage());
        verify(chambreRepository, times(1)).findById(1L);
    }

    @Test
    @Order(5)
    void deleteById_ShouldCallRepository() {
        doNothing().when(chambreRepository).deleteById(1L);

        chambreService.deleteById(1L);

        verify(chambreRepository, times(1)).deleteById(1L);
    }


}

