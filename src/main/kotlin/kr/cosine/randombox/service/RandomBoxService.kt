package kr.cosine.randombox.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.cosine.randombox.config.RandomBoxConfig
import kr.cosine.randombox.config.SettingConfig
import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.registry.RandomBoxRegistry
import kr.cosine.randombox.view.RandomBoxChanceSettingView
import kr.cosine.randombox.view.RandomBoxItemSettingView
import kr.cosine.randombox.view.RandomBoxViewModel
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.framework.nms.extension.getDisplayName
import kr.hqservice.framework.nms.extension.getNmsItemStack
import kr.hqservice.framework.nms.extension.nms
import kr.hqservice.giftbox.api.GiftBoxAPI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

@Service
class RandomBoxService(
    private val settingConfig: SettingConfig,
    private val randomBoxConfig: RandomBoxConfig,
    private val randomBoxRegistry: RandomBoxRegistry,
    private val randomBoxViewModel: RandomBoxViewModel
) {

    private companion object {
        const val RANDOM_BOX_KEY = "HQRandomBox"
    }

    private val giftBoxFactory by lazy { GiftBoxAPI.getFacto() }
    private val giftBoxService by lazy { GiftBoxAPI.getBoxService() }

    fun createRandomBox(key: String): Boolean {
        if (randomBoxRegistry.isRandomBox(key)) return false
        val randomBox = RandomBox(key)
        randomBox.isChanged = true
        randomBoxRegistry.setRandomBox(key, randomBox)
        return true
    }

    fun removeRandomBox(key: String) {
        randomBoxRegistry.removeRandomBox(key)
    }

    fun applyRandomBox(itemStack: ItemStack, key: String): Boolean {
        if (itemStack.type.isAir) return false
        itemStack.nms {
            tag {
                setString(RANDOM_BOX_KEY, key)
            }
        }
        return true
    }

    fun openRandomBoxItemSettingView(player: Player, randomBox: RandomBox) {
        RandomBoxItemSettingView(randomBoxViewModel, randomBox).open(player)
    }

    fun openRandomBoxChanceSettingView(player: Player, randomBox: RandomBox) {
        RandomBoxChanceSettingView(randomBoxViewModel, randomBox).open(player)
    }

    suspend fun save(finishScope: () -> Unit) {
        withContext(Dispatchers.IO) {
            randomBoxConfig.save()
            finishScope()
        }
    }

    fun reload() {
        settingConfig.reload()
    }

    fun useRandomBox(player: Player, itemStack: ItemStack): Boolean {
        val randomBoxKey = itemStack.getRandomBoxKey() ?: return false
        val randomBox = randomBoxRegistry.findRandomBox(randomBoxKey) ?: return false
        val playerInventory = player.inventory
        if (!playerInventory.isAddable()) {
            player.sendMessage(settingConfig.inventoryFullMessage)
            return true
        }
        val chanceItem = randomBox.getRandomChanceItem()
        val chanceItemStack = chanceItem.getOriginalItemStack()
        itemStack.amount--
        val giftBoxSetting = settingConfig.giftBoxSetting
        if (giftBoxSetting.isEnabled) {
            val giftBox = giftBoxFactory.of(giftBoxSetting.displayName, giftBoxSetting.lore, itemStack)
            giftBoxService.send(player.uniqueId, giftBox)
        } else {
            playerInventory.addItem(chanceItemStack)
        }
        settingConfig.pickEffect.playEffect(player, chanceItemStack.getDisplayName())
        return true
    }

    private fun ItemStack.getRandomBoxKey(): String? {
        val nmsItemStack = this.getNmsItemStack()
        if (!nmsItemStack.hasTag()) return null
        return nmsItemStack.getTag().getStringOrNull(RANDOM_BOX_KEY)
    }

    private fun Inventory.isAddable(): Boolean {
        return storageContents.any { it == null || it.type == Material.AIR }
    }
}