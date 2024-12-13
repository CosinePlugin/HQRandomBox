package kr.cosine.randombox.service

import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.registry.ChatObserverRegistry
import kr.cosine.randombox.view.RandomBoxSettingView
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.entity.Player

@Service
class RandomBoxViewService(
    private val plugin: HQBukkitPlugin,
    private val chatObserverRegistry: ChatObserverRegistry
) {
    fun openRandomBoxSettingView(player: Player, randomBox: RandomBox) {
        chatObserverRegistry.removeChatObserver(player.uniqueId)
        RandomBoxSettingView(plugin, chatObserverRegistry, randomBox).open(player)
    }
}