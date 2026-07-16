package com.example.demo.config;

import com.example.demo.util.HtmlSanitizer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Module xssSanitizerModule() {
        SimpleModule module = new SimpleModule("xssSanitizer");
        module.addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
            @Override
            public String deserialize(JsonParser p, DeserializationContext ctxt) {
                try {
                    String value = p.getValueAsString();
                    return HtmlSanitizer.sanitize(value);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        return module;
    }
}
