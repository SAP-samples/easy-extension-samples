// import de.hybris.platform.converters.impl.AbstractPopulatingConverter
// https://blog.mrhaki.com/2011/06/groovy-goodness-add-imports.html
import static com.sap.cx.boosters.easy.core.extension.task.processor.impl.install.EasyBeanDefinitionReader.*
import org.slf4j.LoggerFactory

def easyExtensionName = 'delivery-slot-management'
def LOG = LoggerFactory.getLogger("easy.${easyExtensionName}")
LOG.info ">>> Registering Spring beans for ${easyExtensionName} ... AEI"

println "Registering core beans for ${easyExtensionName}"

easyCoreBeans {

	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService)
	deliverySlotCleanupService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotCleanupService)
	defaultCartGenericDao(de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao, 'Cart')
	homeDeliveryCommercePlaceOrderMethodHook(com.sap.cx.boosters.easy.delivery.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHook)
	deliverySlotManagementAttributeHandler(com.sap.cx.boosters.easy.delivery.handler.DeliverySlotManagementAttributeHandler)
	homeDeliveryModeOrderPopulator(com.sap.cx.boosters.easy.delivery.populators.HomeDeliveryModeOrderPopulator)

	easyWarehouseService(com.sap.cx.boosters.easy.delivery.service.EasyWarehouseService)
	easyEnhancedWarehouseService(com.sap.cx.boosters.easy.delivery.service.EasyEnhancedWarehouseService)
	registerAlias('easyEnhancedWarehouseService', 'warehouseService')

}

println 'Adding the CommercePlaceOrderHook:'
commercePlaceOrderMethodHooks.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryCommercePlaceOrderMethodHook')}
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHook)

println 'Modifying order populator for the home delivery mode:'
orderConverter.populators.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryModeOrderPopulator')}
orderConverter.populators.add(homeDeliveryModeOrderPopulator)

println "Registering REST beans for ${easyExtensionName}"

easyRESTBeans {

	availableSlotsController(com.sap.cx.boosters.easy.delivery.controller.AvailableSlotsController)
	bookDeliveryController(com.sap.cx.boosters.easy.delivery.controller.BookDeliveryController)
	cancelDeliveryController(com.sap.cx.boosters.easy.delivery.controller.CancelDeliveryController)
	confirmDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ConfirmDeliveryController)
	getBookedDeliveryController(com.sap.cx.boosters.easy.delivery.controller.GetBookedDeliveryController)
	changeDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ChangeDeliveryController)

}

println "Registering web /hac beans for ${easyExtensionName}"

easyWebBeans('/hac') {

	changeDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ChangeDeliveryController)

}

LOG.info "Beans registered for ${easyExtensionName}"
