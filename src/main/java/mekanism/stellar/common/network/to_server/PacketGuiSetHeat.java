package mekanism.stellar.common.network.to_server;

import mekanism.common.network.IMekanismPacket;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.util.WorldUtils;
import mekanism.stellar.common.tile.TileEntityStellarGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.BiConsumer;

public class PacketGuiSetHeat implements IMekanismPacket {

    private final GuiHeatValue interaction;
    private final BlockPos tilePosition;
    private final double value;

    public PacketGuiSetHeat(GuiHeatValue interaction, BlockPos tilePosition, double value) {
        this.interaction = interaction;
        this.tilePosition = tilePosition;
        this.value = value;
    }


    public static PacketGuiSetHeat decode(FriendlyByteBuf buffer) {
        return new PacketGuiSetHeat(buffer.readEnum(GuiHeatValue.class), buffer.readBlockPos(), buffer.readDouble());
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        Player player = context.getSender();
        if (player != null) {
            TileEntityMekanism tile = WorldUtils.getTileEntity(TileEntityMekanism.class, player.level, tilePosition);
            if (tile != null) {
                interaction.consume(tile, value);
            }
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeEnum(interaction);
        buffer.writeBlockPos(tilePosition);
        buffer.writeDouble(value);
    }

    public enum GuiHeatValue {
        COOLING_TARGET((tile, value) -> {
            if (tile instanceof TileEntityStellarGenerator stellar) {
                stellar.setCoolingTargetFromPacket(value);
            }
        });

        private final BiConsumer<TileEntityMekanism, Double> consumerForTile;

        GuiHeatValue(BiConsumer<TileEntityMekanism, Double> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityMekanism tile, Double value) {
            consumerForTile.accept(tile, value);
        }
    }
}
