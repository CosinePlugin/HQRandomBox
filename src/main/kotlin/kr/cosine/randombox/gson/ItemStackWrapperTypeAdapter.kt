package kr.cosine.randombox.gson

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import kr.cosine.randombox.data.ItemStackWrapper

object ItemStackWrapperTypeAdapter : TypeAdapter<ItemStackWrapper>() {
    override fun read(jsonReader: JsonReader): ItemStackWrapper {
        return jsonReader.nextString().run(ItemStackWrapper::of)
    }

    override fun write(jsonWriter: JsonWriter, itemStackWrapper: ItemStackWrapper) {
        val compressed = itemStackWrapper.toString()
        jsonWriter.value(compressed)
    }
}