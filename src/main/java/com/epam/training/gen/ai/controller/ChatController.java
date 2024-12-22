package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.AppMessageDTO;
import com.epam.training.gen.ai.exception.GenAiBadRequestException;
import com.epam.training.gen.ai.model.AppMessage;
import com.epam.training.gen.ai.service.KernelHistoryService;
import com.epam.training.gen.ai.service.LightsPluginService;
import com.epam.training.gen.ai.service.SimplePromptService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("chat")
public class ChatController {
  private static final String INPUT_PARAMETER_IS_EMPTY_MESSAGE = "input parameter is empty";

  private ModelMapper modelMapper;
  private SimplePromptService simplePromptService;
  private KernelHistoryService kernelHistoryService;
  private LightsPluginService lightsPluginService;

  /**
   * Creates a new {@link AppMessageDTO} for chat conversation using Semantic Kernel
   *
   * <p>
   *
   * @param appMessageDTO new {@link AppMessageDTO} with {@link String} input
   * @return {@link AppMessageDTO} with {@link List<String>} output
   */
  @PostMapping(value = "/semantic-kernel")
  public AppMessageDTO getSemanticKernelAnswer(@RequestBody AppMessageDTO appMessageDTO) {
    var appMessage = this.mapToAppMessage(appMessageDTO);
    String answer = kernelHistoryService.processWithHistory(appMessage);
    appMessageDTO.setOutput(answer);
    appMessageDTO.setModel(appMessage.getModel());
    return appMessageDTO;
  }

  /**
   * Creates a new {@link AppMessageDTO} for chat conversation using directly OpenAiAsyncClient
   *
   * <p>
   *
   * @param appMessageDTO new {@link AppMessageDTO} with {@link String} input
   * @return {@link AppMessageDTO} with {@link List<String>} output
   */
  @PostMapping(value = "/simple")
  public AppMessageDTO getSimpleAnswer(@RequestBody AppMessageDTO appMessageDTO) {
    var appMessage = this.mapToAppMessage(appMessageDTO);
    var answer = simplePromptService.getChatCompletions(appMessage);
    appMessageDTO.setOutput(answer);
    appMessageDTO.setModel(appMessage.getModel());
    return appMessageDTO;
  }

  /**
   * Creates a new {@link AppMessageDTO} for managing Lights devices of the smart home
   *
   * <p>
   *
   * @param appMessageDTO new {@link AppMessageDTO} with {@link String} input
   * @return {@link AppMessageDTO} with {@link List<String>} output
   */
  @PostMapping(value = "/lights-plugin")
  public AppMessageDTO getLightsPluginAnswer(@RequestBody AppMessageDTO appMessageDTO) {
    var appMessage = this.mapToAppMessage(appMessageDTO);
    var answer = lightsPluginService.getChatCompletions(appMessage);
    appMessageDTO.setOutput(answer);
    appMessageDTO.setModel(appMessage.getModel());
    return appMessageDTO;
  }

  private AppMessage mapToAppMessage(AppMessageDTO appMessageDTO){
    validateAppMessageDTO(appMessageDTO);
    AppMessage appMessage = new AppMessage();
    modelMapper.map(appMessageDTO, appMessage);
    return appMessage;
  }

  private void validateAppMessageDTO(AppMessageDTO message) {
    if (message == null || StringUtils.isBlank(message.getInput())) {
      throw new GenAiBadRequestException(INPUT_PARAMETER_IS_EMPTY_MESSAGE);
    }
  }
}
