package com.epam.training.gen.ai.model;

import lombok.Data;

@Data
public class AppMessage {
    String input;
    String output;
    String model;
}
