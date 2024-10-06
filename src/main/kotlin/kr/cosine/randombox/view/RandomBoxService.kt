package kr.cosine.randombox.view

import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.enums.Message
import kr.cosine.randombox.registry.SettingRegistry
import kr.cosine.randombox.registry.RandomBoxRegistry
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.netty.api.PacketSender
import kr.hqservice.framework.nms.extension.getDisplayName
import kr.hqservice.framework.nms.extension.getNmsItemStack
import kr.hqservice.framework.nms.extension.nms
import kr.hqservice.giftbox.api.GiftBoxAPI
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Service
class RandomBoxService(
    private val packetSender: PacketSender,
    private val settingRegistry: SettingRegistry,
    private val randomBoxRegistry: RandomBoxRegistry
) {
    private val giftBoxFactory by lazy { GiftBoxAPI.getFacto() }
    private val giftBoxService by lazy { GiftBoxAPI.getBoxService() }

    fun createRandomBox(name: String): Boolean {
        if (randomBoxRegistry.isRandomBox(name)) return false
        val randomBox = RandomBox(name)
        randomBoxRegistry.setRandomBox(name, randomBox)
        return true
    }

    fun deleteRandomBox(name: String) {
        randomBoxRegistry.removeRandomBox(name)
    }

    fun makeRandomBox(itemStack: ItemStack, name: String) {
        itemStack.nms {
            tag {
                setString(RANDOM_BOX_KEY, name)
            }
        }
    }

    fun useRandomBox(player: Player, handItemStack: ItemStack): Boolean {
        val name = handItemStack.getNmsItemStack().getTagOrNull()?.getStringOrNull(RANDOM_BOX_KEY) ?: return false
        val randomBox = randomBoxRegistry.findRandomBox(name) ?: return false

        val giftBoxSetting = settingRegistry.findGiftBoxSetting()
        val isGiftBoxEnabled = giftBoxSetting?.isEnabled == true
        val playerInventory = player.inventory
        if (!isGiftBoxEnabled && !playerInventory.isAddable()) {
            Message.LACK_INVENTORY.getMessages().forEach(player::sendMessage)
            return true
        }

        val randomBoxItemStack = randomBox.getRandomBoxItemStackByChance()
        val randomBoxItemMeta = randomBoxItemStack.randomBoxItemMeta

        handItemStack.amount--

        val itemStack = randomBoxItemStack.toOriginalItemStack()
        if (giftBoxSetting != null && isGiftBoxEnabled) {
            val giftBox = giftBoxFactory.of(giftBoxSetting.displayName, giftBoxSetting.lore, itemStack)
            giftBoxService.send(player.uniqueId, giftBox)
        } else {
            playerInventory.addItem(itemStack)
        }

        val replace: (String) -> String = {
            it.replace("%item%", itemStack.getDisplayName())
                .replace("%chance%", randomBoxItemMeta.chance.removeZero())
                .replace("%player%", player.name)
        }
        val playerPickMessage = Message.PLAYER_PICK.getMessages(replace)
        playerPickMessage.forEach(player::sendMessage)
        settingRegistry.findPickSound()?.playSound(player)

        if (randomBoxItemMeta.broadcast) {
            val broadcastPickMessage = Message.BROADCAST_PICK.getMessages(replace)
                .joinToString("\n")
                .run(::TextComponent)
            packetSender.broadcast(broadcastPickMessage, false)
        }
        return true
    }

    private fun Inventory.isAddable(): Boolean {
        return storageContents.any { it == null || it.type == Material.AIR }
    }

    private fun Double.removeZero(): String {
        val integer = toString().toIntOrNull() ?: return "$this"
        return "$integer"
    }

    private companion object {
        const val RANDOM_BOX_KEY = "HQRandomBox"
    }
}