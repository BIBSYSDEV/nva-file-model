package no.unit.nva.file.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.unit.nva.file.model.exception.MissingLicenseException;
import nva.commons.core.JsonUtils;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static no.unit.nva.hamcrest.DoesNotHaveEmptyValues.doesNotHaveEmptyValues;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anEmptyMap;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileModelTest {

    public static final URI CC_BY_URI = URI.create("https://creativecommons.org/licenses/by/4.0/");
    public static final String APPLICATION_PDF = "application/pdf";
    public static final String FIRST_FILE_TXT = "First_file.txt";
    public static final String SECOND_FILE_TXT = "Second_file.txt";
    public static final String CC_BY = "CC-BY";
    public static final String CC_BY_4_0 = "CC-BY 4.0";
    public static final String EN = "en";
    public static final long SIZE = 200L;
    public static final ObjectMapper dataModelObjectMapper = JsonUtils.dtoObjectMapper;

    @Test
    void shouldReturnEmptySetWhenFileSetIsNull() {
        var fileSet = new FileSet(null);
        assertThat(fileSet.getFiles(), is(empty()));
    }

    @Test
    void shouldReturnFileSetWhenInputIsValid() {
        var first = getFile(FIRST_FILE_TXT, true, getCcByLicense());
        var second = getFile(SECOND_FILE_TXT, false, getCcByLicense());
        var fileSet = assertDoesNotThrow(() -> new FileSet(List.of(first, second)));
        assertThat(fileSet, doesNotHaveEmptyValues());
    }

    @Test
    void shouldThrowMissingLicenseExceptionWhenFileIsNotAdministrativeAgreementAndLicenseIsMissing() {
        var file = getFile(FIRST_FILE_TXT, false, null);
        assertThrows(MissingLicenseException.class, file::validate);
    }

    @Test
    void shouldNotThrowMissingLicenseExceptionWhenFileIsAdministrativeAgreementAndLicenseIsMissing() {
        var file = getFile(FIRST_FILE_TXT, true, null);
        assertDoesNotThrow(file::validate);
    }

    @Test
    void shouldNotThrowMissingLicenseExceptionWhenFileIsAdministrativeAgreementAndLicenseIsPresent() {
        var file = getFile(FIRST_FILE_TXT, true, getCcByLicense());
        assertDoesNotThrow(file::validate);
    }

    @Test
    void shouldNotThrowMissingLicenseExceptionWhenFileIsNotAdministrativeAgreementAndLicenseIsPresent() {
        File file = getFile(FIRST_FILE_TXT, false, getCcByLicense());
        assertDoesNotThrow(file::validate);
    }

    @Test
    void shouldReturnEmptySetWhenLicenseLabelsAreNull() {
        var license = new License.Builder()
                .withIdentifier(CC_BY)
                .withLink(CC_BY_URI)
                .build();
        assertThat(license.getLabels(), is(anEmptyMap()));
    }

    @Test
    void shouldReturnSerializedModel() throws JsonProcessingException {
        var fileset = new FileSet(List.of(getFile(FIRST_FILE_TXT, true, getCcByLicense())));
        var mapped = dataModelObjectMapper.writeValueAsString(fileset);
        var unmapped = dataModelObjectMapper.readValue(mapped, FileSet.class);
        assertThat(fileset, equalTo(unmapped));
        assertThat(fileset, doesNotHaveEmptyValues());
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
                .withSize(SIZE)
                .build();
    }

    private License getCcByLicense() {
        return new License.Builder()
                .withIdentifier(CC_BY)
                .withLabels(Map.of(EN, CC_BY_4_0))
                .withLink(CC_BY_URI)
                .build();
    }
}
