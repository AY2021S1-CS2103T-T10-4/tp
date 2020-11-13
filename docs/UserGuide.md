---
layout: page
style: guide
title: User Guide
---
![Logo](images/Logo.png)

# Tutor's Pet - User Guide

<p style="page-break-before: always"/>

<div class="toc">

<h2>Content Page</h2>

1. Table of Contents
{:toc}

</div>

<p style="page-break-before: always"/>

## Introduction

Tutor's Pet is a **student management application** for teaching assistants in NUS Computing.
Tutor's Pet reduces the amount of time you spend on administrative tasks as a teaching assistant.
It is a one stop solution to keep track of your classes and students.

As a teaching assistant, even with spreadsheets, administrative chores such as attendance taking and grading students' participation
can be troublesome. This is why we have created Tutor's Pet, _a
solution for teaching assistants, by students._

## About

This user guide provides documentation on the installation and usage of Tutor's Pet.
It also provides a comprehensive description of features available to you and
includes a [quick-start](#quick-start) section that helps you get started.

This guide uses the following features to make it easier for you to navigate around:

* Words that look like [this](#about) can be clicked to jump to the related section.
* Words that look like `this` refer to keywords used as part of commands or responses from your Tutor's Pet.
* Words that look like <kbd>this</kbd> refer to keyboard keys that you can press.

<div markdown="block" class="alert alert-info">

:information_source: Boxes with the :information_source: icon contain additional useful information.

</div>

<div markdown="block" class="alert alert-primary">

:bulb: Boxes with the :bulb: icon contain additional tips and tricks to help you get the most out of your Tutor's Pet.

</div>

<div markdown="block" class="alert alert-warning">

:warning: Boxes with the :warning: icon contain important information on how to keep your Tutor's Pet data safe.

</div>

--------------------------------------------------------------------------------------------------------------------

<p style="page-break-before: always"/>

## Quick Start
This section guides you to start using Tutor's Pet quickly.

### Installing Tutor's Pet

Here are a few steps to get you started on Tutor's Pet:

1. Ensure you have **Java 11** or above installed in your computer.

1. Download the latest version of Tutor's Pet [here](https://github.com/AY2021S1-CS2103T-T10-4/tp/releases).

1. Copy the downloaded Tutor's Pet to a folder of your choice. This is known as the home folder of your Tutor's Pet.

1. Double-click the file to start the application. An application similar to the one below should appear in a few
   seconds.<br>

   ![Ui](images/Ui.png)

### Using Tutor's Pet

This section offers an overview of the layout in Tutor's Pet.

There are three main areas in Tutor's Pet:

1. the command box and result display box,

1. the main viewing area,

1. the utility area.

#### Command box and result display box

The command box is the area for you to enter your commands. The result display box shows the result of each command. It is located immediately below the command box.

![Command box and result display box](images/ugimages/CommandAndDisplayBox.png)

<br/>

Let us test out some commands to get a feel of the command box and result display box.
To execute a command, type it into the command box and press <kbd>Enter</kbd>.
For example, type **`help`** and press <kbd>Enter</kbd> to open the help window.<br>

   Here are other commands you can try:

   * **`list`** : Lists all students and classes.

   * **`add-student`**`n\John Doe t\johndoe e\johnd@example.com tag\student` : Adds a student named `John Doe`
   to the application.

   * **`delete-student`**`3` : Deletes the 3rd student shown in the current [displayed student list](#displayed-student-list).

   * **`clear-student`** : Deletes all students.

   * **`exit`** : Exits the application.

* Refer to the [Commands](#commands) section below for details of each command.

#### Main viewing area

The main viewing area consists of two sections: **Classes** and **Students**.

<a name="displayed-class-list"></a>

* The **Classes** section contains information on classes and lessons in Tutor's Pet. Use the **`list-class`** command to view the list of all
classes in Tutor's Pet.

  ![Class_section](images/ugimages/ClassPanel.png)

<br/>

<a name="displayed-student-list"></a>

* The **Students** section contains information on students in Tutor's Pet. Use the **`list-student`** command to view the list of all students in Tutor's Pet.

  ![Student_section](images/ugimages/StudentPanel.png)

<br/>

#### Utility area

The utility area consists of three tabs: **File**, **Theme** and **Help**.

  ![Utility area](images/ugimages/UtilityArea.png)

<br/>

* The **File** tab consists of an exit button. To exit Tutor's Pet, click on the exit button. Alternatively, use the `exit` command to exit the application.

* The **Theme** tab consist of 3 different themes: **Light**, **Alternate** and **Dark**. To change the theme of
  your Tutor's Pet, click on one of these buttons.

  ![Theme tab](images/ugimages/ThemeTab.png)

<br/>

* The **Help** tab consists of a help button. If you require any assistance, click on the help button, copy the link
 displayed and paste it into any web browser. Alternatively, press <kbd>F1</kbd> to bring up the help window.

--------------------------------------------------------------------------------------------------------------------

<p style="page-break-before: always"/>

## Commands

Commands are the main way you interact with your Tutor's Pet.

This section provides information about the [command format](#command-format), [overview](#command-overview) of available commands,
and detailed instructions on how to use each command.

### Command Format

<div markdown="block" class="alert alert-info">

This section provides details about the format of commands used in Tutor's Pet.

**:information_source: Notes about the command format:**<br>

* Words in `UPPER_CASE` are the parameters to be supplied by the user.<br>
  e.g. In `add-student n\NAME`, `NAME` is a parameter which can be used as `add-student n\John Doe`.

* Items in square brackets are optional.<br>
  e.g `n\NAME [tag\TAG]` can be used as `n\John Doe tag\student` or as `n\John Doe`.

* Items with `…` after them can be used zero or more times.<br>
  e.g. `[tag\TAG]…` can be used as ` ` (i.e. 0 times), `tag\student`, `tag\average tag\TA candidate` etc.

* Parameters can be in any order.<br>
  e.g. If the command specifies `n\NAME t\TELEGRAM_USERNAME`, typing `t\TELEGRAM_USERNAME n\NAME` is also acceptable.

* Indexes **must be positive whole numbers** 1, 2, 3, …

* The `STUDENT_INDEX` refers to the index number shown in the [displayed student list](#displayed-student-list).

* The `CLASS_INDEX` refers to the index number shown in the [displayed class list](#displayed-class-list).

* The `LESSON_INDEX` refers to the index number of a lesson shown in its class.

* Time must be input in the format, `hh:mm`. <br>
  e.g. `13:00` is allowed, but not `1300`.

</div>

<p style="page-break-before: always"/>

### Command Overview

This section provides an overview of all commands in Tutor's Pet and illustrates the usage of each command in detail.

| Action                            | Format                                                                                      | Example (if applicable)                                            |
|-----------------------------------|---------------------------------------------------------------------------------------------|--------------------------------------------------------------------|
| **Add attendance record**         | `add-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK p\PARTICIPATION_SCORE`  | `add-attendance c\1 l\1 s\1 w\1 p\1`                               |
| **Add class**                     | `add-class n\CLASS_NAME`                                                                    | `add-class n\CS2103T Tutorial T10`                                 |
| **Add lesson**                    | `add-lesson c\CLASS_INDEX d\DAY st\START_TIME et\END_TIME v\VENUE r\NO_OF_TIMES`            | `add-lesson c\1 d\MONDAY st\08:00 et\10:00 v\COM1 #01-01 r\13`     |
| **Add student**                   | `add-student n\NAME t\TELEGRAM_USERNAME e\EMAIL [tag\TAG]…`                                 | `add-student n\John Doe t\johndoe e\johnd@example.com tag\student` |
| **Add student to a class**        | `link s\STUDENT_INDEX c\CLASS_INDEX`                                                        | `link s\1 c\2`                                                     |
| **Clear all classes**             | `clear-class`                                                                               |                                                                    |
| **Clear all students**            | `clear-student`                                                                             |                                                                    |
| **Delete attendance record**      | `delete-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK`                     | `delete-attendance c\1 l\1 s\1 w\1`                                |
| **Delete class**                  | `delete-class CLASS_INDEX`                                                                  | `delete-class 2`                                                   |
| **Delete lesson**                 | `delete-lesson c\CLASS_INDEX l\LESSON_INDEX`                                                | `delete-lesson c\1 l\1`                                            |
| **Delete student**                | `delete-student STUDENT_INDEX`                                                              | `delete-student 3`                                                 |
| **Display statistics**            | `stats c\CLASS_INDEX s\STUDENT_INDEX`                                                       | `stats c\1 s\1`                                                    |
| **Display venue**                 | `display-venue c\CLASS_INDEX l\LESSON_INDEX`                                                | `display-venue c\1 l\1`                                            |
| **Edit attendance record**        | `edit-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK p\PARTICIPATION_SCORE` | `edit-attendance c\1 l\1 s\1 w\1 p\10`                             |
| **Edit class**                    | `edit-class CLASS_INDEX n\CLASS_NAME`                                                       | `edit-class 1 n\CS2103T Tutorial T10`                              |
| **Edit lesson**                   | `edit-lesson c\CLASS_INDEX l\LESSON_INDEX [d\DAY] [st\START_TIME] [et\END_TIME] [v\VENUE]`  | `edit-lesson c\1 l\1 d\TUESDAY st\10:00 et\12:00 v\COM2 #02-02`    |
| **Edit student**                  | `edit-student STUDENT_INDEX [n\NAME] [t\TELEGRAM_USERNAME] [e\EMAIL] [tag\TAG]…`            | `edit-student 2 n\James Lee e\jameslee@example.com`                |
| **Exit**                          | `exit`                                                                                      |                                                                    |
| **Find attendance record**        | `find-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK`                       | `find-attendance c\1 l\1 s\1 w\1`                                  |
| **Find class**                    | `find-class KEYWORD [MORE_KEYWORDS]`                                                        | `find-class CS2103T`                                               |
| **Find student**                  | `find-student KEYWORD [MORE_KEYWORDS]`                                                      | `find-student James Jake`                                          |
| **Help**                          | `help`                                                                                      |                                                                    |
| **List all classes**              | `list-class`                                                                                |                                                                    |
| **List all students**             | `list-student`                                                                              |                                                                    |
| **List all students and classes** | `list`                                                                                      |                                                                    |
| **List students in a class**      | `list-student c\CLASS_INDEX`                                                                | `list-student c\3`                                                 |
| **Redo**                          | `redo`                                                                                      |                                                                    |
| **Remove student from a class**   | `unlink s\STUDENT_INDEX c\CLASS_INDEX`                                                      | `unlink s\1 c\2`                                                   |
| **Reset**                         | `reset`                                                                                     |                                                                    |
| **Undo**                          | `undo`                                                                                      |                                                                    |
| **View change history**           | `view-history`                                                                              |                                                                    |

The following subsections will elaborate on each Tutor's Pet command.

<p style="page-break-before: always"/>

### General

#### Viewing help : `help`

If you require any assistance, you can use this command. The link to this user guide will be displayed.

  ![Help window](images/ugimages/Help.png)

**Format**: `help`

#### Listing all students and classes : `list` - Pin Hsuan

You can use this command to see the entire list of students and classes.

**Format**: `list`

For example:
1. After finding the class CS2103T and student Alex, you wish to view all your students and classes.

1. You type in the command `list` and press <kbd>Enter</kbd>.

   ![List before](images/ugimages/ListBefore.png)

1. Now, you can see all your students and classes.

   ![List after](images/ugimages/ListAfter.png)

#### Undoing a previous change : `undo` - Dexter

If you have accidentally entered a command that permanently changes the data in your Tutor's Pet,
you can use this command to undo the change.

**Format**: `undo`

For example:

1. You want to [delete](#deleting-a-student--delete-student) Alex from your Tutor's Pet. However, you accidentally deleted Bernice instead of Alex.

1. You type `undo` and press <kbd>Enter</kbd>.

   ![Undo before](images/ugimages/UndoBefore.png)

1. Bernice has been undeleted and now you can delete Alex.

   ![Undo after](images/ugimages/UndoAfter.png)

<div markdown="block" class="alert alert-info">

:information_source: **Notes about the command:** Changes that can be undone include those from the
`add-`, `edit-`, `delete-`, `clear-`, `link`, `unlink`, and `reset` commands.

</div>

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:** To view a list of changes that can be undone, use the [`view-history`](#viewing-change-history--view-history) command.

</div>

#### Redoing an undone change : `redo` - Dexter

If you decide that you do not need to undo a change, you can use this command to redo it.

**Format** : `redo`

For example:

1. You want to [undo](#undoing-a-previous-change--undo) the last four changes in your Tutor's Pet. However, you accidentally undid the last five changes instead.

1. You type `redo` and press <kbd>Enter</kbd>.

1. The undone change will be restored.

#### Viewing change history : `view-history` - Dexter

You can use this command to see a list of changes that can be undone or redone.

**Format** : `view-history`

For example:

1. You are [marking the attendance](#adding-an-attendance-record--add-attendance) of your students but accidentally lost track of which student you were at.

1. You type `view-history` into the command box and press <kbd>Enter</kbd>.

   ![View History After](images/ugimages/ViewHistoryBefore.png)

1. The result box will display a list of changes with the latest shown at the top.

   ![View History After](images/ugimages/ViewHistoryAfter.png)

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:**
The `>` indicator shows which point of your Tutor's Pet history is currently being displayed.
You can use this feature to check if there are any changes that you can undo or redo.

The diagram below shows an example where the last change has been undone.<br/>
*Notice how the `>` indicator now points to the second change listed.*

![View History After Undo](images/ugimages/ViewHistoryAfterUndo.png)

</div>

#### Exiting the program : `exit`

You can exit Tutor's Pet by typing this command.
All changes you have made to your Tutor's Pet will be saved.

**Format**: `exit`

#### Resetting the program : `reset` - Pin Hsuan

You can use this command to reset Tutor's Pet. All existing classes and students will be deleted.

**Format**: `reset`

For example:
1. It is the start of a new semester and you want to delete all students and classes.

1. You type in the command `reset` and press <kbd>Enter</kbd>.

   ![Reset before](images/ugimages/ResetBefore.png)

1. You can see that all students and classes have been deleted.

   ![Reset after](images/ugimages/ResetAfter.png)

#### Recalling previously entered commands - Dexter

If you have to enter a similar command repeatedly, you can use this feature to type less.

When the [command box](#using-tutors-pet) is selected, press the <kbd>↑</kbd> or <kbd>↓</kbd> key to navigate around previously entered commands.

For example:

1. You are taking the attendance for one of your lessons in CS2103T Tutorial.
   You have just [recorded the attendance](#adding-an-attendance-record--add-attendance) of the first student.
   You now want to record the attendance of the second student.

1. You press the <kbd>↑</kbd> key.

1. The previous command will be displayed in the command box.

1. Now, you can edit and reuse the command to record the attendance of other students.

#### Saving the data

Tutor's Pet automatically saves your data to your home folder after any changes are made.
There is no need for you to save manually.

### Managing Students

#### Adding a student : `add-student` - Rui Xuan

If you would like to add a student to Tutor's Pet, use this command.

**Format**: `add-student n\NAME t\TELEGRAM_USERNAME e\EMAIL [tag\TAG]…​`
  * Adds a student with the specified `NAME` + `TELEGRAM_USERNAME` + `EMAIL` + `TAG`.

For example:
1. You would like to add a new student, `Joel Lee` to Tutor's Pet.
   Type in `add-student n\Joel Lee t\joeleee e\jlee@example.com` and press <kbd>Enter</kbd>.

    ![AddStudentBefore](images/ugimages/AddStudentBefore.png)

1. Scrolling down the list of students, you will see that `Joel Lee` has been added to Tutor's Pet.

    ![AddStudentAfter](images/ugimages/AddStudentAfter.png)

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:** A student can have any number of tags (including 0).

</div>

#### Listing all students : `list-student`

If you would like to view all students you teach, use this command.

**Format**: `list-student`

#### Editing a student : `edit-student` - Rui Xuan

If you would like to change a student's particulars, use this command.

**Format**: `edit-student STUDENT_INDEX [n\NAME] [t\TELEGRAM_USERNAME] [e\EMAIL] [tag\TAG]…​`
  * Edits the student at the specified `STUDENT_INDEX`.

For example:
1. `Charlotte Oliveiro` gave you the wrong email address on the first day of class. You need to replace her email address.

1. You type `edit-student 3 e\charlotteolive@example.com` and press <kbd>Enter</kbd>.

    ![EditStudentBefore](images/ugimages/EditStudentBefore.png)

1. Scrolling down the list of students, you can see that `Charlotte`'s email has changed to `charlotteolive@example.com`.

    ![EditStudentAfter](images/ugimages/EditStudentAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

* You must provide at least one of the optional fields.

* When editing tags, the existing tags of the student will be removed and replaced by the newly typed in tags.

* You can remove all the tags of a student by typing `tag\` without specifying any tags after it.

</div>

#### Finding a student by name : `find-student` - Rui Xuan

If you would like to search for a student by name, use this command.

**Format**: `find-student KEYWORD [MORE_KEYWORDS]`

For example:
1. `Roy` requested for supplementary notes in class today.
You want to email him these notes.

1. You type `find-student roy` and press <kbd>Enter</kbd>.

    ![FindStudentBefore](images/ugimages/FindStudentBefore.png)

1. You can now retrieve `Roy`'s email and send him the notes.

    ![FindStudentAfter](images/ugimages/FindStudentAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

* The search is case-insensitive.<br/>
  e.g You will receive `Roy` if you type in `roy`.

* The order of the keywords does not matter.<br/>
  e.g. You will receive `Alex Yeoh` if you type in `Yeoh Alex`.

* Only the name is searched.

* Only full words will be matched.<br/>
  e.g. You will not receive `Irfan` if you only type in `Irfa`.

* Students matching at least one keyword will be returned.<br/>
  e.g. You will receive both `Alex Yeoh` and `David Li` if you type in `Alex Li`.

</div>

#### Deleting a student : `delete-student` - Rui Xuan

If you would like to delete a student, you can make use of this command.

**Format**: `delete-student STUDENT_INDEX`
  * Deletes the student at the specified `STUDENT_INDEX`.

For example:
1. `Bernice`, the 2nd student displayed in Tutor's Pet, has swapped `CS2103T Tutorial` slots and you no longer teach her.

1. You want to delete her entry from Tutor's Pet.

1. You type in `delete-student 2` and press <kbd>Enter</kbd>.

    ![DeleteStudentBefore](images/ugimages/DeleteStudentBefore.png)

1. Notice that the number of students in `CS2103T Tutorial` decreases to 2.

    ![DeleteStudentAfter](images/ugimages/DeleteStudentAfter.png)

#### Clearing all students : `clear-student`

If you would like to delete all students in the application, you can use this command.

**Format**: `clear-student`

### Managing Classes

#### Adding a class : `add-class` - Dexter

If you would like to add a new class, you can use this command.

**Format**: `add-class n\CLASS_NAME`
* Adds a class with the specified `CLASS_NAME`.

For example:
1. You have just been assigned to teach a tutorial class, Tutorial 3 of module CS1231.
   Hence, you decide to add this new tutorial class to your Tutor's Pet.

1. You type `add-class n\CS1231 Tutorial 3` and press <kbd>Enter</kbd>.

   ![AddClassBefore](images/ugimages/AddClassBefore.png)

1. The class has been added, and you can see it in the displayed class list.

   ![AddClassAfter](images/ugimages/AddClassAfter.png)

<div markdown="block" class="alert alert-info">

:information_source: **Notes about the command:**

* The class name can only contain alphabets, numbers and spaces.

* The class name cannot be the same as an existing class in your Tutor's Pet.

</div>

#### Listing all classes : `list-class`

If you would like to see a list of all classes in your Tutor's Pet, make use of this command.

**Format**: `list-class`

#### Listing all students within a class : `list-student` - Pin Hsuan

If you would like to see a list of all students in a particular class, make use of this command.

**Format**: `list-student c\CLASS_INDEX`
* Lists the students in the class at the specified `CLASS_INDEX`.

For example:
1. You want to send an email to students in your `CS2100 Lab` and hence would want to see a list of students in this class.

1. You type in the command `list-student c\2` and press <kbd>Enter</kbd>.

   ![List students in class before](images/ugimages/ListStudentsInClassBefore.png)

1. You can see that the 2 students in your `CS2100 Lab` have been listed.

   ![List students in class after](images/ugimages/ListStudentsInClassAfter.png)

#### Editing a class : `edit-class` - Samantha

If you entered the wrong class name or would like to change the name of a class, you can make use of this command.

**Format**: `edit-class CLASS_INDEX n\CLASS_NAME`
* Edits the class at the specified `CLASS_INDEX`.

For example:
1. Your module coordinator decides to allocate another class to you and you want to be able to distinguish between
   the two classes. Hence you decide to edit the `CS2103T Tutorial` class to `CS2103T Tutorial T10`.

1. You type the command `edit-class 1 n\CS2103T Tutorial T10` and press <kbd>Enter</kbd>.

   ![Edit class before](images/ugimages/EditClassBefore.png)

1. The class has been renamed from `CS2103T Tutorial` to `CS2103T Tutorial 10`.

   ![Edit class after](images/ugimages/EditClassAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

* A new class name must be provided.

</div>

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:** Make use of the  [`find-class`](#finding-a-class-by-name--find-class) command to filter the displayed class list to your
desired class. You can now type in index 1 directly to edit the name of that class.

</div>

#### Finding a class by name : `find-class`

If you would like to find classes that contain any of the given keywords in their name, you can make use of this
command.

**Format**: `find-class KEYWORD [MORE_KEYWORDS]`

For example:
1. You would like to find out how many `Tutorial` classes you are currently teaching. You type the command
   `find-class Tutorial` and press <kbd>Enter</kbd>.

   ![Find class before](images/ugimages/FindClassBefore.png)

1. You find out that you are only teaching 2 tutorials this semester and hence decide to accept more classes.

   ![Find class after](images/ugimages/FindClassAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

* The search is case-insensitive.<br />
  e.g `cs2103t` will match `CS2103T`.

* The order of the keywords does not matter.<br />
  e.g. `lab CS2100` will match `CS2100 lab`.

* Only the name is searched.

* Only full words will be matched.<br />
  e.g. `CS` will not match `CS2100`.

* Classes matching at least one keyword will be returned.<br />
  e.g. `CS2103T lab` will return both `CS2103T tut` and `CS2100 lab`.

</div>

#### Deleting a class : `delete-class` - Dexter

If you would like to delete a class, you can use this command.

**Format**: `delete-class INDEX`
* Deletes the class at the specified `INDEX`.

For example:
1. You have just received news that one of your classes, CS2100 Lab, has been permanently removed due to low enrollment.
   Hence, you want to delete the class from your Tutor's Pet.

1. You type `delete-class 2` and press <kbd>Enter</kbd>.

   ![DeleteClassBefore](images/ugimages/DeleteClassBefore.png)

1. The class has been successfully deleted.

   ![DeleteClassAfter](images/ugimages/DeleteClassAfter.png)

#### Clearing all classes : `clear-class`

If you would like to delete all the classes in Tutor's Pet, you can make use of this command.

**Format**: `clear-class`

For example:
1. It is the start of a new semester. You would like to delete all information of all the classes you taught last
   semester. You type the command `clear-class` and press <kbd>Enter</kbd>.

   ![Clear class before](images/ugimages/ClearClassBefore.png)

1. All classes have been cleared. You can start managing your new classes.

   ![Clear class after](images/ugimages/ClearClassAfter.png)

#### Adding a student to a class : `link` - Dexter

You can add students into their classes using this command.

**Format**: `link s\STUDENT_INDEX c\CLASS_INDEX`
* Adds the student at the specified `STUDENT_INDEX` to the class at the specified `CLASS_INDEX`.

For example:
1. You have a new student, David Li, who has transferred into one of your classes, CS2103T Tutorial.
   You have already [added](#adding-a-student--add-student) him to your Tutor's Pet, and want
   to add him to the class.

1. You type `link s\4 c\1` and press <kbd>Enter</kbd>.

   ![Link before](images/ugimages/LinkBefore.png)

1. Your Tutor's Pet displays the students in CS2103T Tutorial to confirm that you have successfully added
   David to the class.

   ![Link after](images/ugimages/LinkAfter.png)

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:** When adding multiple students to a class, you can press the <kbd>↑</kbd> key to retrieve previously
entered commands. This reduces the amount you need to type.

</div>

#### Removing a student from a class : `unlink` - Dexter

You can remove students from their classes using this command.

**Format**: `unlink s\STUDENT_INDEX c\CLASS_INDEX`
* Removes the student at the specified `STUDENT_INDEX` from the class at the specified `CLASS_INDEX`.

For example:
1. One of your students, Alex Yeoh, has transferred out of your class, CS2103T Tutorial, and you decide to remove him from the class.

1. You type `unlink s\1 c\1` and press <kbd>Enter</kbd>.

   ![Unlink before](images/ugimages/UnlinkBefore.png)

1. Your Tutor's Pet displays the remaining students in CS2103T Tutorial to confirm that you have successfully removed
   Alex from the class.

  ![Unlink after](images/ugimages/UnlinkAfter.png)

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:** Make use of the [`list-student`](#listing-all-students-within-a-class--list-student) command to display students
of a particular class. Now it is easier for you to find the student to remove.

</div>

<div markdown="block" class="alert alert-warning">

:warning: **Caution:** The student's attendance in the class will be deleted.

</div>

### Managing Lessons

#### Adding a lesson : `add-lesson` - Jun Long

If you would like to add a lesson to a class, you can make use of this command.

**Format**: `add-lesson c\CLASS_INDEX d\DAY st\START_TIME et\END_TIME v\VENUE r\NO_OF_TIMES`
* Adds a lesson at a specified `CLASS_INDEX`+ `DAY` + `START_TIME` + `END_TIME` + `VENUE` + `NO_OF_TIMES`.

For example:
1. You receive news that you will be teaching `CS2103T Tutorial` on `Tuesday 10:00-11:00` at `COM2 #02-02` from week 1 to 13.

1. You type in the command `add-lesson c\1 d\TUESDAY st\10:00 et\11:00 v\COM2 #02-02 r\13` and press <kbd>Enter</kbd>.

   ![Add lesson before](images/ugimages/AddLessonBefore.png)

1. You have added a lesson to teach.

   ![Add lesson after](images/ugimages/AddLessonAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

  * You must type out the day in full.

  * You are not allowed to add overnight lessons. For example, a lesson cannot start at `23:59` and end at `00:01`.

  * The `Number of Weeks` displayed refers to the total number of weeks you will be teaching the lesson.
    It does not follow the week numbers of a semester.
    For example, if a semester has 13 weeks and your lessons begin in week 3 and end in week 13,
    you should type in `11` for `NO_OF_TIMES`, i.e `add-lesson c\1 d\TUESDAY st\10:00 et\11:00 v\COM2 #02-02 r\11`.

</div>

#### Editing a lesson : `edit-lesson` - Rui Xuan

If you entered wrong details pertaining to your lesson and would like to change it, you can make use of this command.

**Format**: `edit-lesson c\CLASS_INDEX l\LESSON_INDEX [d\DAY] [st\START_TIME] [et\END_TIME] [v\VENUE]`
  * Edits the lesson at the specified `CLASS_INDEX` + `LESSON_INDEX`.

For example:
1. You made an error while entering the `CS2103T Tutorial` lesson. It should be `Tuesday` and not `Thursday`.

1. You type the command `edit-lesson c\1 l\1 d\Tuesday` and press <kbd>Enter</kbd>.

    ![Edit lesson before](images/ugimages/EditLessonBefore.png)

1. The day of the lesson has been changed to `Tuesday`.

    ![Edit lesson after](images/ugimages/EditLessonAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

* You must type out the day in full.

* You are not allowed to add overnight lessons. For example, a lesson cannot start at `23:59` and end at `00:01`.

* You must specify at least one field (DAY, START_TIME, END_TIME, VENUE) to be changed.

* You are not able to change the `Number of Weeks`.

</div>

#### Deleting a lesson : `delete-lesson` - Samantha

If you would like to delete a lesson in a class, you can make use of this command.

**Format**: `delete-lesson c\CLASS_INDEX l\LESSON_INDEX`
* Deletes the lesson at the specified `CLASS_INDEX` + `LESSON_INDEX`.

For example:
1. You receive news that the `Tuesday 10:00-11:00` `CS2103T Tutorial` will be cancelled permanently. You decide to
   remove this lesson from your Tutor's Pet.

1. You type in the command `delete-lesson c\1 l\2` and press
   <kbd>Enter</kbd>.

   ![Delete lesson before](images/ugimages/DeleteLessonBefore.png)

1. You have one less lesson to teach now.

   ![Delete lesson after](images/ugimages/DeleteLessonAfter.png)

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:** If you accidentally delete the wrong lesson, you can retrieve it back immediately using
the `undo` command. However, you cannot `undo` once you exit the application!

</div>

#### Displaying lesson venue : `display-venue` - Samantha

If you would like to find out the venue for a lesson, you can make use of this command.

**Format**: `display-venue c\CLASS_INDEX l\LESSON_INDEX`
* Displays the venue of the lesson at the specified `CLASS_INDEX` + `LESSON_INDEX`.

For example:
1. Your online lesson is starting in 10 minutes and it is time to start the session. You wish to find out the link
   to that session.

1. You type the command `display-venue c\2 l\1` and press <kbd>Enter</kbd>.

   ![Display venue before](images/ugimages/DisplayVenueBefore.png)

1. You can now proceed to copy the link to that zoom session from the command result box.

   ![Display venue after](images/ugimages/DisplayVenueAfter.png)

### Managing Attendance Records

#### Adding an attendance record : `add-attendance` - Samantha

If you would like to record a new attendance for a student, you can make use of this command.

**Format**: `add-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK p\PARTICIPATION_SCORE`

* Adds the attendance of the student at the specified `STUDENT_INDEX` to the specified `CLASS_INDEX` + `LESSON_INDEX`.

For example:
1. Your `CS2103T Tutorial` lesson in week 10 just ended and you would like to give participation scores to the
   students. You enter the command `list-student c\1` to see the students in your `CS2103T Tutorial` class.

1. You decide to start with Alex. Hence, you type in the command `add-attendance c\1 l\1 s\1 w\10 p\80` and
   press <kbd>Enter</kbd>.

   ![Add attendance before](images/ugimages/AddAttendanceBefore.png)

1. You have successfully recorded Alex's attendance.

   ![Add attendance after](images/ugimages/AddAttendanceAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

  * The `WEEK` **must be a positive whole number** 1, 2, 3, …

  * The `PARTICIPATION_SCORE` **must be a non-negative whole number** 0, 1, 2, …

</div>

<div markdown="block" class="alert alert-primary">

:bulb: **Tip:** If you are planning to add multiple attendances consecutively, click on the command
box and make use of the recall function by pressing <kbd>↑</kbd> key. This recall function automatically
inputs your most recent command into the command box, allowing you to type less!

</div>

#### Editing an attendance record : `edit-attendance` - Pin Hsuan

You can use this command to edit the attendance and participation score of a student.

**Format**: `edit-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK p\PARTICIPATION_SCORE`
* Edits the attendance record at the specified `CLASS_INDEX` + `LESSON_INDEX` + `STUDENT_INDEX` + `WEEK`.

For example:
1. Alex is a student in your CS2103T class. After your lesson in week 1, you wish to give him a participation score of 45.
   However, you accidentally gave him 35 instead.

1. You type in the command `edit-attendance c\1 l\1 s\1 w\1 p\45` and press <kbd>Enter</kbd>.

   ![Edit attendance before](images/ugimages/EditAttendanceBefore.png)

1. Now, you have successfully edited Alex's participation score from 35 to 45 points.

   ![Edit attendance after](images/ugimages/EditAttendanceAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

  * The `WEEK` **must be a positive whole number** 1, 2, 3, …

  * The `PARTICIPATION_SCORE` **must be a non-negative whole number** 0, 1, 2, …

</div>

#### Finding an attendance record: `find-attendance`

If you need to find out a student's attendance for a specific week, you can make use of this command.

**Format**: `find-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK`
* Finds the attendance record at the specified `CLASS_INDEX` + `LESSON_INDEX` + `STUDENT_INDEX` + `WEEK`.

For example:
1. It is the end of the semester and you wish to submit your students' attendance.

1. You type the command `list-student c\1` to see the students in your `CS2103T Tutorial` class.

1. Next, you would like to find out if Alex attended the lesson in week 1.

1. You type in `find-attendance c\1 l\1 s\1 w\1` and press <kbd>Enter</kbd>.

   ![Find attendance before](images/ugimages/FindAttendanceBefore.png)

1. Now, you can find out if Alex attended the first lesson and his participation score in week 1.

   ![Find attendance after](images/ugimages/FindAttendanceAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

  * The `WEEK` **must be a positive whole number** 1, 2, 3, …

</div>

#### Deleting an attendance record : `delete-attendance` - Jun Long

If you would like to delete a specific student's attendance from a lesson, you can make use of this command.

**Format**: `delete-attendance c\CLASS_INDEX l\LESSON_INDEX s\STUDENT_INDEX w\WEEK`
* Deletes the attendance record at the specified `CLASS_INDEX` + `LESSON_INDEX` + `STUDENT_INDEX` + `WEEK`.

For example:
1. You would like to delete Alex's attendance for the Thursday 10:00AM to 11:00AM lesson of CS2103T Tutorial
   in Week 1.

1. You type in the command `delete-attendance c\1 l\1 s\1 w\1` and press <kbd>Enter</kbd>.

   ![Delete attendance before](images/ugimages/DeleteAttendanceBefore.png)

1. Alex's attendance for week 1 has been deleted.

   ![Delete attendance after](images/ugimages/DeleteAttendanceAfter.png)

#### Displaying attendance statistics : `stats` - Jun Long

If you would like to find out how your student is performing in class, you can make use of this command.

**Format**: `stats c\CLASS_INDEX s\STUDENT_INDEX`
* Gives a summary of the student's attendance at the specified `CLASS_INDEX` + `STUDENT_INDEX`.

For example:
1. It is the end of the semester and time for you to grade your students' overall class participation.

1. You type in the command `list-student c\1` to see the students in your `CS2103T Tutorial` class.

1. Next, you would like to find out if Alex has been participating actively throughout the semester.

1. You type in the command `stats c\1 s\1` and press <kbd>Enter</kbd>.

   ![Display_stats before](images/ugimages/DisplayStatsBefore.png)

1. Now, you can see his average participation score and overall attendance to give a corresponding grade.

   ![Display_stats after](images/ugimages/DisplayStatsAfter.png)

<div markdown="block" class="alert alert-info">

**:information_source: Notes about the command:**<br>

  * `Lesson(s) not attended` displays all lessons that the student did not attend. This means that future lessons
    are also displayed.

</div>

--------------------------------------------------------------------------------------------------------------------

## FAQ

**Q**: How do I transfer my data to another computer?<br>
**A**: Download Tutor's Pet in the other computer. Replace the empty data file it creates with the file that
contains data from your previous Tutor's Pet home folder.

--------------------------------------------------------------------------------------------------------------------

## Glossary

This glossary provides definitions for the special terms used in this user guide.

**Attendance Record:** The attendance and participation score of a student in a particular week of a particular lesson.

**Case-insensitive:** The treatment of uppercase and lowercase letters in the same way.

**Class:** A group of students that attend the same lessons.

**Command:** The text typed in by the user as an instruction for Tutor's Pet.

**Index:** The number beside the corresponding entry in a list.

**Lesson:** The timeslot, venue, and number of times a lesson takes place.

**Tag:** A word or phrase that labels a student.

**Telegram:** A popular social messaging application.

**Venue:** The location or an online meeting room link where a lesson is held.

--------------------------------------------------------------------------------------------------------------------
