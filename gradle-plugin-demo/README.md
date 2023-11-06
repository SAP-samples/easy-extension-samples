# Configurable CronJob Demo

This example demonstrated creation of a custom cron job type

Test in hac:

```groovy
def cj = cronJobService.getCronJob('configurableCronJob')
println "${cj.code} ${cj.configuration}"
cronJobService.performCronJob(cj,true)
println "${cj.status} ${cj.result} ${cj.endTime}"
```

