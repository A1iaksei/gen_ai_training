package com.epam.training.gen.ai.plugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  Model of light device
 */
@Data
@AllArgsConstructor
public class LightModel {
    private int id;
    private String name;
    private boolean isOn;
}
