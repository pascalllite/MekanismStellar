package mekanism.stellar.client.provider;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nullable;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatSplitter {
    private static final Pattern fsPattern = Pattern.compile("%(\\d+\\$)?([-#+ 0,(<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z])");

    public static List<Component> split(String text) {
        Matcher matcher = fsPattern.matcher(text);
        List<Component> components = new ArrayList<>();
        int start = 0;
        while (matcher.find()) {
            int curStart = matcher.start();
            if (curStart > start) {
                components.add(new TextComponent(text.substring(start, curStart)));
            }
            String piece = matcher.group();
            components.add(new FormatComponent(piece));
            start = matcher.end();
        }
        if (start < text.length()) {
            if (start == 0) {
                components.addAll(splitMessageFormatInternal(text));
            } else {
                components.add(new TextComponent(text.substring(start)));
            }
        }
        return ImmutableList.copyOf(components);
    }

    public static List<Component> splitMessageFormat(String text) {
        return ImmutableList.copyOf(splitMessageFormatInternal(text));
    }

    private static List<Component> splitMessageFormatInternal(String text) {
        List<Component> components = new ArrayList<>();
        StringBuilder formattingCode = new StringBuilder();
        StringBuilder rawText = new StringBuilder();
        char[] exploded = text.toCharArray();
        int leftBrackets = 0;
        int firstBracket = -1;
        int secondBracket = -1;
        for (int i = 0; i < exploded.length; i++) {
            char c = exploded[i];
            if (c == '{') {
                if (leftBrackets == 0) {
                    firstBracket = i;
                    String raw = rawText.toString();
                    if (!raw.isEmpty()) {
                        components.add(new TextComponent(raw));
                        rawText = new StringBuilder();
                    }
                } else if (leftBrackets == 1) {
                    secondBracket = i;
                }
                leftBrackets++;
                formattingCode.append(c);
            } else if (leftBrackets > 0) {
                formattingCode.append(c);
                if (c == '}') {
                    leftBrackets--;
                    if (leftBrackets == 0) {
                        String piece = formattingCode.toString();
                        MessageFormatComponent component = MessageFormatComponent.fromContents(piece);
                        if (component == null) {
                            if (secondBracket != -1) {
                                components.add(new TextComponent(text.substring(firstBracket, secondBracket)));
                                i = secondBracket - 1;
                            } else {
                                components.add(new TextComponent(piece));
                            }
                        } else {
                            components.add(component);
                        }
                        formattingCode = new StringBuilder();
                        firstBracket = -1;
                        secondBracket = -1;
                    }
                }
            } else {
                rawText.append(c);
            }
        }
        if (leftBrackets == 0) {
            String raw = rawText.toString();
            if (!raw.isEmpty()) {
                components.add(new TextComponent(raw));
            }
        } else {
            if (secondBracket != -1) {
                components.add(new TextComponent(text.substring(firstBracket, secondBracket)));
                components.addAll(splitMessageFormatInternal(text.substring(secondBracket)));
            } else {
                String remainingString = formattingCode.toString();
                if (!remainingString.isEmpty()) {
                    components.add(new TextComponent(remainingString));
                }
            }
        }
        return components;
    }

    public interface Component {

        String contents();
    }

    private record TextComponent(String contents) implements Component {
    }

    public static class FormatComponent implements Component {

        private final String formattingCode;

        private FormatComponent(String formattingCode) {
            this.formattingCode = formattingCode;
        }

        @Override
        public String contents() {
            return formattingCode;
        }
    }

    public static class MessageFormatComponent extends FormatComponent {

        private final int argumentIndex;
        @Nullable
        private final String formatType;
        @Nullable
        private final String formatStyle;
        private final boolean isChoice;

        private MessageFormatComponent(String contents, int argumentIndex, @Nullable String formatType, @Nullable String formatStyle, boolean isChoice) {
            super(contents);
            this.argumentIndex = argumentIndex;
            this.formatType = formatType;
            this.formatStyle = formatStyle;
            this.isChoice = isChoice;
        }

        @Nullable
        private static MessageFormatComponent fromContents(String contents) {
            int length = contents.length();
            if (length < 3 || contents.charAt(0) != '{' || contents.charAt(length - 1) != '}') {
                return null;
            }
            int firstComma = contents.indexOf(',');
            int argumentIndex;
            try {
                argumentIndex = Integer.parseInt(contents.substring(1, firstComma == -1 ? length - 1 : firstComma));
            } catch (NumberFormatException e) {
                return null;
            }
            if (argumentIndex < 0 || argumentIndex > 9) {
                return null;
            }
            if (firstComma == -1) {
                return new MessageFormatComponent(contents, argumentIndex, null, null, false);
            }
            int secondComma = contents.indexOf(',', firstComma + 1);
            String formatType = contents.substring(firstComma + 1, secondComma == -1 ? length - 1 : secondComma);
            String formatStyle = secondComma == -1 ? null : contents.substring(secondComma + 1, length - 1);
            String trimmedFormatType = formatType.trim();
            boolean isChoice = false;
            switch (trimmedFormatType) {
                case "number" -> {
                    if (formatStyle != null && !formatStyle.equals("integer") && !formatStyle.equals("currency") && !formatStyle.equals("percent")) {
                        try {
                            new DecimalFormat(formatStyle);
                        } catch (IllegalArgumentException e) {
                            //If it is not a valid DecimalFormat then it is not a valid format overall, so return null
                            return null;
                        }
                    }
                }
                case "date", "time" -> {
                    if (formatStyle != null && !formatStyle.equals("short") && !formatStyle.equals("medium") && !formatStyle.equals("long") && !formatStyle.equals("full")) {
                        try {
                            new SimpleDateFormat(formatStyle, Locale.ENGLISH);
                        } catch (IllegalArgumentException e) {
                            return null;
                        }
                    }
                }
                case "choice" -> {
                    if (formatStyle == null) {
                        return null;
                    }
                    try {
                        new ChoiceFormat(formatStyle);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                    isChoice = true;
                }
                case "modinfo" -> {
                    if (formatStyle == null || (!formatStyle.equals("id") && !formatStyle.equals("name"))) {
                        return null;
                    }
                }
                case "lower", "upper", "vr" -> {
                    if (formatStyle != null) {
                        return null;
                    }
                }
                case "exc" -> {
                    if (formatStyle == null || (!formatStyle.equals("class") && !formatStyle.equals("msg"))) {
                        return null;
                    }
                }
                case "i18n", "ornull" -> {
                    if (formatStyle == null) {
                        return null;
                    }
                }
                default -> {
                    return null;
                }
            }
            return new MessageFormatComponent(contents, argumentIndex, formatType, formatStyle, isChoice);
        }

        public int getArgumentIndex() {
            return argumentIndex;
        }

        @Nullable
        public String getFormatType() {
            return formatType;
        }

        @Nullable
        public String getFormatStyle() {
            return formatStyle;
        }

        public boolean isChoice() {
            return isChoice;
        }
    }
}
