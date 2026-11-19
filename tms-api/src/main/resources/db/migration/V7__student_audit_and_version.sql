alter table students add column last_updated timestamptz;
alter table students add column version bigint not null default 0;
