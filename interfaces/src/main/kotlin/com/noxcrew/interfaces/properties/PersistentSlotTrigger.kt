package com.noxcrew.interfaces.properties

import com.noxcrew.interfaces.grid.GridPoint
import com.noxcrew.interfaces.slot.SlotPredicate

public class PersistentSlotTrigger(private val predicate: SlotPredicate) : Trigger by DelegateTrigger() {
    public fun handleSlotUpdates(points: List<GridPoint>) {
        if (points.any { predicate.includes(it) }) {
            trigger()
        }
    }
}
