package kr.cosine.randombox.data

class RandomBoxItemStackList(
    private val randomBoxItemStacks: MutableList<RandomBoxItemStack> = mutableListOf()
) : MutableList<RandomBoxItemStack> by randomBoxItemStacks