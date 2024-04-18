/*
* Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED BY EASY GRADLE PLUGIN AND WILL BE
* --- OVERWRITTEN IF REGENERATED AGAIN USING easy-class-gen GRADLE TASK!
* --- Generated at Tue Apr 16 23:19:31 BST 2024
* ----------------------------------------------------------------
*/

package com.sap.cx.boosters.easy.easytutorialstep2.models

import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.core.model.order.AbstractOrderModel
import com.sap.cx.boosters.easy.easytutorialstep2.models.DeliverySlotModel
import com.sap.cx.boosters.easy.easytutorialstep2.enums.DeliverySlotStatus
import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.servicelayer.model.ItemModelContext

/**
* Generated model class for easy type DeliverySlotManagement.
*/
class DeliverySlotManagementModel extends ItemModel {

    /**<i>Generated model type code constant.</i>*/
    public static final String _TYPECODE = "DeliverySlotManagement"

    /** <i>Generated constant</i> - Attribute key of <code>DeliverySlotManagement.abstractorder</code>. */
    public static final String ABSTRACTORDER = "abstractorder"

    /** <i>Generated constant</i> - Attribute key of <code>DeliverySlotManagement.deliveryslot</code>. */
    public static final String DELIVERYSLOT = "deliveryslot"

    /** <i>Generated constant</i> - Attribute key of <code>DeliverySlotManagement.status</code>. */
    public static final String STATUS = "status"

    /** <i>Generated constant</i> - Attribute key of <code>DeliverySlotManagement.timestamp</code>. */
    public static final String TIMESTAMP = "timestamp"

    /** <i>Generated constant</i> - Attribute key of <code>DeliverySlotManagement.code</code>. */
    public static final String CODE = "code"

    /**
    * <i>Generated constructor</i> - Default constructor for generic creation.
    */
    DeliverySlotManagementModel() {
        super()
    }

    /**
    * <i>Generated constructor</i> - Default constructor for creation with existing context
    * @param ctx the model context to be injected, must not be null
    */
    DeliverySlotManagementModel(final ItemModelContext ctx) {
        super(ctx)
    }


    /**
    * <i>Generated method</i> - Getter of the <code>DeliverySlotManagement.abstractorder</code> attribute.
    * @return the abstractorder
    */
    @Accessor(qualifier = ABSTRACTORDER, type = Accessor.Type.GETTER)
    AbstractOrderModel getAbstractorder() {
        return getPersistenceContext().getPropertyValue(ABSTRACTORDER)
    }

    /**
    * <i>Generated method</i> - Setter of <code>DeliverySlotManagement.abstractorder</code> attribute.
    *
    * @param value the abstractorder
    */
    @Accessor(qualifier = ABSTRACTORDER, type = Accessor.Type.SETTER)
    void setAbstractorder(final AbstractOrderModel value) {
        getPersistenceContext().setPropertyValue(ABSTRACTORDER, value)
    }

    /**
    * <i>Generated method</i> - Getter of the <code>DeliverySlotManagement.deliveryslot</code> attribute.
    * @return the deliveryslot
    */
    @Accessor(qualifier = DELIVERYSLOT, type = Accessor.Type.GETTER)
    DeliverySlotModel getDeliveryslot() {
        return getPersistenceContext().getPropertyValue(DELIVERYSLOT)
    }

    /**
    * <i>Generated method</i> - Setter of <code>DeliverySlotManagement.deliveryslot</code> attribute.
    *
    * @param value the deliveryslot
    */
    @Accessor(qualifier = DELIVERYSLOT, type = Accessor.Type.SETTER)
    void setDeliveryslot(final DeliverySlotModel value) {
        getPersistenceContext().setPropertyValue(DELIVERYSLOT, value)
    }

    /**
    * <i>Generated method</i> - Getter of the <code>DeliverySlotManagement.status</code> attribute.
    * @return the status
    */
    @Accessor(qualifier = STATUS, type = Accessor.Type.GETTER)
    DeliverySlotStatus getStatus() {
        return getPersistenceContext().getPropertyValue(STATUS)
    }

    /**
    * <i>Generated method</i> - Setter of <code>DeliverySlotManagement.status</code> attribute.
    *
    * @param value the status
    */
    @Accessor(qualifier = STATUS, type = Accessor.Type.SETTER)
    void setStatus(final DeliverySlotStatus value) {
        getPersistenceContext().setPropertyValue(STATUS, value)
    }

    /**
    * <i>Generated method</i> - Getter of the <code>DeliverySlotManagement.timestamp</code> attribute.
    * @return the timestamp
    */
    @Accessor(qualifier = TIMESTAMP, type = Accessor.Type.GETTER)
    Date getTimestamp() {
        return getPersistenceContext().getPropertyValue(TIMESTAMP)
    }

    /**
    * <i>Generated method</i> - Setter of <code>DeliverySlotManagement.timestamp</code> attribute.
    *
    * @param value the timestamp
    */
    @Accessor(qualifier = TIMESTAMP, type = Accessor.Type.SETTER)
    void setTimestamp(final Date value) {
        getPersistenceContext().setPropertyValue(TIMESTAMP, value)
    }

    /**
    * <i>Generated method</i> - Getter of the <code>DeliverySlotManagement.code</code> attribute.
    * @return the code
    */
    @Accessor(qualifier = CODE, type = Accessor.Type.GETTER)
    String getCode() {
        return getPersistenceContext().getPropertyValue(CODE)
    }

    /**
    * <i>Generated method</i> - Setter of <code>DeliverySlotManagement.code</code> attribute.
    *
    * @param value the code
    */
    @Accessor(qualifier = CODE, type = Accessor.Type.SETTER)
    void setCode(final String value) {
        getPersistenceContext().setPropertyValue(CODE, value)
    }
}
