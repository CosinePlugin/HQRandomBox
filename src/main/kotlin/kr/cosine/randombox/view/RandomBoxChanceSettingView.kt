package kr.cosine.randombox.view

import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.extension.format
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.container.HQContainer
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory

class RandomBoxChanceSettingView(
    private val randomBoxViewModel: RandomBoxViewModel,
    private val randomBox: RandomBox
) : HQContainer(54, "${randomBox.key} 랜덤 박스 - 확률 설정", true) {

    private val chanceItems = randomBox.getChanceItems()

    override fun initialize(inventory: Inventory) {
        chanceItems.forEachIndexed { index, chanceItem ->
            val itemStack = chanceItem.getItemStack()
            HQButtonBuilder(itemStack).apply {
                setLore(getLore() + listOf(
                    "",
                    "§a§l| §f확률: ${chanceItem.getChance().format()}%",
                    "",
                    "§a[ 클릭 시 확률을 설정합니다. ]"
                ))
                setClickFunction { event ->
                    if (event.getClickType() != ClickType.LEFT) return@setClickFunction
                    val player = event.getWhoClicked()
                    player.closeInventory()
                    player.sendMessage("§a${itemStack.getDisplayName()}§a이(가) 뽑힐 확률을 입력해주세요.")
                    randomBoxViewModel.setChance(player, chanceItem) { isSuccessful ->
                        reopen(player)
                        if (isSuccessful) {
                            randomBox.isChanged = true
                        }
                    }
                }
            }.build().setSlot(this, index)
        }
    }

    private fun reopen(player: Player) {
        randomBoxViewModel.runSync {
            refresh()
            open(player)
        }
    }
}