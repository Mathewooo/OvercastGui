package gg.overcast.gui.colors;

import net.md_5.bungee.api.ChatColor;

import javax.annotation.Nonnull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTranslator {
    private static final Pattern HEX_PATTERN =
            Pattern.compile(
                    "(&#[\\da-fA-F]{6})"
            );

    public static String translateColorCodes(@Nonnull String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder stringBuilder = new StringBuilder();
        while (matcher.find()) {
            String hex = matcher
                    .group(1)
                    .substring(1);
            matcher.appendReplacement(
                    stringBuilder, "" + ChatColor.of(hex)
            );
        }
        matcher.appendTail(stringBuilder);
        String hexColored = stringBuilder.toString();
        return ChatColor.translateAlternateColorCodes(
                '&', hexColored
        );
    }
}