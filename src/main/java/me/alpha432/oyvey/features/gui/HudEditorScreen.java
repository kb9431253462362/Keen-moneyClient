package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.items.buttons.ModuleButton;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class HudEditorScreen extends Screen {
    private final ArrayList<Widget> components = new ArrayList<>();
    public HudModule currentDragging;
    public boolean anyHover;

    public HudEditorScreen() {
        super(Component.literal("oyvey-hudeditor"));
        load();
    }

    private void load() {
        Widget hud = new Widget("Hud", 50, 50, true);
        OyVey.moduleManager.stream()
                .filter(m -> m.getCategory() == Module.Category.HUD && !m.hidden)
                .map(ModuleButton::new)
                .forEach(hud::addButton);
        this.components.add(hud);
        this.components.forEach(component -> component.getItems().sort(Comparator.comparing(Feature::getName)));
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        /*
         * MODIFIED: Added fill command to draw a dark green background tint.
         * Color(0, 50, 0, 120) is Dark Green with 120/255 (approx 47%) opacity.
         */
        context.fill(0, 0, context.guiWidth(), context.guiHeight(), new Color(0, 50, 0, 120).hashCode());
        
        anyHover = false;
        this.components.forEach(component -> component.drawScreen(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        this.components.forEach(component -> component.mouseClicked((int) click.x(), (int) click.y(), click.button()));
        return super.mouseClicked(click, doubled);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        this.components.forEach(component -> component.mouseReleased((int) click.x(), (int) click.y(), click.button()));
        return super.mouseReleased(click);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (verticalAmount > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        this.components.forEach(component -> component.onKeyPressed(input.input()));
        return super.keyPressed(input);
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
        this.components.forEach(component -> component.onKeyTyped(input.codepointAsString(), input.modifiers()));
        return super.charTyped(input);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics context, int mouseX, int mouseY, float delta) {
    }

    public ArrayList<Widget> getComponents() {
        return components;
    }
}
