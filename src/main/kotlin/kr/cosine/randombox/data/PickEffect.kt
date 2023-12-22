package kr.cosine.randombox.data

import org.bukkit.entity.Player

data class PickEffect(
    private val message: String = "§6%item% 획득!",
    private val sound: String = "minecraft:ui.toast.challenge_complete",
    private val volume: Float = 1f,
    private val pitch: Float = 1f
) {

    fun playEffect(player: Player, itemName: String) {
        player.sendMessage(message.replace("%item%", itemName))
        player.playSound(player.location, sound, volume, pitch)
    }
}
