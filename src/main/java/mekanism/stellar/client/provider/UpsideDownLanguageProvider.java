package mekanism.stellar.client.provider;

import mekanism.common.Mekanism;
import mekanism.stellar.client.provider.FormatSplitter.Component;
import mekanism.stellar.client.provider.FormatSplitter.FormatComponent;
import mekanism.stellar.client.provider.FormatSplitter.MessageFormatComponent;
import net.minecraft.data.DataGenerator;

import java.text.ChoiceFormat;
import java.util.List;

public class UpsideDownLanguageProvider extends ConvertibleLanguageProvider {

    private static final String normal = "abcdefghijklmnopqrstuvwxyz" +
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "0123456789" +
            ",.?!;\"'`&_^()[]{}<>";

    private static final char[] upside_down = ("ɐqɔpǝɟᵷɥᴉɾʞꞁɯuodbɹsʇnʌʍxʎz" +
            "ⱯᗺƆᗡƎℲ⅁HIՐꞰꞀWNOԀꝹᴚS⟘∩ΛMX⅄Z" +
            "0⥝ᘔƐ߈ϛ9ㄥ86" +
            "'˙¿¡؛„,,⅋‾v)(][}{><").toCharArray();

    public UpsideDownLanguageProvider(DataGenerator gen, String modid) {
        super(gen, modid, "en_ud");
    }

    private static char flip(char c) {
        int index = normal.indexOf(c);
        return index == -1 ? c : upside_down[index];
    }

    private static String convertFormattingComponent(FormatComponent component, int curIndex, int numArguments) {
        if (component instanceof MessageFormatComponent messageFormatComponent) {
            return convertMessageFormatCode(messageFormatComponent);
        }
        String formattingCode = component.contents();
        String ending;
        int storedIndex = curIndex;
        String[] split = formattingCode.split("\\$");
        if (split.length == 2) {
            ending = split[1];
            storedIndex = Integer.parseInt(split[0].substring(1));
        } else {
            ending = formattingCode.substring(1);
        }
        if (storedIndex == numArguments - curIndex + 1) {
            return "%" + ending;
        }
        return "%" + storedIndex + "$" + ending;
    }

    private static String convertMessageFormatCode(MessageFormatComponent component) {
        String formatStyle = component.getFormatStyle();
        if (formatStyle != null && component.isChoice()) {
            String newFormatStyle = invertChoice(formatStyle);
            try {
                new ChoiceFormat(newFormatStyle);
            } catch (IllegalArgumentException e) {
                Mekanism.logger.warn("Failed to convert '{}' to an upside down choice format. Got: '{}' which was invalid.", formatStyle, newFormatStyle);
                return component.contents();
            }
            return "{" + component.getArgumentIndex() + "," + component.getFormatType() + "," + newFormatStyle + "}";
        }
        return component.contents();
    }

    private static String invertChoice(String choice) {
        StringBuilder converted = new StringBuilder();
        StringBuilder literalBuilder = new StringBuilder();
        StringBuilder textBuilder = new StringBuilder();
        char[] exploded = choice.toCharArray();
        int leftBrackets = 0;
        boolean inLiteral = true;
        for (char c : exploded) {
            if (inLiteral) {
                literalBuilder.append(c);
                if (c == '#' || c == '<' || c == '≤') {
                    inLiteral = false;
                    converted.append(literalBuilder);
                    literalBuilder = new StringBuilder();
                }
            } else {
                if (c == '{') {
                    leftBrackets++;
                } else if (c == '}') {
                    leftBrackets--;
                } else if (c == '|' && leftBrackets == 0) {
                    inLiteral = true;
                    converted.append(convertComponents(FormatSplitter.splitMessageFormat(textBuilder.toString())));
                    textBuilder = new StringBuilder();
                }
                if (inLiteral) {
                    literalBuilder.append(c);
                } else {
                    textBuilder.append(c);
                }
            }
        }
        if (inLiteral) {
            converted.append(literalBuilder);
        } else {
            converted.append(convertComponents(FormatSplitter.splitMessageFormat(textBuilder.toString())));
        }
        return converted.toString();
    }

    private static String convertComponents(List<Component> splitText) {
        int numArguments = (int) splitText.stream().filter(component -> component instanceof FormatComponent).count();
        StringBuilder converted = new StringBuilder();
        int curIndex = numArguments;
        for (int i = splitText.size() - 1; i >= 0; i--) {
            Component component = splitText.get(i);
            if (component instanceof FormatComponent formatComponent) {
                converted.append(convertFormattingComponent(formatComponent, curIndex--, numArguments));
            } else {
                char[] toConvertArr = component.contents().toCharArray();
                for (int j = toConvertArr.length - 1; j >= 0; j--) {
                    converted.append(flip(toConvertArr[j]));
                }
            }
        }
        return new String(converted);
    }

    @Override
    public void convert(String key, List<Component> splitEnglish) {
        add(key, convertComponents(splitEnglish));
    }
}
