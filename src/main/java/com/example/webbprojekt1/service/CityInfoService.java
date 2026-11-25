
package com.example.webbprojekt1.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class CityInfoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> getCityInfo(String cityName) {

        Map<String, String> result = new HashMap<>();

        try {
            String formattedCity = cityName.trim().replace(" ", "_");

            String encodedCity = UriUtils.encodePathSegment(
                    formattedCity,
                    StandardCharsets.UTF_8
            );

            String url = "https://sv.wikipedia.org/api/rest_v1/page/summary/"
                    + encodedCity;

            org.springframework.http.HttpHeaders headers =
                    new org.springframework.http.HttpHeaders();

            headers.set("User-Agent",
                    "EventFinderStudentProject/1.0 (lanam@student.se)");

            org.springframework.http.HttpEntity<String> entity =
                    new org.springframework.http.HttpEntity<>(headers);

            org.springframework.http.ResponseEntity<String> responseEntity =
                    restTemplate.exchange(
                            url,
                            org.springframework.http.HttpMethod.GET,
                            entity,
                            String.class
                    );

            String response = responseEntity.getBody();

            JsonNode json = objectMapper.readTree(response);

            if (json.has("extract")) {
                result.put("description",
                        json.get("extract").asText());
            }

            if (json.has("thumbnail")
                    && json.get("thumbnail").has("source")) {

                result.put("image",
                        json.get("thumbnail").get("source").asText());
            }

            if (json.has("description")) {
                result.put("shortDescription",
                        json.get("description").asText());
            }

            result.put("source", "Wikipedia (SV)");

        } catch (Exception e) {

            result.put("error",
                    "Kunde inte hämta information om staden: "
                            + e.getMessage());
        }

        return result;
    }
}