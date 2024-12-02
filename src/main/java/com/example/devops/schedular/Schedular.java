package com.example.devops.schedular;


import com.example.devops.services.chambre.IChambreService;
import com.example.devops.services.reservation.IReservationService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class Schedular {

    IChambreService iChambreService;
    IReservationService iReservationService;

    @Scheduled(cron = "0 * * * * *")
    void service1() {
        iChambreService.listeChambresParBloc();
    }


}
