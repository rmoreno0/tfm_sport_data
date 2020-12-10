package main;

import java.util.concurrent.ExecutionException;
import producer.json.Json;


public class Main{

    //Atributos
    public static String KAFKA_HOST_1 = "localhost:19092";
    public static String KAFKA_HOST_2 = "localhost:29092";
    public static String KAFKA_HOST_3 = "localhost:39092";
    public static String TOPICSPORT = "sport";
    public static String TOPICHEALTH = "health";


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        String[] idsFiles = {"TC01", "DC02", "EM03", "SR04", "RV05", "TK08", "LM10", "CC14", "KB09", "EH07", "VJ20"};
        Thread[] producersHealth = new Thread[idsFiles.length];
        Thread[] producersSport = new Thread[idsFiles.length];

        for (int i = 0; i < idsFiles.length; i++) {
            String fileHealth = "src/main/resources/health/" + idsFiles[i] + "_health.txt";
            String keyProducerHealth = idsFiles[i] + "_HEALTH";
            producersHealth[i] = new Thread(new Json(KAFKA_HOST_1, TOPICHEALTH, fileHealth, keyProducerHealth));
            producersHealth[i].start();

            String fileSport = "src/main/resources/sport/" + idsFiles[i] + "_sport.txt";
            String keyProducerSport = idsFiles[i] + "_SPORT";
            producersSport[i] = new Thread(new Json(KAFKA_HOST_2, TOPICSPORT, fileSport, keyProducerSport));
            producersSport[i].start();
        }

        try {
            for (Thread h : producersHealth)
                h.join();

            for (Thread s : producersSport)
                s.join();
        } catch (InterruptedException e) {
            System.out.println("Error: Hilo interrumpido.");
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Ejecución finalizada.");
        }
    }
}
