package com.cheesecake.common.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.descriptors.nullable
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
        throw NotImplementedError("Deserialization is not supported")
    }
}