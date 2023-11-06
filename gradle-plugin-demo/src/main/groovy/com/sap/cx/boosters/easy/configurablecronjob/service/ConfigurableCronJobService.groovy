package com.sap.cx.boosters.easy.configurablecronjob.service

import com.sap.cx.boosters.easy.configurablecronjob.easytype.enums.CronJobType
import de.hybris.platform.enumeration.EnumerationService
import org.springframework.stereotype.Service

import javax.annotation.Resource

@Service
class ConfigurableCronJobService {

    @Resource
    EnumerationService enumerationService

    boolean isImport(final CronJobType type) {
        type == CronJobType.IMPORT
    }

    boolean isExport(final CronJobType type) {
        type == CronJobType.EXPORT
    }

    String getEnumerationValue(final String code) {
        enumerationService.getEnumerationValue(CronJobType, code)
    }

}
