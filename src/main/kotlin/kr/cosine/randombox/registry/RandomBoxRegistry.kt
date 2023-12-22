package kr.cosine.randombox.registry

import kr.cosine.randombox.data.RandomBox
import kr.hqservice.framework.global.core.component.Bean

@Bean
class RandomBoxRegistry {

    private val randomBoxMap = mutableMapOf<String, RandomBox>()

    var isRemoved = false

    fun isRandomBox(key: String): Boolean = randomBoxMap.containsKey(key)

    fun findRandomBox(key: String): RandomBox? = randomBoxMap[key]

    fun setRandomBox(key: String, randomBox: RandomBox) {
        randomBoxMap[key] = randomBox
    }

    fun removeRandomBox(key: String) {
        randomBoxMap.remove(key)
        isRemoved = true
    }

    fun getKeys(): List<String> = randomBoxMap.keys.toList()

    fun getRandomBoxMap(): Map<String, RandomBox> = randomBoxMap
}