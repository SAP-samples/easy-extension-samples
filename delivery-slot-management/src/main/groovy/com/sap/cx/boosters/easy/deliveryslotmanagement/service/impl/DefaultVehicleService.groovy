package com.sap.cx.boosters.easy.deliveryslotmanagement.service.impl

import com.sap.cx.boosters.easy.deliveryslotmanagement.models.VehicleModel
import com.sap.cx.boosters.easy.deliveryslotmanagement.service.VehicleService
import de.hybris.platform.servicelayer.internal.dao.GenericDao

import javax.annotation.Resource

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateIfSingleResult
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
