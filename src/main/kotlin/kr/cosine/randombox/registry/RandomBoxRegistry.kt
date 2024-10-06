package kr.cosine.randombox.registry

import kr.cosine.randombox.data.RandomBox
import kr.hqservice.framework.global.core.component.Bean

@Bean
class RandomBoxRegistry {
    private val _randomBoxMap = mutableMapOf<String, RandomBox>()
    val randomBoxMap: Map<String, RandomBox> get() = _randomBoxMap

    var isChanged = false

    fun restore(randomBoxRegistry: RandomBoxRegistry) {
        _randomBoxMap.clear()
        _randomBoxMap.putAll(randomBoxRegistry.randomBoxMap)
    }

    fun isRandomBox(name: String): Boolean {
        return _randomBoxMap.containsKey(name)
    }

    fun findRandomBox(name: String): RandomBox? {
        return _randomBoxMap[name]
    }

    fun setRandomBox(name: String, randomBox: RandomBox) {
        _randomBoxMap[name] = randomBox
        isChanged = true
    }

    fun removeRandomBox(name: String) {
        _randomBoxMap.remove(name)
        isChanged = true
    }

    fun getNames(): List<String> {
        return _randomBoxMap.keys.toList()
    }
}