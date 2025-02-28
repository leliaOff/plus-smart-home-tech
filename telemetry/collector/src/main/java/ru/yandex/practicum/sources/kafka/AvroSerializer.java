package ru.yandex.practicum.sources.kafka;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AvroSerializer<T extends GenericRecord> implements Serializer<T> {
    @Override
    public byte[] serialize(String topic, T data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] result = null;
            final EncoderFactory encoderFactory = EncoderFactory.get();
            BinaryEncoder encoder;
            encoder = encoderFactory.binaryEncoder(out, null);
            if (data != null) {
                DatumWriter<T> writer = new SpecificDatumWriter<>(data.getSchema());
                writer.write(data, encoder);
                encoder.flush();
                result = out.toByteArray();
            }
            return result;
        } catch (IOException ex) {
            throw new SerializationException("Во время сериализации произошла ошибка", ex);
        }

    }
}
