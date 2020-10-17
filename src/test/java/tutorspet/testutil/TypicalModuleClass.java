package tutorspet.testutil;

import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2100_LAB;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2100_TUTORIAL;
import static tutorspet.logic.commands.CommandTestUtil.VALID_NAME_CS2103T_TUTORIAL;
import static tutorspet.testutil.LessonBuilder.insertAttendanceRecords;
import static tutorspet.testutil.TypicalAttendanceRecord.RECORD_ALICE_51_BENSON_33;
import static tutorspet.testutil.TypicalAttendanceRecord.RECORD_ALICE_80;
import static tutorspet.testutil.TypicalLesson.LESSON_FRI_8_TO_10;
import static tutorspet.testutil.TypicalLesson.LESSON_THU_10_TO_11;
import static tutorspet.testutil.TypicalLesson.ONLINE_LESSON_TUE_1030_1130;
import static tutorspet.testutil.TypicalStudent.ALICE;
import static tutorspet.testutil.TypicalStudent.BENSON;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tutorspet.model.moduleclass.ModuleClass;

public class TypicalModuleClass {

    /**
     * A {@code ModuleClass} for testing with ALICE and BENSON as students and THU_10_TO_11 lesson.
     */
    public static final ModuleClass CS2103T_TUTORIAL = new ModuleClassBuilder()
            .withName(VALID_NAME_CS2103T_TUTORIAL)
            .withStudentUuids(ALICE.getUuid(), BENSON.getUuid())
            .withLessons(insertAttendanceRecords(LESSON_THU_10_TO_11, RECORD_ALICE_80, RECORD_ALICE_51_BENSON_33))
            .build();

    /**
     * A {@code ModuleClass} for testing with no students and the following lessons:
     * LESSON_FRI_8_TO_10 and ONLINE_LESSON_TUE_1030_1130.
     */
    public static final ModuleClass CS2100_LAB = new ModuleClassBuilder()
            .withName(VALID_NAME_CS2100_LAB)
            .withLessons(LESSON_FRI_8_TO_10, ONLINE_LESSON_TUE_1030_1130)
            .build();

    /**
     * A {@code ModuleClass} for testing with ALICE as student and no lessons.
     */
    public static final ModuleClass CS2100_TUTORIAL = new ModuleClassBuilder()
            .withName(VALID_NAME_CS2100_TUTORIAL).withStudentUuids(ALICE.getUuid()).build();

    private TypicalModuleClass() {} // prevents instantiation

    public static List<ModuleClass> getTypicalModuleClasses() {
        return new ArrayList<>(Arrays.asList(CS2103T_TUTORIAL, CS2100_LAB, CS2100_TUTORIAL));
    }
}
