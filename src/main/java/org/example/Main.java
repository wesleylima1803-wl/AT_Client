package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.*;

public class Main {
    private static final String urlBase = "http://localhost:7070/equipes";
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final Gson gson = new Gson();

    public static void main() throws Exception {
        System.out.println("Cliente da Gestão\n");

        String body = """
                {
                  "nome": "Vasco da Gama",
                  "sigla": "VAS",
                  "modalidade": "Futebol",
                  "categoria": "Profissional",
                  "tecnico": "Renato Gaúcho",
                  "capitao": "Thiago Mendes",
                  "quantidadeAtletas": 35,
                  "dataFundacao": "1898-08-21",
                  "status": "ATIVA"
                }
                """;

        var postResp = send("POST", urlBase, body);
        System.out.println("Etapa 1(POST): " + urlBase);
        System.out.println("Status: " + postResp.statusCode());
        System.out.println("Body: " + postResp.body() + "\n");

        var getAllResp = send("GET", urlBase, null);
        System.out.println("Etapa 2(GET): " + urlBase);
        System.out.println("Status: " + getAllResp.statusCode());
        System.out.println("Body: " + getAllResp.body() + "\n");

        int id = getId(postResp.body());
        var getByIdResp = send("GET", urlBase + "/" + id, null);
        System.out.println("Etapa 3(GET): " + urlBase + "/" + id);
        System.out.println("Status: " + getByIdResp.statusCode());
        System.out.println("Body: " + getByIdResp.body() + "\n");

        String bodyAtualizado = """
                {
                  "nome": "CR Vasco da Gama",
                  "sigla": "VAS",
                  "modalidade": "Futebol",
                  "categoria": "Profissional",
                  "tecnico": "Renato Gaucho",
                  "capitao": "Leonardo Jardim",
                  "quantidadeAtletas": 32,
                  "dataFundacao": "1898-08-21",
                  "status": "ATIVA"
                }
                """;

        var putResp = send("PUT", urlBase + "/" + id, bodyAtualizado);
        System.out.println("Etapa 4(PUT): " + urlBase + "/" + id);
        System.out.println("Status: " + putResp.statusCode()+ "\n");

        var deleteResp = send("DELETE", urlBase + "/" + id, null);
        System.out.println("Etapa 5(DELETE): " + urlBase + "/" + id);
        System.out.println("Status: " + deleteResp.statusCode());
        System.out.println("Body: " + deleteResp.statusCode()+ "\n");

        var getAllAfterDelete = send("GET", urlBase + "/" + id, null);
        System.out.println("Etapa 5(GET após DELETE): " + urlBase + "/" + id);
        System.out.println("Status: " + getAllAfterDelete.statusCode());
        System.out.println("Body: " + getAllAfterDelete.body()+ "\n");
    }

    private static HttpResponse<String> send(String metodo, String url, String body) throws Exception {
        var builder = HttpRequest
                .newBuilder()
                .uri(URI.create(url));

        if (body != null) {
            builder.method(metodo, HttpRequest.BodyPublishers.ofString(body));
        } else {
            builder.method(metodo, HttpRequest.BodyPublishers.noBody());
        }
        return httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }

    private static int getId(String json) {
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return jsonObject.get("id").getAsInt();
    }
}