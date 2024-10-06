package kr.cosine.randombox.config

import kr.cosine.randombox.data.GiftBoxSetting
import kr.cosine.randombox.data.Sound
import kr.cosine.randombox.enums.Message
import kr.cosine.randombox.registry.SettingRegistry
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration
import kr.hqservice.framework.yaml.config.HQYamlConfigurationSection

@Bean
class SettingConfig(
    private val config: HQYamlConfiguration,
    private val settingRegistry: SettingRegistry
) {
    fun load() {
        loadGiftBox()
        loadMessage()
        loadSound()
    }

    private fun loadGiftBox() {
        config.getSection("gift-box")?.apply {
            val giftBoxSetting = GiftBoxSetting(
                getBoolean("enabled"),
                getString("display-name").colorize(),
                getColorizedStringList("lore")
            )
            settingRegistry.setGiftBoxSetting(giftBoxSetting)
        }
    }

    private fun loadMessage() {
        config.getSection("message")?.apply {
            getKeys().forEach { messageText ->
                val message = Message.of(messageText) ?: return@forEach
                message.setMessages(getColorizedStringList(messageText))
            }
        }
    }

    private fun HQYamlConfigurationSection.getColorizedStringList(path: String): List<String> {
        return getStringList(path).map(String::colorize)
    }

    private fun loadSound() {
        config.getSection("sound")?.apply {
            val pickSound = Sound(
                getBoolean("enabled"),
                getString("name"),
                getDouble("volume").toFloat(),
                getDouble("pitch").toFloat()
            )
            settingRegistry.setPickSound(pickSound)
        }
    }

    fun reload() {
        settingRegistry.clear()
        config.reload()
        load()
    }
}