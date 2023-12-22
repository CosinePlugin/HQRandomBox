package kr.cosine.randombox.config

import kr.cosine.randombox.data.PickEffect
import kr.hqservice.framework.bukkit.core.extension.colorize
import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.framework.yaml.config.HQYamlConfiguration

@Bean
class SettingConfig(
    private val config: HQYamlConfiguration
) {

    val autoSavePeriod get() = config.getLong("auto-save-period", 6000)

    var inventoryFullMessage = "§c인벤토리에 공간이 부족합니다."
        private set

    var pickEffect = PickEffect()
        private set

    fun load() {
        config.getSection("message")?.apply {
            inventoryFullMessage = getString("inventory-full").colorize()
        }
        config.getSection("pick-effect")?.apply {
            val message = getString("message").colorize()
            val sound = getString("sound.name")
            val volume = getDouble("sound.volume").toFloat()
            val pitch = getDouble("sound.pitch").toFloat()
            pickEffect = PickEffect(message, sound, volume, pitch)
        }
    }

    fun reload() {
        config.reload()
        load()
    }
}