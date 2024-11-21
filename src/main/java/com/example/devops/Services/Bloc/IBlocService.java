package com.example.devops.Services.Bloc;



import com.example.devops.DAO.Entities.Bloc;

import java.util.List;

public interface IBlocService {
    Bloc addOrUpdate(Bloc b);

    Bloc addOrUpdate2(Bloc b);

    List<Bloc> findAll();

    Bloc findById(long id);

    void deleteById(long id);

    void delete(Bloc b);

    Bloc affecterChambresABloc(List<Long> numChambre, String nomBloc);

    Bloc affecterBlocAFoyer(String nomBloc, String nomFoyer);

    Bloc ajouterBlocEtSesChambres(Bloc b);

    // Not yet
    Bloc ajouterBlocEtAffecterAFoyer(Bloc b, String nomFoyer);

}
