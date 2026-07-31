package lx.utils;

import lx.model.PriceRule;

import java.io.BufferedReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class PriceRuleUtils {
    private PriceRuleUtils() {
    }

    public static List<PriceRule> read(String path) {
        List<PriceRule> rules = new ArrayList<>();
        Path file = Path.of(path);
        if (!Files.exists(file)) {
            return rules;
        }
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] fields = line.split("\\|", -1);
                if (fields.length != 3) {
                    throw new IllegalArgumentException(path + "第" + lineNumber
                            + "行格式错误，应为：关键词|最低价|最高价");
                }
                rules.add(new PriceRule(fields[0], parseBoundary(fields[1], path, lineNumber),
                        parseBoundary(fields[2], path, lineNumber)));
            }
            return rules;
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(path + "文件读取失败", e);
        }
    }

    private static BigDecimal parseBoundary(String value, String path, int lineNumber) {
        value = value.trim();
        if (value.isEmpty() || "-".equals(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.replace(",", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(path + "第" + lineNumber + "行价格格式错误: " + value, e);
        }
    }
}
