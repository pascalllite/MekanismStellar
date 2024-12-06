package mekanism.stellar.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import mekanism.client.render.tileentity.MekanismTileEntityRenderer;
import mekanism.stellar.common.CompressedEternalHeatGenerators;
import mekanism.stellar.common.StellarProfilerConstants;
import mekanism.stellar.common.tile.TileEntityCompressedEternalHeatGenerator;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RenderCompressedEternalHeatGenerator extends MekanismTileEntityRenderer<TileEntityCompressedEternalHeatGenerator> {
    private final CompressedEternalHeatGenerators type;

    public RenderCompressedEternalHeatGenerator(BlockEntityRendererProvider.Context context, CompressedEternalHeatGenerators type) {
        super(context);
        this.type = type;
    }

    @Override
    protected void render(TileEntityCompressedEternalHeatGenerator tileEntityCompressedEternalHeatGenerator, float partialTick, PoseStack matrix, MultiBufferSource renderer, int light, int overlayLight, ProfilerFiller profiler) {

    }

    @Override
    protected String getProfilerSection() {
        return StellarProfilerConstants.COMPRESSED_ETERNAL_HEAT_GENERATORS.get(type);
    }
}
