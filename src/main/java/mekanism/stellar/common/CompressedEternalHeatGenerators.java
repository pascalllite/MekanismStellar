package mekanism.stellar.common;

import mekanism.api.text.ILangEntry;
import net.minecraft.Util;

public enum CompressedEternalHeatGenerators implements ILangEntry {
    COMPRESSED_01(8D),// 圧縮量 8 8 発電量 1K 1024
    COMPRESSED_02(64D),// 圧縮量 64 64 発電量 8K 8192
    COMPRESSED_03(512D),// 圧縮量 512 512 発電量 65K 6万
    COMPRESSED_04(4_096D),// 圧縮量 4K 4096 発電量 524K 52万
    COMPRESSED_05(32_768D),// 圧縮量 32K 3万 発電量 4M 419万
    COMPRESSED_06(262_144D),// 圧縮量 262K 26万 発電量 33M 3355万
    COMPRESSED_07(2_097_152D),// 圧縮量 2M 209万 発電量 268M 2億
    COMPRESSED_08(16_777_216D),// 圧縮量 16M 1677万 発電量 2G 21億
    COMPRESSED_09(134_217_728D),// 圧縮量 134M 1億 発電量 17G 171億
    COMPRESSED_10(1_073_741_824D),// 圧縮量 1G 10億 発電量 137G 1374億
    COMPRESSED_11(8_589_934_592D),// 圧縮量 8G 85億 発電量 1T 1兆
    COMPRESSED_12(68_719_476_736D),// 圧縮量 68G 687億 発電量 8T 8兆
    COMPRESSED_13(549_755_813_888D),// 圧縮量 549G 5497億 発電量 70T 70兆
    COMPRESSED_14(4_398_046_511_104D),// 圧縮量 4T 4兆 発電量 562T 562兆
    COMPRESSED_15(35_184_372_088_832D),// 圧縮量 35T 35兆 発電量 4P 4503兆
    COMPRESSED_16(281_474_976_710_656D),// 圧縮量 281T 281兆 発電量 36P 3京
    COMPRESSED_17(2_251_799_813_685_248D),// 圧縮量 2P 2251兆 発電量 288P 28京
    COMPRESSED_18(18_014_398_509_481_984D),// 圧縮量 18P 1京 発電量 2E 230京
    COMPRESSED_19(144_115_188_075_855_872D),// 圧縮量 144P 14京 発電量 18E 1844京
    COMPRESSED_20(1_152_921_504_606_846_976D);// 圧縮量 1E 115京 発電量 147E 1垓

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

    public static String numeral(double value) {
        var x = (int) Math.floor(Math.floor(Math.log10(value)) / 4);
        return (int) Math.floor(value / Math.pow(10, x * 4)) + switch (x) {
            case 0 -> "";
            case 1 -> "万";
            case 2 -> "億";
            case 3 -> "兆";
            case 4 -> "京";
            case 5 -> "垓";
            case 6 -> "𥝱";
            case 7 -> "穣";
            case 8 -> "溝";
            case 9 -> "澗";
            case 10 -> "正";
            case 11 -> "載";
            case 12 -> "極";
            case 13 -> "恒河沙";
            case 14 -> "阿僧祇";
            case 15 -> "那由他";
            case 16 -> "不可思議";
            case 17 -> "無量大数";
            default -> "_";
        };
    }

    public String description() {
        return switch (this) {
            default -> "This is the true perpetual motion machine!";
        };
    }

    public String descriptionJa() {
        return switch (this) {
            default -> "これが真の永久機械だ！";
        };
    }

    public String path() {
        return "compressed_ehg_tier" + (this.ordinal() + 1);
    }

    public String si() {
        return this.si;
    }

    public String numeral() {
        return numeral(this.multiplier);
    }

    public double multiplier() {
        return this.multiplier;
    }

    @Override
    public String getTranslationKey() {
        return Util.makeDescriptionId("description", Stellar.rl(this.path()));
    }
}
