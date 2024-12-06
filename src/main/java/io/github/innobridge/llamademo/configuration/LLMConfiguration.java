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
import io.github.innobridge.llama.client.args.RopeScalingType;

import java.io.File;

@Configuration
public class LLMConfiguration {
  private static final Logger logger = LoggerFactory.getLogger(LLMConfiguration.class);

  @Value("${llama.model.filepath}")
  private String modelFilepath;

  @Value("${llama.model.n-gpu-layers}")
  private int nGpuLayers;

  @Value("${llama.model.no-kv-offload}")
  private boolean noKvOffload;

  @Bean
  public LlamaClient llamaClient() {
    
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
    
    try {
      // CUDA optimization settings
      System.setProperty("CUDA_LAUNCH_BLOCKING", "0");
      System.setProperty("CUDA_VISIBLE_DEVICES", "0");
      System.setProperty("GGML_CUDA_FORCE_CUBLAS", "1");
      System.setProperty("GGML_CUDA_FORCE_DMMV", "0");
      System.setProperty("GGML_CUDA_FORCE_MMQ", "0");
      System.setProperty("GGML_CUDA_FORCE_KQ", "1");
      System.setProperty("GGML_CUDA_FORCE_KQV", "1");
      System.setProperty("GGML_CUDA_MALLOC_ASYNC", "1");
      System.setProperty("GGML_CUDA_NO_PINNED", "1");
      System.setProperty("GGML_CUDA_STREAM_POOL_SIZE", "16");
      System.setProperty("GGML_CUDA_MAX_STREAM_REUSE", "32");
      
      logger.info("Loading model from: {}", modelFilepath);
      logger.info("Model file absolute path: {}", modelFile.getAbsolutePath());
      logger.info("Model file canonical path: {}", modelFile.getCanonicalPath());
      logger.info("Model file parent directory exists: {}", modelFile.getParentFile().exists());
      logger.info("Model file parent directory readable: {}", modelFile.getParentFile().canRead());
      
      // Create model with proven parameters
      ModelParameters params = new ModelParameters()
          .setModelFilePath(modelFilepath)
          .setNGpuLayers(28)         
          .setNoKvOffload(false)     
          .setNThreads(6)            
          .setNBatch(384)            
          .setNUbatch(384)           
          .setNCtx(1024)             
          .setUseMmap(true)
          .setUseMlock(false)
          .setEmbedding(false)
          .setRopeScalingType(RopeScalingType.LINEAR)
          .setMainGpu(0)
          .setTensorSplit(new float[]{0.7f})
          .setFlashAttention(true)    
          .setGrpAttnN(8)            // Qwen's KV head count
          .setGrpAttnW(1024)         // Qwen's GQA size
          .setRopeFreqBase(1000000.0f)  // Qwen-specific
          .setRopeFreqScale(1.0f)       // Qwen-specific
          .setDefragmentationThreshold(0.7f)
          .setNParallel(2)           
          .setNKeep(24);             
            
      logger.info("Initializing model with parameters: {}", params);
      LlamaModel model = new LlamaModel(params);
      return new LlamaClient(model);
    } catch (Exception e) {
      logger.error("Failed to load model", e);
      throw new RuntimeException("Failed to load model: " + e.getMessage(), e);
    }
  }
}
