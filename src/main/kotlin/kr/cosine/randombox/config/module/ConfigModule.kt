package kr.cosine.randombox.config.module

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kr.cosine.randombox.config.RandomBoxConfig
import kr.cosine.randombox.config.SettingConfig
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.framework.bukkit.core.component.module.Teardown
import kr.hqservice.framework.bukkit.core.coroutine.bukkitDelay
import kr.hqservice.framework.bukkit.core.coroutine.element.TeardownOptionCoroutineContextElement

@Module
class ConfigModule(
    private val plugin: HQBukkitPlugin,
    private val settingConfig: SettingConfig,
    private val randomBoxConfig: RandomBoxConfig
) {
    @Setup
    fun setup() {
        settingConfig.load()
        randomBoxConfig.load()

        plugin.launch(Dispatchers.IO + TeardownOptionCoroutineContextElement(true)) {
            while (isActive) {
                bukkitDelay(1200) // 1분
                randomBoxConfig.save()
            }
        }
    }

    @Teardown
    fun teardown() {
        randomBoxConfig.save()
    }
}