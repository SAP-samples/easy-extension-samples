/*
 * Copyright (c) 2022. SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cx.boosters.easy.delivery.easytype.model

import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ItemModelContext

class DeliverySlotModel extends ItemModel{

    public static final String _TYPECODE = "DeliverySlot"
    public static final String CODE = "code"
    public static final String WAREHOUSE = "warehouse"
    public static final String VEHICLE = "vehicle"
    public static final String STARTTIME = "starttime"
    public static final String ENDTIME = "endtime"
    public static final String AVAILABLE = "available"

    DeliverySlotModel() {
        super()
    }

    DeliverySlotModel(final ItemModelContext ctx) {
        super(ctx)
    }

    @Accessor(qualifier = CODE, type = Accessor.Type.GETTER)
    String getCode() {
        return getPersistenceContext().getPropertyValue(CODE)
    }

    @Accessor(qualifier = CODE, type = Accessor.Type.SETTER)
    void setCode(final String value) {
        getPersistenceContext().setPropertyValue(CODE, value)
    }

    @Accessor(qualifier = WAREHOUSE, type = Accessor.Type.GETTER)
    WarehouseModel getWarehouse() {
        return getPersistenceContext().getPropertyValue(WAREHOUSE)
    }

    @Accessor(qualifier = WAREHOUSE, type = Accessor.Type.SETTER)
    void setWarehouse(final WarehouseModel value) {
        getPersistenceContext().setPropertyValue(WAREHOUSE, value)
    }

    @Accessor(qualifier = VEHICLE, type = Accessor.Type.GETTER)
    VehicleModel getVehicle() {
        return getPersistenceContext().getPropertyValue(VEHICLE)
    }

    @Accessor(qualifier = VEHICLE, type = Accessor.Type.SETTER)
    void setVehicle(final VehicleModel value) {
        getPersistenceContext().setPropertyValue(VEHICLE, value)
    }

    @Accessor(qualifier = STARTTIME, type = Accessor.Type.GETTER)
    Date getStarttime() {
        return getPersistenceContext().getPropertyValue(STARTTIME)
    }

    @Accessor(qualifier = STARTTIME, type = Accessor.Type.SETTER)
    void setStarttime(final Date value) {
        getPersistenceContext().setPropertyValue(STARTTIME, value)
    }

    @Accessor(qualifier = ENDTIME, type = Accessor.Type.GETTER)
    Date getEndtime() {
        return getPersistenceContext().getPropertyValue(ENDTIME)
    }

    @Accessor(qualifier = ENDTIME, type = Accessor.Type.SETTER)
    void setEndtime(final Date value) {
        getPersistenceContext().setPropertyValue(ENDTIME, value)
    }

    @Accessor(qualifier = AVAILABLE, type = Accessor.Type.GETTER)
    Integer getAvailable() {
        return getPersistenceContext().getPropertyValue(AVAILABLE)
    }

    @Accessor(qualifier = AVAILABLE, type = Accessor.Type.SETTER)
    void setAvailable(final Integer value) {
        getPersistenceContext().setPropertyValue(AVAILABLE, value)
    }

}