package kr.cosine.randombox.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.cosine.randombox.config.RandomBoxConfig
import kr.cosine.randombox.config.SettingConfig
import kr.cosine.randombox.data.RandomBox
import kr.cosine.randombox.service.RandomBoxViewService
import kr.cosine.randombox.view.RandomBoxService
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "랜덤박스관리", isOp = true)
class RandomBoxAdminCommand(
    private val settingConfig: SettingConfig,
    private val randomBoxConfig: RandomBoxConfig,
    private val randomBoxService: RandomBoxService,
    private val randomBoxViewService: RandomBoxViewService
) {
    @CommandExecutor("생성", "랜덤박스를 생성합니다.", priority = 1)
    fun createRandomBox(
        sender: CommandSender,
        @ArgumentLabel("이름") name: String
    ) {
        if (randomBoxService.createRandomBox(name)) {
            sender.sendMessage("§a${name} 랜덤박스를 생성하였습니다.")
        } else {
            sender.sendMessage("§c이미 존재하는 랜덤박스입니다.")
        }
    }

    @CommandExecutor("제거", "랜덤박스를 제거합니다.", priority = 2)
    fun deleteRandomBox(
        sender: CommandSender,
        @ArgumentLabel("이름") randomBox: RandomBox
    ) {
        val name = randomBox.name
        randomBoxService.deleteRandomBox(name)
        sender.sendMessage("§a${name} 랜덤박스를 제거하였습니다.")
    }

    @CommandExecutor("설정", "랜덤박스 설정 화면을 오픈합니다.", priority = 3)
    fun openRandonBoxSettingView(
        player: Player,
        @ArgumentLabel("이름") randomBox: RandomBox
    ) {
        randomBoxViewService.openRandomBoxSettingView(player, randomBox)
    }

    @CommandExecutor("적용", "손에 든 아이템을 랜덤박스로 만듭니다.", priority = 4)
    fun makeRandomBox(
        player: Player,
        @ArgumentLabel("이름") randomBox: RandomBox
    ) {
        val itemStack = player.inventory.itemInMainHand
        if (itemStack.type.isAir) {
            player.sendMessage("§c손에 아이템을 들어주세요.")
            return
        }
        val name = randomBox.name
        randomBoxService.makeRandomBox(itemStack, name)
        player.sendMessage("§a손에 든 아이템을 $name 랜덤박스로 만들었습니다.")
    }

    @CommandExecutor("저장", "변경된 사항을 수동으로 저장합니다.", priority = 5)
    suspend fun save(sender: CommandSender) {
        withContext(Dispatchers.IO) {
            randomBoxConfig.save()
            sender.sendMessage("§a변경된 사항을 수동으로 저장하였습니다.")
        }
    }

    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 6)
    fun reload(sender: CommandSender) {
        settingConfig.reload()
        sender.sendMessage("§aconfig.yml을 리로드하였습니다.")
    }
}