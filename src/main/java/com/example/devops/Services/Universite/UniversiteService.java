package com.example.devops.Services.Universite;

import com.example.devops.DAO.Entities.Universite;
import com.example.devops.DAO.Repositories.UniversiteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@AllArgsConstructor
public class UniversiteService implements IUniversiteService {
    UniversiteRepository repo;

    @Override
    public Universite addOrUpdate(Universite u) {
        return repo.save(u);
    }

    @Override
    public List<Universite> findAll() {
        return repo.findAll();
    }

    @Override
    public Universite findById(long id) {
        return repo.findById(id).get();
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Universite u) {
        repo.delete(u);
    }

    @Override
    public Universite ajouterUniversiteEtSonFoyer(Universite u) {
        return repo.save(u);
    }
}
