package Hackathon.githubmodels;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AIModelFactory {

    private final GPTService gptService;
    private final DeepSeekService deepSeekService;
    private final MetaService metaService;
    private final MaverickService maverickService;

    @Autowired
    public AIModelFactory(GPTService gptService, DeepSeekService deepSeekService, MetaService metaService, MaverickService maverickService) {
        this.gptService = gptService;
        this.deepSeekService = deepSeekService;
        this.metaService = metaService;
        this.maverickService = maverickService;
    }

    public AIModel getModelService(String tipoModelo) {
        switch (tipoModelo.toUpperCase()) {
            case "GPT":
                return gptService;
            case "DEEPSEEK-R1":
                return deepSeekService;
            case "META":
                return metaService;
            case "MAVERICK":
                return maverickService;
            default:
                throw new IllegalArgumentException("Modelo no soportado: " + tipoModelo);
        }
    }
}
