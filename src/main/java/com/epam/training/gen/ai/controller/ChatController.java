package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.dto.AppMessageDTO;
import com.epam.training.gen.ai.exception.GenAiBadRequestException;
import com.epam.training.gen.ai.service.KernelHistoryService;
import com.epam.training.gen.ai.service.SimplePromptService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("chat")
public class ChatController {
  private static final String INPUT_PARAMETER_IS_EMPTY_MESSAGE = "input parameter is empty";

  private SimplePromptService simplePromptService;
  private KernelHistoryService kernelHistoryService;

  /**
   * Creates a new {@link AppMessageDTO} for chat conversation using Semantic Kernel
   *
   * <p>
   *
   * @param message new {@link AppMessageDTO} with {@link String} input
   * @return {@link AppMessageDTO} with {@link List<String>} output
   */
  @PostMapping(value = "/semantic-kernel")
  public AppMessageDTO getSemanticKernelAnswer(@RequestBody AppMessageDTO message) {
    validateAppMessageDTO(message);
    String answer = kernelHistoryService.processWithHistory(message.getInput());
    message.setOutput(answer);
    return message;
  }

  /**
   * Creates a new {@link AppMessageDTO} for chat conversation using directly OpenAiAsyncClient
   *
   * <p>
   *
   * @param message new {@link AppMessageDTO} with {@link String} input
   * @return {@link AppMessageDTO} with {@link List<String>} output
   */
  @PostMapping(value = "/simple")
  public AppMessageDTO getSimpleAnswer(@RequestBody AppMessageDTO message) {
    validateAppMessageDTO(message);
    var answer = simplePromptService.getChatCompletions(message.getInput());
    message.setOutput(answer);
    return message;
  }

  private void validateAppMessageDTO(AppMessageDTO message) {
    if (message == null || StringUtils.isBlank(message.getInput())) {
      throw new GenAiBadRequestException(INPUT_PARAMETER_IS_EMPTY_MESSAGE);
    }
  }
}
