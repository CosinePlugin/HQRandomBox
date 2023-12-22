package kr.cosine.randombox

import kr.cosine.randombox.config.RandomBoxConfig
import kr.cosine.randombox.config.SettingConfig
import kr.cosine.randombox.scheduler.RandomBoxSaveScheduler
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.Component
import kr.hqservice.framework.global.core.component.HQModule
import kr.hqservice.framework.yaml.config.HQYamlConfiguration

@Component
class HQRandomBoxModule(
    private val plugin: HQBukkitPlugin,
    private val settingConfig: SettingConfig,
    private val randomBoxConfig: RandomBoxConfig
) : HQModule {

    override fun onEnable() {
        settingConfig.load()
        randomBoxConfig.load()
        val autoSavePeriod = settingConfig.autoSavePeriod
        RandomBoxSaveScheduler(randomBoxConfig).runTaskTimerAsynchronously(plugin, autoSavePeriod, autoSavePeriod)
    }

    override fun onDisable() {
        randomBoxConfig.save()
    }
}