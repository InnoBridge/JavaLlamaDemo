package io.github.innobridge.llamademo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.innobridge.llama.client.LLMClient;
import io.github.innobridge.llama.client.LlamaClient;
import io.github.innobridge.llama.client.LlamaModel;
import io.github.innobridge.llama.client.ModelParameters;

@Configuration
public class LLMConfiguration {

  @Bean
  public LLMClient llamaClient(
      @Value("${model.file.path}") String modelFilepath,
      @Value("${model.gpu.layers}") Integer nGpuLayers) {
    System.out.println("Creating LlamaClient with modelFilepath: " + modelFilepath + " and nGpuLayers: " + nGpuLayers);
    LlamaModel model = new LlamaModel(new ModelParameters()
        .setModelFilePath(modelFilepath)
        .setNGpuLayers(nGpuLayers));
    return new LlamaClient(model);
  }
}
