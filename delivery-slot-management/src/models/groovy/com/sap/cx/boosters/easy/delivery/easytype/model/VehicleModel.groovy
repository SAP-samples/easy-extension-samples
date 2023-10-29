/*
 * Copyright (c) 2022. SAP SE or an SAP affiliate company. All rights reserved.
 */

package com.sap.cx.boosters.easy.delivery.easytype.model

import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.servicelayer.model.ItemModelContext

class VehicleModel extends ItemModel{

    public static final String _TYPECODE = "Vehicle"
    public static final String CODE = "code"
    public static final String NAME = "name"

    VehicleModel() {
        super()
    }

    VehicleModel(final ItemModelContext ctx) {
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

    @Accessor(qualifier = NAME, type = Accessor.Type.GETTER)
    String getName() {
        return getName(null)
    }

    @Accessor(qualifier = NAME, type = Accessor.Type.GETTER)
    String getName(final Locale loc) {
        return getPersistenceContext().getLocalizedValue(NAME, loc)
    }

    @Accessor(qualifier = NAME, type = Accessor.Type.SETTER)
    void setName(final String value) {
        setName(value, null)
    }

    @Accessor(qualifier = NAME, type = Accessor.Type.SETTER)
    void setName(final String value, final Locale loc) {
        getPersistenceContext().setLocalizedValue(NAME, loc, value)
    }

}