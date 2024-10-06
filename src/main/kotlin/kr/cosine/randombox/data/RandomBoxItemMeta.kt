package kr.cosine.randombox.data

data class RandomBoxItemMeta(
    var chance: Double,
    var broadcast: Boolean
) {
    companion object {
        fun init(): RandomBoxItemMeta {
            return RandomBoxItemMeta(100.0, false)
        }
    }
}