/*
 * Copyright (c) 2022. SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cx.boosters.easy.delivery.easytype.models

import com.sap.cx.boosters.easy.delivery.easytype.enums.DeliverySlotStatus
import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.order.AbstractOrderModel
import de.hybris.platform.servicelayer.model.ItemModelContext

class DeliverySlotManagementModel extends ItemModel{
    public static final String _TYPECODE = "DeliverySlotManagement"
    public static final String CODE = "code"
    public static final String DELIVERYSLOT = "deliveryslot"
    public static final String ABSTRACTORDER = "abstractorder"
    public static final String TIMESTAMP = "timestamp"
    public static final String STATUS = "status"

    DeliverySlotManagementModel(){
        super()
    }

    DeliverySlotManagementModel(final ItemModelContext ctx)
    {
        super(ctx)
    }

    @Accessor(qualifier = CODE, type = Accessor.Type.GETTER)
    String getCode()
    {
        return getPersistenceContext().getPropertyValue(CODE)
    }

    @Accessor(qualifier = CODE, type = Accessor.Type.SETTER)
    void setCode(final String value)
    {
        getPersistenceContext().setPropertyValue(CODE, value)
    }

    @Accessor(qualifier = DELIVERYSLOT, type = Accessor.Type.GETTER)
    DeliverySlotModel getDeliveryslot()
    {
        return getPersistenceContext().getPropertyValue(DELIVERYSLOT)
    }

    @Accessor(qualifier = DELIVERYSLOT, type = Accessor.Type.SETTER)
    void setDeliveryslot(final DeliverySlotModel value)
    {
        getPersistenceContext().setPropertyValue(DELIVERYSLOT, value)
    }

    @Accessor(qualifier = ABSTRACTORDER, type = Accessor.Type.GETTER)
    AbstractOrderModel getAbstractorder()
    {
        return getPersistenceContext().getPropertyValue(ABSTRACTORDER)
    }

    @Accessor(qualifier = ABSTRACTORDER, type = Accessor.Type.SETTER)
    void setAbstractorder(final AbstractOrderModel value)
    {
        getPersistenceContext().setPropertyValue(ABSTRACTORDER, value)
    }

    @Accessor(qualifier = TIMESTAMP, type = Accessor.Type.GETTER)
    Date getTimestamp()
    {
        return getPersistenceContext().getPropertyValue(TIMESTAMP)
    }

    @Accessor(qualifier = TIMESTAMP, type = Accessor.Type.SETTER)
    void setTimestamp(final Date value)
    {
        getPersistenceContext().setPropertyValue(TIMESTAMP, value)
    }

    @Accessor(qualifier = STATUS, type = Accessor.Type.GETTER)
    DeliverySlotStatus getStatus()
    {
        return getPersistenceContext().getPropertyValue(STATUS)
    }

    @Accessor(qualifier = STATUS, type = Accessor.Type.SETTER)
    void setStatus(final DeliverySlotStatus value)
    {
        getPersistenceContext().setPropertyValue(STATUS, value)
    }

}