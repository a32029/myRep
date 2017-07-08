create table user (
  user_id int generated always as identity,
  name varchar(50) not null,
  email varchar(50) not null unique,
  role varchar(45) not null,

  constraint pk_user primary key ( user_id )
);

create table teacher (
  teacher_id varchar(6) not null,
  user_id int not null unique,

  constraint pk_teacher primary key ( teacher_id ),
  constraint fk_teacher foreign key ( user_id ) references user ( user_id )
);

create table student (
  student_id varchar(6) not null,
  user_id int not null unique,

  constraint pk_student primary key ( student_id ),
  constraint fk_student foreign key ( user_id ) references user ( user_id )
);

create table credentials (
  user_id int not null,
  username varchar(50) not null unique,
  password varchar(20) not null,
  enabled tinyint not null,

  constraint pk_credentials primary key ( user_id ),
  constraint fk_user foreign key ( user_id ) references user ( user_id )
);

create table course (
  course_id int generated always as identity,
  name varchar(100) not null unique, --e.g. "Software Laboratory"
  acronym varchar(10) not null unique, -- e.g. "LS"
  coordinator_id varchar(6) not null unique,
  active boolean not null,

  constraint pk_course primary key ( course_id ),
  constraint fk_course foreign key ( coordinator_id ) references teacher ( teacher_id )
);

create table semester (
  semester_id varchar(5),
  constraint pk_semester primary key ( semester_id )
);

create table class (
  class_id varchar(20) unique not null,
  identifier varchar(5) not null, -- D1, N1
  course_acronym varchar(10) not null,
  semester_id varchar(5) not null,
  auto_enrollment boolean not null, --(True or False)
  max_students_group int not null,
  active boolean not null,

  constraint pk_class primary key ( identifier, course_acronym, semester_id ),
  constraint fk4_class foreign key ( course_acronym ) references course ( acronym ), -- A class is associated with one course
  constraint fk3_class foreign key ( semester_id ) references semester ( semester_id ) -- A class is associated with one semester
);

create table classes_teachers ( -- A class has one or more teachers, and a teacher teaches in one or more classes on a given semester.
  class_id varchar(20) not null,
  teacher_id varchar(6) not null,

  constraint pk_classes_teachers primary key ( class_id, teacher_id ),
  constraint fk1_classes_teachers foreign key ( class_id ) references class ( class_id ),
  constraint fk2_classes_teachers foreign key ( teacher_id ) references teacher ( teacher_id )
);

create table classes_students ( -- A class has zero or more students.
  class_id varchar(20) not null,
  student_id varchar(6) not null,

  constraint pk_classes_students primary key ( class_id, student_id ),
  constraint fk1_classes_students foreign key ( class_id ) references class ( class_id ),
  constraint fk2_classes_students foreign key ( student_id ) references student ( student_id )
);

create table groups ( -- A group is a set of one or more students, formed in the context of a class.
  class_id varchar(20) not null,
  group_id int not null,
  student_id varchar(6) not null,

  constraint pk_groups primary key ( class_id, student_id ),
  constraint fk1_groups foreign key ( class_id ) references class ( class_id ),
  constraint fk2_groups foreign key ( student_id ) references student ( student_id )
);
