package no.unit.nva.file.model;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import nva.commons.core.SingletonCollector;

public enum FileType {
    @Deprecated
    FILE("File"),
    PUBLISHED_FILE("PublishedFile"),
    UNPUBLISHED_FILE("UnpublishedFile"),
    UNPUBLISHABLE_FILE("UnpublishableFile");

    public static final String ERROR_MESSAGE_TEMPLATE = "%s not a valid FileType, expected one of: %s";
    public static final String DELIMITER = ", ";

    private final String value;

    FileType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static FileType lookUp(String value) {
        return stream(values())
                   .filter(nameType -> nameType.getValue().equals(value))
                   .collect(SingletonCollector.tryCollect())
                   .orElseThrow(failure -> throwException(value));
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    private static RuntimeException throwException(String value) {
        return new IllegalArgumentException(
            format(ERROR_MESSAGE_TEMPLATE, value,
                   stream(FileType.values()).map(FileType::toString).collect(joining(DELIMITER))));
    }
}
