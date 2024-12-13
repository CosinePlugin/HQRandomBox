package kr.cosine.randombox.registry

import kr.cosine.randombox.data.RandomBox
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.inventory.ItemStack

@Bean
class RandomBoxRegistry {
    private val randomBoxMap = mutableMapOf<String, RandomBox>()

    fun restore(randomBoxRegistry: RandomBoxRegistry) {
        randomBoxMap.clear()
        randomBoxMap.putAll(randomBoxRegistry.randomBoxMap)
    }

    fun isRandomBox(name: String): Boolean {
        return randomBoxMap.containsKey(name)
    }

    fun findRandomBox(name: String): RandomBox? {
        return randomBoxMap[name]
    }

    fun setRandomBox(name: String, randomBox: RandomBox) {
        randomBoxMap[name] = randomBox
        isChanged = true
    }

    fun removeRandomBox(name: String) {
        randomBoxMap.remove(name)
        isChanged = true
    }

    fun findRandomBoxByItemStack(itemStack: ItemStack): RandomBox? {
        return randomBoxMap.entries.find { it.value.isSimilar(itemStack) }?.value
    }

    fun getNames(): List<String> {
        return randomBoxMap.keys.toList()
    }

    companion object {
        var isChanged = false
    }
}