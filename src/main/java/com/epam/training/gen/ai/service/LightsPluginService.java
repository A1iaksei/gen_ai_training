package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.AppMessage;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.InvocationReturnMode;
import com.microsoft.semantickernel.orchestration.ToolCallBehavior;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
@Service
public abstract class LightsPluginService {
    private final InvocationContext invocationContext;
    private final ChatHistory chatHistory;

    @Lookup
    protected abstract ChatCompletionService getChatCompletionService(String deploymentName);

    @Lookup(value = "lightsPluginKernel")
    protected abstract Kernel getKernel(ChatCompletionService chatCompletionService);

    public LightsPluginService() {
        this.chatHistory = new ChatHistory();
        this.invocationContext = new InvocationContext.Builder()
                .withReturnMode(InvocationReturnMode.LAST_MESSAGE_ONLY)
                .withToolCallBehavior(ToolCallBehavior.allowAllKernelFunctions(true))
                .build();
    }

    public String getChatCompletions(AppMessage message) {
        chatHistory.addUserMessage(message.getInput());

        ChatCompletionService chatCompletionService = getChatCompletionService(message.getModel());
        var response =
                chatCompletionService
                        .getChatMessageContentsAsync(chatHistory, getKernel(chatCompletionService), invocationContext)
                        .block();

        var answer =
                response.stream().map(ChatMessageContent::getContent).collect(Collectors.joining("\n"));
        log.info(answer);

        return answer;
    }
}
