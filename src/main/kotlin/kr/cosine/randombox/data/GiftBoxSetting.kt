package kr.cosine.randombox.data

data class GiftBoxSetting(
    val isEnabled: Boolean = false,
    val displayName: String = "",
    val lore: List<String> = emptyList()
)