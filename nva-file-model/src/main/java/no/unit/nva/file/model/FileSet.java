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

    @JsonProperty(FILES)
    private final List<File> files;

    /**
     * Constructs the FileSet object, allows (de-)serialization of the FileSet description.
     * @param files A list of file descriptions that logically belong together.
     */
    @JsonCreator
    public FileSet(@JsonProperty(FILES) List<File> files) {
        this.files = files;
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
