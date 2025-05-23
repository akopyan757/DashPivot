package com.cheesecake.common.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.nullable
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class ApiResponse<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
)

class ApiResponseSerializer<T>(
    private val dataSerializer: KSerializer<T>
) : KSerializer<ApiResponse<T>> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ApiResponse") {
        element<Int>("code")
        element<String>("message")
        element("data", dataSerializer.descriptor.nullable)
    }

    override fun serialize(encoder: Encoder, value: ApiResponse<T>) {
        val composite = encoder.beginStructure(descriptor)
        composite.encodeIntElement(descriptor, 0, value.code)
        composite.encodeStringElement(descriptor, 1, value.message)
        composite.encodeNullableSerializableElement(descriptor, 2, dataSerializer, value.data)
        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): ApiResponse<T> {
        var code: Int? = null
        var message: String? = null
        var data: T? = null

        val composite = decoder.beginStructure(descriptor)
        loop@ while (true) {
            when (val index = composite.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                0 -> code = composite.decodeIntElement(descriptor, 0)
                1 -> message = composite.decodeStringElement(descriptor, 1)
                2 -> data =
                    composite.decodeNullableSerializableElement(descriptor, 2, dataSerializer)
                else -> throw SerializationException("Unexpected index $index")
            }
        }
        composite.endStructure(descriptor)

        if (code != null && message != null) {
            return ApiResponse(code, message, data)
        } else {
            throw SerializationException("Missing required fields: code or message")
        }

    }
}