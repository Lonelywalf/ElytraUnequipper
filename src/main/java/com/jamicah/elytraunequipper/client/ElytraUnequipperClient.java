package com.jamicah.elytraunequipper.client;


import com.jamicah.elytraunequipper.events.InputEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ElytraUnequipperClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(new InputEvent());
    }
}
