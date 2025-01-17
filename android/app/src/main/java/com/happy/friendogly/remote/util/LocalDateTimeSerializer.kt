package com.happy.friendogly.remote.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private const val SERIAL_NAME = "LocalDateTime"

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(SERIAL_NAME, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): LocalDateTime {
        val pattern = "yyyy-MM-dd HH:mm:ss.SSS"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return java.time.LocalDateTime.parse(decoder.decodeString(), formatter)
            .toKotlinLocalDateTime()
    }

    override fun serialize(
        encoder: Encoder,
        value: LocalDateTime,
    ) {
        encoder.encodeString(value.toString())
    }
}

object JavaLocalDateTimeSerializer : KSerializer<java.time.LocalDateTime> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: java.time.LocalDateTime,
    ) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): java.time.LocalDateTime {
        return java.time.LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}
