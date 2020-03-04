CREATE TABLE TASK (
  task_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title varchar(100) DEFAULT NULL,
  description varchar(100) DEFAULT NULL,
  priority INT DEFAULT NULL
);

CREATE TABLE security_role (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  description varchar(100) DEFAULT NULL,
  role_name varchar(100) DEFAULT NULL
);


CREATE TABLE security_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL
);


CREATE TABLE user_role (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  CONSTRAINT FK_SECURITY_USER_ID FOREIGN KEY (user_id) REFERENCES security_user (id),
  CONSTRAINT FK_SECURITY_ROLE_ID FOREIGN KEY (role_id) REFERENCES security_role (id)
);