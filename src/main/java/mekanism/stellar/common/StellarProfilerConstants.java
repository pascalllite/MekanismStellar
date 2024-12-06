package mekanism.stellar.common;

import java.util.EnumMap;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class StellarProfilerConstants {

    public static final String STELLAR_GENERATOR = "stellarGenerator";
    public static final String ETERNAL_HEAT_GENERATOR = "eternalHeatGenerator";
    public static final EnumMap<CompressedEternalHeatGenerators, String> COMPRESSED_ETERNAL_HEAT_GENERATORS = Stream.of(CompressedEternalHeatGenerators.values()).collect(Collectors.toMap(UnaryOperator.identity(), type -> "compressedEternalHeatGenerator" + type.si(), (a, b) -> a, () -> new EnumMap<>(CompressedEternalHeatGenerators.class)));
    private StellarProfilerConstants() {
    }
}
