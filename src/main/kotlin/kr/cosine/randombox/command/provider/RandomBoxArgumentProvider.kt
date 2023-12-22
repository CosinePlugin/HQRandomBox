package kr.cosine.randombox.command.provider

import kr.cosine.randombox.command.argument.RandomBoxArgument
import kr.cosine.randombox.registry.RandomBoxRegistry
import kr.hqservice.framework.command.CommandArgumentProvider
import kr.hqservice.framework.command.CommandContext
import kr.hqservice.framework.command.argument.exception.ArgumentFeedback
import kr.hqservice.framework.global.core.component.Component
import org.bukkit.Location

@Component
class RandomBoxArgumentProvider(
    private val randomBoxRegistry: RandomBoxRegistry
) : CommandArgumentProvider<RandomBoxArgument> {

    override suspend fun cast(context: CommandContext, argument: String?): RandomBoxArgument {
        if (argument == null) throw ArgumentFeedback.Message("§c랜덤 박스의 이름을 입력해주세요.")
        val randomBox = randomBoxRegistry.findRandomBox(argument) ?: throw ArgumentFeedback.Message("§c존재하지 않는 랜덤 박스입니다.")
        return RandomBoxArgument(randomBox)
    }

    override suspend fun getTabComplete(context: CommandContext, location: Location?): List<String> {
        return randomBoxRegistry.getKeys()
    }
}