package kr.cosine.randombox.view

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.cosine.randombox.observer.ChatObserver
import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.data.RandomBoxItemStack
import kr.cosine.randombox.registry.ChatObserverRegistry
import kr.cosine.randombox.registry.RandomBoxRegistry
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.coroutine.extension.BukkitMain
import kr.hqservice.framework.bukkit.core.extension.editMeta
import kr.hqservice.framework.inventory.button.HQButtonBuilder
import kr.hqservice.framework.inventory.container.HQContainer
import kr.hqservice.framework.nms.extension.getDisplayName
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.AsyncPlayerChatEvent
import org.bukkit.inventory.Inventory

class RandomBoxSettingView(
    private val plugin: HQBukkitPlugin,
    private val chatObserverRegistry: ChatObserverRegistry,
    private val randomBoxRegistry: RandomBoxRegistry,
    private val randomBox: RandomBox
) : HQContainer(54, "${randomBox.name} 랜덤박스 설정") {
    private var page = 0

    private val randomBoxItemStacks get() = randomBox.randomBoxItemStacks
    private var currentRandomBoxItemStacks = emptyList<RandomBoxItemStack>()

    override fun initialize(inventory: Inventory) {
        inventory.clear()
        drawRandomBoxItemStacks()
        drawPageButton()
    }

    private fun drawRandomBoxItemStacks() {
        currentRandomBoxItemStacks = randomBoxItemStacks.drop(ITEM_SIZE * page).take(ITEM_SIZE)
        currentRandomBoxItemStacks.forEachIndexed { slot, randomBoxItemStack ->
            val randomBoxItemMeta = randomBoxItemStack.randomBoxItemMeta
            val itemStack = randomBoxItemStack.toItemStack().editMeta {
                lore = (lore ?: emptyList()) + listOf(
                    "",
                    "§a§l| §f확률: §a${randomBoxItemMeta.chance}",
                    "§b§l| §f공지: ${randomBoxItemMeta.broadcast.run { if (this) "§a" else "§c비" }}활성화",
                    "",
                    "§a좌클릭 §7▸ §f확률을 설정합니다.",
                    "§b우클릭 §7▸ §f공지 여부를 토글합니다.",
                    "§c쉬프트+우클릭 §7▸ §f목록에서 삭제합니다."
                )
            }
            inventory.setItem(slot, itemStack)
        }
    }

    private fun drawPageButton() {
        HQButtonBuilder(Material.RED_STAINED_GLASS_PANE).apply {
            setRemovable(true)
            setDisplayName("§c이전 페이지로 이동")
            setClickFunction { event ->
                if (event.getClickType() != ClickType.LEFT) return@setClickFunction
                val player = event.getWhoClicked()
                player.playButtonSound()
                if (page == 0) {
                    player.sendMessage("§c이전 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                page--
                refresh()
            }
        }.build().setSlot(this, BEFORE_PAGE_SLOT)

        HQButtonBuilder(Material.LIME_STAINED_GLASS_PANE).apply {
            setRemovable(true)
            setDisplayName("§a다음 페이지로 이동")
            setClickFunction { event ->
                if (event.getClickType() != ClickType.LEFT) return@setClickFunction
                val player = event.getWhoClicked()
                player.playButtonSound()
                if (randomBoxItemStacks.size <= (page + 1) * ITEM_SIZE) {
                    player.sendMessage("§c다음 페이지가 존재하지 않습니다.")
                    return@setClickFunction
                }
                page++
                refresh()
            }
        }.build().setSlot(this, NEXT_PAGE_SLOT)

        HQButtonBuilder(Material.WHITE_STAINED_GLASS_PANE).apply {
            setRemovable(true)
            setDisplayName("§r")
        }.build().setSlot(this, backgroundSlots)
    }

    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return
        val player = event.whoClicked as Player
        if (event.rawSlot < inventory.size) {
            onTopClick(event, player)
        } else {
            if (event.click != ClickType.LEFT) return
            onBottomClick(event, player)
        }
    }

    private val InventoryClickEvent.randomBoxItemStack get() = currentRandomBoxItemStacks.getOrNull(rawSlot)

    private fun onTopClick(event: InventoryClickEvent, player: Player) {
        val randomBoxItemStack = event.randomBoxItemStack ?: return
        when (event.click) {
            ClickType.SHIFT_RIGHT -> {
                randomBox.removeRandomBoxItemStack(randomBoxItemStack)
                randomBoxRegistry.isChanged = true
                if (page > 0 && currentRandomBoxItemStacks.size != ITEM_SIZE && randomBoxItemStacks.size % ITEM_SIZE == 0) {
                    page--
                }
                player.playButtonSound()
                refresh()
            }

            ClickType.LEFT -> {
                player.playButtonSound()
                setRandomBoxItemStackChance(player, randomBoxItemStack)
            }

            ClickType.RIGHT -> {
                randomBoxItemStack.editMeta {
                    this.broadcast = !this.broadcast
                }
                randomBoxRegistry.isChanged = true
                player.playButtonSound()
                refresh()
            }

            else -> {}
        }
    }

    private fun setRandomBoxItemStackChance(player: Player, randomBoxItemStack: RandomBoxItemStack) {
        player.closeInventory()
        player.sendMessage("§a확률을 입력해주세요. §c(취소: -)")
        val playerUniqueId = player.uniqueId
        val chatObserver = object : ChatObserver {
            override fun onChat(event: AsyncPlayerChatEvent) {
                if (event.player.uniqueId != playerUniqueId) return
                event.isCancelled = true
                val message = event.message
                if (message == "-") {
                    chatObserverRegistry.removeChatObserver(playerUniqueId)
                    player.sendMessage("§a설정이 취소되었습니다.")
                    reopen(player)
                    return
                }
                val chance = message.toDoubleOrNull() ?: run {
                    player.sendMessage("§c숫자만 입력할 수 있습니다.")
                    return
                }
                if (chance <= 0.0) {
                    player.sendMessage("§c양수만 입력할 수 있습니다.")
                    return
                }
                chatObserverRegistry.removeChatObserver(playerUniqueId)

                randomBoxItemStack.editMeta {
                    this.chance = chance
                }
                randomBoxRegistry.isChanged = true

                player.sendMessage("§a${randomBoxItemStack.toItemStack().getDisplayName()}의 확률을 ${chance}퍼센트로 설정하였습니다.")
                reopen(player)
            }
        }
        chatObserverRegistry.addChatObserver(playerUniqueId, chatObserver)
    }

    private fun reopen(player: Player) {
        plugin.launch(Dispatchers.BukkitMain) {
            refresh()
            open(player)
        }
    }

    private fun onBottomClick(event: InventoryClickEvent, player: Player) {
        val itemStack = event.currentItem?.clone() ?: return
        player.playButtonSound()
        val randomBoxItemStack = RandomBoxItemStack.of(itemStack)
        randomBox.addRandomBoxItemStack(randomBoxItemStack)
        randomBoxRegistry.isChanged = true
        refresh()
    }

    private fun Player.playButtonSound() {
        playSound(location, Sound.UI_BUTTON_CLICK, 1f, 1f)
    }

    private companion object {
        const val ITEM_SIZE = 45

        const val BEFORE_PAGE_SLOT = 45
        const val NEXT_PAGE_SLOT = 53
        val backgroundSlots = 46..52
    }
}