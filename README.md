# Spring AI, Whisper, and Text-to-Speech

This is a sample application showing how to capture audio from a webpage,
submit it to a Spring MVC controller, and submit it to OpenAI's Whisper API
to get a transcription. That transcription is then sent to OpenAI's 
gpt-3.5-turbo model to get a response. Finally, the response is then sent to the
text-to-speech API to get an audio response that is returned to the client.

Note that at the moment, the prompt sent to the gpt-3.5-turbo model includes
text asking for the response to be kept brief. At this time, no conversational
memory or RAG is applied to the prompt for context (but that could change in
the future).

## Building and running the application

Before running the application, you'll need to obtain an OpenAI API
key and set it to the `OPENAI_API_KEY` environment variable. E.g.,

```
% export OPENAI_API_KEY=sk-...
```

This is a Spring Boot application built with Maven. Therefore, you can run
the application just like any other Spring Boot application using the Spring
Boot Maven plugin:

```
% ./mvnw spring-boot:run
```

Or you can build it to an executable JAR file and then run it:

```
% ./mvnw package
% java -jar target/speechai-0.0.1-SNAPSHOT.jar
```

## Using the application

Once the application is running, open http://localhost:8080/ask in your
web browser. Press and hold the "Listen" buttons while talking.

After releasing the "Listen (Whisper API)" button, the audio captured by the
browser will be sent to the  server and from there to OpenAI's Whisper API for
transcription. From there the transcritpion will be sent to OpenAI's gpt-3.5-turbo
model to get a response. Finally, the response will be sent to the text-to-speech
API to get an audio response that is returned to the client.

## A few notes...

- At this time, Spring AI does not (yet) support integration with OpenAI's
Whisper or TTS APIs. Therefore, this application is invoking those APIs directly
using Spring's `RestClient` in the `SpeechClient` class.
- I've tested this on Chrome, Firefox, and Safari web browsers. It works on
all three, but Safari doesn't seem to capture everything said when the "Listen"
button is held (for some reason). Most of my testing has been with Chrome and
it works incredibly well. I welcome any insight into why it doesn't work as well
with Safari.
