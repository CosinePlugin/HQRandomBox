package kr.cosine.randombox.listener

import kr.cosine.randombox.service.RandomBoxService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

@Listener
class RandomBoxListener(
    private val randomBoxService: RandomBoxService
) {

    private companion object {
        const val RIGHT_CLICK = "RIGHT_CLICK"
    }

    @Subscribe
    fun onRightClick(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        if (!event.action.name.contains(RIGHT_CLICK)) return
        val itemStack = event.item ?: return
        if (itemStack.type.isAir) return
        val player = event.player
        if (randomBoxService.useRandomBox(player, itemStack)) {
            event.isCancelled = true
        }
    }
}