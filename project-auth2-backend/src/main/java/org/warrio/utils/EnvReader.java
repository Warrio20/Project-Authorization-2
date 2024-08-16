package org.warrio.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class EnvReader {
    public static Map<String, String> readFile(String path){
        Map<String, String> variables = new HashMap<>();
        try(BufferedReader br = Files.newBufferedReader(Path.of(path), StandardCharsets.UTF_8)){
            br.lines().forEach(line -> {
                String[] split = line.split("=", 2);
                if(split.length==2){
                    variables.put(split[0].trim(),split[1].trim());
                }
            });
        } catch (IOException ignored) {};
        System.getenv().forEach(variables::putIfAbsent);
        return variables;
    }
}
