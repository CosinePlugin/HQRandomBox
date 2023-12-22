package kr.cosine.randombox.view

import kr.cosine.randombox.data.ChanceItem
import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.extension.format
import kr.hqservice.framework.bukkit.core.extension.editMeta
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.nms.extension.getDisplayName
import kr.hqservice.framework.nms.extension.virtual
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

@Bean
class RandomBoxViewModel(
    private val plugin: Plugin,
    private val server: Server
) {

    private val anvilTitle = TextComponent("확률 설정")
    private val baseItemStack = ItemStack(Material.PAPER).editMeta { setDisplayName("§r") }
    private val resultItemStack = ItemStack(Material.PAPER).editMeta { setDisplayName("§f설정하기") }

    fun runSync(actionScope: () -> Unit) {
        server.scheduler.runTask(plugin, actionScope)
    }

    fun setChanceItems(randomBox: RandomBox, itemStacks: List<ItemStack>) {
        val chanceItems = itemStacks.map {
            ChanceItem(it).apply {
                if (!hasChance()) {
                    setChance()
                }
            }
        }
        randomBox.setChanceItems(chanceItems)
    }

    fun setChance(player: Player, chanceItem: ChanceItem, openScope: (Boolean) -> Unit) {
        player.virtual {
            anvil(anvilTitle) {
                setBaseItem(baseItemStack)
                setResultItem(resultItemStack)
                setConfirmHandler { input ->
                    if (input.isEmpty()) {
                        player.sendMessage("§c확률을 입력해주세요.")
                        return@setConfirmHandler false
                    }
                    val chance = input.toDoubleOrNull() ?: run {
                        player.sendMessage("§c숫자만 입력할 수 있습니다.")
                        return@setConfirmHandler false
                    }
                    chanceItem.setChance(chance)
                    player.sendMessage("§a${chanceItem.getItemStack().getDisplayName()}§a이(가) 뽑힐 확률을 ${chance.format()}%로 설정하였습니다.")
                    openScope(true)
                    return@setConfirmHandler true
                }
                setCloseHandler {
                    openScope(false)
                }
            }
        }
    }
}