package habuma.speechai;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class WhisperClient {

    private final OpenAiConfig config;

    public WhisperClient(OpenAiConfig config) {
        this.config = config;
    }

    public String transcribe(Resource audioFile) {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(config.getApiKey());

        MultiValueMap<String, Object> body
                = new LinkedMultiValueMap<>();
        body.add("file", audioFile);
        body.add("model", "whisper-1");

        HttpEntity<MultiValueMap<String, Object>> requestEntity
                = new HttpEntity<>(body, headers);

        TranscriptionResponse response = rest.postForObject(
                "https://api.openai.com/v1/audio/transcriptions",
                requestEntity,
                TranscriptionResponse.class);

        return response.text();
    }

    public static record TranscriptionResponse(String text) {}

}
