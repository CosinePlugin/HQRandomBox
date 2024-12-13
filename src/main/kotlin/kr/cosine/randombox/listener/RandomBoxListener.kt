package kr.cosine.randombox.listener

import kr.cosine.randombox.registry.ChatObserverRegistry
import kr.cosine.randombox.service.RandomBoxService
import kr.hqservice.framework.bukkit.core.listener.HandleOrder
import kr.hqservice.framework.bukkit.core.listener.Listener
import kr.hqservice.framework.bukkit.core.listener.Subscribe
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent

@Listener
class RandomBoxListener(
    private val chatObserverRegistry: ChatObserverRegistry,
    private val randomBoxService: RandomBoxService
) {
    @Subscribe
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (!event.action.name.contains("RIGHT_CLICK")) return
        val itemStack = event.item ?: return
        if (randomBoxService.useRandomBox(event.player, itemStack)) {
            event.isCancelled = true
        }
    }

    @Subscribe(handleOrder = HandleOrder.FIRST, ignoreCancelled = true)
    fun onPlayerAsyncChat(event: AsyncPlayerChatEvent) {
        chatObserverRegistry.observe(event)
    }

    @Subscribe
    fun onPlayerQuit(event: PlayerQuitEvent) {
        chatObserverRegistry.removeChatObserver(event.player.uniqueId)
    }
}