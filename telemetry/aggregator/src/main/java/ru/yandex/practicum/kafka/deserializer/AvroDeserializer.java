package ru.yandex.practicum.kafka.deserializer;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;
import ru.yandex.practicum.exceptions.DeserializationException;

import java.io.ByteArrayInputStream;

public class AvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {
    private final Schema schema;
    private final DecoderFactory decoderFactory;

    public AvroDeserializer(Schema schema) {
        this(DecoderFactory.get(), schema);
    }

    public AvroDeserializer(DecoderFactory decoderFactory, Schema schema) {
        this.decoderFactory = decoderFactory;
        this.schema = schema;
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data != null) {
                DatumReader<T> reader = new SpecificDatumReader<>(schema);
                return reader.read(null, decoderFactory.binaryDecoder(new ByteArrayInputStream(data), null));
            }
            return null;
        } catch (Exception e) {
            throw new DeserializationException("Ошибка десериализации данных из топика [" + topic + "]");
        }
    }
}
