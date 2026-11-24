-- V8: the API calls it maxCapacity, so the column and its check say so too
alter table courses rename column capacity to max_capacity;
alter table courses rename constraint ck_courses_capacity_positive
    to ck_courses_max_capacity_positive;