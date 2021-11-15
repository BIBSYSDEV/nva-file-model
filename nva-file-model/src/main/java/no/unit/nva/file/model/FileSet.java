package no.unit.nva.file.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nva.commons.core.JacocoGenerated;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

/**
 * This is a container class that allows the serialization of the description of a set of files.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
public class FileSet {

    public static final String FILES = "files";
    public static final String DUPLICATE_FILE_IDENTIFIER_ERROR =
            "The FileSet cannot contain two files with the same identifier";

    @JsonProperty(FILES)
    private final List<File> files;

    /**
     * Constructs the FileSet object, allows (de-)serialization of the FileSet description.
     * @param files A list of file descriptions that logically belong together.
     */
    @JsonCreator
    public FileSet(@JsonProperty(FILES) List<File> files) {
        this.files = validate(files);
    }

    private List<File> validate(List<File> files) {
        if (assertIdentifiersAreUnique(files)) {
            return files;
        }
        throw new IllegalArgumentException(DUPLICATE_FILE_IDENTIFIER_ERROR);
    }

    private boolean assertIdentifiersAreUnique(List<File> files) {
        return isNull(files) || files.isEmpty() || files.stream()
                .map(File::getIdentifier)
                .distinct()
                .count() == files.size();
    }

    public List<File> getFiles() {
        return isNull(files) ? Collections.emptyList() : files;
    }

    @JacocoGenerated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileSet fileSet = (FileSet) o;
        return Objects.equals(getFiles(), fileSet.getFiles());
    }

    @JacocoGenerated
    @Override
    public int hashCode() {
        return Objects.hash(getFiles());
    }
}
