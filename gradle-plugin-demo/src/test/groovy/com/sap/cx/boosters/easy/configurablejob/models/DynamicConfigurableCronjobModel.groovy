package com.sap.cx.boosters.easy.configurablejob.models

import com.sap.cx.boosters.easy.configurablecronjob.easytype.enums.CronJobType
import com.sap.cx.boosters.easytype.ItemModelAttribute
import de.hybris.platform.cronjob.model.CronJobModel

class DynamicConfigurableCronjobModel extends CronJobModel implements ItemModelTrait {

    @ItemModelAttribute(read = true, write = true)
    CronJobType type

    @ItemModelAttribute(read = true, write = true)
    Map<String, String> params

    @ItemModelAttribute(read = true, write = true)
    Integer maxDuration

}
