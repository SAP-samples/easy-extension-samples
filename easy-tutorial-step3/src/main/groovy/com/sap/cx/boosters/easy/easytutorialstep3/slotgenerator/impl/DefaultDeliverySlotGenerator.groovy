package com.sap.cx.boosters.easy.easytutorialstep3.slotgenerator.impl


import com.sap.cx.boosters.easy.easytutorialstep3.slotgenerator.DeliverySlotsGenerator

class DefaultDeliverySlotGenerator implements DeliverySlotsGenerator {

    @Override
    void generateSlots(String warehouseCode, String from, String to, String vehicleCode, int available) {
        //TODO Retrieve the warehouse for code using warehouse service
        // Retrieve the vehicle using vehicle service for vehicle code
        // Create the two hours long slots everyday between the from and to dates. The slots should be defined between 8 AM and 8 PM
    }

    @Override
    void generateSlotsForNextXDays(String warehouseCode, int daysCount, String vehicleCode, int available) {
        //TODO Define the from date as today's date
        // Define the to date as today's date + daysCount
        // call the generate slots for the applicable arguments
        // Hint format the dates in the acceptable date format
    }
}
