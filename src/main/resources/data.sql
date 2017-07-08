insert into user (name, email, role) values
('U0_T0_ADMIN', 'U0_T0_ADMIN@isel.pt', 'ROLE_ADMIN'),
('U1_T1_ADMIN', 'U1_T1_ADMIN@isel.pt', 'ROLE_ADMIN'),
('U2_T2_ADMIN', 'U2_T2_ADMIN@isel.pt', 'ROLE_ADMIN'),
('U3_T3_USER', 'U3_T3_USER@isel.pt', 'ROLE_USER'),
('U4_T4_USER', 'U4_T4_USER@isel.pt', 'ROLE_USER'),
('U5_T5_USER', 'U5_T5_USER@isel.pt', 'ROLE_USER'),
('U6_T6_USER', 'U6_T6_USER@isel.pt', 'ROLE_USER'),
('U7_T7_USER', 'U7_T7_USER@isel.pt', 'ROLE_USER'),
('U8_T8_USER', 'U8_T8_USER@isel.pt', 'ROLE_USER'),
('U9_T9_USER', 'U9_T9_USER@isel.pt', 'ROLE_USER'),
('U0_S0_USER', 'U0_S0_USER@isel.pt', 'ROLE_USER'),
('U1_S1_USER', 'U1_S1_USER@isel.pt', 'ROLE_USER'),
('U2_S2_USER', 'U2_S2_USER@isel.pt', 'ROLE_USER'),
('U3_S3_USER', 'U3_S3_USER@isel.pt', 'ROLE_USER'),
('U4_S4_USER', 'U4_S4_USER@isel.pt', 'ROLE_USER'),
('U5_S5_USER', 'U5_S5_USER@isel.pt', 'ROLE_USER'),
('U6_S6_USER', 'U6_S6_USER@isel.pt', 'ROLE_USER'),
('U7_S7_USER', 'U7_S7_USER@isel.pt', 'ROLE_USER'),
('U8_S8_USER', 'U8_S8_USER@isel.pt', 'ROLE_USER'),
('U9_S9_USER', 'U9_S9_USER@isel.pt', 'ROLE_USER');

insert into teacher (user_id, teacher_id) values
(0, 'T00001'),
(1, 'T00002'),
(2, 'T00003'),
(3, 'T00004'),
(4, 'T00005'),
(5, 'T00006'),
(6, 'T00007'),
(7, 'T00008'),
(8, 'T00009'),
(9, 'T00010');

insert into student (user_id, student_id) values
(10, 'A00001'),
(11, 'A00002'),
(12, 'A00003'),
(13, 'A00004'),
(14, 'A00005'),
(15, 'A00006'),
(16, 'A00007'),
(17, 'A00008'),
(18, 'A00009'),
(19, 'A00010');

insert into credentials (user_id, openid, username, password, enabled) values
(0, '116110254597972482396', 'u0_t0_admin', 'u0_t0_admin', 1),
(1, '1', 'u1_t1_admin', 'u1_t1_admin', 1),
(2, '2', 'u2_t2_admin', 'u2_t2_admin', 1),
(3, '3', 'u3_t3_user', 'u3_t3_user', 1),
(4, '4', 'u4_t4_user', 'u4_t4_user', 1),
(5, '5', 'u5_t5_user', 'u5_t5_user', 1),
(6, '6', 'u6_t6_user', 'u6_t6_user', 1),
(7, '7', 'u7_t7_user', 'u7_t7_user', 1),
(8, '8', 'u8_t8_user', 'u8_t8_user', 1),
(9, '9', 'u9_t9_user', 'u9_t9_user', 1),
(10, '10', 'u0_s0_user', 'u0_s0_user', 1),
(11, '11', 'u1_s1_user', 'u1_s1_user', 1),
(12, '12', 'u2_s2_user', 'u2_s2_user', 1),
(13, '13', 'u3_s3_user', 'u3_s3_user', 1),
(14, '14', 'u4_s4_user', 'u4_s4_user', 1),
(15, '15', 'u5_s5_user', 'u5_s5_user', 1),
(16, '16', 'u6_s6_user', 'u6_s6_user', 1),
(17, '17', 'u7_s7_user', 'u7_s7_user', 1),
(18, '18', 'u8_s8_user', 'u8_s8_user', 1),
(19, '19', 'u9_s9_user', 'u9_s9_user', 1);

