-- 参考
-- https://docs.spring.io/spring-security/site/docs/3.0.x/reference/appendix-schema.html

create table users(
  username varchar_ignorecase(255) not null primary key,
  password varchar_ignorecase(255) not null,
  enabled boolean not null
);

create table authorities (
  username varchar_ignorecase(255) not null,
  authority varchar_ignorecase(255) not null,
  constraint fk_authorities_users foreign key(username) references users(username) on delete cascade
);

create unique index ix_auth_username on authorities (username,authority);
