package kr.cosine.randombox.data

import kr.cosine.randombox.view.RandomBoxViewModel
import kr.hqservice.framework.nms.extension.getNmsItemStack
import kr.hqservice.framework.nms.extension.nms
import org.bukkit.inventory.ItemStack

class ChanceItem(
    private val itemStack: ItemStack
) {

    private companion object {
        const val CHANCE_KEY = "HQRandomBoxChance"
    }

    fun hasChance(): Boolean {
        val nmsItemStack = itemStack.getNmsItemStack()
        if (!nmsItemStack.hasTag()) return false
        val tag = nmsItemStack.getTag().getDoubleOrNull(CHANCE_KEY)
        return tag != null
    }

    fun getChance(): Double {
        return itemStack.getNmsItemStack().getTag().getDouble(CHANCE_KEY)
    }

    fun setChance(chance: Double = 100.0) {
        itemStack.nms {
            tag {
                setDouble(CHANCE_KEY, chance)
            }
        }
    }

    fun getItemStack(): ItemStack = itemStack.clone()

    fun getOriginalItemStack(): ItemStack {
        return getItemStack().nms {
            tag {
                remove(CHANCE_KEY)
            }
        }
    }
}