package com.sap.cx.boosters.easy.configurablecronjob.easytype.enums

import de.hybris.platform.core.HybrisEnumValue
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

@EqualsAndHashCode
@ToString
class CronJobType implements HybrisEnumValue {

    public final static String _TYPECODE = CronJobType.simpleName

    private static final ConcurrentMap<String, HybrisEnumValue> cache = new ConcurrentHashMap<String, HybrisEnumValue>()

    public static final CronJobType IMPORT = valueOf('IMPORT')

    public static final CronJobType EXPORT = valueOf('EXPORT')

    private final String code

    static CronJobType valueOf(final String code) {
        final var key = code.toLowerCase()
        cache.computeIfAbsent(key,it -> new CronJobType(it))
    }

    private CronJobType(final String code) {
        this.code = code.intern()
    }

    @Override
    String getType() {
        return _TYPECODE
    }

    @Override
    String getCode() {
        return code
    }

}
