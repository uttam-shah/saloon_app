package com.example.saloon.app.saloon_app.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dfyjdbmr2");
        config.put("api_key", "741789622887595");
        config.put("api_secret", "BwFRE0rlcVz9XKTtA1YQDVO7yI0");
        return new Cloudinary(config);

        //CLOUDINARY_URL=cloudinary://741789622887595:BwFRE0rlcVz9XKTtA1YQDVO7yI0@dfyjdbmr2
    }
}
