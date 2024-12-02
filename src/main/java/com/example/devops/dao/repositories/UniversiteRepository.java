package com.example.devops.dao.repositories;

import com.example.devops.dao.entities.Universite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversiteRepository extends JpaRepository<Universite, Long> {
   Universite findByNomUniversite(String nomUniversite);

}
