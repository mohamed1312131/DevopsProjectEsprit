package com.example.devops.services.bloc;

import com.example.devops.dao.entities.Bloc;
import com.example.devops.dao.entities.Chambre;
import com.example.devops.dao.repositories.BlocRepository;
import com.example.devops.dao.repositories.ChambreRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.devops.dao.entities.TypeChambre.SIMPLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlocServiceTest {

   @Mock private BlocRepository blocRepository;
    @Mock private ChambreRepository chambreRepository;

    @InjectMocks private BlocService blocService;

 // test with order
    @Test
    void testAddOrUpdate() {
        Bloc bloc = new Bloc(1L, "Bloc A", 100L, null, new ArrayList<>());
        Chambre chambre = new Chambre(1L,2,SIMPLE,null,null);
        bloc.getChambres().add(chambre);

        when(blocRepository.save(bloc)).thenReturn(bloc);
        when(chambreRepository.save(any(Chambre.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Bloc result = blocService.addOrUpdate(bloc);

        assertNotNull(result);
        assertEquals(1, result.getChambres().size());
        assertEquals(bloc, chambre.getBloc());
        verify(blocRepository, times(1)).save(bloc);
        verify(chambreRepository, times(1)).save(chambre);
    }

    @Test
    void testFindAll() {
        List<Bloc> mockBlocs = List.of(
                new Bloc(1L, "Bloc A", 100L, null, new ArrayList<>()),
                new Bloc(2L, "Bloc B", 150L, null, new ArrayList<>())
        );
        when(blocRepository.findAll()).thenReturn(mockBlocs);

        List<Bloc> result = blocService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bloc A", result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        Bloc mockBloc = new Bloc(1L, "Bloc A", 100L, null, new ArrayList<>());
        when(blocRepository.findById(1L)).thenReturn(Optional.of(mockBloc));

        Bloc result = blocService.findById(1L);

        assertNotNull(result);
        assertEquals("Bloc A", result.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdThrowsException() {
        when(blocRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> blocService.findById(1L));

        assertEquals("Bloc not found with id: 1", exception.getMessage());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        Bloc mockBloc = new Bloc(1L, "Bloc A", 100L, null, new ArrayList<>());
        when(blocRepository.findById(1L)).thenReturn(Optional.of(mockBloc));

        blocService.deleteById(1L);

        verify(chambreRepository, times(1)).deleteAll(mockBloc.getChambres());
        verify(blocRepository, times(1)).delete(mockBloc);
    }




}
