package habuma.speechai;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "speechai")
public class SpeechAiConfig {

    private Voice voice = Voice.Alloy;

    public Voice getVoice() {
        return voice;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public static enum Voice {
        Alloy("alloy"), Echo("echo"), Fable("fable"), Onyx("onyx"), Nova("nova"), Shimmer("shimmer");

        private final String label;

        private Voice(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }

    }

}
