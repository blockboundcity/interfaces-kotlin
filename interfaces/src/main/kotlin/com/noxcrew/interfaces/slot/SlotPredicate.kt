package com.noxcrew.interfaces.slot

import com.noxcrew.interfaces.grid.GridPoint

public interface SlotPredicate {
    public fun includes(point: GridPoint): Boolean
}
