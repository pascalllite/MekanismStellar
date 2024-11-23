package mekanism.stellar.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.client.gui.element.tab.GuiHeatTab;
import mekanism.client.gui.element.text.GuiTextField;
import mekanism.common.MekanismLang;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils;
import mekanism.common.util.text.EnergyDisplay;
import mekanism.common.util.text.InputValidator;
import mekanism.generators.common.GeneratorsLang;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.StellarLang;
import mekanism.stellar.common.network.to_server.PacketGuiSetHeat;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class GuiStellarGenerator extends GuiMekanismTile<TileEntityStellarGenerator, MekanismTileContainer<TileEntityStellarGenerator>> {
    private GuiTextField coolingTargetField;

    public GuiStellarGenerator(MekanismTileContainer<TileEntityStellarGenerator> container, Inventory inv, Component title) {
        super(container, inv, title);
        inventoryLabelY += 2;
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addRenderableWidget(new GuiEnergyTab(this, () -> List.of(GeneratorsLang.PRODUCING_AMOUNT.translate(EnergyDisplay.of(tile.getProducingEnergy())))));
        addRenderableWidget(new GuiVerticalPowerBar(this, tile.getEnergyContainer(), 164, 15));
        addRenderableWidget(new GuiHeatTab(this, () -> List.of(
                MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(tile.getTotalTemperature(), UnitDisplayUtils.TemperatureUnit.KELVIN, true)),
                MekanismLang.TEMPERATURE.translate(MekanismUtils.getTemperatureDisplay(Arrays.stream(EnumUtils.DIRECTIONS).filter(direction -> tile.getAdjacent(direction) == null).mapToDouble(tile::getTotalTemperature).sum(), UnitDisplayUtils.TemperatureUnit.KELVIN, true)),
                MekanismLang.TRANSFERRED_RATE.translate(MekanismUtils.getTemperatureDisplay(tile.getLastTransferLoss(), UnitDisplayUtils.TemperatureUnit.KELVIN, false))
        )));
        addRenderableWidget(new GuiInnerScreen(this, 48, 23, 80, 42, () -> List.of(
                StellarLang.COOLING_TARGET.translate(MekanismUtils.getTemperatureDisplay(tile.getCoolingTarget(), UnitDisplayUtils.TemperatureUnit.KELVIN, true)),
                StellarLang.HEAT_LOSS.translate(MekanismUtils.getTemperatureDisplay(tile.getLoss(), UnitDisplayUtils.TemperatureUnit.KELVIN, false))
        )).clearFormat());
        coolingTargetField = addRenderableWidget(new GuiTextField(this, 50, 51, 76, 12));
        coolingTargetField.setText(coolingTargetField.getText());
        coolingTargetField.setInputValidator(InputValidator.DIGIT);
        coolingTargetField.configureDigitalInput(this::setCoolingTarget);
        coolingTargetField.setFocused(true);
    }

    @Override
    protected void drawForegroundText(@Nonnull PoseStack matrix, int mouseX, int mouseY) {
        renderTitleText(matrix);
        drawString(matrix, playerInventoryTitle, inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }

    private void setCoolingTarget() {
        if (!coolingTargetField.getText().isEmpty()) {
            try {
                Stellar.packetHandler().sendToServer(new PacketGuiSetHeat(PacketGuiSetHeat.GuiHeatValue.COOLING_TARGET, tile.getBlockPos(),
                        MekanismConfig.common.tempUnit.get().convertToK(Double.parseDouble(coolingTargetField.getText()), false)));
            } catch (NumberFormatException ignored) {
            }
            coolingTargetField.setText("");
        }
    }
}
