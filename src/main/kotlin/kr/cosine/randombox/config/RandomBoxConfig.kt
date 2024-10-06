package kr.cosine.randombox.config

import com.google.gson.GsonBuilder
import kr.cosine.randombox.data.RandomBoxItemStackList
import kr.cosine.randombox.gson.RandomBoxItemStackListTypeAdapter
import kr.cosine.randombox.registry.RandomBoxRegistry
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.plugin.Plugin
import java.io.File

@Bean
class RandomBoxConfig(
    plugin: Plugin,
    private val randomBoxRegistry: RandomBoxRegistry
) {
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .registerTypeAdapter(
            RandomBoxItemStackList::class.java,
            RandomBoxItemStackListTypeAdapter()
        ).create()

    private val file = File(plugin.dataFolder, "random-box.json")

    fun load() {
        if (!file.exists()) return
        val randomBoxRegistry = gson.fromJson(
            file.bufferedReader(),
            RandomBoxRegistry::class.java
        )
        this.randomBoxRegistry.restore(randomBoxRegistry)
    }

    fun save() {
        if (randomBoxRegistry.isChanged) {
            randomBoxRegistry.isChanged = false
            val json = gson.toJson(randomBoxRegistry)
            file.bufferedWriter().use {
                it.appendLine(json)
                it.flush()
            }
        }
    }
}