package com.example.devops.services.bloc;

import com.example.devops.dao.entities.Etudiant;
import com.example.devops.dao.repositories.EtudiantRepository;
import com.example.devops.services.etudiant.EtudiantService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.Console;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddOrUpdateEtudiant() {
        Etudiant etudiant = Etudiant.builder()
                .nomEt("Doe")
                .prenomEt("John")
                .cin(12345678L)
                .ecole("Ecole1")
                .dateNaissance(LocalDate.of(2000, 1, 1))
                .build();

        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant savedEtudiant = etudiantService.addOrUpdate(etudiant);

        assertThat(savedEtudiant.getNomEt()).isEqualTo("Doe");
        verify(etudiantRepository, times(1)).save(etudiant);
    }

    @Test
    void shouldFindEtudiantById() {
        Etudiant etudiant = Etudiant.builder()
                .idEtudiant(1L)
                .nomEt("Doe")
                .prenomEt("John")
                .cin(12345678L)
                .ecole("Ecole1")
                .dateNaissance(LocalDate.of(2000, 1, 1))
                .build();

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));

        Etudiant foundEtudiant = etudiantService.findById(1L);

        assertThat(foundEtudiant.getNomEt()).isEqualTo("Doe");
        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEtudiantNotFound() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> etudiantService.findById(1L));

        verify(etudiantRepository, times(1)).findById(1L);
    }

    @Test
    void shouldFindAllEtudiants() {
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

        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(etudiant1, etudiant2));

        List<Etudiant> etudiants = etudiantService.findAll();

        assertThat(etudiants).hasSize(2);
        verify(etudiantRepository, times(1)).findAll();
    }
}
