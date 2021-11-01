package no.unit.nva.file.model;

import no.unit.nva.file.model.exception.MissingLicenseException;
import no.unit.nva.hamcrest.DoesNotHaveEmptyValues;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileModelTest {

    public static final URI CC_BY_URI = URI.create("https://creativecommons.org/licenses/by/4.0/");
    public static final String APPLICATION_PDF = "application/pdf";

    @Test
    void shouldReturnEmptySetWhenFileSetIsNull() {
        var fileSet = new FileSet(null);
        assertThat(fileSet.getFiles(), is(empty()));
    }

    @Test
    void shouldReturnFileSetWhenInputIsValid() {
        var first = getFile("First_file.txt", true, getCcByLicense());
        var second = getFile("Second_file.txt", false, getCcByLicense());
        var fileSet = assertDoesNotThrow(() -> new FileSet(List.of(first, second)));
        assertThat(fileSet, DoesNotHaveEmptyValues.doesNotHaveEmptyValues());
    }

    @Test
    void shouldThrowMissingLicenseExceptionWhenFileIsNotAdministrativeAgreementAndLicenseIsMissing() {
        File file = getFile("First_file.txt", false, null);
        assertThrows(MissingLicenseException.class, file::validate);
    }

    @Test
    void shouldNotThrowMissingLicenseExceptionWhenFileIsAdministrativeAgreementAndLicenseIsMissing() {
        File file = getFile("First_file.txt", true, null);
        assertDoesNotThrow(file::validate);
    }

    @Test
    void shouldNotThrowMissingLicenseExceptionWhenFileIsAdministrativeAgreementAndLicenseIsPresent() {
        File file = getFile("First_file.txt", true, getCcByLicense());
        assertDoesNotThrow(file::validate);
    }

    @Test
    void shouldNotThrowMissingLicenseExceptionWhenFileIsNotAdministrativeAgreementAndLicenseIsPresent() {
        File file = getFile("First_file.txt", false, getCcByLicense());
        assertDoesNotThrow(file::validate);
    }

    @Test
    void shouldReturnEmptySetWhenLicenseLabelsAreNull() {
        var license = new License.Builder()
                .withIdentifier("CC-BY")
                .withLink(CC_BY_URI)
                .build();
        assertThat(license.getLabels(), is(anEmptyMap()));
    }

    private File getFile(String fileName, boolean administrativeAgreement, License license) {
        return new File.Builder()
                .withAdministrativeAgreement(administrativeAgreement)
                .withEmbargoDate(Instant.now())
                .withIdentifier(UUID.randomUUID())
                .withLicense(license)
                .withMimeType(APPLICATION_PDF)
                .withName(fileName)
                .withPublisherAuthority(true)
                .withSize(200L)
                .build();
    }

    private License getCcByLicense() {
        return new License.Builder()
                .withIdentifier("CC-BY")
                .withLabels(Map.of("en", "CC-BY 4.0"))
                .withLink(CC_BY_URI)
                .build();
    }
}
