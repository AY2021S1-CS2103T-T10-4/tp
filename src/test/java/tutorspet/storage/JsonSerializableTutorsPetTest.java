package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.storage.JsonAdaptedLesson.INVALID_FIELD_MESSAGE_FORMAT;
import static tutorspet.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.commons.util.JsonUtil;
import tutorspet.model.TutorsPet;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.attendance.Week;
import tutorspet.storage.attendance.JsonAdaptedAttendanceRecord;
import tutorspet.storage.attendance.JsonAdaptedAttendanceRecordList;
import tutorspet.storage.attendance.JsonAdaptedStudentAttendance;
import tutorspet.testutil.TypicalTutorsPet;

public class JsonSerializableTutorsPetTest {

    private static final Path TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableTutorsPetTest");
    private static final Path STUDENT_TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableTutorsPetTest", "Student");
    private static final Path CLASS_TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableTutorsPetTest", "Class");
    private static final Path LESSON_TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableTutorsPetTest", "Lesson");
    private static final Path ATTENDANCE_TEST_DATA_FOLDER =
            Paths.get("src", "test", "data", "JsonSerializableTutorsPetTest", "Attendance");

    private static final Path TYPICAL_TUTORS_PET_FILE =
            TEST_DATA_FOLDER.resolve("typicalTutorsPet.json");

    private static final Path INVALID_STUDENT_FILE = STUDENT_TEST_DATA_FOLDER.resolve("invalidStudentTutorsPet.json");
    private static final Path DUPLICATE_STUDENT_NAME_AND_TELEGRAM_FILE =
            STUDENT_TEST_DATA_FOLDER.resolve("duplicateStudentNameAndTelegramTutorsPet.json");
    private static final Path DUPLICATE_STUDENT_NAME_AND_EMAIL_FILE =
            STUDENT_TEST_DATA_FOLDER.resolve("duplicateStudentNameAndEmailTutorsPet.json");
    private static final Path DUPLICATE_STUDENT_UUID_FILE =
            STUDENT_TEST_DATA_FOLDER.resolve("duplicateStudentUuidTutorsPet.json");

    private static final Path INVALID_CLASS_FILE = CLASS_TEST_DATA_FOLDER.resolve("invalidClassTutorsPet.json");
    private static final Path INVALID_STUDENT_UUID_IN_CLASS_FILE_1 =
            CLASS_TEST_DATA_FOLDER.resolve("invalidStudentUuidInClass1.json");
    private static final Path INVALID_STUDENT_UUID_IN_CLASS_FILE_2 =
            CLASS_TEST_DATA_FOLDER.resolve("invalidStudentUuidInClass2.json");
    private static final Path DUPLICATE_CLASS_FILE = CLASS_TEST_DATA_FOLDER.resolve("duplicateClassTutorsPet.json");
    private static final Path DUPLICATE_STUDENT_UUID_IN_CLASS_FILE =
            CLASS_TEST_DATA_FOLDER.resolve("duplicateStudentUuidInClass.json");
    private static final Path INVALID_STUDENT_UUID_IN_LESSON_FILE =
            CLASS_TEST_DATA_FOLDER.resolve("invalidStudentUuidInLesson.json");

    private static final Path DUPLICATE_LESSON_FILE =
            LESSON_TEST_DATA_FOLDER.resolve("duplicateLessonTutorsPet.json");
    private static final Path INVALID_LESSON_FILE =
            LESSON_TEST_DATA_FOLDER.resolve("invalidLessonTutorsPet.json");
    private static final Path NULL_LESSON_FILE =
            LESSON_TEST_DATA_FOLDER.resolve("nullLessonTutorsPet.json");

    private static final Path INVALID_PARTICIPATION_VALUE_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("invalidParticipationValue.json");
    private static final Path NULL_PARTICIPATION_VALUE_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("nullParticipationValue.json");
    private static final Path INVALID_STUDENT_UUID_IN_STUDENT_ATTENDANCE_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("invalidStudentUuidInStudentAttendance.json");
    private static final Path NULL_STUDENT_UUID_IN_STUDENT_ATTENDANCE_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("nullStudentUuidInStudentAttendance.json");
    private static final Path NULL_STUDENT_ATTENDANCE_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("nullStudentAttendance.json");
    private static final Path DUPLICATE_RECORD_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("duplicateRecord.json");
    private static final Path DUPLICATE_WEEK_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("duplicateWeek.json");
    private static final Path INCORRECT_WEEK_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("incorrectWeek.json");
    private static final Path INVALID_WEEK_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("invalidWeek.json");
    private static final Path MISSING_WEEK_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("missingWeek.json");
    private static final Path NULL_WEEK_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("nullWeek.json");
    private static final Path NULL_RECORD_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("nullRecord.json");
    private static final Path NULL_RECORD_LIST_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("nullRecordList.json");
    private static final Path EMPTY_RECORD_LIST_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("emptyRecordList.json");
    private static final Path NULL_ATTENDANCE_RECORD_LIST_FILE =
            ATTENDANCE_TEST_DATA_FOLDER.resolve("nullAttendanceRecordList.json");

