-- パスワードはいずれもpassword
insert into users (username,password,enabled) values
('root','$2a$10$UZvhb9hCAKHUuYVW7qBbV.phVLA8LONl3AZLo7eeReWduopd.Zrx2',true),
('user','$2a$10$UZvhb9hCAKHUuYVW7qBbV.phVLA8LONl3AZLo7eeReWduopd.Zrx2',true),
('guest','$2a$10$UZvhb9hCAKHUuYVW7qBbV.phVLA8LONl3AZLo7eeReWduopd.Zrx2',true);

insert into authorities (username,authority) values
('root','ADMIN'),
('user','USER'),
('guest','GUEST');

create table hoge(
  username varchar_ignorecase(255) not null primary key,
  password varchar_ignorecase(255) not null,
  enabled boolean not null
);
