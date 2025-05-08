package Hackathon.githubmodels;

import com.azure.ai.inference.ChatCompletionsClient;
import com.azure.ai.inference.ChatCompletionsClientBuilder;
import com.azure.ai.inference.models.*;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MetaService implements AIModel {

    private ChatCompletionsClient client;

    public MetaService() {
        String key = System.getenv("AZURE_KEY");
        String endpoint = "https://models.github.ai/inference";

        this.client = new ChatCompletionsClientBuilder()
                .credential(new AzureKeyCredential(key))
                .endpoint(endpoint)
                .buildClient();
    }

    @Override
    public String generarRespuesta(String prompt) {
        // Lógica de GPT
        List<ChatRequestMessage> messages = Arrays.asList(
                new ChatRequestSystemMessage("You are a helpful assistant."),
                new ChatRequestUserMessage(prompt)
        );

        ChatCompletionsOptions options = new ChatCompletionsOptions(messages);
        options.setModel("meta/Llama-3.3-70B-Instruct");

        ChatCompletions completions = client.complete(options);
        if (completions.getChoices() != null && !completions.getChoices().isEmpty()) {
            return completions.getChoices().get(0).getMessage().getContent();
        } else {
            return "No se recibieron respuestas del modelo.";
        }
    }
}
