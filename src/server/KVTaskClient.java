package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final HttpClient client;
    private String apiToken;
    private final String url;

    public KVTaskClient(String url) {
        this.url = url;
        HttpRequest request = HttpRequest.newBuilder().GET().uri(URI.create(url + "/register")).build();
        client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            apiToken = response.body();
            statusCheck(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public void put(String key, String json) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            statusCheck(response.statusCode());
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET().uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken)).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                statusCheck(response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Во время выполнения запроса ресурса по url-адресу: '" + url + "' возникла ошибка.\n" +
                    "Проверьте, пожалуйста, адрес и повторите попытку.");
        }
        return null;
    }

    private void statusCheck(int status) {
        switch (status) {
            case 400:
                System.out.println("| " + status + " | В запросе содержится ошибка. Проверьте параметры и повторите запрос. |");
                break;
            case 404:
                System.out.println("| " + status + " | По указанному адресу нет ресурса. Проверьте URL-адрес ресурса и повторите запрос. |");
                break;
            case 500:
                System.out.println("| " + status + " | На стороне сервера произошла непредвиденная ошибка. |");
                break;
            case 503:
                System.out.println("| " + status + " | Сервер временно недоступен. Попробуйте повторить запрос позже. |");
                break;
        }
    }

}
