package mekanism.stellar.common;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum CompressedEternalHeatGenerators implements ILangEntry {
    COMPRESSED_01(8D),
    COMPRESSED_02(64D),
    COMPRESSED_03(512D),
    COMPRESSED_04(4_096D),
    COMPRESSED_05(32_768D),
    COMPRESSED_06(262_144D),
    COMPRESSED_07(2_097_152D),
    COMPRESSED_08(16_777_216D),
    COMPRESSED_09(134_217_728D),
    COMPRESSED_10(1_073_741_824D),
    COMPRESSED_11(8_589_934_592D),
    COMPRESSED_12(68_719_476_736D),
    COMPRESSED_13(549_755_813_888D),
    COMPRESSED_14(4_398_046_511_104D),
    COMPRESSED_15(35_184_372_088_832D),
    COMPRESSED_16(281_474_976_710_656D),
    COMPRESSED_17(2_251_799_813_685_248D),
    COMPRESSED_18(18_014_398_509_481_984D),
    COMPRESSED_19(144_115_188_075_855_872D),
    COMPRESSED_20(1_152_921_504_606_846_976D);

    private final double multiplier;
    private final String si;

    CompressedEternalHeatGenerators(double multiplier) {
        this.multiplier = multiplier;
        this.si = si(multiplier);
    }

    public static String si(double value) {
        var x = (int) Math.floor(Math.floor(Math.log10(value)) / 3);
        return (int) Math.floor(value / Math.pow(10, x * 3)) + switch (x) {
            case 0 -> "";
            case 1 -> "k";
            case 2 -> "m";
            case 3 -> "g";
            case 4 -> "t";
            case 5 -> "p";
            case 6 -> "e";
            case 7 -> "z";
            case 8 -> "y";
            case 9 -> "r";
            case 10 -> "q";
            default -> "_";
        };
    }

    public String path() {
        return "compressed_ehg_" + this.si;
    }

    public double multiplier() {
        return this.multiplier;
    }

    @Override
    public String getTranslationKey() {
        return Util.makeDescriptionId("description", Stellar.rl(this.path()));
    }
}
