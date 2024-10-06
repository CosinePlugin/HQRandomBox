package kr.cosine.randombox.listener

import kr.cosine.randombox.view.RandomBoxService
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

@Listener
class RandomBoxListener(
    private val randomBoxService: RandomBoxService
) {
    @Subscribe
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.hand != EquipmentSlot.HAND) return
        if (!event.action.name.contains("RIGHT_CLICK")) return
        val itemStack = event.item ?: return
        if (randomBoxService.useRandomBox(event.player, itemStack)) {
            event.isCancelled = true
        }
    }
}