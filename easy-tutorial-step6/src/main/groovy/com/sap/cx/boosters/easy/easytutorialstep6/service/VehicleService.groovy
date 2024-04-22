package com.sap.cx.boosters.easy.easytutorialstep6.service

import com.sap.cx.boosters.easy.easytutorialstep6.models.VehicleModel

interface VehicleService {
    /**
     * Finds the vehicle by vehicle code
     * @param code the vehicle code
     * @return the vehicle
     */
    VehicleModel getForCode(String code);

}
