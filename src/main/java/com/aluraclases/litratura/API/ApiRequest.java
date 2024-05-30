package com.aluraclases.litratura.API;

import com.aluraclases.litratura.estrucutradelJSON.Resultado;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiRequest {


    private static final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL) // Habilitar seguimiento de redirecciones
            .build();

    public Resultado urlLibro(String url) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(responseBody, Resultado.class);
        } else {
            throw new IOException("Error: " + response.statusCode());
        }
    }
}
