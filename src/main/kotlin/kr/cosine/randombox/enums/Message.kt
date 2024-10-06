package kr.cosine.randombox.enums

enum class Message(
    private var messages: List<String>
) {
    LACK_INVENTORY(listOf("§c인벤토리에 공간이 부족합니다.")),
    PLAYER_PICK(listOf("§a%item%을(를) 뽑았습니다!")),
    BROADCAST_PICK(
        listOf(
            "",
            "§a%player%님이 %item%을(를) 뽑았습니다!",
            ""
        )
    );

    fun getMessages(replce: (String) -> String = { it }): List<String> {
        return messages.map(replce)
    }

    fun setMessages(messages: List<String>) {
        this.messages = messages
    }

    companion object {
        fun of(text: String): Message? {
            return runCatching { valueOf(text.uppercase().replace("-", "_")) }.getOrNull()
        }
    }
}