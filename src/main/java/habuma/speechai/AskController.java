package habuma.speechai;

import org.apache.tomcat.util.codec.binary.Base64;
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
        String output = !input.trim().isEmpty()
                ? chatClient.call("Respond to the following input, keeping your response to no more than 3 sentences: " + input)
                : "Sorry, I didn't catch that.";
        byte[] ttsBytes = ttsClient.synthesize(output);
        String base64 = Base64.encodeBase64String(ttsBytes);
        return new RecordingResponse(input, output, base64);
    }

    public static final record RecordingResponse(String input, String response, String ttsBase64) {}

    public static final record TTSRequest(String text) {}

}
