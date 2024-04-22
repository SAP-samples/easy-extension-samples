package com.sap.cx.boosters.easy.easytutorialstep6.slotgenerator.impl

import com.sap.cx.boosters.easy.easytutorialstep6.models.DeliverySlotModel
import com.sap.cx.boosters.easy.easytutorialstep6.service.DeliverySlotService
import com.sap.cx.boosters.easy.easytutorialstep6.service.VehicleService
import com.sap.cx.boosters.easy.easytutorialstep6.slotgenerator.DeliverySlotsGenerator
import de.hybris.platform.ordersplitting.WarehouseService
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ModelService
import org.apache.commons.lang3.time.DateFormatUtils

import javax.annotation.Resource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class DefaultDeliverySlotGenerator implements DeliverySlotsGenerator {

    @Resource
    private WarehouseService warehouseService

    @Resource
    private ModelService modelService

    @Resource
    private VehicleService vehicleService

    @Resource
    private DeliverySlotService deliverySlotService

    @Override
    void generateSlots(String warehouseCode, String from, String to, String vehicleCode, int available) {
        def warehouse = warehouseService.getWarehouseForCode(warehouseCode)
        def vehicle = vehicleService.getForCode(vehicleCode)
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        DateTimeFormatter codeDateFormat = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")

        LocalDateTime startTime = LocalDate.parse(from, dateFormatter).atStartOfDay().withHour(8).withMinute(0).withSecond(0)
        LocalDateTime endTime = LocalDate.parse(to, dateFormatter).atStartOfDay().withHour(19).withMinute(59).withSecond(59)

        def slots = new ArrayList<DeliverySlotModel>()

        for (LocalDateTime slotStartTime = startTime; slotStartTime.isBefore(endTime); slotStartTime = slotStartTime.plusHours(2)) {
            LocalDateTime slotEndTime = slotStartTime.plusHours(1).plusMinutes(59).plusSeconds(59)
            LocalDateTime dayStart = slotStartTime.withHour(7).withMinute(59).withSecond(59)
            LocalDateTime dayEnd = slotStartTime.withHour(20).withMinute(0).withSecond(0)
            if (slotStartTime.isAfter(dayStart) && slotEndTime.isBefore(dayEnd)) {
                def code = String.format('%s_%s-%s', warehouseCode, codeDateFormat.format(slotStartTime), codeDateFormat.format(slotEndTime))
                if (null == deliverySlotService.findSlotByCode(code)) {
                    DeliverySlotModel deliverySlot = modelService.create(DeliverySlotModel)

                    deliverySlot.code = code
                    deliverySlot.warehouse = warehouse
                    deliverySlot.vehicle = vehicle
                    deliverySlot.starttime = Date.from(slotStartTime.atZone(ZoneId.systemDefault()).toInstant())
                    deliverySlot.endtime = Date.from(slotEndTime.atZone(ZoneId.systemDefault()).toInstant())
                    deliverySlot.available = available

                    slots.add(deliverySlot)
                }
            }
        }
        if (!slots.isEmpty()) {
            modelService.saveAll(slots)
        }
    }

    @Override
    void generateSlotsForNextXDays(String warehouseCode, int daysCount, String vehicleCode, int available) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        String from = dateFormatter.format(LocalDateTime.now())
        String to = dateFormatter.format(LocalDateTime.now().plusDays(daysCount))
        this.generateSlots(warehouseCode, from, to, vehicleCode, available)
    }
}
