package com.example.devops.services.chambre;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.devops.dao.repositories.ChambreRepository;
import com.example.devops.dao.repositories.BlocRepository;
import com.example.devops.dao.entities.Chambre;
import com.example.devops.dao.entities.TypeChambre;
import com.example.devops.dao.entities.Reservation;
import com.example.devops.dao.entities.Bloc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Service
@AllArgsConstructor
@Slf4j
public class ChambreService implements IChambreService {
    private final ChambreRepository chambreRepository;
    ChambreRepository repo;
    BlocRepository blocRepository;

    @Override
    public Chambre addOrUpdate(Chambre c) {
        return repo.save(c);
    }

    @Override
    public List<Chambre> findAll() {
        return repo.findAll();
    }

    @Override
    public Chambre findById(long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Chambre not found with id: " + id));
    }

    @Override
    public void deleteById(long id) {
        repo.deleteById(id);
    }

    @Override
    public void delete(Chambre c) {
        repo.delete(c);
    }

    @Override
    public List<Chambre> getChambresParNomBloc(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public long nbChambreParTypeEtBloc(TypeChambre type, long idBloc) {
        long compteur = 0;
        List<Chambre> list = chambreRepository.findAll();
        for (Chambre chambre : list) {
            if (chambre.getBloc().getIdBloc() == idBloc
                    && chambre.getTypeC().equals(type)) {
                compteur++;
            }
        }
        return compteur;

    }



    @Scheduled(cron = "0 * * * * *")
    public void listeChambresParBloc() {
        for (Bloc b : blocRepository.findAll()) {
            log.info("Bloc => " + b.getNomBloc() +
                    " ayant une capacité " + b.getCapaciteBloc());
            if (b.getChambres().isEmpty()) {
                log.info("La liste des chambres pour ce bloc: ");
                for (Chambre c : b.getChambres()) {
                    log.info("NumChambre: " + c.getNumeroChambre() +
                            " type: " + c.getTypeC());
                }
            } else {
                log.info("Pas de chambre disponible dans ce bloc");
            }
            log.info("********************");
        }
    }

    @Override
    public void pourcentageChambreParTypeChambre() {
        long totalChambre = repo.count();
        double pSimple = (double) (repo.countChambreByTypeC(TypeChambre.SIMPLE) * 100) / totalChambre;
        double pDouble = (double) (repo.countChambreByTypeC(TypeChambre.DOUBLE) * 100) / totalChambre;
        double pTriple = (double) (repo.countChambreByTypeC(TypeChambre.TRIPLE) * 100) / totalChambre;
        log.info("Nombre total des chambre: " + totalChambre);
        log.info("Le pourcentage des chambres pour le type SIMPLE est égale à " + pSimple);
        log.info("Le pourcentage des chambres pour le type DOUBLE est égale à " + pDouble);
        log.info("Le pourcentage des chambres pour le type TRIPLE est égale à " + pTriple);

    }

    @Override
    public List<Chambre> getChambresParNomBlocJava(String nomBloc) {
        Bloc b = blocRepository.findByNomBloc(nomBloc);
        return b.getChambres();
    }

    @Override
    public List<Chambre> getChambresParNomBlocKeyWord(String nomBloc) {
        return repo.findByBlocNomBloc(nomBloc);
    }

    @Override
    public List<Chambre> getChambresParNomBlocJPQL(String nomBloc) {
        return repo.getChambresParNomBlocJPQL(nomBloc);
    }

    @Override
    public List<Chambre> getChambresParNomBlocSQL(String nomBloc) {
        return repo.getChambresParNomBlocSQL(nomBloc);
    }
}
