package habuma.speechai;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class SpeechClient {

    private final SpeechAiConfig speechAiConfig;
    private final RestClient restClient;

    public SpeechClient(OpenAiConfig openAiConfig, SpeechAiConfig speechAiConfig) {
        this.speechAiConfig = speechAiConfig;
        this.restClient = RestClient.builder()
                .baseUrl("https://api.openai.com/v1/audio")
                .defaultHeader("Authorization", "Bearer " + openAiConfig.getApiKey())
                .build();
    }

    public String transcribe(Resource audioFile) {
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("file", audioFile);
        requestBody.add("model", "whisper-1");

        return restClient
                .post()
                .uri("/transcriptions")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(requestBody)
                .retrieve()
                .body(TranscriptionResponse.class)
                .text();
    }

    public byte[] synthesize(String text) {
        return restClient
                .post()
                .uri("/speech")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new TTSRequest("tts-1", text, speechAiConfig.getVoice().getLabel()))
                .accept(new MediaType("audio", "mp3"))
                .retrieve()
                .body(byte[].class);
    }

    public static record TranscriptionResponse(String text) {}

    private static record TTSRequest(String model, String input, String voice) {}

}
