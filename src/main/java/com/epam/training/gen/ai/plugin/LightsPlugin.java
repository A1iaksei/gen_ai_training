package com.epam.training.gen.ai.plugin;

import com.epam.training.gen.ai.plugin.model.LightModel;
import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightsPlugin {
    private static final Map<Integer, LightModel> lights = new HashMap<>();

    /*
       Map of lights devices (id, name and state) of smart home
     */
    static {
        lights.put(1, new LightModel(1,"Table Lamp", false));
        lights.put(2, new LightModel(2,"Porch light", false));
        lights.put(3, new LightModel(3, "Chandelier", true));
    }

    /**
     *  Provides information about status of all light devices of smart home
     */
    @DefineKernelFunction(name = "get_lights", description = "Get a list of lights and their current state")
    public List<LightModel> getLights() {
        return new ArrayList<>(lights.values());
    }

    /**
     *  Changes state of the selected light device
     */
    @DefineKernelFunction(name = "change_state", description = "Changes the state of the light")
    public LightModel changeState(
            @KernelFunctionParameter(name = "id", description = "The ID of the light to change") int id,
            @KernelFunctionParameter(name = "isOn", description = "The new state of the light") boolean isOn) {
        System.out.println("Changing light " + id + " " + isOn);
        if (!lights.containsKey(id)) {
            throw new IllegalArgumentException("Light not found");
        }
        lights.get(id).setOn(isOn);

        return lights.get(id);
    }
}
