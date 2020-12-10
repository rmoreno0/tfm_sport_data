package serializers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Map;


// Clase JsonDeserializer: implementa la interfaz Deserializer
public class JsonDeserializer implements Deserializer<JsonNode> {

    // Atributos
    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);
    ObjectMapper mapper = new ObjectMapper();


    // Método deserialize: dado un array de bytes lo convierte a JsonNode
    @Override
    public JsonNode deserialize(String topic, byte[] data) {
        JsonNode json = null;
        if(data != null) {
            try {
                json = mapper.readTree(data);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return json;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public void close() {}
}