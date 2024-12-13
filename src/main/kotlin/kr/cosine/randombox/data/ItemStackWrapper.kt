package kr.cosine.randombox.data

import kr.hqservice.framework.bukkit.core.extension.toByteArray
import kr.hqservice.framework.bukkit.core.extension.toItemStack
import org.bukkit.inventory.ItemStack
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

data class ItemStackWrapper(
    private val itemStack: ItemStack
) {
    fun isSimilar(itemStack: ItemStack): Boolean {
        return this.itemStack.isSimilar(itemStack)
    }

    fun clone(): ItemStack {
        return itemStack.clone()
    }

    override fun toString(): String {
        return itemStack.toByteArray().run(Base64Coder::encodeLines)
    }

    companion object {
        fun of(compressed: String): ItemStackWrapper {
            return compressed
                .run(Base64Coder::decodeLines)
                .toItemStack()
                .run(::ItemStackWrapper)
        }
    }
}