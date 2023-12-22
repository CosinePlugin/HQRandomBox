package kr.cosine.randombox.config

import kr.cosine.randombox.data.ChanceItem
import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.registry.RandomBoxRegistry
import kr.hqservice.framework.bukkit.core.extension.toByteArray
import kr.hqservice.framework.bukkit.core.extension.toItemArray
import kr.hqservice.framework.global.core.component.Bean
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.io.File

@Bean
class RandomBoxConfig(
    plugin: Plugin,
    private val randomBoxRegistry: RandomBoxRegistry
) {

    private companion object {
        const val RANDOM_BOX_SECTION_KEY = "random-box"
    }

    private val file = File(plugin.dataFolder, "$RANDOM_BOX_SECTION_KEY.yml")
    private val config = YamlConfiguration.loadConfiguration(file)

    fun load() {
        if (!file.exists()) return
        config.getConfigurationSection(RANDOM_BOX_SECTION_KEY)?.apply {
            getKeys(false).forEach { key ->
                val compressed = getString(key)
                val byteArray = Base64Coder.decodeLines(compressed)
                val itemStacks = byteArray.toItemArray()
                val chanceItems = itemStacks.map { ChanceItem(it) }.toMutableList()
                val randomBox = RandomBox(key, chanceItems)
                randomBoxRegistry.setRandomBox(key, randomBox)
            }
        }
    }

    fun save() {
        val randomBoxMap = randomBoxRegistry.getRandomBoxMap().toMap()
        val filteredRandomBoxMap = randomBoxMap.filter { it.value.isChanged }
        if (filteredRandomBoxMap.isNotEmpty() || randomBoxRegistry.isRemoved) {
            config.set(RANDOM_BOX_SECTION_KEY, null)
            randomBoxMap.forEach { (key, randomBox) ->
                val sectionKey = "$RANDOM_BOX_SECTION_KEY.$key"
                val itemStacks = randomBox.getChanceItems().map { it.getItemStack() } as List<ItemStack?>
                val byteArray = itemStacks.toTypedArray().toByteArray()
                val compressed = Base64Coder.encodeLines(byteArray)
                config.set(sectionKey, compressed)
                randomBox.isChanged = false
            }
            config.save(file)
            randomBoxRegistry.isRemoved = false
        }
    }
}