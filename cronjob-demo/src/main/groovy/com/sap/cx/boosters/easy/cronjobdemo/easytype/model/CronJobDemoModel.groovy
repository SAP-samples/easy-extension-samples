package com.sap.cx.boosters.easy.cronjobdemo.easytype.model

import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.servicelayer.model.ItemModelContext

class CronJobDemoModel extends CronJobModel {
    static final String _TYPECODE = "CronJobDemo"
    static final String DEMOCONFIGURATION = "demoConfiguration"

    CronJobDemoModel() {
        super()
    }

    CronJobDemoModel(final ItemModelContext ctx) {
        super(ctx)
    }

    @Accessor(qualifier = DEMOCONFIGURATION, type = Accessor.Type.GETTER)
    String getDemoConfiguration() {
        return getPersistenceContext().getPropertyValue(DEMOCONFIGURATION)
    }

    @Accessor(qualifier = DEMOCONFIGURATION, type = Accessor.Type.SETTER)
    void setDemoConfiguration(final String value) {
        getPersistenceContext().setPropertyValue(DEMOCONFIGURATION, value)
    }
}
