package com.jamicah.elytraunequipper.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class InputEvent implements ClientTickEvents.EndTick {

    int elytraFlightCheckCounter = 0;
    boolean isPlayerFlying = false;
    boolean jumped = false;
    boolean wasJumpingLastTick = false;

    boolean hasPlayerJumped = false;
    boolean wasPlayerJumpingLastTick = false;
    boolean hasPlayerPressedJumpThisTick = false;

    boolean isElytraMode = false;

    boolean test = false;

    // this is such a mess of a code
    // there are probably better ways to do this,
    // but I'm too tired to refactor this
    @Override
    public void onEndTick(MinecraftClient client) {
        MinecraftClient c = MinecraftClient.getInstance();

        if (c.player == null) {
            return;
        }



        if (!test) {
            test = true;
            System.out.println("isPlayerFlying: " + isPlayerFlying);
            System.out.println("hasPlayerJumped: " + hasPlayerJumped);
            System.out.println("wasPlayerJumpingLastTick: " + wasPlayerJumpingLastTick);
            System.out.println("hasPlayerPressedJumpThisTick: " + hasPlayerPressedJumpThisTick);
            System.out.println("isElytraMode: " + isElytraMode);
            System.out.println("elytraFlightCheckCounter: " + elytraFlightCheckCounter);
            System.out.println("jumped: " + jumped);
            System.out.println("wasJumpingLastTick: " + wasJumpingLastTick);
            System.out.println("isElytraMode: " + isElytraMode);
            System.out.println();
        }
        isPlayerFlying = c.player.isFallFlying();

        hasPlayerJumped = c.player.input.jumping;
        hasPlayerPressedJumpThisTick = !wasPlayerJumpingLastTick && hasPlayerJumped;
        wasPlayerJumpingLastTick = hasPlayerJumped;


        jumped = c.player.input.jumping;

        // mostly happens when the player held down the space bar
        // whilst flying down and landing
        if (!c.player.isFallFlying()) {
            reset();
        }

        // this if statement is true when
        // either the player pressed jump
        // or released jump
        if (wasJumpingLastTick != jumped) {

            if (isPlayerFlying) {
                elytraFlightCheckCounter++;

                // this gets executed when the player
                // releases the jump button after starting
                // to fly with the elytra
                if (elytraFlightCheckCounter == 2) {
                    isElytraMode = true;
                    elytraFlightCheckCounter = 0;
                }

            }
        }
        wasJumpingLastTick = jumped;


        // this is executed when the player is currently
        // flying with the elytra and presses the jump button
        if (isElytraMode && c.player.input.jumping) {
            unequipElytra(c);

            reset();
        }

            // locate the elytra in the inventory and send it to chat
            // for debugging purposes



    }

    // reset values back to its original state
    public void reset() {
        elytraFlightCheckCounter = 0;
        isElytraMode = false;
    }

    public void unequipElytra(MinecraftClient c) {


        new Thread(() -> {
            assert c.player != null;
            assert c.interactionManager != null;
            c.interactionManager.clickSlot(
                    c.player.playerScreenHandler.syncId,
                    6,          // starts index from top to bottom (source)
                    0,                 // action type, 0 is left click; 1 is right click
                    SlotActionType.PICKUP,
                    c.player

            );
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {

            }
            c.interactionManager.clickSlot(
                    c.player.playerScreenHandler.syncId,
                    6,          // starts index from top to bottom (source)
                    0,
                    SlotActionType.PICKUP,
                    c.player

            );
        }).start();

        isElytraMode = false;
    }

    public boolean isChestplate(Item item) {
        return item == Items.LEATHER_CHESTPLATE
                || item == Items.CHAINMAIL_CHESTPLATE
                || item == Items.IRON_CHESTPLATE
                || item == Items.GOLDEN_CHESTPLATE
                || item == Items.DIAMOND_CHESTPLATE
                || item == Items.NETHERITE_CHESTPLATE;
    }
}
