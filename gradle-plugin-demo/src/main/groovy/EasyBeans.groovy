import com.sap.cx.boosters.easy.configurablecronjob.job.ConfigurableJobPerformable
import com.sap.cx.boosters.easy.configurablecronjob.service.ConfigurableCronJobService

logger.info "[${extension.id}] registering Spring beans for ..."

easyCoreBeans {
    configurableJobPerformable(ConfigurableJobPerformable) { beanDef ->
        beanDef.parent = abstractJobPerformable
    }
    dummyService(ConfigurableCronJobService)
}

logger.info "[${extension.id}] spring beans registered"
