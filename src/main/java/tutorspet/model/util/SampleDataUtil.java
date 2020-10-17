package tutorspet.model.util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import tutorspet.model.ReadOnlyTutorsPet;
import tutorspet.model.TutorsPet;
import tutorspet.model.attendance.Attendance;
import tutorspet.model.attendance.AttendanceRecord;
import tutorspet.model.attendance.AttendanceRecordList;
import tutorspet.model.components.name.Name;
import tutorspet.model.components.tag.Tag;
import tutorspet.model.lesson.Day;
import tutorspet.model.lesson.Lesson;
import tutorspet.model.lesson.NumberOfOccurrences;
import tutorspet.model.lesson.Venue;
import tutorspet.model.moduleclass.ModuleClass;
import tutorspet.model.student.Email;
import tutorspet.model.student.Student;
import tutorspet.model.student.Telegram;

/**
 * Contains utility methods for populating {@code TutorsPet} with sample data.
 */
public class SampleDataUtil {

    private static final Student ALEX_YEOH = new Student(new Name("Alex Yeoh"), new Telegram("41ex_Yo"),
            new Email("alexyeoh@example.com"), getTagSet("Average"));
    private static final Student BERNICE_YU = new Student(new Name("Bernice Yu"), new Telegram("b3rnice"),
            new Email("berniceyu@example.com"), getTagSet("Good", "Experienced"));
    private static final Student CHARLOTTE_OLIVEIRO = new Student(new Name("Charlotte Oliveiro"),
            new Telegram("C_Ol1ve"), new Email("charlotte@example.com"), getTagSet("Struggling"));
    private static final Student DAVID_LI = new Student(new Name("David Li"), new Telegram("li_DAvid"),
            new Email("lidavid@example.com"), getTagSet("Weak"));
    private static final Student IRFAN_IBRAHIM = new Student(new Name("Irfan Ibrahim"), new Telegram("IIbr4hmm"),
            new Email("irfan@example.com"), getTagSet("Struggling"));
    private static final Student ROY_BALAKRISHANN = new Student(new Name("Roy Balakrishnan"), new Telegram("B_Roy"),
            new Email("royb@example.com"), getTagSet("Average"));

    private static final Attendance ATTENDANCE_SCORE_35 = new Attendance(35);
    private static final Attendance ATTENDANCE_SCORE_80 = new Attendance(80);

    private static final AttendanceRecord CS2103T_TUTORIAL_WEEK_ONE_ATTENDANCE =
            new AttendanceRecord(Map.of(ALEX_YEOH.getUuid(), ATTENDANCE_SCORE_35,
                    CHARLOTTE_OLIVEIRO.getUuid(), ATTENDANCE_SCORE_80));
    private static final AttendanceRecord CS2103T_TUTORIAL_WEEK_TWO_ATTENDANCE =
            new AttendanceRecord(Map.of(ALEX_YEOH.getUuid(), ATTENDANCE_SCORE_80));
    private static final List<AttendanceRecord> cs2103tAttendanceList =
            new ArrayList<>(Collections.nCopies(10, new AttendanceRecord()));

    static {
        cs2103tAttendanceList.set(0, CS2103T_TUTORIAL_WEEK_ONE_ATTENDANCE);
        cs2103tAttendanceList.set(1, CS2103T_TUTORIAL_WEEK_TWO_ATTENDANCE);
    }

    private static final AttendanceRecordList CS2103T_TUTORIAL_ATTENDANCE =
            new AttendanceRecordList(cs2103tAttendanceList);

    private static final Lesson CS2103T_TUTORIAL_THURSDAY_1000_1100 = new Lesson(LocalTime.of(10, 0),
            LocalTime.of(11, 0), Day.THURSDAY, new NumberOfOccurrences(10), new Venue("AS6-0211"),
            CS2103T_TUTORIAL_ATTENDANCE);
    private static final Lesson CS2100_LAB_MONDAY_1400_1500 = new Lesson(LocalTime.of(14, 0),
            LocalTime.of(15, 0), Day.MONDAY, new NumberOfOccurrences(10),
            new Venue("https://zoom/j/95317249?)pwd=Ulld2tWY3MwMkRibjQyUkdZZz09"));

    private static final ModuleClass CS2103T_TUTORIAL = new ModuleClass(new Name("CS2103T Tutorial"),
            new HashSet<>(Arrays.asList(ALEX_YEOH.getUuid(), BERNICE_YU.getUuid(), CHARLOTTE_OLIVEIRO.getUuid())),
            Arrays.asList(CS2103T_TUTORIAL_THURSDAY_1000_1100));
    private static final ModuleClass CS2100_LAB = new ModuleClass(new Name("CS2100 Lab"),
            new HashSet<>(Arrays.asList(ALEX_YEOH.getUuid(), DAVID_LI.getUuid())),
            Arrays.asList(CS2100_LAB_MONDAY_1400_1500));
    private static final ModuleClass CS2100_TUTORIAL = new ModuleClass(new Name("CS2100 Tutorial"));

    private static Student[] getSampleStudents() {
        return new Student[]{ALEX_YEOH, BERNICE_YU, CHARLOTTE_OLIVEIRO, DAVID_LI, IRFAN_IBRAHIM, ROY_BALAKRISHANN};
    }

    private static ModuleClass[] getSampleModuleClasses() {
        return new ModuleClass[]{CS2103T_TUTORIAL, CS2100_LAB, CS2100_TUTORIAL};
    }

    public static ReadOnlyTutorsPet getSampleTutorsPet() {
        TutorsPet sampleTp = new TutorsPet();

        for (Student sampleStudent : getSampleStudents()) {
            sampleTp.addStudent(sampleStudent);
        }

        for (ModuleClass moduleClass : getSampleModuleClasses()) {
            sampleTp.addModuleClass(moduleClass);
        }

        return sampleTp;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }
}
