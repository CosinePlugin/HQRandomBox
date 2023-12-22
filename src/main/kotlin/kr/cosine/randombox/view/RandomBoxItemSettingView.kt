package kr.cosine.randombox.view

import kr.cosine.randombox.data.RandomBox
import kr.hqservice.framework.inventory.container.HQContainer
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory

class RandomBoxItemSettingView(
    private val randomBoxViewModel: RandomBoxViewModel,
    private val randomBox: RandomBox
) : HQContainer(54, "${randomBox.key} 랜덤 박스 - 아이템 설정", false) {

    private val originalChanceItems = randomBox.getChanceItems()

    override fun initialize(inventory: Inventory) {
        originalChanceItems.forEachIndexed { index, chanceItem ->
            inventory.setItem(index, chanceItem.getItemStack())
        }
    }

    override fun onClose(event: InventoryCloseEvent) {
        val player = event.player
        val itemStacks = event.inventory.contents.filter { it != null && !it.type.isAir }
        randomBoxViewModel.setChanceItems(randomBox, itemStacks)
        player.sendMessage("§a아이템이 설정되었습니다.")
    }
}