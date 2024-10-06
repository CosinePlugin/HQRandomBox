package kr.cosine.randombox.data

import com.google.gson.Gson
import kr.hqservice.framework.nms.extension.getNmsItemStack
import kr.hqservice.framework.nms.extension.nms
import kr.hqservice.framework.nms.wrapper.item.NmsNBTTagCompoundWrapper
import org.bukkit.inventory.ItemStack

class RandomBoxItemStack(
    private val itemStack: ItemStack
) {
    private val tag get() = itemStack.getNmsItemStack().getTag()

    var randomBoxItemMeta = tag.getRandomBoxItemMeta(RANDOM_BOX_ITEM_META_KEY)
        private set

    fun setRandomBoxItemMeta(randomBoxItemMeta: RandomBoxItemMeta) {
        itemStack.nms {
            tag {
                setRandomBoxItemMeta(RANDOM_BOX_ITEM_META_KEY, randomBoxItemMeta)
                this@RandomBoxItemStack.randomBoxItemMeta = randomBoxItemMeta
            }
        }
    }

    fun editMeta(block: RandomBoxItemMeta.() -> Unit) {
        val newRandomBoxItemMeta = randomBoxItemMeta.apply(block)
        setRandomBoxItemMeta(newRandomBoxItemMeta)
    }

    fun toItemStack(): ItemStack {
        return itemStack.clone()
    }

    fun toOriginalItemStack(): ItemStack {
        return toItemStack().nms {
            tag {
                remove(RANDOM_BOX_ITEM_META_KEY)
            }
        }
    }

    companion object {
        private val gson = Gson()

        private const val RANDOM_BOX_ITEM_META_KEY = "HQRandomBoxItemMeta"

        fun of(itemStack: ItemStack): RandomBoxItemStack {
            return RandomBoxItemStack(
                itemStack.nms {
                    tag {
                        val randomBoxItemMeta = RandomBoxItemMeta.init()
                        setRandomBoxItemMeta(RANDOM_BOX_ITEM_META_KEY, randomBoxItemMeta)
                    }
                }
            )
        }

        private fun NmsNBTTagCompoundWrapper.setRandomBoxItemMeta(key: String, randomBoxItemMeta: RandomBoxItemMeta) {
            val json = gson.toJson(randomBoxItemMeta)
            setString(key, json)
        }

        private fun NmsNBTTagCompoundWrapper.getRandomBoxItemMeta(key: String): RandomBoxItemMeta {
            val json = getString(key)
            return gson.fromJson(json, RandomBoxItemMeta::class.java)
        }
    }
}