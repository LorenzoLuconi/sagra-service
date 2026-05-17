package it.loreluc.sagraservice.appconfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum AppConfigurationDefinition {
    GENERAL_NAME("general", "name", AppConfigurationType.STRING),
    GENERAL_DATE_START("general", "date-start", AppConfigurationType.DATE),
    GENERAL_DATE_END("general", "date-end", AppConfigurationType.DATE),
    GENERAL_LOGO_SVG("general", "logo-svg", AppConfigurationType.STRING),
    PRINT_SPLIT_BY("print", "split-by", AppConfigurationType.STRING, List.of("none", "course", "department")),
    PRINT_FORMAT("print", "format", AppConfigurationType.STRING, List.of("A4", "A5")),
    PRINT_CUSTOMER_COPY("print", "customer-copy", AppConfigurationType.BOOLEAN),
    ORDER_NAME_MANDATORY("order", "name-mandatory", AppConfigurationType.BOOLEAN),
    ORDER_TAKE_AWAY_ENABLED("order", "take-away-enabled", AppConfigurationType.BOOLEAN),
    ORDER_SERVICE_ENABLED("order", "service-enabled", AppConfigurationType.BOOLEAN),
    ORDER_SERVICE_COST("order", "service-cost", AppConfigurationType.DECIMAL),

    ;
    private final String group;
    private final String key;
    private final AppConfigurationType type;
    private final List<String> allowedValues;

    AppConfigurationDefinition(String group, String key, AppConfigurationType type) {
        this(group, key, type, List.of());
    }

    AppConfigurationDefinition(String group, String key, AppConfigurationType type, List<String> allowedValues) {
        this.group = group;
        this.key = key;
        this.type = type;
        this.allowedValues = allowedValues;
    }

    public String getGroup() {
        return group;
    }

    public String getKey() {
        return key;
    }

    public AppConfigurationType getType() {
        return type;
    }

    public List<String> getAllowedValues() {
        return allowedValues;
    }

    public static List<AppConfigurationDefinition> findByGroup(String group) {
        return Arrays.stream(values())
                .filter(definition -> definition.group.equals(group))
                .toList();
    }

    public static Optional<AppConfigurationDefinition> findByGroupAndKey(String group, String key) {
        return Arrays.stream(values())
                .filter(definition -> definition.group.equals(group) && definition.key.equals(key))
                .findFirst();
    }

    public boolean isValidValue(String value) {
        if (value == null) {
            return true;
        }
        if (!allowedValues.isEmpty() && !allowedValues.contains(value)) {
            return false;
        }
        try {
            switch (type) {
                case STRING -> {
                    return true;
                }
                case INTEGER -> {
                    Integer.parseInt(value);
                    return true;
                }
                case DECIMAL -> {
                    new BigDecimal(value);
                    return true;
                }
                case BOOLEAN -> {
                    return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
                }
                case DATE -> {
                    LocalDate.parse(value);
                    return true;
                }
                default -> {
                    return false;
                }
            }
        } catch (RuntimeException e) {
            return false;
        }
    }
}
