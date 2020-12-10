package producer.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;


// Clase Json: implementa la interfaz Runnable.
// Permite paralelizar productores Kafka de tipo Json.
public class Json implements Runnable{

    // Atributos
    private final String kafka_host;
    private final String topic;
    private final String fileRead;
    private final String key_producer;

    // Constructor
    public Json(String kafka_host, String topic, String fileRead, String key_producer) {
        this.kafka_host = kafka_host;
        this.topic = topic;
        this.fileRead = fileRead;
        this.key_producer = key_producer;
    }

    // Función run: configura e inicia un productor Kafka de tipo Json.
    // Lee de fichero y escribe en el topic establecido.
    @Override
    public void run() {
        // Inicialización de las propiedades
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafka_host);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "serializers.JsonSerializer");
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, "producer.SimplePartitioner");

        // Instanciación del productor con sus propiedades
        Producer<String, JsonNode> producer = new KafkaProducer<>(props);

        // Instanciación de la clase ObjectMapper para generar un objeto mapper
        ObjectMapper mapper = new ObjectMapper();

        // Definición de la variable br de tipo BufferedReader
        BufferedReader br = null;

        try {
            // Generación del objeto BufferedReader y lectura de la primera linea del fichero
            br = new BufferedReader(new FileReader(this.fileRead));
            String textLine = br.readLine();

            // Mientras que no se llegue al final de fichero
            while(textLine != null){

                // Se genera una variable de tipo JsonNode y se envía en intervalos de 1 segundo mensajes al topic de Kafka
                JsonNode actualObj = mapper.readTree(textLine);
                System.out.println("Sending message with: " + this.key_producer);
                producer.send(new ProducerRecord<>(this.topic, this.key_producer, actualObj));
                Thread.sleep(1000);
                textLine = br.readLine();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: Fichero no encontrado.");
            System.out.println(e.getMessage());
        }
        catch(Exception e) {
            System.out.println("Error: Fallo en la lectura del fichero.");
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if(br != null)
                    br.close();
            }
            catch (Exception e) {
                System.out.println("Error: Fallo al cerrar el fichero.");
                System.out.println(e.getMessage());
            }
        }

        producer.flush();
        producer.close();
    }
}