package com.sap.cx.boosters.easy.cronjobdemo.job


import de.hybris.platform.cronjob.enums.CronJobResult
import de.hybris.platform.cronjob.enums.CronJobStatus
import de.hybris.platform.cronjob.model.CronJobModel
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable
import de.hybris.platform.servicelayer.cronjob.PerformResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class DemoJobPerformable extends AbstractJobPerformable<CronJobModel> {
    static final Logger LOG = LoggerFactory.getLogger(DemoJobPerformable.class)

    @Override
    PerformResult perform(CronJobModel cronJob) {
        LOG.info("demoConfiguration : {}", cronJob.demoConfiguration)
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED)
    }
}
