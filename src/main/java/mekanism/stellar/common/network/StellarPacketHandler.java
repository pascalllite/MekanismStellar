// NetworkHandler.java
package mekanism.stellar.common.network;

import mekanism.common.network.BasePacketHandler;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.network.to_server.PacketGuiSetHeat;
import net.minecraftforge.network.simple.SimpleChannel;

public class StellarPacketHandler extends BasePacketHandler {
    private final SimpleChannel netHandler = createChannel(Stellar.rl(Stellar.MODID), Stellar.instance.versionNumber);

    @Override
    protected SimpleChannel getChannel() {
        return netHandler;
    }

    @Override
    public void initialize() {
        registerClientToServer(PacketGuiSetHeat.class, PacketGuiSetHeat::decode);
    }
}