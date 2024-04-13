CREATE TABLE if not exists user (
  id          int         NOT NULL AUTO_INCREMENT,
  username    varchar(50) NOT NULL,
  password    char(60)    NOT NULL,
  first_name  varchar(20) NOT NULL,
  date_joined datetime    NOT NULL DEFAULT current_timestamp,
  last_login  datetime    NOT NULL DEFAULT current_timestamp,
  PRIMARY KEY (id),
  UNIQUE KEY username (username)
);

CREATE TABLE if not exists post (
  id            int          NOT NULL AUTO_INCREMENT,
  title         varchar(255) NOT NULL,
  content       text         NOT NULL,
  user_id       int          NOT NULL,            -- references user.id
  first_name    varchar(20)  NOT NULL DEFAULT '', -- jpa에서 사용하지 않음. user.firstName으로 사용
  pub_date      datetime     NOT NULL DEFAULT current_timestamp(),
  last_modified timestamp    NOT NULL DEFAULT current_timestamp() on update current_timestamp(),
  PRIMARY KEY (id),
  FULLTEXT KEY title (title),
  FULLTEXT KEY content (content),
  FOREIGN KEY (user_id) REFERENCES user (id)
);