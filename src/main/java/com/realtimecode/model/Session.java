package com.realtimecode.model;
import java.util.Random;

public class Session {
  private String id;   // Shorter session key
    private String name;

    public Session(String name) {
        this.id = generateShortSessionKey();
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    private String generateShortSessionKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder key = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            key.append(characters.charAt(random.nextInt(characters.length())));
        }
        return key.toString();
    }
}
