/*
* Copyright (c) 2024 SAP SE or an SAP affiliate company. All rights reserved.
* ----------------------------------------------------------------
* --- WARNING: THIS FILE IS GENERATED BY EASY GRADLE PLUGIN AND WILL BE
* --- OVERWRITTEN IF REGENERATED AGAIN USING easy-class-gen GRADLE TASK!
* --- Generated at Fri Jun 21 11:41:41 CEST 2024
* ----------------------------------------------------------------
*/

package com.sap.cx.boosters.easy.easyapi.models

import de.hybris.platform.core.model.ItemModel
import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.servicelayer.model.ItemModelContext

/**
* Generated model class for easy type EasyApiKey.
*/
class EasyApiKeyModel extends ItemModel {

    /**<i>Generated model type code constant.</i>*/
    public static final String _TYPECODE = "EasyApiKey"

    /** <i>Generated constant</i> - Attribute key of <code>EasyApiKey.key</code>. */
    public static final String KEY = "key"

    /** <i>Generated constant</i> - Attribute key of <code>EasyApiKey.id</code>. */
    public static final String ID = "id"

    /**
    * <i>Generated constructor</i> - Default constructor for generic creation.
    */
    EasyApiKeyModel() {
        super()
    }

    /**
    * <i>Generated constructor</i> - Default constructor for creation with existing context
    * @param ctx the model context to be injected, must not be null
    */
    EasyApiKeyModel(final ItemModelContext ctx) {
        super(ctx)
    }


    /**
    * <i>Generated method</i> - Getter of the <code>EasyApiKey.key</code> attribute.
    * @return the key
    */
    @Accessor(qualifier = KEY, type = Accessor.Type.GETTER)
    String getKey() {
        return getPersistenceContext().getPropertyValue(KEY)
    }

    /**
    * <i>Generated method</i> - Setter of <code>EasyApiKey.key</code> attribute.
    *
    * @param value the key
    */
    @Accessor(qualifier = KEY, type = Accessor.Type.SETTER)
    void setKey(final String value) {
        getPersistenceContext().setPropertyValue(KEY, value)
    }

    /**
    * <i>Generated method</i> - Getter of the <code>EasyApiKey.id</code> attribute.
    * @return the id
    */
    @Accessor(qualifier = ID, type = Accessor.Type.GETTER)
    String getId() {
        return getPersistenceContext().getPropertyValue(ID)
    }

    /**
    * <i>Generated method</i> - Setter of <code>EasyApiKey.id</code> attribute.
    *
    * @param value the id
    */
    @Accessor(qualifier = ID, type = Accessor.Type.SETTER)
    void setId(final String value) {
        getPersistenceContext().setPropertyValue(ID, value)
    }
}
