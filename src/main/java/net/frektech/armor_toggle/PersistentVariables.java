package net.frektech.armor_toggle;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class PersistentVariables {
    public static Map<String, Boolean> loadData(String path) {
        Map<String, Boolean> data = new HashMap<String, Boolean>();

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("config/" + path));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.contains(":")) {
                    continue;
                }

                String[] splittedLine = line.split(":");

                if (!data.containsKey(splittedLine[0])) {
                    data.put(splittedLine[0], false);
                }

                data.replace(splittedLine[0], splittedLine[1].trim().equalsIgnoreCase("true"));
            }

        } catch (FileNotFoundException ex) {
            return null;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return data;
    }

    public static void saveData(String path, Map<String, Boolean> data) {
        StringBuilder parsedData = new StringBuilder();

        for (Map.Entry<String, Boolean> entry : data.entrySet()) {
            String key = entry.getKey();
            Object val = entry.getValue();

            parsedData.append(key).append(":").append(val).append("\n");
        }

        try {
            FileWriter file = new FileWriter("config/" + path);
            file.write(parsedData.toString());
            file.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
