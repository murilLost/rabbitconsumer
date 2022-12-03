package br.com.fiap.scjr.rabbitconsumer.validator;

import br.com.fiap.scjr.rabbitconsumer.model.DroneModel;

public class DroneValidator {

    public boolean validar(DroneModel drone){
        if ((drone.getTemperatura() >= 35 || drone.getTemperatura() <= 0) || (drone.getUmidade() <= 15)){
            return false;
        }
        return true;
    }

}
