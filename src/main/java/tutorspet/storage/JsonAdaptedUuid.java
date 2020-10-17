package tutorspet.storage;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import tutorspet.commons.exceptions.IllegalValueException;

/**
 * Jackson-friendly version of {@link java.util.UUID}.
 */
public class JsonAdaptedUuid {

    public static final String MESSAGE_NULL_UUID = "UUID is null";
    public static final String MESSAGE_INVALID_UUID = "UUID is invalid";

    private final String uuidString;

    /**
     * Constructs a {@code JsonAdaptedUuid} with the given {@code UUID}.
     */
    @JsonCreator
    public JsonAdaptedUuid(String uuidString) {
        this.uuidString = uuidString;
    }

    /**
     * Converts a given {@code UUID} into this class for Jackson use.
     */
    public JsonAdaptedUuid(UUID source) {
        uuidString = source.toString();
    }

    @JsonValue
    public String getUuidString() {
        return uuidString;
    }

    /**
     * Converts this Jackson-friendly adapted UUID object into the model's {@code UUID} object.
     *
     * @throws IllegalValueException if adapted UUID is null.
     */
    public UUID toModelType() throws IllegalValueException {
        if (uuidString == null) {
            throw new IllegalValueException(MESSAGE_NULL_UUID);
        }

        // catch invalid UUID
        UUID uuid;
        try {
            uuid = UUID.fromString(uuidString);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(MESSAGE_INVALID_UUID);
        }

        return uuid;
    }
}
