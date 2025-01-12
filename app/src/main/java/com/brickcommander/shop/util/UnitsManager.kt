package com.brickcommander.shop.util

data class ItemUnit(
    val id: Int,
    val name: String,
    val type: UnitType,  // Mass, Volume, etc.
    val conversionFactor: Double // Conversion factor to the base unit
)

enum class UnitType {
    NONE, MASS, VOLUME, COUNT, LENGTH
}

object UnitsManager {
    private val units = listOf(
        ItemUnit(0, "None", UnitType.NONE, 1.0),
        ItemUnit(1, "kg", UnitType.MASS, 1000.0),
        ItemUnit(2, "gram", UnitType.MASS, 1.0),
        ItemUnit(3, "mg", UnitType.MASS, 0.001),
        ItemUnit(4, "ton", UnitType.MASS, 1_000_000.0),
        ItemUnit(5, "liter", UnitType.VOLUME, 1.0),
        ItemUnit(6, "mL", UnitType.VOLUME, 0.001),
        ItemUnit(7, "pcs", UnitType.COUNT, 1.0),
        ItemUnit(8, "dozen", UnitType.COUNT, 12.0),
        ItemUnit(9, "ft", UnitType.LENGTH, 3.28084167),
        ItemUnit(10, "inch", UnitType.LENGTH, 39.3701),
        ItemUnit(11, "meter", UnitType.LENGTH, 1.0),
        ItemUnit(12, "cm", UnitType.LENGTH, 0.01),
        ItemUnit(13, "mm", UnitType.LENGTH, 0.001)
    )

    // Get list of units
    fun getUnits() = units.toList()

    fun getUnitsByUnitType(unitId: Int): List<ItemUnit> {
        if(unitId == -1) return units
        return units.filter { units[unitId].type == it.type }.toList()
    }

    fun getUnitNamesByUnitType(unitId: Int): List<String> {
        if(unitId == -1) return units.map { it.name }
        return units.filter { units[unitId].type == it.type }.map { it.name }.toList()
    }

    fun getUnitIdByName(name: String) = units.find { it.name == name }!!.id

    fun getNameById(id: Int) = units.find { it.id == id }!!.name

    fun getUnitById(id: Int) = units.find { it.id == id }!!

    fun hasSameUnitType(id1: Int, id2: Int): Boolean {
        return getUnitById(id1).type == getUnitById(id2).type
    }

    fun hasSameUnitType(id1: Int, id2: Int, it3: Int): Boolean {
        return getUnitById(id1).type == getUnitById(id2).type && getUnitById(id2).type == getUnitById(it3).type
    }

//    // Add or update a unit
//    fun addOrUpdateUnit(unit: Unit) {
//        units.removeAll { it.name == unit.name }
//        units.add(unit)
//    }
//
//    // Remove a unit
//    fun removeUnit(unitName: String) {
//        units.removeAll { it.name == unitName }
//    }

    // Convert between units
    fun convert(value: Double, fromUnit_Id: Int, toUnit_Id: Int): Double {
        val from = units.find { it.id == fromUnit_Id }
        val to = units.find { it.id == toUnit_Id }

        if (from == null || to == null || from.type != to.type) {
            throw IllegalArgumentException("Invalid or incompatible units")
        }

//        if (from.conversionFactor == null || to.conversionFactor == null) {
//            throw IllegalArgumentException("Cannot convert between non-standard units")
//        }

        return value * (to.conversionFactor / from.conversionFactor)
    }
}
