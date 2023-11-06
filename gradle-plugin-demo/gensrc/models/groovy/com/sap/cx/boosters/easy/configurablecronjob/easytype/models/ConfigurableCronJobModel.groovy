package com.sap.cx.boosters.easy.configurablecronjob.easytype.models

import com.sap.cx.boosters.easy.configurablecronjob.easytype.enums.CronJobType
import com.sap.cx.boosters.easytype.ItemModelTrait
import de.hybris.bootstrap.annotations.Accessor
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.servicelayer.model.ItemModelContext
import groovy.transform.InheritConstructors

// @InheritConstructors not strictly required probably, only default constructor is used
@InheritConstructors
class ConfigurableCronJobModel extends CronJobModel {

    static final String _TYPECODE = ConfigurableCronJobModel.simpleName - 'Model'

    static final String TYPE = 'type'

    static final String PARAMETERS = 'parameters'

    static final String MAX_DURATION = 'maxDuration'

    @Accessor(qualifier = TYPE, type = Accessor.Type.GETTER)
    CronJobType getType() {
        return getPersistenceContext().getPropertyValue(TYPE)
    }

    @Accessor(qualifier = TYPE, type = Accessor.Type.SETTER)
    void setType(final CronJobType value) {
        getPersistenceContext().setPropertyValue(TYPE, value)
    }

    @Accessor(qualifier = PARAMETERS, type = Accessor.Type.GETTER)
    Map<String, String> getParams() {
        return getPersistenceContext().getPropertyValue(PARAMETERS)
    }

    @Accessor(qualifier = PARAMETERS, type = Accessor.Type.SETTER)
    void setParams(final Map<String, String> value) {
        getPersistenceContext().setPropertyValue(PARAMETERS, value)
    }

    @Accessor(qualifier = MAX_DURATION, type = Accessor.Type.GETTER)
    Integer getMaxDuration() {
        return getPersistenceContext().getPropertyValue(MAX_DURATION)
    }

    @Accessor(qualifier = MAX_DURATION, type = Accessor.Type.SETTER)
    void setMaxDuration(final Integer value) {
        getPersistenceContext().setPropertyValue(MAX_DURATION, value)
    }

}
