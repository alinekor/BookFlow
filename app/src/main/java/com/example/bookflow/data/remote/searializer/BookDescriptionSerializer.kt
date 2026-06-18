package com.example.bookflow.data.remote.searializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class DescriptionDeserializer : JsonDeserializer<String?> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): String? {
        return when {
            json == null || json.isJsonNull -> null

            json.isJsonPrimitive ->
                json.asJsonPrimitive.takeIf { it.isString }?.asString

            json.isJsonObject ->
                json.asJsonObject.get("value")?.takeIf { it.isJsonPrimitive }?.asString

            else -> null
        }
    }
}