insert into course (name, acronym, coordinator_id, active) values
('Software Laboratory' , 'LS', 'T00004', true),
('Desenvolvimento de Aplicações Web' , 'DAW', 'T00005', true),
('Programacao III' , 'PRG', 'T00006', true);

insert into semester (semester_id) values
('1516i'),
('1516v'),
('1617i'),
('1617v');

insert into class (class_id, identifier, course_acronym, semester_id, auto_enrollment, max_students_group, active) values
('LS/1617i/D1', 'D1', 'LS', '1617i' , false, 3, true),
('PRG/1617i/N1', 'N1', 'PRG', '1617i' , false, 2, true),
('LS/1617v/N2', 'N2', 'LS', '1617v' , true, 2, true),
('DAW/1617v/N2', 'N2', 'DAW', '1617v' , false, 3, true),
('PRG/1617v/N1', 'N1', 'PRG', '1617v' , true, 2, true);

insert into classes_teachers (class_id, teacher_id) values
('LS/1617i/D1', 'T00001'),
('LS/1617i/D1', 'T00006'),
('PRG/1617i/N1', 'T00002'),
('PRG/1617i/N1', 'T00007'),
('LS/1617v/N2', 'T00003'),
('LS/1617v/N2', 'T00008'),
('DAW/1617v/N2', 'T00004'),
('DAW/1617v/N2', 'T00009'),
('PRG/1617v/N1', 'T00005'),
('PRG/1617v/N1', 'T00010');

insert into classes_students (class_id, student_id) values
('LS/1617i/D1', 'A00001'),
('LS/1617i/D1', 'A00002'),
('LS/1617i/D1', 'A00003'),
('LS/1617i/D1', 'A00004'),
('LS/1617i/D1', 'A00005'),
('PRG/1617i/N1', 'A00001'),
('PRG/1617i/N1', 'A00002'),
('PRG/1617i/N1', 'A00003'),
('PRG/1617i/N1', 'A00004'),
('LS/1617v/N2', 'A00004'),
('LS/1617v/N2', 'A00005'),
('LS/1617v/N2', 'A00007'),
('LS/1617v/N2', 'A00008'),
('LS/1617v/N2', 'A00009'),
('LS/1617v/N2', 'A00010'),
('DAW/1617v/N2', 'A00001'),
('DAW/1617v/N2', 'A00004'),
('DAW/1617v/N2', 'A00006'),
('DAW/1617v/N2', 'A00008'),
('DAW/1617v/N2', 'A00010'),
('PRG/1617v/N1', 'A00003'),
('PRG/1617v/N1', 'A00004'),
('PRG/1617v/N1', 'A00006'),
('PRG/1617v/N1', 'A00007'),
('PRG/1617v/N1', 'A00008');

insert into groups (class_id, group_id, student_id) values 
('LS/1617i/D1', 5, 'A00001'),
('LS/1617i/D1', 5, 'A00002'),
('LS/1617i/D1', 11, 'A00003'),
('LS/1617i/D1', 11, 'A00004'),
('PRG/1617i/N1', 2, 'A00001'),
('PRG/1617i/N1', 3, 'A00002'),
('PRG/1617i/N1', 5, 'A00003'),
('PRG/1617i/N1', 7, 'A00004'),
('LS/1617v/N2', 5, 'A00004'),
('LS/1617v/N2', 5, 'A00005'),
('LS/1617v/N2', 6, 'A00007'),
('LS/1617v/N2', 6, 'A00008');