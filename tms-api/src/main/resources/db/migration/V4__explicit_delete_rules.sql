-- A course or student that still has enrollments cannot be deleted:
-- academic records are never removed as a side effect.
alter table enrollments drop constraint fk_enrollments_course;
alter table enrollments add constraint fk_enrollments_course
    foreign key (course_id) references courses (id) on delete restrict;

-- alter table enrollments drop constraint fk_enrollemnts_student;
-- alter table enrollments add constraint fk_enrollments_student
--     foreign key (student_id) references students (id) on delete restrict;

alter table enrollments drop constraint fk_enrollments_student;
alter table enrollments add constraint fk_enrollments_student
    foreign key (student_id) references students (id) on delete restrict;