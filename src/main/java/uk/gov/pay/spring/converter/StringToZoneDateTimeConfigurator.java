package uk.gov.pay.spring.converter;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class StringToZoneDateTimeConfigurator implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToZoneDateTime());
    }

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    static class StringToZoneDateTime implements Converter<String, ZonedDateTime> {
        @Override
        public ZonedDateTime convert(String value) {
            LocalDateTime localDateTime = LocalDateTime.parse(value, formatter);
            return localDateTime.atZone(ZoneId.systemDefault());
        }
    }
}