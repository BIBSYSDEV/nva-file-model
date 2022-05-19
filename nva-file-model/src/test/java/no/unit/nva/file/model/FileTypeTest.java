package no.unit.nva.file.model;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static no.unit.nva.file.model.FileType.DELIMITER;
import static no.unit.nva.file.model.FileType.ERROR_MESSAGE_TEMPLATE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class FileTypeTest {

    public static final String NONSENSE = "nonsense";

    @Test
    void shouldThrowRuntimeExceptionWhenInputIsInvalid() {
        Executable executable = () -> FileType.lookUp(NONSENSE);
        var exception = assertThrows(RuntimeException.class, executable);
        var expected = format(ERROR_MESSAGE_TEMPLATE, NONSENSE, stream(FileType.values())
                                                                    .map(FileType::toString)
                                                                    .collect(joining(DELIMITER)));

        assertThat(exception.getMessage(), is(equalTo(expected)));
    }
}
