package kr.cosine.randombox.command

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.cosine.randombox.command.argument.RandomBoxArgument
import kr.cosine.randombox.service.RandomBoxService
import kr.hqservice.framework.command.ArgumentLabel
import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@Command(label = "랜덤박스관리", isOp = true)
class RandomBoxCommand(
    private val randomBoxService: RandomBoxService
) {

    @CommandExecutor("생성", "랜덤 박스를 생성합니다.", priority = 1)
    fun createRandomBox(
        player: Player,
        @ArgumentLabel("이름") key: String
    ) {
        if (randomBoxService.createRandomBox(key)) {
            player.sendMessage("§a${key} 랜덤 박스를 생성하였습니다.")
        } else {
            player.sendMessage("§c이미 존재하는 랜덤 박스입니다.")
        }
    }

    @CommandExecutor("제거", "랜덤 박스를 제거합니다.", priority = 2)
    fun removeRandomBox(
        player: Player,
        @ArgumentLabel("이름") randomBoxArgument: RandomBoxArgument
    ) {
        val key = randomBoxArgument.randomBox.key
        randomBoxService.removeRandomBox(key)
        player.sendMessage("§a${key} 랜덤 박스가 제거되었습니다.")
    }

    @CommandExecutor("적용", "손에 든 아이템을 랜덤 박스로 만듭니다.", priority = 3)
    fun applyRandomBox(
        player: Player,
        @ArgumentLabel("이름") randomBoxArgument: RandomBoxArgument
    ) {
        val itemStack = player.inventory.itemInMainHand
        val key = randomBoxArgument.randomBox.key
        if (randomBoxService.applyRandomBox(itemStack, key)) {
            player.sendMessage("§a손에 든 아이템을 $key 랜덤 박스로 설정되었습니다.")
        } else {
            player.sendMessage("§c손에 아이템을 들어주세요.")
        }
    }

    @CommandExecutor("아이템설정", "랜덤 박스의 아이템을 설정합니다.", priority = 4)
    fun openRandomBoxItemSettingView(
        player: Player,
        @ArgumentLabel("이름") randomBoxArgument: RandomBoxArgument
    ) {
        randomBoxService.openRandomBoxItemSettingView(player, randomBoxArgument.randomBox)
    }

    @CommandExecutor("확률설정", "랜덤 박스 아이템의 확률을 설정합니다.", priority = 5)
    fun openRandomBoxChanceSettingView(
        player: Player,
        @ArgumentLabel("이름") randomBoxArgument: RandomBoxArgument
    ) {
        randomBoxService.openRandomBoxChanceSettingView(player, randomBoxArgument.randomBox)
    }

    @CommandExecutor("저장", "랜덤 박스의 변경된 사항을 수동으로 저장합니다.", priority = 6)
    suspend fun save(sender: CommandSender) {
        randomBoxService.save {
            sender.sendMessage("§a랜덤 박스의 변경된 사항이 저장되었습니다.")
        }
    }

    @CommandExecutor("리로드", "config.yml을 리로드합니다.", priority = 6)
    fun reload(sender: CommandSender) {
        randomBoxService.reload()
        sender.sendMessage("§aconfig.yml을 리로드하였습니다.")
    }
}