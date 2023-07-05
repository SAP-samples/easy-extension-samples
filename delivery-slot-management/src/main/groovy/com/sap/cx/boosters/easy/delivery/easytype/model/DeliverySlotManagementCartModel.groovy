/*
 * Copyright (c) 2022. SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cx.boosters.easy.delivery.easytype.model

import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.model.ItemModelContext

class DeliverySlotManagementCartModel extends CartModel {
    public static final String DELIVERYSLOTMANAGEMENT = "deliveryslotmanagement"

    DeliverySlotManagementCartModel() {
        super()
    }

    DeliverySlotManagementCartModel(final ItemModelContext ctx) {
        super(ctx)
    }

    @Accessor(qualifier = DELIVERYSLOTMANAGEMENT, type = Accessor.Type.GETTER)
    def getDeliveryslotmanagement() {
        return getPersistenceContext().getDynamicValue(this, DELIVERYSLOTMANAGEMENT);
    }

}