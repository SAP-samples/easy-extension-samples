package com.sap.cx.boosters.easy.easytutorialstep3.service.impl

import com.sap.cx.boosters.easy.easytutorialstep3.models.VehicleModel
import com.sap.cx.boosters.easy.easytutorialstep3.service.VehicleService

class DefaultVehicleService implements VehicleService {

    @Override
    VehicleModel getForCode(String code) {
        // TODO Should return the vehicle found in the database based on the vehicle code

        // Hint: Declare a dao (vehicleGenericDao) using the DefaultGenericDao and call it's find method to get
        // the list of the vehicles and return if a unique record is found, otherwise throw exception
        // You can also use flexible search service to search the vehicle by code using a query like:
        // SELECT {PK} FROM {Vehicle} WHERE {code}=?code

        return null
    }
}
