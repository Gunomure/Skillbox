create table LinkedPurchaseList (price int not null, subscription_date datetime, course_id int unsigned not null, student_id int unsigned not null, primary key (course_id, student_id));
alter table LinkedPurchaseList add constraint FKqgd9r2abdpokesiwrohktlval foreign key (course_id) references Courses (id);
alter table LinkedPurchaseList add constraint FKmaa0tauf5n44fobi9nsjbsur7 foreign key (student_id) references Students (id);


create table Teacher_Course (teacher_id int unsigned not null, course_id int unsigned not null, primary key (teacher_id, course_id));
alter table Teacher_Course add constraint FK66m94fokend1yq80i339eors0 foreign key (course_id) references Courses (id);
alter table Teacher_Course add constraint FKrtws6stph5tblxq37joe9r8yv foreign key (teacher_id) references Teachers (id);

--первичное заполнение новой таблицы
insert into Teacher_Course (teacher_id, course_id) select teacher_id, id from Courses;