    @Test
    public void toModelType_typicalStudentsAndClassesFile_success() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(TYPICAL_TUTORS_PET_FILE,
                JsonSerializableTutorsPet.class).get();
        TutorsPet tutorsPetFromFile = dataFromFile.toModelType();
        TutorsPet typicalStudentsTutorsPet = TypicalTutorsPet.getTypicalTutorsPet();
        assertEquals(typicalStudentsTutorsPet, tutorsPetFromFile);
    }

    @Test
    public void toModelType_invalidStudentFile_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateStudentsNameAndTelegram_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_NAME_AND_TELEGRAM_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableTutorsPet.MESSAGE_DUPLICATE_STUDENT,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateStudentsNameAndEmail_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_NAME_AND_EMAIL_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableTutorsPet.MESSAGE_DUPLICATE_STUDENT,
                dataFromFile::toModelType);
    }

    /**
     * Ensures that Tutor's Pet will not be able to boot up if two or more {@code Student}s
     * are found with the same {@code UUID}.
     */
    @Test
    public void toModelType_duplicateStudentsUuid_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_UUID_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonSerializableTutorsPet.MESSAGE_DUPLICATE_STUDENT,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidClassFile_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_CLASS_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    /**
     * Ensures that Tutor's Pet will not be able to boot up although Java tries
     * to pad missing {@code UUID} digits in broken {@code UUID}s with zeros.
     */
    @Test
    public void toModelType_invalidStudentUuidInClassFile1_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_UUID_IN_CLASS_FILE_1,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    /**
     * Ensures that Tutor's Pet will not be able to boot up given an incomplete {@code UUID}.
     * (e.g "0c527a3f-8a6f-4c16-b57d-").
     */
    @Test
    public void toModelType_invalidStudentUuidInClassFile2_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_UUID_IN_CLASS_FILE_2,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateClasses_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_CLASS_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    /**
     * If Tutor's Pet encounters duplicate {@code Student UUID}s in a class, it will not load
     * duplicate {@code UUID}s into the model. Tutor's Pet should still boot up successfully.
     */
    @Test
    public void toModelType_duplicateStudentsInClassFile_success() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_STUDENT_UUID_IN_CLASS_FILE,
                JsonSerializableTutorsPet.class).get();
        TutorsPet tutorsPetFromFile = dataFromFile.toModelType();
        TutorsPet typicalStudentsTutorsPet = TypicalTutorsPet.getTypicalTutorsPet();
        assertEquals(tutorsPetFromFile, typicalStudentsTutorsPet);
    }

    @Test
    public void toModelType_invalidStudentUuidInLesson_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_UUID_IN_LESSON_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedModuleClass.MESSAGE_INVALID_STUDENTS_IN_LESSON,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidLessonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_LESSON_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    /**
     * Ensures that Tutor's Pet will not be able to boot up given duplicate {@code Lesson}s.
     */
    @Test
    public void toModelType_duplicateLesson_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_LESSON_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    /**
     * Ensures that Tutor's Pet will not be able to boot up when there exists null {@code Lesson}s.
     */
    @Test
    public void toModelType_nullLesson_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_LESSON_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidParticipationValue_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_PARTICIPATION_VALUE_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, Attendance.MESSAGE_CONSTRAINTS, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullParticipationValue_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_PARTICIPATION_VALUE_FILE,
                JsonSerializableTutorsPet.class).get();
        String expectedMessage = String.format(JsonAdaptedStudentAttendance.MISSING_FIELD_MESSAGE_FORMAT,
                Attendance.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidStudentUuidInRecord_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_STUDENT_UUID_IN_STUDENT_ATTENDANCE_FILE,
                JsonSerializableTutorsPet.class).get();
        String expectedMessage = String.format(JsonAdaptedStudentAttendance.MISSING_FIELD_MESSAGE_FORMAT,
                JsonAdaptedStudent.STUDENT_UUID_FIELD);
        assertThrows(IllegalValueException.class, JsonAdaptedUuid.MESSAGE_INVALID_UUID, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullStudentUuidInRecord_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_STUDENT_UUID_IN_STUDENT_ATTENDANCE_FILE,
                JsonSerializableTutorsPet.class).get();
        String expectedMessage = String.format(JsonAdaptedStudentAttendance.MISSING_FIELD_MESSAGE_FORMAT,
                JsonAdaptedStudent.STUDENT_UUID_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullStudentAttendance_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_STUDENT_ATTENDANCE_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedAttendanceRecord.MESSAGE_INVALID_ATTENDANCE,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateRecord_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_RECORD_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedAttendanceRecord.MESSAGE_DUPLICATE_ATTENDANCE,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicateWeek_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(DUPLICATE_WEEK_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedAttendanceRecordList.MESSAGE_DUPLICATE_ATTENDANCE_RECORD,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_incorrectWeek_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INCORRECT_WEEK_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedAttendanceRecordList.MESSAGE_INVALID_RECORD,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_invalidWeek_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(INVALID_WEEK_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, Week.MESSAGE_CONSTRAINTS, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_missingWeek_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(MISSING_WEEK_FILE,
                JsonSerializableTutorsPet.class).get();
        String expectedMessage = String.format(INVALID_FIELD_MESSAGE_FORMAT,
                AttendanceRecordList.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullWeek_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_WEEK_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, Week.MESSAGE_CONSTRAINTS,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullRecord_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_RECORD_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class, JsonAdaptedAttendanceRecordList.MESSAGE_INVALID_RECORD,
                dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullRecordList_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_RECORD_LIST_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_MISSING_ATTENDANCE_RECORD_LIST, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_emptyRecordList_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(EMPTY_RECORD_LIST_FILE,
                JsonSerializableTutorsPet.class).get();
        assertThrows(IllegalValueException.class,
                JsonAdaptedAttendanceRecordList.MESSAGE_MISSING_ATTENDANCE_RECORD_LIST, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_nullAttendanceRecordList_throwsIllegalValueException() throws Exception {
        JsonSerializableTutorsPet dataFromFile = JsonUtil.readJsonFile(NULL_ATTENDANCE_RECORD_LIST_FILE,
                JsonSerializableTutorsPet.class).get();
        String expectedMessage = String.format(JsonAdaptedLesson.MISSING_FIELD_MESSAGE_FORMAT,
                AttendanceRecordList.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, dataFromFile::toModelType);
    }
}
