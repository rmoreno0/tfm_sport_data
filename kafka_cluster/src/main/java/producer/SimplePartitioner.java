package producer;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import java.util.Map;


// Clase SimplePartitioner: implementa la interfaz Partitioner
public class SimplePartitioner implements Partitioner {

    // Método partition: realiza un hash de la clave para determinar a que partición se envía el mensaje
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        return Math.abs(key.hashCode() % cluster.partitionCountForTopic(topic));
    }

    @Override
    public void configure(Map<String, ?> conf) {}

    @Override
    public void close() {}
}