package habuma.speechai;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Controller
@RequestMapping("/ask")
public class AskController {

    private static final Logger log = LoggerFactory.getLogger(AskController.class);

    private final SpeechClient speechClient;
    private final ChatClient chatClient;

    @Value("classpath:prompt-template.st")
    private Resource promptTemplateResource;

    public AskController(
            SpeechClient speechClient,
            ChatClient chatClient) {
        this.speechClient = speechClient;
        this.chatClient = chatClient;
    }

    @GetMapping
    public String record() {
        return "askView";
    }


    @PostMapping
    public @ResponseBody RecordingResponse process(@RequestParam("audio") MultipartFile blob) {
        String input = speechClient.transcribe(blob.getResource());
        log.info("Heard: " + input);
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateResource);
        Prompt prompt = promptTemplate.create(Map.of("input", input));

        String output = !input.trim().isEmpty()
                ? chatClient.call(prompt).getResult().getOutput().getContent()
                : "Sorry, I didn't catch that.";

        log.info("Response: " + output);
        byte[] ttsBytes = speechClient.synthesize(output);
        String base64 = Base64.encodeBase64String(ttsBytes);
        log.info("TTS Bytes: " + ttsBytes.length + " bytes");
        return new RecordingResponse(input, output, base64);
    }

    public static final record RecordingResponse(String input, String response, String ttsBase64) {}

    public static final record TTSRequest(String text) {}

}
