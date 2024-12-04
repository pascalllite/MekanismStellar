package mekanism.stellar.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.generators.common.GeneratorsLang;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import java.util.List;

public class GuiEternalHeatGenerator extends GuiMekanismTile<TileEntityEternalHeatGenerator, MekanismTileContainer<TileEntityEternalHeatGenerator>> {
    public GuiEternalHeatGenerator(MekanismTileContainer<TileEntityEternalHeatGenerator> container, Inventory inv, Component title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, () -> List.of(GeneratorsLang.PRODUCING_AMOUNT.translate(EnergyDisplay.of(tile.getProducingEnergy())))));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15));
    }

    @Override
    protected void drawForegroundText(@Nonnull PoseStack matrix, int mouseX, int mouseY) {
        renderTitleText(matrix);
        drawString(matrix, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }
}
