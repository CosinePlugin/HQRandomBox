package kr.cosine.randombox

import org.junit.jupiter.api.Test

class Test {

    class ItemStack(
        val display: String,
        val lore: List<String>
    ) {}

    @Test
    fun instance_test() {
        val list1 = listOf(ItemStack("", listOf("1")))
        val list2 = listOf(ItemStack("", listOf("1")))
        println("같은지: ${list1 == list2}")
    }
}