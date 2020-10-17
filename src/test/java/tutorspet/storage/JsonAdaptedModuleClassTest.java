package tutorspet.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static tutorspet.logic.commands.CommandTestUtil.VALID_PARTICIPATION_SCORE_33;
import static tutorspet.storage.JsonAdaptedModuleClass.INVALID_FIELD_MESSAGE_FORMAT;
import static tutorspet.storage.JsonAdaptedModuleClass.MISSING_FIELD_MESSAGE_FORMAT;
import static tutorspet.testutil.Assert.assertThrows;
import static tutorspet.testutil.LessonBuilder.insertAttendanceRecords;
import static tutorspet.testutil.TypicalLesson.LESSON_THU_10_TO_11;
import static tutorspet.testutil.TypicalLesson.LESSON_WED_2_TO_4;
import static tutorspet.testutil.TypicalModuleClass.CS2103T_TUTORIAL;
import static tutorspet.testutil.TypicalStudent.CARL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import tutorspet.commons.exceptions.IllegalValueException;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.components.name.Name;
import tutorspet.model.lesson.Lesson;
import tutorspet.storage.attendance.JsonAdaptedAttendanceRecordList;

public class JsonAdaptedModuleClassTest {

    private static final String INVALID_NAME_VALUE = "CS3230@Tutorial";
    private static final String INVALID_STUDENT_UUID_VALUE = "584346cb-8886-4518-8282-";
    private static final String INVALID_TIME_VALUE = "1400";

