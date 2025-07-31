package com.example.urlshortener.controller;

import com.example.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UrlShortenerController {

    @Autowired
    private UrlShortenerService service;

    // Show the homepage with form
    @GetMapping("/")
    public String home() {
        return "index"; // This returns src/main/resources/templates/index.html
    }

    // Handle form submission :} <3
    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam("url") String originalUrl, Model model) {
        System.out.println("🔗 Shortening URL: " + originalUrl);

        String shortCode = service.shortenUrl(originalUrl);
        String shortUrl = "http://localhost:8080/" + shortCode;

        System.out.println("Short URL generated: " + shortUrl);

        model.addAttribute("shortUrl", shortUrl);
        return "result"; 
    }

    // Handle redirect from short URL means short url works same as long one
    @GetMapping("/{shortCode}")
    public String redirectToOriginal(@PathVariable String shortCode) {
        if ("favicon.ico".equalsIgnoreCase(shortCode)) {
            return "forward:/noop";
        }

        System.out.println("Looking up short code: " + shortCode);

        String originalUrl = service.getOriginalUrl(shortCode);

        if (originalUrl != null) {
            System.out.println("Redirecting to: " + originalUrl);
            return "redirect:" + originalUrl;
        } else {
            System.out.println("No match found for short code: " + shortCode);
            return "error";
        }
    }

    @GetMapping("/noop")
    @ResponseBody
    public void noop() {
        // No-op to silently handle favicon.ico requests
    }

}
