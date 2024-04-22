package com.sap.cx.boosters.easy.easytutorialstep3.slotgenerator

interface DeliverySlotsGenerator {
    /**
     * Generates the delivery slots between two dates for a warehouse
     * @param warehouseCode the warehouse code
     * @param from the date (dd-MM-yyyy) from when the delivery slots are to be generated
     * @param to the date (dd-MM-yyyy) to when the delivery slots are to be generated
     * @param vehicleCode the vehicle code for the delivery slot
     * @param available the available bookings for the slot
     */
    void generateSlots(String warehouseCode, String from, String to, String vehicleCode, int available)

    /**
     * Generates the delivery slots between two dates for a warehouse
     * @param warehouseCode the warehouse code
     * @param daysCount number of days to generate the slots from the current date
     * @param vehicleCode the vehicle code for the delivery slot
     * @param available the available bookings for the slot
     */
    void generateSlotsForNextXDays(String warehouseCode, int daysCount, String vehicleCode, int available)

}
