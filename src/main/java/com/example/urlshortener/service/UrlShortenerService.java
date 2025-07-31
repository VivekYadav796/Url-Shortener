package com.example.urlshortener.service;

import com.example.urlshortener.entity.ShortUrl;
import com.example.urlshortener.repository.ShortUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UrlShortenerService {

    @Autowired
    private ShortUrlRepository repository;

    private static final String ALPHANUMERIC = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_CODE_LENGTH = 6;

    
     //Shortens a given original URL
    public String shortenUrl(String originalUrl) {
        System.out.println("🔗 Original URL received: " + originalUrl);

        String shortCode = generateShortCode();

        // Ensure uniqueness
        while (repository.findByShortCode(shortCode) != null) {
            shortCode = generateShortCode();
        }

        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);

        try {
            repository.save(url);
            System.out.println("✅ Saved to database: " + shortCode + " -> " + originalUrl);
        } catch (Exception e) {
            System.err.println("❌ Error saving URL: " + e.getMessage());
            e.printStackTrace();
        }

        return shortCode;
    }

    // Retrieves the original URL for a given short code.
     
    public String getOriginalUrl(String shortCode) {
        System.out.println("📥 Looking up short code: " + shortCode);
        ShortUrl url = repository.findByShortCode(shortCode);

        if (url != null) {
            System.out.println("➡️ Found URL: " + url.getOriginalUrl());
            return url.getOriginalUrl();
        } else {
            System.out.println("⚠️ No match found for short code: " + shortCode);
            return null;
        }
    }

    
    // Generates a random alphanumeric short code.
    private String generateShortCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(SHORT_CODE_LENGTH);
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            sb.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return sb.toString();
    }
}

