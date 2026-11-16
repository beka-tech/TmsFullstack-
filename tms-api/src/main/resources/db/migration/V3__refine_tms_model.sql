-- Natural keys are unique; values stay in their legal ranges

alter table students
 add constraint uk_students_registration_number unique (registration_number);
alter table courses
    add constraint uk_courses_code unique (code);
alter table enrollments
    add constraint uk_enrollments_student_course unique (student_id, course_id);
alter table courses
    add constraint ck_courses_capacity_positive check (capacity > 0);
alter table students
    add constraint ck_students_gpa_range check (gpa between 0 and 4);
