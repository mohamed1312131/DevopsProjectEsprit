package com.example.devops.services.foyer;

import com.example.devops.dao.entities.Bloc;
import com.example.devops.dao.entities.Foyer;
import com.example.devops.dao.entities.Universite;
import com.example.devops.dao.repositories.BlocRepository;
import com.example.devops.dao.repositories.FoyerRepository;
import com.example.devops.dao.repositories.UniversiteRepository;
import com.example.devops.services.universite.UniversiteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class FoyerService implements IFoyerService {
    private final FoyerRepository foyerRepository;
    FoyerRepository repo;
    UniversiteRepository universiteRepository;
    BlocRepository blocRepository;
    UniversiteService universiteService;

    @Override
    public Foyer addOrUpdate(Foyer f) {
        return repo.save(f);
    }

    @Override
    public List<Foyer> findAll() {
        return repo.findAll();
    }

    @Override
    public Foyer findById(long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Foyer not found with id: " + id));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Foyer f) {
        repo.delete(f);
    }

    @Override
    public Universite affecterFoyerAUniversite(long idFoyer, String nomUniversite) {
       Foyer f = findById(idFoyer); // Child
        Universite u = universiteRepository.findByNomUniversite(nomUniversite); // Parent
        // On affecte le child au parent
        u.setFoyer(f);
        return universiteRepository.save(u);
    }

    @Override
    public Universite desaffecterFoyerAUniversite(long idUniversite) {
        Universite u = universiteService.findById(idUniversite);
        u.setFoyer(null);
        return universiteRepository.save(u);
    }

    @Override
    public Foyer ajouterFoyerEtAffecterAUniversite(Foyer foyer, long idUniversite) {
        // Récuperer la liste des blocs avant de faire l'ajout
        List<Bloc> blocs = foyer.getBlocs();
        // Foyer est le child et universite est parent
        Foyer f = repo.save(foyer);
        Universite u = universiteService.findById(idUniversite);
        // Foyer est le child et bloc est le parent
        //On affecte le child au parent
        for (Bloc bloc : blocs) {
            bloc.setFoyer(foyer);
            blocRepository.save(bloc);
        }
        u.setFoyer(f);
        return universiteRepository.save(u).getFoyer();
    }

    @Override
    public Foyer ajoutFoyerEtBlocs(Foyer foyer) {
        List<Bloc> blocs = foyer.getBlocs();
        foyer = repo.save(foyer);
        for (Bloc b : blocs) {
            b.setFoyer(foyer);
            blocRepository.save(b);
        }
        return foyer;
    }

    @Override
    public Universite affecterFoyerAUniversite(long idF, long idU) {
        Universite u= universiteService.findById(idU);
        Foyer f= foyerRepository.findById(idF).orElseThrow(() -> new EntityNotFoundException("Foyer not found with id: " + idF));
        u.setFoyer(f);
        return universiteRepository.save(u);
    }

}
