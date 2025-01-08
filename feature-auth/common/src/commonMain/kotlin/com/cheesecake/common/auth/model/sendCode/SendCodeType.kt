package com.cheesecake.common.auth.model.sendCode

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = RequestCodeTypeSerializer::class)
enum class SendCodeType(val type: String) {
    REGISTRATION("registration"),
    RESET_PASSWORD("reset_password"),
}

private object RequestCodeTypeSerializer : KSerializer<SendCodeType> {
    override val descriptor: SerialDescriptor
        = PrimitiveSerialDescriptor("RequestCodeType", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: SendCodeType) {
        encoder.encodeString(value.type)
    }

    override fun deserialize(decoder: Decoder): SendCodeType {
        val type = decoder.decodeString()
        return SendCodeType.entries.first { it.type == type }
    }
}