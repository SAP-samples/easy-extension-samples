/*
* Copyright (c) 2023 SAP SE or an SAP affiliate company. All rights reserved.
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED BY EASY GRADLE PLUGIN AND WILL BE
* --- OVERWRITTEN IF REGENERATED AGAIN USING easy-class-gen GRADLE TASK!
* --- Generated at Wed Nov 29 14:24:13 GMT 2023
* ----------------------------------------------------------------
*/

package com.sap.cx.boosters.easy.deliveryslotmanagement.models

import de.hybris.platform.core.model.ItemModel
import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.servicelayer.model.ItemModelContext

/**
* Generated model class for easy type Vehicle.
*/
class VehicleModel extends ItemModel {

    /**<i>Generated model type code constant.</i>*/
    public static final String _TYPECODE = "Vehicle"

    /** <i>Generated constant</i> - Attribute key of <code>Vehicle.code</code>. */
    public static final String CODE = "code"

    /** <i>Generated constant</i> - Attribute key of <code>Vehicle.name</code>. */
    public static final String NAME = "name"

    /**
    * <i>Generated constructor</i> - Default constructor for generic creation.
    */
    VehicleModel() {
        super()
    }

    /**
    * <i>Generated constructor</i> - Default constructor for creation with existing context
    * @param ctx the model context to be injected, must not be null
    */
    VehicleModel(final ItemModelContext ctx) {
        super(ctx)
    }


    /**
    * <i>Generated method</i> - Getter of the <code>Vehicle.code</code> attribute.
    * @return the code
    */
    @Accessor(qualifier = CODE, type = Accessor.Type.GETTER)
    String getCode() {
        return getPersistenceContext().getPropertyValue(CODE)
    }


    /**
    * <i>Generated method</i> - Getter of the <code>Vehicle.name</code> attribute.
    * @return the name
    */
    @Accessor(qualifier = NAME, type = Accessor.Type.GETTER)
    String getName() {
        return getName(null)
    }

    /**
    * <i>Generated method</i> - Getter of the <code>Vehicle.name</code> attribute.
    * @param loc the value localization key
    * @return the name
    * @throws IllegalArgumentException if localization key cannot be mapped to data language
    */
    @Accessor(qualifier = NAME, type = Accessor.Type.GETTER)
    String getName(final Locale loc) {
        return getPersistenceContext().getLocalizedValue(NAME, loc)
    }

    /**
    * <i>Generated method</i> - Setter of <code>Vehicle.name</code> attribute.
    *
    * @param value the name
    */
    @Accessor(qualifier = NAME, type = Accessor.Type.GETTER)
    void setName(final String value) {
        setName(value, null)
    }

    /**
    * <i>Generated method</i> - Setter of <code>Vehicle.name</code> attribute.
    *
    * @param value the name
    * @param loc the value localization key
    * @throws IllegalArgumentException if localization key cannot be mapped to data language
    */
    @Accessor(qualifier = NAME, type = Accessor.Type.GETTER)
    void setName(final String value, final Locale loc) {
        getPersistenceContext().setLocalizedValue(NAME, loc, value)
    }
}
