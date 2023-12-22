package kr.cosine.randombox.scheduler

import kr.cosine.randombox.config.RandomBoxConfig
import org.bukkit.scheduler.BukkitRunnable

class RandomBoxSaveScheduler(
    private val randomBoxConfig: RandomBoxConfig
) : BukkitRunnable() {

    override fun run() {
        randomBoxConfig.save()
    }
}