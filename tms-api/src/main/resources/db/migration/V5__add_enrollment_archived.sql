-- Enrollments can be archived in bulk (Exercise 9)
alter table enrollments add column archived boolean not null default false;