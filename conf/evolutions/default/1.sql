# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table activity (
  id                        bigint auto_increment not null,
  employee_id               varchar(255),
  machine_id                bigint,
  check_in_date             datetime,
  check_out_date            datetime,
  constraint pk_activity primary key (id))
;

create table alert (
  id                        bigint auto_increment not null,
  user_username             varchar(255),
  alert_type_id             bigint,
  treshold                  integer,
  treshold_value            integer,
  created_at                datetime,
  constraint ck_alert_treshold check (treshold in (0,1,2)),
  constraint pk_alert primary key (id))
;

create table alert_type (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  created_at                datetime,
  last_modified             datetime,
  constraint pk_alert_type primary key (id))
;

create table employee (
  id                        varchar(255) not null,
  name                      varchar(255),
  address                   varchar(255),
  phone_number              bigint,
  age                       integer,
  health_issue              integer,
  created_at                datetime,
  last_modified             datetime,
  constraint ck_employee_health_issue check (health_issue in (0,1,2)),
  constraint pk_employee primary key (id))
;

create table machine (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  date_purchased            datetime,
  last_maintainence         datetime,
  noise_level               integer,
  constraint pk_machine primary key (id))
;

create table machine_type (
  id                        bigint auto_increment not null,
  name                      varchar(255),
  description               varchar(255),
  hazardous_level           integer,
  constraint pk_machine_type primary key (id))
;

create table user (
  username                  varchar(255) not null,
  fullname                  varchar(255),
  password                  varchar(255),
  phone_number              bigint,
  created_at                datetime,
  last_modified             datetime,
  constraint pk_user primary key (username))
;


create table machine_machine_type (
  machine_id                     bigint not null,
  machine_type_id                bigint not null,
  constraint pk_machine_machine_type primary key (machine_id, machine_type_id))
;
alter table activity add constraint fk_activity_employee_1 foreign key (employee_id) references employee (id) on delete restrict on update restrict;
create index ix_activity_employee_1 on activity (employee_id);
alter table activity add constraint fk_activity_machine_2 foreign key (machine_id) references machine (id) on delete restrict on update restrict;
create index ix_activity_machine_2 on activity (machine_id);
alter table alert add constraint fk_alert_user_3 foreign key (user_username) references user (username) on delete restrict on update restrict;
create index ix_alert_user_3 on alert (user_username);
alter table alert add constraint fk_alert_alertType_4 foreign key (alert_type_id) references alert_type (id) on delete restrict on update restrict;
create index ix_alert_alertType_4 on alert (alert_type_id);



alter table machine_machine_type add constraint fk_machine_machine_type_machine_01 foreign key (machine_id) references machine (id) on delete restrict on update restrict;

alter table machine_machine_type add constraint fk_machine_machine_type_machine_type_02 foreign key (machine_type_id) references machine_type (id) on delete restrict on update restrict;

# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table activity;

drop table alert;

drop table alert_type;

drop table employee;

drop table machine;

drop table machine_machine_type;

drop table machine_type;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

