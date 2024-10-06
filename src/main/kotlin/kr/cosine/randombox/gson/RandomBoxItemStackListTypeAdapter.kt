package kr.cosine.randombox.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kr.cosine.randombox.data.RandomBoxItemStack
import kr.cosine.randombox.data.RandomBoxItemStackList
import kr.hqservice.framework.bukkit.core.extension.toByteArray
import kr.hqservice.framework.bukkit.core.extension.toItemArray
import org.bukkit.inventory.ItemStack
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

class RandomBoxItemStackListTypeAdapter : TypeAdapter<RandomBoxItemStackList>() {
    override fun write(jsonWriter: JsonWriter, randomBoxItemStacks: RandomBoxItemStackList) {
        val compressedRandomBoxItemStacks = randomBoxItemStacks
            .map(RandomBoxItemStack::toItemStack)
            .toTypedArray<ItemStack?>()
            .toByteArray()
            .run(Base64Coder::encodeLines)
        jsonWriter.value(compressedRandomBoxItemStacks)
    }

    override fun read(jsonReader: JsonReader): RandomBoxItemStackList {
        return jsonReader.nextString()
            .run(Base64Coder::decodeLines)
            .toItemArray()
            .map(::RandomBoxItemStack)
            .toMutableList()
            .run(::RandomBoxItemStackList)
    }
}