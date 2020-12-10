package serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;


// Clase JsonSerializer: implementa la interfaz Serializer
public class JsonSerializer implements Serializer<JsonNode> {

    // Atributos de la clase
    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);
    ObjectMapper mapper = new ObjectMapper();

    // Método serialize: dado un JsonNode lo convierte a bytes
    @Override
    public byte[] serialize(String topic, JsonNode data) {
        byte[] raw = null;
        try {
            raw = mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return raw;
    }

    @Override
    public void configure(Map configs, boolean isKey) {}

    @Override
    public void close() {}
}