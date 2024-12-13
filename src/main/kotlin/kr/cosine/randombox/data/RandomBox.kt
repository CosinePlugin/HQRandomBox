package kr.cosine.randombox.data

import kr.cosine.randombox.registry.RandomBoxRegistry.Companion.isChanged
import org.bukkit.inventory.ItemStack
import kotlin.math.ln
import kotlin.random.Random

class RandomBox(
    val name: String
) {
    private var itemStack: ItemStackWrapper? = null

    private val randomBoxItemStacks = RandomBoxItemStackList()

    fun isSimilar(itemStack: ItemStack): Boolean {
        return this.itemStack?.isSimilar(itemStack) == true
    }

    fun findItemStack(): ItemStack? {
        return itemStack?.clone()
    }

    fun setItemStack(itemStack: ItemStack) {
        this.itemStack = ItemStackWrapper(itemStack)
        isChanged = true
    }

    fun addRandomBoxItemStack(randomBoxItemStack: RandomBoxItemStack) {
        randomBoxItemStacks.add(randomBoxItemStack)
        isChanged = true
    }

    fun removeRandomBoxItemStack(randomBoxItemStack: RandomBoxItemStack) {
        randomBoxItemStacks.remove(randomBoxItemStack)
        isChanged = true
    }

    fun getRandomBoxItemStackByChance(): RandomBoxItemStack {
        return randomBoxItemStacks.minBy {
            -ln(random.nextDouble()) / it.randomBoxItemMeta.chance
        }
    }

    fun getRandomBoxItemStacks(): List<RandomBoxItemStack> {
        return randomBoxItemStacks
    }

    private companion object {
        val random: Random = Random
    }
}