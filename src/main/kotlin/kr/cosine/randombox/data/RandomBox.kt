package kr.cosine.randombox.data

import kotlin.math.ln
import kotlin.random.Random

class RandomBox(
    val name: String
) {
    private val _randomBoxItemStacks = RandomBoxItemStackList()
    val randomBoxItemStacks: List<RandomBoxItemStack> get() = _randomBoxItemStacks

    fun addRandomBoxItemStack(randomBoxItemStack: RandomBoxItemStack) {
        _randomBoxItemStacks.add(randomBoxItemStack)
    }

    fun removeRandomBoxItemStack(randomBoxItemStack: RandomBoxItemStack) {
        _randomBoxItemStacks.remove(randomBoxItemStack)
    }

    fun getRandomBoxItemStackByChance(): RandomBoxItemStack {
        return _randomBoxItemStacks.minBy {
            -ln(random.nextDouble()) / it.randomBoxItemMeta.chance
        }
    }

    private companion object {
        val random: Random = Random
    }
}