package kr.cosine.randombox.data

import kotlin.math.ln
import kotlin.random.Random

class RandomBox(
    val key: String
) {

    private companion object {
        val random by lazy { Random }
    }

    private var chanceItems = mutableListOf<ChanceItem>()

    constructor(
        key: String,
        chanceItems: MutableList<ChanceItem>
    ) : this(key) {
        this.chanceItems = chanceItems
    }

    var isChanged = false

    fun getRandomChanceItem(): ChanceItem {
        return chanceItems.minByOrNull {
            -ln(random.nextDouble()) / it.getChance()
        } ?: throw IllegalArgumentException()
    }

    fun getChanceItems(): List<ChanceItem> = chanceItems

    fun setChanceItems(chanceItems: List<ChanceItem>) {
        this.chanceItems = chanceItems.toMutableList()
        isChanged = true
    }
}