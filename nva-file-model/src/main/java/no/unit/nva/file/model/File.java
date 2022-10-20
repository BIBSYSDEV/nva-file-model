package no.unit.nva.file.model;

import static java.util.Objects.isNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import no.unit.nva.file.model.exception.MissingLicenseException;
import nva.commons.core.JacocoGenerated;

/**
 * An object that represents the description of a file.
 */
public class File {

    public static final String MISSING_LICENSE =
        "The file is not annotated as an administrative agreement and should have a license";
    public static final String TYPE_FIELD = "type";
    public static final String IDENTIFIER_FIELD = "identifier";
    public static final String NAME_FIELD = "name";
    public static final String MIME_TYPE_FIELD = "mimeType";
    public static final String SIZE_FIELD = "size";
    public static final String LICENSE_FIELD = "license";
    public static final String ADMINISTRATIVE_AGREEMENT_FIELD = "administrativeAgreement";
    public static final String PUBLISHER_AUTHORITY_FIELD = "publisherAuthority";
    public static final String EMBARGO_DATE_FIELD = "embargoDate";
    @JsonProperty(TYPE_FIELD)
    private final FileType type;
    @JsonProperty(IDENTIFIER_FIELD)
    private final UUID identifier;
    @JsonProperty(NAME_FIELD)
    private final String name;
    @JsonProperty(MIME_TYPE_FIELD)
    private final String mimeType;
    @JsonProperty(SIZE_FIELD)
    private final Long size;
    @JsonProperty(LICENSE_FIELD)
    private final License license;
    @JsonProperty(ADMINISTRATIVE_AGREEMENT_FIELD)
    private final boolean administrativeAgreement;
    @JsonProperty(PUBLISHER_AUTHORITY_FIELD)
    private final boolean publisherAuthority;
    @JsonProperty(EMBARGO_DATE_FIELD)
    private final Instant embargoDate;

    /**
     * Constructor for no.unit.nva.file.model.File objects. A file object is valid if it has a license or is explicitly
     * marked as an administrative agreement.
     *
     * @param type                    The type of file
     * @param identifier              A UUID that identifies the file in storage
     * @param name                    The original name of the file
     * @param mimeType                The mimetype of the file
     * @param size                    The size of the file
     * @param license                 The license for the file, may be null if and only if the file is an administrative
     *                                agreement
     * @param administrativeAgreement True if the file is an administrative agreement
     * @param publisherAuthority      True if the file owner has publisher authority
     * @param embargoDate             The date after which the file may be published
     */
    @JsonCreator
    public File(
        @JsonProperty(TYPE_FIELD) String type,
        @JsonProperty(IDENTIFIER_FIELD) UUID identifier,
        @JsonProperty(NAME_FIELD) String name,
        @JsonProperty(MIME_TYPE_FIELD) String mimeType,
        @JsonProperty(SIZE_FIELD) Long size,
        @JsonProperty(LICENSE_FIELD) License license,
        @JsonProperty(ADMINISTRATIVE_AGREEMENT_FIELD) boolean administrativeAgreement,
        @JsonProperty(PUBLISHER_AUTHORITY_FIELD) boolean publisherAuthority,
        @JsonProperty(EMBARGO_DATE_FIELD) Instant embargoDate) {

        this.type = getAppropriateFileType(FileType.lookUp(type), administrativeAgreement);
        this.identifier = identifier;
        this.name = name;
        this.mimeType = mimeType;
        this.size = size;
        this.license = license;
        this.administrativeAgreement = administrativeAgreement;
        this.publisherAuthority = publisherAuthority;
        this.embargoDate = embargoDate;
    }

    /**
     * Validate the file.
     */
    public void validate() {
        if (!administrativeAgreement && isNull(license)) {
            throw new MissingLicenseException(MISSING_LICENSE);
        }
    }

    private File(Builder builder) {
        this(
            builder.type.getValue(),
            builder.identifier,
            builder.name,
            builder.mimeType,
            builder.size,
            builder.license,
            builder.administrativeAgreement,
            builder.publisherAuthority,
            builder.embargoDate
        );
    }

    public String getType() {
        return type.getValue();
    }

    public UUID getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Long getSize() {
        return size;
    }

    public License getLicense() {
        return license;
    }

    public boolean isAdministrativeAgreement() {
        return administrativeAgreement;
    }

    public boolean isPublisherAuthority() {
        return publisherAuthority;
    }

    public Optional<Instant> getEmbargoDate() {
        return Optional.ofNullable(embargoDate);
    }

    @JsonIgnore
    public boolean isVisibleForNonOwner() {
        return !administrativeAgreement
               && (isNull(embargoDate) || Instant.now().isAfter(embargoDate))
               && FileType.UNPUBLISHED_FILE != type;
    }

    @JsonIgnore
    private FileType getAppropriateFileType(FileType fileType, boolean administrativeAgreement) {
        if (administrativeAgreement) {
            return FileType.UNPUBLISHABLE_FILE;
        }
        return fileType == FileType.FILE ? FileType.PUBLISHED_FILE : fileType;
    }

    @JacocoGenerated
    @Override
    public int hashCode() {
        return Objects.hash(getIdentifier(), getName(), getMimeType(), getSize(),
                            getLicense(), isAdministrativeAgreement(), isPublisherAuthority(), getEmbargoDate(),
                            getType());
    }

    @JacocoGenerated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        File file = (File) o;
        return isAdministrativeAgreement() == file.isAdministrativeAgreement()
               && isPublisherAuthority() == file.isPublisherAuthority()
               && Objects.equals(getIdentifier(), file.getIdentifier())
               && Objects.equals(getName(), file.getName())
               && Objects.equals(getMimeType(), file.getMimeType())
               && Objects.equals(getSize(), file.getSize())
               && Objects.equals(getLicense(), file.getLicense())
               && Objects.equals(getEmbargoDate(), file.getEmbargoDate())
               && Objects.equals(getType(), file.getType());
    }

    public static final class Builder {

        private FileType type;
        public boolean administrativeAgreement;
        public boolean publisherAuthority;
        public Instant embargoDate;
        private UUID identifier;
        private String name;
        private String mimeType;
        private Long size;
        private License license;

        public Builder() {
        }

        public Builder withIdentifier(UUID identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withMimeType(String mimeType) {
            this.mimeType = mimeType;
            return this;
        }

        public Builder withSize(Long size) {
            this.size = size;
            return this;
        }

        public Builder withLicense(License license) {
            this.license = license;
            return this;
        }

        public Builder withAdministrativeAgreement(boolean administrativeAgreement) {
            this.administrativeAgreement = administrativeAgreement;
            return this;
        }

        public Builder withPublisherAuthority(boolean publisherAuthority) {
            this.publisherAuthority = publisherAuthority;
            return this;
        }

        public Builder withEmbargoDate(Instant embargoDate) {
            this.embargoDate = embargoDate;
            return this;
        }

        public Builder withType(FileType type) {
            this.type = type;
            return this;
        }

        public File build() {
            return new File(this);
        }
    }
}