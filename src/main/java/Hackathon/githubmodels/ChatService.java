package Hackathon.githubmodels;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.*;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class ChatService {
    private final ChatCompletionsClient client;

    public ChatService() {
        // Leer la clave del entorno
        String key = System.getenv("AZURE_KEY");
        String endpoint = "https://models.github.ai/inference";

        this.client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }

    public String chat(String userPrompt) {
        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage("You are a helpful assistant."),
                new ChatRequestUserMessage(userPrompt)
        );

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        String model = "openai/gpt-4.1-nano";
        options.setModel(model);

        ChatCompletions completions = client.complete(options);
        if (completions.getChoices() != null && !completions.getChoices().isEmpty()) {
            return completions.getChoices().get(0).getMessage().getContent();
        } else {
            return "No se recibieron respuestas del modelo.";
        }
    }
}
