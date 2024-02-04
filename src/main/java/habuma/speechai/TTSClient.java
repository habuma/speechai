package habuma.speechai;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TTSClient {

    private final OpenAiConfig config;

    public TTSClient(OpenAiConfig config) {
        this.config = config;
    }

    public byte[] synthesize(String text) {
        RestClient rest = RestClient.create("https://api.openai.com");
        return rest
                .post()
                .uri("/v1/audio/speech")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + config.getApiKey())
                .body(new TTSRequest("tts-1", text, "alloy"))
                .accept(new MediaType("audio", "mp3"))
                .retrieve()
                .body(byte[].class);
    }

    private static record TTSRequest(String model, String input, String voice) {}

}
