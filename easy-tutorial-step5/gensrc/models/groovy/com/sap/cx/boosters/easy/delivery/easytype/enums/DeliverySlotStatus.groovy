/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 */
package com.sap.cx.boosters.easy.delivery.easytype.enums

import de.hybris.platform.core.HybrisEnumValue

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

class DeliverySlotStatus implements HybrisEnumValue
{
    public final static String _TYPECODE = "DeliverySlotStatus"
    public final static String SIMPLE_CLASSNAME = "DeliverySlotStatus"
    private static final ConcurrentMap<String, HybrisEnumValue> cache = new ConcurrentHashMap<String, HybrisEnumValue>()

    public static final DeliverySlotStatus BOOKED = valueOf("BOOKED")
    public static final DeliverySlotStatus CONFIRMED = valueOf("CONFIRMED")

    private final String code
    private final String codeLowerCase
    private static final long serialVersionUID = 0L

    private DeliverySlotStatus(final String code)
    {
        this.code = code.intern();
        this.codeLowerCase = this.code.toLowerCase().intern()
    }

    @Override
    String getType() {
        return SIMPLE_CLASSNAME
    }

    @Override
    String getCode() {
        return this.code
    }

    static DeliverySlotStatus valueOf(final String code)
    {
        final String key = code.toLowerCase();
        DeliverySlotStatus result = cache.get(key);
        if (result == null)
        {
            DeliverySlotStatus newValue = new DeliverySlotStatus(code)
            DeliverySlotStatus previous = cache.putIfAbsent(key, newValue)
            result = previous != null ? previous : newValue
        }
        return result;
    }

    @Override
    boolean equals(final Object obj)
    {
        try
        {
            final HybrisEnumValue enum2 = (HybrisEnumValue) obj

            return this.is(enum2) || (enum2 != null && !this.getClass().isEnum() && !enum2.getClass().isEnum() && this.getType().equalsIgnoreCase(enum2.getType()) && this.getCode().equalsIgnoreCase(enum2.getCode()))
        }
        catch (final ClassCastException e)
        {
            return false
        }
    }

    @Override
    int hashCode()
    {
        return this.codeLowerCase.hashCode()
    }

    @Override
    String toString()
    {
        return this.code.toString()
    }
}
