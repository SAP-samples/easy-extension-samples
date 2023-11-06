package com.sap.cx.boosters.easy.configurablecronjob.job

import com.sap.cx.boosters.easy.configurablecronjob.easytype.models.ConfigurableCronJobModel
import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable
import de.hybris.platform.servicelayer.cronjob.PerformResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ConfigurableJobPerformable extends AbstractJobPerformable<ConfigurableCronJobModel> {

    static final Logger LOG = LoggerFactory.getLogger(ConfigurableJobPerformable.class)

    @Override
    PerformResult perform(ConfigurableCronJobModel cronJob) {
        LOG.info('cronjob configuration : {}', cronJob.params)
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED)
    }

}
