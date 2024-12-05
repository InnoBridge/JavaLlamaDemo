package io.github.innobridge.llamademo.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.github.innobridge.llama.client.LLMClient;
import io.github.innobridge.llama.client.LlamaClient;
import io.github.innobridge.llama.client.LlamaModel;
import io.github.innobridge.llama.client.ModelParameters;

import java.io.File;

@Configuration
public class LLMConfiguration {
  private static final Logger logger = LoggerFactory.getLogger(LLMConfiguration.class);

  @Bean
  public LLMClient llamaClient(
      @Value("${model.file.path}") String modelFilepath,
      @Value("${model.gpu.layers}") Integer nGpuLayers,
      @Value("${model.no.kv.offload}") boolean noKvOffload) {
    
    // Validate model file exists and is readable
    File modelFile = new File(modelFilepath);
    logger.debug("Model file path: {}", modelFilepath);
    logger.debug("Model file exists: {}", modelFile.exists());
    logger.debug("Model file can read: {}", modelFile.canRead());
    logger.debug("Model file length: {} bytes", modelFile.length());
    logger.debug("GPU Layers: {}", nGpuLayers);
    logger.debug("No KV Offload: {}", noKvOffload);

    if (!modelFile.exists() || !modelFile.canRead()) {
      throw new IllegalStateException("Model file does not exist or is not readable: " + modelFilepath);
    }

    logger.info("Creating LlamaClient with modelFilepath: {} and nGpuLayers: {}", modelFilepath, nGpuLayers);
    
    // Create model with optimized parameters
    ModelParameters params = new ModelParameters()
        .setModelFilePath(modelFilepath)
        .setNGpuLayers(nGpuLayers)
        .setNoKvOffload(noKvOffload)
        .setNThreads(Runtime.getRuntime().availableProcessors()) // Use all available CPU threads
        .setNBatch(512)  // Increase batch size for better throughput
        .setNCtx(4096)   // Set context window
        .setUseMmap(true)  // Use memory mapping for faster loading
        .setUseMlock(true)  // Lock memory to prevent swapping
        .setEmbedding(false);  // Disable embedding if not needed
        
    LlamaModel model = new LlamaModel(params);
    return new LlamaClient(model);
  }
}
