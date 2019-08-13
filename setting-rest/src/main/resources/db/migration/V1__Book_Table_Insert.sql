CREATE TABLE hoge(
     id INT NOT NULL AUTO_INCREMENT,
     name varchar(30) NOT NULL,
     PRIMARY KEY (id)
);

INSERT INTO hoge (name) VALUES ('aaa'),('bbb');

-- --------------------------------------------------------

-- 出版社
create table publisher (
  id bigint(20) not null auto_increment,
  name varchar(255),
  primary key (id)
);

-- 著者
create table author (
  id bigint(20) not null auto_increment,
  first_name varchar(255),
  last_name varchar(255),
  primary key (id)
);

-- 本
create table book (
  id bigint(20) not null auto_increment,
  title varchar(255),
  isbn varchar(255),
  publication_year varchar(4),
  pub_id bigint(20),

  foreign key (pub_id) references publisher(id) on delete cascade,

  primary key (id)
);

-- 本 * 著者 のリレーション
create table writing (
  book_id bigint(20) not null,
  author_id bigint(20) not null,

  foreign key (book_id) references book(id) on delete cascade,
  foreign key (author_id) references author(id) on delete cascade
);
