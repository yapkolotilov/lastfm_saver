package me.kolotilov.lastfm_saver.repositories.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class ArrayOrInstanceDeserializer<T> : JsonDeserializer<List<T>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): List<T> {
        val result = mutableListOf<T>()
        if (json.isJsonArray) {
            for (el in json.asJsonArray) {
                result.add(context.deserialize(json, typeOfT))
            }
        } else if (json.isJsonObject) {
            result.add(context.deserialize(json, typeOfT))
        }
        return result
    }
}