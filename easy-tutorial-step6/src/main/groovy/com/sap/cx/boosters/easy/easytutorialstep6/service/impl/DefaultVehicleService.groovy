package com.sap.cx.boosters.easy.easytutorialstep6.service.impl

import com.sap.cx.boosters.easy.easytutorialstep6.models.VehicleModel
import com.sap.cx.boosters.easy.easytutorialstep6.service.VehicleService
import de.hybris.platform.servicelayer.internal.dao.GenericDao
import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult

import jakarta.annotation.Resource

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull

class DefaultVehicleService implements VehicleService {

    @Resource
    private GenericDao<VehicleModel> vehicleGenericDao

    @Override
    VehicleModel getForCode(String code) {
        validateParameterNotNull(code, 'vehicle code cannot be null')
        def vehicles = vehicleGenericDao.find([code: code])
        validateIfSingleResult(vehicles, VehicleModel, 'code', code)
        return vehicles.get(0)
    }
}
