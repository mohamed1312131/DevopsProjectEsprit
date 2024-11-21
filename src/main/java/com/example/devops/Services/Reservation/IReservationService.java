package com.example.devops.Services.Reservation;

import com.example.devops.DAO.Entities.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    Reservation addOrUpdate(Reservation r);
    List<Reservation> findAll();
    Reservation findById(String id);
    void deleteById(String id);
    void delete(Reservation r);
    Reservation ajouterReservationEtAssignerAChambreEtAEtudiant (Long numChambre, long cin) ;
    long  getReservationParAnneeUniversitaire(LocalDate debutAnnee, LocalDate finAnnee ) ;
    String annulerReservation (long cinEtudiant) ;

    void annulerReservations();


    void affectReservationAChambre(String idRes,long idChambre);
    void deaffectReservationAChambre(String idRes, long idChambre);

}
