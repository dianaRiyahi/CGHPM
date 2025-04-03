package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class dataReader {
    static List<String[]> getConservationStatuses() {
        String filePath = "src/main/resources/wildlife_data.csv";
        List<String[]> conservationStatuses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                String animalName = values[1];
                String conservationStatus = values[4];

                if(animalName != "") {
                    conservationStatuses.add(new String[]{animalName, conservationStatus});
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return conservationStatuses;
    }
}

class printStatuses {
    public static void main(String[] args) {
        System.out.println(dataReader.getConservationStatuses());
    }
}
