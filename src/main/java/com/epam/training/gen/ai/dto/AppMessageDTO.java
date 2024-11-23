package com.epam.training.gen.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class AppMessageDTO {
    String input;
    List<String> output;
}
