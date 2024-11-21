package com.example.devops.RestControllers;

import com.example.devops.DAO.Entities.Universite;
import com.example.devops.Services.Universite.IUniversiteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("universite")
@AllArgsConstructor
public class UniversiteRestController {
    IUniversiteService service;

    @PostMapping("addOrUpdate")
    Universite addOrUpdate(@RequestBody Universite u) {
        return service.addOrUpdate(u);
    }

    @GetMapping("findAll")
    List<Universite> findAll() {
        return service.findAll();
    }

    @GetMapping("findById")
    Universite findById(@RequestParam long id) {
        return service.findById(id);
    }

    @DeleteMapping("delete")
    void delete(@RequestBody Universite u) {
        service.delete(u);
    }

    @DeleteMapping("deleteById")
    void deleteById(@RequestParam long id) {
        service.deleteById(id);
    }

    @PostMapping("ajouterUniversiteEtSonFoyer")
    Universite ajouterUniversiteEtSonFoyer(@RequestBody Universite u)
    {
        return service.ajouterUniversiteEtSonFoyer(u);
    }


}
