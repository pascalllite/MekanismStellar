package mekanism.stellar.common.provider;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mekanism.api.providers.IBlockProvider;
import mekanism.stellar.common.Stellar;
import mekanism.stellar.common.registries.StellarBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeRegistryTagsProvider;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class StellarTagProvider implements DataProvider {
    private final Map<TagType<?>, Map<TagKey<?>, Tag.Builder>> supportedTagTypes = new Object2ObjectLinkedOpenHashMap<>();
    private final Set<Block> knownHarvestRequirements = new HashSet<>();
    private final ExistingFileHelper existingFileHelper;
    private final DataGenerator gen;

    public StellarTagProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper) {
        this.gen = gen;
        this.existingFileHelper = existingFileHelper;
        addTagType(TagType.ITEM);
        addTagType(TagType.BLOCK);
        addTagType(TagType.ENTITY_TYPE);
        addTagType(TagType.FLUID);
        addTagType(TagType.BLOCK_ENTITY_TYPE);
        addTagType(TagType.GAS);
        addTagType(TagType.INFUSE_TYPE);
        addTagType(TagType.PIGMENT);
        addTagType(TagType.SLURRY);
    }

    protected List<IBlockProvider> getAllBlocks() {
        return StellarBlocks.BLOCKS.getAllBlocks();
    }

    @Nonnull
    @Override
    public String getName() {
        return "Tags: " + Stellar.MODID;
    }

    //Protected to allow for extensions to add their own supported types if they have one
    protected <TYPE extends IForgeRegistryEntry<TYPE>> void addTagType(TagType<TYPE> tagType) {
        supportedTagTypes.computeIfAbsent(tagType, type -> new Object2ObjectLinkedOpenHashMap<>());
    }

    protected void registerTags() {
        addHarvestRequirements();
    }

    private void addHarvestRequirements() {
        addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE,
                StellarBlocks.STELLAR_GENERATOR,
                StellarBlocks.ETERNAL_HEAT_GENERATOR
        );
        addToHarvestTag(BlockTags.MINEABLE_WITH_PICKAXE,
                StellarBlocks.COMPRESSED_ETERNAL_HEAT_GENERATORS
        );
    }


    protected void hasHarvestData(Block block) {
        knownHarvestRequirements.add(block);
    }

    @Override
    public void run(@Nonnull HashCache cache) {
        supportedTagTypes.values().forEach(Map::clear);
        registerTags();
        for (IBlockProvider blockProvider : getAllBlocks()) {
            Block block = blockProvider.getBlock();
            if (block.defaultBlockState().requiresCorrectToolForDrops() && !knownHarvestRequirements.contains(block)) {
                throw new IllegalStateException("Missing harvest tool type for block '" + block.getRegistryName() +
                        "' that requires the correct tool for drops.");
            }
        }
        supportedTagTypes.forEach((tagType, tagTypeMap) -> act(cache, tagType, tagTypeMap));
    }

    private <TYPE extends IForgeRegistryEntry<TYPE>> void act(@Nonnull HashCache cache, TagType<TYPE> tagType, Map<TagKey<?>, Tag.Builder> tagTypeMap) {
        if (!tagTypeMap.isEmpty()) {
            new ForgeRegistryTagsProvider<>(gen, tagType.getRegistry(), Stellar.MODID, existingFileHelper) {
                @Override
                protected void addTags() {
                    tagTypeMap.forEach((tag, tagBuilder) -> builders.put(tag.location(), new Tag.Builder() {
                        @Nonnull
                        @Override
                        public JsonObject serializeToJson() {
                            return cleanJsonTag(tagBuilder.serializeToJson());
                        }

                        @Nonnull
                        @Override
                        public Stream<Tag.BuilderEntry> getEntries() {
                            return tagBuilder.getEntries();
                        }
                    }));
                }

                @Nonnull
                @Override
                public String getName() {
                    return tagType.name() + " Tags: " + Stellar.MODID;
                }
            }.run(cache);
        }
    }

    private JsonObject cleanJsonTag(JsonObject tagAsJson) {
        if (tagAsJson.has(DataGenJsonConstants.REPLACE)) {
            JsonPrimitive replace = tagAsJson.getAsJsonPrimitive(DataGenJsonConstants.REPLACE);
            if (replace.isBoolean() && !replace.getAsBoolean()) {
                tagAsJson.remove(DataGenJsonConstants.REPLACE);
            }
        }
        return tagAsJson;
    }

    protected <TYPE extends IForgeRegistryEntry<TYPE>> ForgeRegistryTagBuilder<TYPE> getBuilder(TagType<TYPE> tagType, TagKey<TYPE> tag) {
        return new ForgeRegistryTagBuilder<>(supportedTagTypes.get(tagType).computeIfAbsent(tag, ignored -> Tag.Builder.tag()), Stellar.MODID);
    }

    protected ForgeRegistryTagBuilder<Block> getBlockBuilder(TagKey<Block> tag) {
        return getBuilder(TagType.BLOCK, tag);
    }

    protected void addToHarvestTag(TagKey<Block> tag, IBlockProvider... blockProviders) {
        ForgeRegistryTagBuilder<Block> tagBuilder = getBlockBuilder(tag);
        for (IBlockProvider blockProvider : blockProviders) {
            Block block = blockProvider.getBlock();
            tagBuilder.add(block);
            hasHarvestData(block);
        }
    }

    @SafeVarargs
    protected final void addToHarvestTag(TagKey<Block> blockTag, Map<?, ? extends IBlockProvider>... blockProviders) {
        ForgeRegistryTagBuilder<Block> tagBuilder = getBlockBuilder(blockTag);
        for (Map<?, ? extends IBlockProvider> blockProvider : blockProviders) {
            for (IBlockProvider value : blockProvider.values()) {
                Block block = value.getBlock();
                tagBuilder.add(block);
                hasHarvestData(block);
            }
        }
    }
}