    private static final JsonAdaptedName VALID_CLASS_JSON_ADAPTED_NAME =
            new JsonAdaptedName(CS2103T_TUTORIAL.getName().toString());
    private static final List<JsonAdaptedUuid> VALID_JSON_ADAPTED_UUIDS = CS2103T_TUTORIAL.getStudentUuids().stream()
            .map(JsonAdaptedUuid::new)
            .collect(Collectors.toList());
    private static final List<JsonAdaptedLesson> VALID_JSON_ADAPTED_LESSONS = CS2103T_TUTORIAL.getLessons().stream()
            .map(JsonAdaptedLesson::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validModuleClassDetails_returnsModuleClass() throws Exception {
        JsonAdaptedModuleClass moduleClass = new JsonAdaptedModuleClass(CS2103T_TUTORIAL);
        assertEquals(CS2103T_TUTORIAL, moduleClass.toModelType());
    }

    @Test
    public void toModelType_validIndividualModuleClassDetails_returnsModuleClass() throws Exception {
        JsonAdaptedModuleClass moduleClass = new JsonAdaptedModuleClass(
                VALID_CLASS_JSON_ADAPTED_NAME, VALID_JSON_ADAPTED_UUIDS, VALID_JSON_ADAPTED_LESSONS);
        assertEquals(CS2103T_TUTORIAL, moduleClass.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedName invalidName = new JsonAdaptedName(INVALID_NAME_VALUE);
        JsonAdaptedModuleClass moduleClass =
                new JsonAdaptedModuleClass(invalidName, VALID_JSON_ADAPTED_UUIDS, VALID_JSON_ADAPTED_LESSONS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, moduleClass::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedModuleClass moduleClass =
                new JsonAdaptedModuleClass(null, VALID_JSON_ADAPTED_UUIDS, VALID_JSON_ADAPTED_LESSONS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, moduleClass::toModelType);
    }

    @Test
    public void toModelType_invalidStudentUuids_throwsIllegalValueException() {
        List<JsonAdaptedUuid> invalidStudentUuids = new ArrayList<>(VALID_JSON_ADAPTED_UUIDS);
        JsonAdaptedUuid invalidJsonAdaptedUuid = new JsonAdaptedUuid(INVALID_STUDENT_UUID_VALUE);
        invalidStudentUuids.add(invalidJsonAdaptedUuid);
        String expectedMessage = String.format(INVALID_FIELD_MESSAGE_FORMAT, JsonAdaptedStudent.STUDENT_UUID_FIELD);
        JsonAdaptedModuleClass moduleClass = new JsonAdaptedModuleClass(VALID_CLASS_JSON_ADAPTED_NAME,
                invalidStudentUuids,
                VALID_JSON_ADAPTED_LESSONS);
        assertThrows(IllegalValueException.class, expectedMessage, moduleClass::toModelType);
    }

    @Test
    public void toModelType_nullStudentUuids_throwsIllegalValueException() {
        List<JsonAdaptedUuid> invalidStudentUuids = new ArrayList<>(VALID_JSON_ADAPTED_UUIDS);
        invalidStudentUuids.add(null);
        JsonAdaptedModuleClass moduleClass = new JsonAdaptedModuleClass(
                VALID_CLASS_JSON_ADAPTED_NAME, invalidStudentUuids, VALID_JSON_ADAPTED_LESSONS);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, JsonAdaptedStudent.STUDENT_UUID_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, moduleClass::toModelType);
    }

    @Test
    public void toModelType_invalidLessonStartTime_throwsIllegalValueException() {
        List<JsonAdaptedLesson> invalidLessons = new ArrayList<>(VALID_JSON_ADAPTED_LESSONS);
        JsonAdaptedLesson invalidJsonAdaptedLesson = new JsonAdaptedLesson(
                INVALID_TIME_VALUE,
                LESSON_WED_2_TO_4.getEndTime().toString(),
                LESSON_WED_2_TO_4.getDay().toString(),
                LESSON_WED_2_TO_4.getNumberOfOccurrences().value,
                LESSON_WED_2_TO_4.getVenue().toString(),
                new JsonAdaptedAttendanceRecordList(LESSON_WED_2_TO_4.getAttendanceRecordList())
        );
        invalidLessons.add(invalidJsonAdaptedLesson);
        JsonAdaptedModuleClass moduleClass = new JsonAdaptedModuleClass(
                VALID_CLASS_JSON_ADAPTED_NAME, VALID_JSON_ADAPTED_UUIDS, invalidLessons);
        String expectedMessage = String.format(
                JsonAdaptedLesson.INVALID_FIELD_MESSAGE_FORMAT, JsonAdaptedLesson.START_TIME_FIELD);
        assertThrows(IllegalValueException.class, expectedMessage, moduleClass::toModelType);
    }

    @Test
    public void toModelType_nullLessons_throwsIllegalValueException() {
        List<JsonAdaptedLesson> invalidLessons = new ArrayList<>(VALID_JSON_ADAPTED_LESSONS);
        invalidLessons.add(null);
        JsonAdaptedModuleClass moduleClass = new JsonAdaptedModuleClass(
                VALID_CLASS_JSON_ADAPTED_NAME, VALID_JSON_ADAPTED_UUIDS, invalidLessons);
        String expectedMessage = String.format(
                MISSING_FIELD_MESSAGE_FORMAT, Lesson.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, moduleClass::toModelType);
    }

    @Test
    public void toModelType_invalidStudentInLesson_throwsIllegalValueException() {
        AttendanceRecord attendanceRecord =
                new AttendanceRecord(Map.of(CARL.getUuid(), new Attendance(VALID_PARTICIPATION_SCORE_33)));
        Lesson lessonWithInvalidStudent = insertAttendanceRecords(LESSON_THU_10_TO_11, attendanceRecord);
        List<JsonAdaptedLesson> jsonAdaptedLessons =
                Collections.singletonList(new JsonAdaptedLesson(lessonWithInvalidStudent));
        JsonAdaptedModuleClass moduleClass = new JsonAdaptedModuleClass(
                VALID_CLASS_JSON_ADAPTED_NAME, VALID_JSON_ADAPTED_UUIDS, jsonAdaptedLessons);
        assertThrows(IllegalValueException.class, JsonAdaptedModuleClass.MESSAGE_INVALID_STUDENTS_IN_LESSON,
                moduleClass::toModelType);
    }
}
