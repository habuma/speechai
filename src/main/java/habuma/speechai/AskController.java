package habuma.speechai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/ask")
public class AskController {

    private static final Logger log = LoggerFactory.getLogger(AskController.class);

    private final WhisperClient whisperClient;
    private final TTSClient ttsClient;
    private final ChatClient chatClient;

    public AskController(
            WhisperClient whisperClient,
            TTSClient ttsClient,
            ChatClient chatClient) {
        this.whisperClient = whisperClient;
        this.ttsClient = ttsClient;
        this.chatClient = chatClient;
    }

    @GetMapping
    public String record() {
        return "askView";
    }


    @PostMapping
    public @ResponseBody RecordingResponse process(@RequestParam("audio") MultipartFile blob) {
        String input = whisperClient.transcribe(blob.getResource());
        if (!input.trim().isEmpty()) {
            String response = chatClient.call(input);
            return new RecordingResponse(input, response);
        }
        return new RecordingResponse(input, "Sorry, I didn't catch that.");
    }

    @GetMapping(path="/tts", produces = "audio/mpeg")
    public @ResponseBody byte[] ttsGet() {
        return ttsClient.synthesize("Hello, how can I help you?");
    }

    @PostMapping(path="/tts", consumes="application/json", produces = "audio/mpeg")
    public @ResponseBody byte[] tts(@RequestBody TTSRequest request) {
        return ttsClient.synthesize(request.text());
    }

    public static final record RecordingResponse(String input, String response) {}

    public static final record TTSRequest(String text) {}

}
