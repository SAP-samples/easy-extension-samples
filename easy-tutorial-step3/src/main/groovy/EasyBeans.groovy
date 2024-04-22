import com.sap.cx.boosters.easy.easytutorialstep3.service.impl.DefaultDeliverySlotService

logger.info "[${extension.id}] registering Spring beans ..."

easyCoreBeans {
    logger.info "[${extension.id}] registering Spring core beans ..."

    //TODO Declare vehicle generic dao from generic Dao as vehicleGenericDao(DefaultGenericDao, VehicleModel._TYPECODE)
    // you can inject it in vehicleService as
    // @Resource
    // private GenericDao<VehicleModel> vehicleGenericDao

    //TODO Alternatively use FlexibleSearchService by injecting it to VehicleService class as:
    // @Resource
    // private FlexibleSearchService flexibleSearchService

    //TODO Register vehicle service here as
    // vehicleService(DefaultVehicleService)

    //TODO The Delivery Slot generator can generate the delivery slots for next X days or between a dates duration
    // This can be called in the impex script 02-deliveryslots.impex
    // deliverySlotGenerator(DefaultDeliverySlotGenerator)

    deliverySlotService(DefaultDeliverySlotService)

    logger.info "[${extension.id}] registered Spring core beans."

}

easyWebBeans('/easyrest') {
    logger.info "[${extension.id}] registering [/easyrest] Spring beans ..."

    logger.info "[${extension.id}] registered Spring core beans ..."
}

logger.info "[${extension.id}] registered [/easyrest] Spring beans."

