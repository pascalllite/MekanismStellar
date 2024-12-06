package mekanism.stellar.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import mekanism.stellar.common.StellarProfilerConstants;
import mekanism.stellar.common.tile.TileEntityEternalHeatGenerator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RenderEternalHeatGenerator extends MekanismTileEntityRenderer<TileEntityEternalHeatGenerator> {
    public RenderEternalHeatGenerator(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void render(TileEntityEternalHeatGenerator tileEntityEternalHeatGenerator, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {

    }

    @Override
    protected String getProfilerSection() {
        return StellarProfilerConstants.ETERNAL_HEAT_GENERATOR;
    }

}
