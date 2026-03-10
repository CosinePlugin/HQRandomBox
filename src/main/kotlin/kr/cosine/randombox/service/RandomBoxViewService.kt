package kr.cosine.randombox.service

import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.view.RandomBoxSettingView
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Service
import org.bukkit.entity.Player

@Service
class RandomBoxViewService(
    private val plugin: HQBukkitPlugin
) {
    fun openRandomBoxSettingView(player: Player, randomBox: RandomBox) {
        RandomBoxSettingView(plugin, randomBox).open(player)
    }
}