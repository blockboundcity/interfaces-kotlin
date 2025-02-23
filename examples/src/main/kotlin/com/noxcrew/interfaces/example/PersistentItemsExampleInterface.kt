package com.noxcrew.interfaces.example

import com.noxcrew.interfaces.drawable.Drawable.Companion.drawable
import com.noxcrew.interfaces.element.StaticElement
import com.noxcrew.interfaces.grid.GridPoint
import com.noxcrew.interfaces.interfaces.Interface
import com.noxcrew.interfaces.interfaces.buildChestInterface
import com.noxcrew.interfaces.properties.PersistentSlotTrigger
import com.noxcrew.interfaces.slot.SlotPredicate
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

public class SingleSlotPredicate(private val point: GridPoint) : SlotPredicate {
    override fun includes(point: GridPoint): Boolean {
        return this.point == point
    }
}

public class PersistentItemsExampleInterface(private val plugin: ExamplePlugin) : RegistrableInterface {

    override val subcommand: String = "persistent-items"

    override fun create(): Interface<*, *> = buildChestInterface {
        rows = 1

        preventClickingEmptySlots = false
        persistAddedItems = true

        addCloseHandler(InventoryCloseEvent.Reason.entries) { _, view ->
            Bukkit.getScheduler().runTask(
                plugin,
                Runnable {
                    val copy = view.persistentItems.toMap()

                    copy.forEach { (point, itemStack) ->
                        view.setPersistentItem(point, ItemStack.empty())
                        view.player.inventory.addItem(itemStack)
                    }
                },
            )
        }

        val persistentSlotTrigger = PersistentSlotTrigger(SingleSlotPredicate(GridPoint(0, 1)))

        withTransform(persistentSlotTrigger) { pane, view ->
            println("re-render")

            val item = view.persistentItems[GridPoint(0, 1)]
            val itemName = item?.type?.name ?: "Empty"

            val deleteItem = ItemStack(Material.ACACIA_SIGN).name("Delete $itemName")

            // can test event cancellation works correctly by trying to shift-click, double click, etc. with grass blocks
            pane[0, 3] = StaticElement(drawable(ItemStack(Material.GRASS_BLOCK)))

            // can test re-renders with various ways the watched slot is updated (shift click, drag, click, drop, etc.)
            pane[0, 6] = StaticElement(drawable(deleteItem)) { context ->
                context.view.setPersistentItem(GridPoint(0, 1), ItemStack.empty())
            }
        }
    }
}
