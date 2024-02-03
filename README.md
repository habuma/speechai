# Spring and the Whisper API

This is a sample application showing how to capture audio from a webpage,
submit it to a Spring MVC controller, and submit it to OpenAI's Whisper API
to get a transcription.

## Building and running the application

This is a Spring Boot application built with Maven. Therefore, you can run
the application just like any other Spring Boot application using the Spring
Boot Maven plugin:

```
% ./mvnw spring-boot:run
```

Or you can build it to an executable JAR file and then run it:

```
% ./mvnw package
% java -jar target/speech-to-text-0.0.1-SNAPSHOT.jar
```

Before running the application, though, you'll need to obtain an OpenAI API
key and set it to the `OPENAI_API_KEY` environment variable. E.g.,

```
% export OPENAI_API_KEY=st-...
```

## Using the application

Once the application is running, open http://localhost:8080/transcribe in your
web browser. Press and hold either of the "Listen" buttons while talking.</p>

After releasing the "Listen (Whisper API)" button, the audio captured by the
browser will be sent to the  server and from there to OpenAI's Whisper API for
transcription. Then, the transcription will be sent back to the client and 
appended to a `<div>` just below the button. This button will work in any
browser, but it requires an OpenAI API key.

The "Listen (Browser)" button uses the browser's built-in speech recognition
capability. It only works in browsers that support this feature. It does not
require an OpenAI API key.

Press either button as often as you like.
