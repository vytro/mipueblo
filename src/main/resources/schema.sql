CREATE SCHEMA IF NOT EXISTS mipueblo;

SET NAMES 'UTF8MB4';
-- SET TIME_ZONE = 'America/New_York';
-- SET TIME_ZONE = '-4:00';


USE mipueblo;

DROP TABLE IF EXISTS UsersIssues;

DROP TABLE IF EXISTS Issues;

DROP TABLE IF EXISTS UsersComments;

DROP TABLE IF EXISTS Statuses;

DROP TABLE IF EXISTS Comments;

DROP TABLE IF EXISTS TwoFactorVerification;

DROP TABLE IF EXISTS ResetPasswordVerification;

DROP TABLE IF EXISTS AccountVerifications;

DROP TABLE IF EXISTS AccountVerifications;

DROP TABLE IF EXISTS UsersRoles;

DROP TABLE IF EXISTS UsersEvents;

DROP TABLE IF EXISTS Users;

CREATE TABLE Users
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
-- identity_card was included? 2:15:45 in the video
--  identity_card VARCHAR(10) NOT NULL,
    first_name VARCHAR(50)     NOT NULL,
    last_name  VARCHAR(50)     NOT NULL,
    email      VARCHAR(100)    NOT NULL,
    password   VARCHAR(255) DEFAULT NULL,
    address    VARCHAR(255) DEFAULT NULL,
    phone      VARCHAR(30)  DEFAULT NULL,
    title      VARCHAR(50)  DEFAULT NULL,
    bio        VARCHAR(255) DEFAULT NULL,
    enabled    BOOLEAN      DEFAULT FALSE,
    non_locked BOOLEAN      DEFAULT TRUE,
--   if they're using multifactored authentication
    using_mfa  BOOLEAN      DEFAULT FALSE,
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP,
    image_url  VARCHAR(255) DEFAULT 'https://static-00.iconduck.com/assets.00/person-icon-512x512-5lhrcpms.png',
    CONSTRAINT UQ_Users_Email UNIQUE (email)
);



DROP TABLE IF EXISTS Roles;

CREATE TABLE Roles
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(50)     NOT NULL,
    -- user/read, authority/delete
    permission VARCHAR(255)    NOT NULL,
    CONSTRAINT UQ_Roles_Name UNIQUE (name)
);

INSERT INTO Roles (name, permission)
VALUES ('ROLE_USER', 'READ:USER,READ:CUSTOMER'),
       ('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
       ('ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
       ('ROLE_SYSADMIN',
        'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');


-- DROP TABLE IF EXISTS UsersRoles;

CREATE TABLE UsersRoles
(
    id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    role_id BIGINT UNSIGNED NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES Roles (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_UsersRoles_User_Id UNIQUE (user_id)
);



DROP TABLE IF EXISTS Events;

# CREATE TABLE Events
# (
#     id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#     type        VARCHAR(50)     NOT NULL CHECK ( type IN (
#                                                           'LOGIN_ATTEMPT',
#                                                           'LOGIN_ATTEMPT_FAILURE',
#                                                           'LOGIN_ATTEMPT_SUCCESS',
#                                                           'PROFILE_UPDATE',
#                                                           'PROFILE_PICTURE_UPDATE',
#                                                           'ROLE_UPDATE',
#                                                           'ACCOUNT_SETTINGS_UPDATE',
#                                                           'PASSWORD_UPDATE',
#                                                           'MFA_UPDATE') ),
#     description VARCHAR(255)    NOT NULL,
#     CONSTRAINT UQ_Events_Type UNIQUE (type)
# );

CREATE TABLE Events
(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    type        VARCHAR(50)     NOT NULL CHECK (type IN
                                                ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILURE', 'LOGIN_ATTEMPT_SUCCESS',
                                                 'PROFILE_UPDATE', 'PROFILE_PICTURE_UPDATE', 'ROLE_UPDATE',
                                                 'ACCOUNT_SETTINGS_UPDATE', 'PASSWORD_UPDATE', 'MFA_UPDATE')),
    description VARCHAR(255)    NOT NULL,
    CONSTRAINT UQ_Events_Type UNIQUE (type)
);


INSERT INTO Events (type, description)
VALUES ('LOGIN_ATTEMPT', 'Intento de Inicio de Sesion'),
       ('LOGIN_ATTEMPT_SUCCESS', 'Inicio de Sesion Exitoso'),
       ('LOGIN_ATTEMPT_FAILURE', 'Fallo de Inicio de Sesion'),
       ('PROFILE_UPDATE', 'Actualizacion de Perfil'),
       ('PROFILE_PICTURE_UPDATE', 'Actualizacion de Foto de Perfil'),
       ('ROLE_UPDATE', 'Actualizacion de Rol'),
       ('ACCOUNT_SETTINGS_UPDATE', 'Actualizacion de Configuracion de Cuenta'),
       ('MFA_UPDATE', 'Actualizacion de MFA'),
       ('PASSWORD_UPDATE', 'Actualizacion de Contraseña');


-- DROP TABLE IF EXISTS UsersEvents;

CREATE TABLE UsersEvents
(
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT UNSIGNED NOT NULL,
    event_id   BIGINT UNSIGNED NOT NULL,
    device     VARCHAR(100) DEFAULT NULL,
    ip_address VARCHAR(100) DEFAULT NULL,
    created_at DATETIME     DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES Events (id) ON DELETE CASCADE ON UPDATE CASCADE
);


-- DROP TABLE IF EXISTS AccountVerifications;

CREATE TABLE AccountVerifications
(
    id      BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNSIGNED NOT NULL,
    url     VARCHAR(255)    NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_AccountVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_AccountVerifications_Url UNIQUE (url)
);


-- DROP TABLE IF EXISTS ResetPasswordVerification;

CREATE TABLE ResetPasswordVerification
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT UNSIGNED NOT NULL,
    url             VARCHAR(255)    NOT NULL,
    expiration_date DATETIME        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_AccountVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_AccountVerifications_Url UNIQUE (url)
);


-- DROP TABLE IF EXISTS TwoFactorVerification;

CREATE TABLE TwoFactorVerification
(
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT UNSIGNED NOT NULL,
    code            VARCHAR(10)     NOT NULL,
    expiration_date DATETIME        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_TwoFactorVerification_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_TwoFactorVerification_Code UNIQUE (code)
);



# CREATE TABLE Comments (
#   id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#   user_id       BIGINT UNSIGNED NOT NULL,
#   comment_text  TEXT NOT NULL,
#   created_at    DATETIME DEFAULT CURRENT_TIMESTAMP,
#   status        VARCHAR(20) NOT NULL,
#   FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE
# );
#
# CREATE INDEX idx_user_id ON Comments (user_id);
#
# -- when a user gets deleted, their comments get deleted as well
# ALTER TABLE Comments
#     ADD CONSTRAINT FK_UserComment
#         FOREIGN KEY (user_id) REFERENCES Users (id)
#             ON DELETE CASCADE
#             ON UPDATE CASCADE;


CREATE TABLE Statuses
(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    status_name VARCHAR(20)     NOT NULL
);

INSERT INTO Statuses (status_name)
VALUES ('Active'),
       ('Inactive'),
       ('Solved'),
       ('On Hold');

# CREATE TABLE UsersComments (
#    id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
#    user_id       BIGINT UNSIGNED NOT NULL,
#    comment_id    BIGINT UNSIGNED NOT NULL,
#    interaction_type VARCHAR(20) NOT NULL,
#    FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
#    FOREIGN KEY (comment_id) REFERENCES Comments (id) ON DELETE CASCADE ON UPDATE CASCADE
# );


CREATE TABLE Issues
(
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(255)    NOT NULL,
    description TEXT,
    image_url   VARCHAR(255),
    location    VARCHAR(255),
    status_id   BIGINT UNSIGNED NOT NULL,
    created_at  DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT FK_Issues_Statuses FOREIGN KEY (status_id) REFERENCES Statuses (id)
);



CREATE TABLE UsersIssues
(
    id       BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id  BIGINT UNSIGNED NOT NULL,
    issue_id BIGINT UNSIGNED NOT NULL,
    CONSTRAINT FK_UsersIssues_Users FOREIGN KEY (user_id) REFERENCES Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT FK_UsersIssues_Issues FOREIGN KEY (issue_id) REFERENCES Issues (id) ON DELETE CASCADE ON UPDATE CASCADE
);



INSERT INTO Users
VALUES (1, 'Brayan', 'Cano', 'brayan2@gmail.com', '$2a$12$z0FS/Vg17e2VjHiGP.dqwu6nqwZviKiFRK3TJofzPxy/WG95lcXie', NULL,
        '59177903057', NULL, NULL, 1, 1, 0, '2024-02-12 13:43:39',
        'http://localhost:8080/user/image/brayan2@gmail.com.png'),
       (2, 'Test', 'Me', 'test1@gmail.com', '$2a$12$8DBqacNuQDwrHe7MjubTEOHPzShGmFtCPd28ZrlIsHxhn3kh2pOsG', NULL, NULL,
        NULL, NULL, 1, 1, 0, '2024-02-12 14:01:02',
        'https://static-00.iconduck.com/assets.00/person-icon-512x512-5lhrcpms.png'),
       (7, 'Tester', 'Tester', 'tester@gmail.com', '$2a$12$AP62K4IFAEbbXfC1eKZjDe72eR5TZ.lAli.eBIzapUNfAktvqM65m', NULL,
        NULL, NULL, NULL, 0, 1, 0, '2024-02-17 18:31:19',
        'https://static-00.iconduck.com/assets.00/person-icon-512x512-5lhrcpms.png'),
       (13, 'Luz', 'Cano', 'fraluzbran@gmail.com', '$2a$12$kXH4otNCQwI2qcIZE3j2XuEbxL2.OdhLJ4EEzVtqYVTKUwaBEEkVi', NULL,
        NULL, NULL, NULL, 1, 1, 0, '2024-02-24 20:35:55',
        'https://static-00.iconduck.com/assets.00/person-icon-512x512-5lhrcpms.png'),
       (14, 'delete', 'delete', 'delete@gmail.com', '$2a$12$NK53OnB3RTkYcDXQ1O3J6u9PobFICmQP8x.EnL9va29ArcrzqwQou',
        NULL, NULL, NULL, NULL, 1, 1, 0, '2024-02-26 18:45:17',
        'https://static-00.iconduck.com/assets.00/person-icon-512x512-5lhrcpms.png');
#
# INSERT INTO customer
# VALUES (1, 'Cameroon Street', '2024-02-12 17:31:36.374000', 'kyle@gmail.com',
#         'https://images.unsplash.com/photo-1480365334925-2aee561aa28e?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDh8dG93SlpGc2twR2d8fGVufDB8fHx8fA%3D%3D',
#         'Kyle Quest', '17038450194', 'INACTIVE', 'INDIVIDUAL'),
#        (2, 'Wallstreet', '2024-02-12 17:32:47.884000', 'tester@gmail.com',
#         'https://images.unsplash.com/photo-1622016724812-b0d972e54791?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
#         'Test 1', '17034829444', 'ACTIVE', 'INDIVIDUAL'),
#        (3, '91551 Vidon Drive', '2024-02-02 20:44:26.000000', 'sdashwood1@microsoft.com',
#         'http://dummyimage.com/249x100.png/5fa2dd/ffffff', 'Sukey', '745-619-0337', 'INACTIVE', 'INDIVIDUAL'),
#        (4, '115 Florence Street', '2024-02-02 20:44:26.000000', 'awinson2@amazon.com',
#         'http://dummyimage.com/241x100.png/cc0000/ffffff', 'Angelique', '485-909-6034', 'ACTIVE', 'INDIVIDUAL'),
#        (5, '7 Dahle Terrace', '2024-02-02 20:44:26.000000', 'hbaynard3@guardian.co.uk',
#         'http://dummyimage.com/183x100.png/cc0000/ffffff', 'Harwilll', '886-429-1918', 'INACTIVE', 'INDIVIDUAL'),
#        (6, '35990 Union Court', '2024-02-02 20:44:26.000000', 'astabbins4@nhs.uk',
#         'http://dummyimage.com/239x100.png/ff4444/ffffff', 'Aida', '879-794-9479', 'ACTIVE', 'INDIVIDUAL'),
#        (7, '03 Kim Point', '2024-02-02 20:44:26.000000', 'cmoriarty5@mapquest.com',
#         'http://dummyimage.com/142x100.png/ff4444/ffffff', 'Cary', '117-931-7153', 'INACTIVE', 'INDIVIDUAL'),
#        (8, '34 La Follette Place', '2024-02-02 20:44:26.000000', 'carter6@globo.com',
#         'http://dummyimage.com/128x100.png/ff4444/ffffff', 'Charin', '463-659-9921', 'ACTIVE', 'INDIVIDUAL'),
#        (9, '71 Forster Lane', '2024-02-02 20:44:26.000000', 'reversfield7@tinyurl.com',
#         'http://dummyimage.com/165x100.png/5fa2dd/ffffff', 'Ros', '463-802-9564', 'INACTIVE', 'INDIVIDUAL'),
#        (10, '21896 Arkansas Road', '2024-02-02 20:44:26.000000', 'ameneghelli8@xing.com',
#         'http://dummyimage.com/176x100.png/cc0000/ffffff', 'Allianora', '529-679-5762', 'ACTIVE', 'INDIVIDUAL'),
#        (11, '62 Judy Center', '2024-02-02 20:44:26.000000', 'wbryde9@princeton.edu',
#         'http://dummyimage.com/248x100.png/cc0000/ffffff', 'Wilbur', '856-987-6591', 'INACTIVE', 'INDIVIDUAL'),
#        (12, '68028 Cody Way', '2024-02-02 20:44:26.000000', 'lbrunrotha@barnesandnoble.com',
#         'http://dummyimage.com/158x100.png/dddddd/000000', 'Lib', '811-763-4435', 'ACTIVE', 'INDIVIDUAL'),
#        (13, '336 Merrick Court', '2024-02-02 20:44:26.000000', 'lruttb@t.co',
#         'http://dummyimage.com/208x100.png/cc0000/ffffff', 'Laurena', '410-506-2356', 'INACTIVE', 'INDIVIDUAL'),
#        (14, '951 Kropf Pass', '2024-02-02 20:44:26.000000', 'kbranchec@dropbox.com',
#         'http://dummyimage.com/172x100.png/5fa2dd/ffffff', 'Kessia', '691-307-2447', 'ACTIVE', 'INDIVIDUAL'),
#        (15, '97 Oak Valley Circle', '2024-02-02 20:44:26.000000', 'khamprechtd@hao123.com',
#         'http://dummyimage.com/212x100.png/cc0000/ffffff', 'Kennedy', '713-651-7079', 'INACTIVE', 'INDIVIDUAL'),
#        (16, '71085 Manitowish Way', '2024-02-02 20:44:26.000000', 'ebollone@ucoz.ru',
#         'http://dummyimage.com/131x100.png/dddddd/000000', 'Eamon', '914-691-4410', 'ACTIVE', 'INDIVIDUAL'),
#        (17, '3 Hintze Plaza', '2024-02-02 20:44:26.000000', 'jpevsnerf@cornell.edu',
#         'http://dummyimage.com/173x100.png/5fa2dd/ffffff', 'Joann', '347-532-9386', 'INACTIVE', 'INDIVIDUAL'),
#        (18, '1925 Derek Trail', '2024-02-02 20:44:26.000000', 'hbenasikg@delicious.com',
#         'http://dummyimage.com/208x100.png/ff4444/ffffff', 'Hilly', '643-159-3861', 'ACTIVE', 'INDIVIDUAL'),
#        (19, '9905 Autumn Leaf Way', '2024-02-02 20:44:26.000000', 'pdalglieshh@wp.com',
#         'http://dummyimage.com/149x100.png/dddddd/000000', 'Pru', '338-130-1433', 'INACTIVE', 'INDIVIDUAL'),
#        (20, '2327 Nancy Alley', '2024-02-02 20:44:26.000000', 'mswynfeni@devhub.com',
#         'http://dummyimage.com/160x100.png/ff4444/ffffff', 'Missie', '825-715-5870', 'ACTIVE', 'INDIVIDUAL'),
#        (21, '2 Dayton Terrace', '2024-02-02 20:44:26.000000', 'mtilerj@wired.com',
#         'http://dummyimage.com/143x100.png/cc0000/ffffff', 'Martelle', '994-506-0987', 'INACTIVE', 'INDIVIDUAL'),
#        (22, '110 Carioca Plaza', '2024-02-02 20:44:26.000000', 'vcollomossek@yolasite.com',
#         'http://dummyimage.com/117x100.png/cc0000/ffffff', 'Verine', '311-368-4945', 'ACTIVE', 'INDIVIDUAL'),
#        (23, '4 Melody Way', '2024-02-02 20:44:26.000000', 'jantonil@barnesandnoble.com',
#         'http://dummyimage.com/183x100.png/ff4444/ffffff', 'Josselyn', '498-549-4855', 'INACTIVE', 'INDIVIDUAL'),
#        (24, '10 Shelley Crossing', '2024-02-02 20:44:26.000000', 'lpetrushkam@sun.com',
#         'http://dummyimage.com/190x100.png/5fa2dd/ffffff', 'Letisha', '545-484-6271', 'ACTIVE', 'INDIVIDUAL'),
#        (25, '2270 Arkansas Hill', '2024-02-02 20:44:26.000000', 'acousinsn@mac.com',
#         'http://dummyimage.com/216x100.png/5fa2dd/ffffff', 'Abeu', '952-490-4372', 'INACTIVE', 'INDIVIDUAL'),
#        (26, '5222 Nova Point', '2024-02-02 20:44:26.000000', 'astanelando@wired.com',
#         'http://dummyimage.com/124x100.png/dddddd/000000', 'Ardelis', '975-789-5446', 'ACTIVE', 'INDIVIDUAL'),
#        (27, '18549 Darwin Court', '2024-02-02 20:44:26.000000', 'rbertomeup@histats.com',
#         'http://dummyimage.com/189x100.png/ff4444/ffffff', 'Rubina', '992-936-8769', 'INACTIVE', 'INDIVIDUAL'),
#        (28, '51726 Mifflin Terrace', '2024-02-02 20:44:26.000000', 'tbennerq@163.com',
#         'http://dummyimage.com/166x100.png/5fa2dd/ffffff', 'Thaine', '644-135-4703', 'ACTIVE', 'INDIVIDUAL'),
#        (29, '1808 Havey Point', '2024-02-02 20:44:26.000000', 'vcreaghr@google.com',
#         'http://dummyimage.com/193x100.png/5fa2dd/ffffff', 'Verge', '909-994-2289', 'INACTIVE', 'INDIVIDUAL'),
#        (30, '67147 Lotheville Place', '2024-02-02 20:44:26.000000', 'cwestmerlands@archive.org',
#         'http://dummyimage.com/196x100.png/cc0000/ffffff', 'Calv', '468-414-8138', 'ACTIVE', 'INDIVIDUAL'),
#        (31, '12026 Warrior Center', '2024-02-02 20:44:26.000000', 'ogovenlockt@opensource.org',
#         'http://dummyimage.com/234x100.png/5fa2dd/ffffff', 'Olwen', '698-122-6453', 'INACTIVE', 'INDIVIDUAL'),
#        (32, '7710 Transport Court', '2024-02-02 20:44:26.000000', 'cbrunsdenu@xinhuanet.com',
#         'http://dummyimage.com/103x100.png/cc0000/ffffff', 'Celisse', '221-719-1146', 'ACTIVE', 'INDIVIDUAL'),
#        (33, '0843 5th Court', '2024-02-02 20:44:26.000000', 'osemainev@house.gov',
#         'http://dummyimage.com/144x100.png/cc0000/ffffff', 'Otho', '530-528-8789', 'INACTIVE', 'INDIVIDUAL'),
#        (34, '93 Glendale Lane', '2024-02-02 20:44:26.000000', 'dfranew@earthlink.net',
#         'http://dummyimage.com/141x100.png/dddddd/000000', 'Danya', '722-699-0909', 'ACTIVE', 'INDIVIDUAL'),
#        (35, '630 Eagan Court', '2024-02-02 20:44:26.000000', 'slangshawx@webmd.com',
#         'http://dummyimage.com/154x100.png/ff4444/ffffff', 'Sigismund', '188-550-4822', 'INACTIVE', 'INDIVIDUAL'),
#        (36, '522 Bultman Road', '2024-02-02 20:44:26.000000', 'aleytony@flickr.com',
#         'http://dummyimage.com/212x100.png/dddddd/000000', 'Amandy', '343-687-8075', 'ACTIVE', 'INDIVIDUAL'),
#        (37, '697 Hoard Way', '2024-02-02 20:44:26.000000', 'mfrottonz@apache.org',
#         'http://dummyimage.com/104x100.png/ff4444/ffffff', 'Maddy', '523-955-5240', 'INACTIVE', 'INDIVIDUAL'),
#        (38, '68369 Merry Lane', '2024-02-02 20:44:26.000000', 'jsisland10@surveymonkey.com',
#         'http://dummyimage.com/147x100.png/dddddd/000000', 'Julee', '984-997-8386', 'ACTIVE', 'INDIVIDUAL'),
#        (39, '96766 Lakewood Gardens Parkway', '2024-02-02 20:44:26.000000', 'lpatience11@facebook.com',
#         'http://dummyimage.com/134x100.png/5fa2dd/ffffff', 'Leontine', '720-957-6373', 'INACTIVE', 'INDIVIDUAL'),
#        (40, '860 Westport Alley', '2024-02-02 20:44:26.000000', 'fthoma12@cisco.com',
#         'http://dummyimage.com/130x100.png/dddddd/000000', 'Fred', '481-742-6919', 'ACTIVE', 'INDIVIDUAL'),
#        (41, '71 Mariners Cove Trail', '2024-02-02 20:44:26.000000', 'kfaivre13@indiegogo.com',
#         'http://dummyimage.com/210x100.png/ff4444/ffffff', 'Ky', '945-993-5520', 'INACTIVE', 'INDIVIDUAL'),
#        (42, '64 Clove Center', '2024-02-02 20:44:26.000000', 'ebucktharp14@behance.net',
#         'http://dummyimage.com/175x100.png/5fa2dd/ffffff', 'Enrica', '943-410-1418', 'ACTIVE', 'INDIVIDUAL'),
#        (43, '2316 Rockefeller Court', '2024-02-02 20:44:26.000000', 'gaspall15@dell.com',
#         'http://dummyimage.com/223x100.png/dddddd/000000', 'Gannie', '685-678-3214', 'INACTIVE', 'INDIVIDUAL'),
#        (44, '2 Waywood Way', '2024-02-02 20:44:26.000000', 'sdisbury16@joomla.org',
#         'http://dummyimage.com/134x100.png/5fa2dd/ffffff', 'Sonia', '970-434-0794', 'ACTIVE', 'INDIVIDUAL'),
#        (45, '7398 Banding Road', '2024-02-02 20:44:26.000000', 'mguyonnet17@delicious.com',
#         'http://dummyimage.com/239x100.png/dddddd/000000', 'Mozes', '987-254-9816', 'INACTIVE', 'INDIVIDUAL'),
#        (46, '90789 Butterfield Place', '2024-02-02 20:44:26.000000', 'rbengtsson18@miitbeian.gov.cn',
#         'http://dummyimage.com/189x100.png/ff4444/ffffff', 'Ric', '133-928-2873', 'ACTIVE', 'INDIVIDUAL'),
#        (47, '87781 Sunbrook Way', '2024-02-02 20:44:26.000000', 'kissatt19@youku.com',
#         'http://dummyimage.com/231x100.png/dddddd/000000', 'Kilian', '680-354-9156', 'INACTIVE', 'INDIVIDUAL'),
#        (48, '0 Ronald Regan Alley', '2024-02-02 20:44:26.000000', 'cgarbutt1a@netvibes.com',
#         'http://dummyimage.com/205x100.png/5fa2dd/ffffff', 'Corri', '865-490-4379', 'ACTIVE', 'INDIVIDUAL'),
#        (49, '9 Sunbrook Court', '2024-02-02 20:44:26.000000', 'lkamienski1b@cnn.com',
#         'http://dummyimage.com/232x100.png/5fa2dd/ffffff', 'Lotta', '770-294-4654', 'INACTIVE', 'INDIVIDUAL'),
#        (50, '91081 Boyd Plaza', '2024-02-02 20:44:26.000000', 'ahaddrill1c@sbwire.com',
#         'http://dummyimage.com/107x100.png/dddddd/000000', 'Arleen', '783-197-1813', 'ACTIVE', 'INDIVIDUAL'),
#        (51, '6729 Oakridge Way', '2024-02-02 20:44:26.000000', 'bhardware1d@globo.com',
#         'http://dummyimage.com/234x100.png/ff4444/ffffff', 'Bord', '560-693-7448', 'INACTIVE', 'INDIVIDUAL'),
#        (52, '80 Mcguire Park', '2024-02-02 20:44:26.000000', 'descale1e@mac.com',
#         'http://dummyimage.com/112x100.png/cc0000/ffffff', 'Dietrich', '978-156-9012', 'ACTIVE', 'INDIVIDUAL'),
#        (53, '2 Morningstar Parkway', '2024-02-02 20:44:26.000000', 'bphippen1f@livejournal.com',
#         'http://dummyimage.com/193x100.png/dddddd/000000', 'Benedikta', '753-360-4646', 'INACTIVE', 'INDIVIDUAL'),
#        (54, '205 Pankratz Alley', '2024-02-02 20:44:26.000000', 'clonergan1g@ehow.com',
#         'http://dummyimage.com/188x100.png/cc0000/ffffff', 'Claus', '705-864-4661', 'ACTIVE', 'INDIVIDUAL'),
#        (55, '18 Shoshone Center', '2024-02-02 20:44:26.000000', 'lbaldock1h@instagram.com',
#         'http://dummyimage.com/203x100.png/5fa2dd/ffffff', 'Leigha', '515-805-9225', 'INACTIVE', 'INDIVIDUAL'),
#        (56, '78 Esch Way', '2024-02-02 20:44:26.000000', 'asynnot1i@sbwire.com',
#         'http://dummyimage.com/130x100.png/dddddd/000000', 'Aldous', '853-434-7802', 'ACTIVE', 'INDIVIDUAL'),
#        (57, '02 Larry Terrace', '2024-02-02 20:44:26.000000', 'garundel1j@blinklist.com',
#         'http://dummyimage.com/183x100.png/dddddd/000000', 'Gav', '933-801-8305', 'INACTIVE', 'INDIVIDUAL'),
#        (58, '578 Anthes Circle', '2024-02-02 20:44:26.000000', 'dabrahams1k@arizona.edu',
#         'http://dummyimage.com/247x100.png/dddddd/000000', 'Delphinia', '565-164-0311', 'ACTIVE', 'INDIVIDUAL'),
#        (59, '208 Duke Point', '2024-02-02 20:44:26.000000', 'bpattle1l@virginia.edu',
#         'http://dummyimage.com/108x100.png/ff4444/ffffff', 'Baird', '718-761-6062', 'INACTIVE', 'INDIVIDUAL'),
#        (60, '3 Scott Terrace', '2024-02-02 20:44:26.000000', 'epyrah1m@nationalgeographic.com',
#         'http://dummyimage.com/169x100.png/dddddd/000000', 'Englebert', '308-517-9653', 'ACTIVE', 'INDIVIDUAL'),
#        (61, '31872 Delaware Road', '2024-02-02 20:44:26.000000', 'sattenbrow1n@auda.org.au',
#         'http://dummyimage.com/142x100.png/cc0000/ffffff', 'Shay', '640-520-5593', 'INACTIVE', 'INDIVIDUAL'),
#        (62, '45 Vermont Terrace', '2024-02-02 20:44:26.000000', 'rtarling1o@51.la',
#         'http://dummyimage.com/193x100.png/ff4444/ffffff', 'Ramsey', '677-873-8833', 'ACTIVE', 'INDIVIDUAL'),
#        (63, '3428 Bartillon Place', '2024-02-02 20:44:26.000000', 'loshiel1p@over-blog.com',
#         'http://dummyimage.com/215x100.png/cc0000/ffffff', 'Leicester', '717-324-4250', 'INACTIVE', 'INDIVIDUAL'),
#        (64, '020 Summerview Street', '2024-02-02 20:44:26.000000', 'gcordelette1q@netvibes.com',
#         'http://dummyimage.com/250x100.png/cc0000/ffffff', 'Gelya', '902-971-1530', 'ACTIVE', 'INDIVIDUAL'),
#        (65, '3 Lakewood Circle', '2024-02-02 20:44:26.000000', 'mgogarty1r@seesaa.net',
#         'http://dummyimage.com/178x100.png/dddddd/000000', 'Megan', '362-991-6550', 'INACTIVE', 'INDIVIDUAL'),
#        (66, '44785 Talmadge Center', '2024-02-02 20:44:26.000000', 'mabela1s@wikimedia.org',
#         'http://dummyimage.com/228x100.png/dddddd/000000', 'Margie', '206-787-0460', 'ACTIVE', 'INDIVIDUAL'),
#        (67, '44 Iowa Alley', '2024-02-02 20:44:26.000000', 'sdecent1t@blogtalkradio.com',
#         'http://dummyimage.com/185x100.png/cc0000/ffffff', 'Sabrina', '755-639-4583', 'INACTIVE', 'INDIVIDUAL'),
#        (68, '37856 Hoffman Alley', '2024-02-02 20:44:26.000000', 'eyemm1u@psu.edu',
#         'http://dummyimage.com/178x100.png/cc0000/ffffff', 'Ellsworth', '684-369-4908', 'ACTIVE', 'INDIVIDUAL'),
#        (69, '477 Hintze Circle', '2024-02-02 20:44:26.000000', 'imanifold1v@indiatimes.com',
#         'http://dummyimage.com/209x100.png/5fa2dd/ffffff', 'Imogene', '932-347-4709', 'INACTIVE', 'INDIVIDUAL'),
#        (70, '8774 Vera Alley', '2024-02-02 20:44:26.000000', 'tdreini1w@dailymail.co.uk',
#         'http://dummyimage.com/181x100.png/5fa2dd/ffffff', 'Tilly', '866-625-1531', 'ACTIVE', 'INDIVIDUAL'),
#        (71, '6 Dorton Plaza', '2024-02-02 20:44:26.000000', 'lkrauss1x@howstuffworks.com',
#         'http://dummyimage.com/136x100.png/5fa2dd/ffffff', 'Lutero', '741-798-1681', 'INACTIVE', 'INDIVIDUAL'),
#        (72, '0420 Glendale Center', '2024-02-02 20:44:26.000000', 'uperceval1y@lulu.com',
#         'http://dummyimage.com/144x100.png/5fa2dd/ffffff', 'Upton', '756-803-0652', 'ACTIVE', 'INDIVIDUAL'),
#        (73, '79 Garrison Junction', '2024-02-02 20:44:26.000000', 'bbreens1z@mayoclinic.com',
#         'http://dummyimage.com/135x100.png/ff4444/ffffff', 'Brittany', '399-954-0598', 'INACTIVE', 'INDIVIDUAL'),
#        (74, '7 Jay Alley', '2024-02-02 20:44:26.000000', 'dbeardshall20@google.it',
#         'http://dummyimage.com/229x100.png/ff4444/ffffff', 'Davetaa', '286-965-8143', 'ACTIVE', 'INDIVIDUAL'),
#        (75, '74715 Donald Court', '2024-02-02 20:44:26.000000', 'pnaden21@uiuc.edu',
#         'http://dummyimage.com/211x100.png/ff4444/ffffff', 'Preston', '993-821-3196', 'INACTIVE', 'INDIVIDUAL'),
#        (76, '2 Kensington Pass', '2024-02-02 20:44:26.000000', 'ytyzack22@opensource.org',
#         'http://dummyimage.com/249x100.png/dddddd/000000', 'Yvor', '795-315-8664', 'ACTIVE', 'INDIVIDUAL'),
#        (77, '2595 Calypso Point', '2024-02-02 20:44:26.000000', 'jzmitrovich23@about.com',
#         'http://dummyimage.com/167x100.png/dddddd/000000', 'Julianna', '341-744-1422', 'INACTIVE', 'INDIVIDUAL'),
#        (78, '98 Haas Hill', '2024-02-02 20:44:26.000000', 'oosman24@si.edu',
#         'http://dummyimage.com/129x100.png/cc0000/ffffff', 'Osborn', '841-913-5204', 'ACTIVE', 'INDIVIDUAL'),
#        (79, '76 Mayer Pass', '2024-02-02 20:44:26.000000', 'dlaudham25@gizmodo.com',
#         'http://dummyimage.com/110x100.png/ff4444/ffffff', 'Drucie', '331-923-2128', 'INACTIVE', 'INDIVIDUAL'),
#        (80, '7409 Maple Park', '2024-02-02 20:44:26.000000', 'rsemper26@odnoklassniki.ru',
#         'http://dummyimage.com/167x100.png/dddddd/000000', 'Rusty', '941-914-0738', 'ACTIVE', 'INDIVIDUAL'),
#        (81, '1 Petterle Junction', '2024-02-02 20:44:26.000000', 'abailiss27@gnu.org',
#         'http://dummyimage.com/174x100.png/dddddd/000000', 'Andy', '161-111-4366', 'INACTIVE', 'INDIVIDUAL'),
#        (82, '01 Waubesa Street', '2024-02-02 20:44:26.000000', 'ldevuyst28@tiny.cc',
#         'http://dummyimage.com/248x100.png/cc0000/ffffff', 'Loise', '981-552-7564', 'ACTIVE', 'INDIVIDUAL'),
#        (83, '07461 Talmadge Place', '2024-02-02 20:44:26.000000', 'cvarfalameev29@linkedin.com',
#         'http://dummyimage.com/224x100.png/ff4444/ffffff', 'Colin', '273-493-2099', 'INACTIVE', 'INDIVIDUAL'),
#        (84, '142 Mitchell Plaza', '2024-02-02 20:44:26.000000', 'jbaudi2a@yale.edu',
#         'http://dummyimage.com/171x100.png/dddddd/000000', 'Jamie', '349-145-3526', 'ACTIVE', 'INDIVIDUAL'),
#        (85, '2421 Southridge Street', '2024-02-02 20:44:26.000000', 'rskirlin2b@e-recht24.de',
#         'http://dummyimage.com/204x100.png/5fa2dd/ffffff', 'Raddie', '292-734-8652', 'INACTIVE', 'INDIVIDUAL'),
#        (86, '360 Center Point', '2024-02-02 20:44:26.000000', 'ctrewhela2c@narod.ru',
#         'http://dummyimage.com/137x100.png/5fa2dd/ffffff', 'Candace', '292-121-4356', 'ACTIVE', 'INDIVIDUAL'),
#        (87, '9 Grim Hill', '2024-02-02 20:44:26.000000', 'rshoebotham2d@apple.com',
#         'http://dummyimage.com/238x100.png/dddddd/000000', 'Robbie', '399-948-6261', 'INACTIVE', 'INDIVIDUAL'),
#        (88, '2543 Veith Court', '2024-02-02 20:44:26.000000', 'gadamsky2e@aol.com',
#         'http://dummyimage.com/181x100.png/5fa2dd/ffffff', 'Gray', '937-600-3465', 'ACTIVE', 'INDIVIDUAL'),
#        (89, '42 Kensington Center', '2024-02-02 20:44:26.000000', 'skharchinski2f@imdb.com',
#         'http://dummyimage.com/241x100.png/dddddd/000000', 'Sigismond', '287-970-3258', 'INACTIVE', 'INDIVIDUAL'),
#        (90, '1044 Quincy Junction', '2024-02-02 20:44:26.000000', 'lstreets2g@epa.gov',
#         'http://dummyimage.com/183x100.png/cc0000/ffffff', 'Lexi', '375-752-0320', 'ACTIVE', 'INDIVIDUAL'),
#        (91, '525 Milwaukee Plaza', '2024-02-02 20:44:26.000000', 'mblaxland2h@wired.com',
#         'http://dummyimage.com/182x100.png/dddddd/000000', 'Misha', '103-549-9422', 'INACTIVE', 'INDIVIDUAL'),
#        (92, '00 Sycamore Trail', '2024-02-02 20:44:26.000000', 'rmccullough2i@rakuten.co.jp',
#         'http://dummyimage.com/174x100.png/cc0000/ffffff', 'Raddie', '918-499-9361', 'ACTIVE', 'INDIVIDUAL'),
#        (93, '7 Harbort Road', '2024-02-02 20:44:26.000000', 'edootson2j@vkontakte.ru',
#         'http://dummyimage.com/102x100.png/cc0000/ffffff', 'Estel', '580-109-6275', 'INACTIVE', 'INDIVIDUAL'),
#        (94, '60 Oriole Circle', '2024-02-02 20:44:26.000000', 'mhanford2k@google.pl',
#         'http://dummyimage.com/230x100.png/5fa2dd/ffffff', 'Mycah', '812-818-3065', 'ACTIVE', 'INDIVIDUAL'),
#        (95, '1 School Court', '2024-02-02 20:44:26.000000', 'rkrochmann2l@ning.com',
#         'http://dummyimage.com/114x100.png/5fa2dd/ffffff', 'Rosemarie', '435-284-8167', 'INACTIVE', 'INDIVIDUAL'),
#        (96, '87665 Cardinal Alley', '2024-02-02 20:44:26.000000', 'vjansen2m@npr.org',
#         'http://dummyimage.com/191x100.png/dddddd/000000', 'Vivyan', '541-740-0444', 'ACTIVE', 'INDIVIDUAL'),
#        (97, '52 Fairfield Center', '2024-02-02 20:44:26.000000', 'mpeltz2n@rambler.ru',
#         'http://dummyimage.com/138x100.png/ff4444/ffffff', 'Mallory', '177-773-4282', 'INACTIVE', 'INDIVIDUAL'),
#        (98, '2 Del Mar Avenue', '2024-02-02 20:44:26.000000', 'rgiacopello2o@state.tx.us',
#         'http://dummyimage.com/191x100.png/ff4444/ffffff', 'Rosina', '302-759-7432', 'ACTIVE', 'INDIVIDUAL'),
#        (99, '5890 Merrick Hill', '2024-02-02 20:44:26.000000', 'ublasli2p@sfgate.com',
#         'http://dummyimage.com/139x100.png/5fa2dd/ffffff', 'Ulrikaumeko', '487-387-1532', 'INACTIVE', 'INDIVIDUAL'),
#        (100, '44 East Parkway', '2024-02-02 20:44:26.000000', 'dtincknell2q@utexas.edu',
#         'http://dummyimage.com/116x100.png/5fa2dd/ffffff', 'Desiree', '163-799-2665', 'ACTIVE', 'INDIVIDUAL'),
#        (101, '6195 Dapin Terrace', '2024-02-02 20:44:26.000000', 'afrizzell2r@berkeley.edu',
#         'http://dummyimage.com/228x100.png/cc0000/ffffff', 'Aristotle', '435-839-7976', 'INACTIVE', 'INDIVIDUAL'),
#        (102, 'No Name Street', '2024-02-02 20:44:26.000000', 'newcomment@gmail.com',
#         'https://images.unsplash.com/photo-1637579103895-9ba8218e9aca?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
#         'New Comment', '17038496524', 'ACTIVE', 'INDIVIDUAL'),
#        (103, 'A Street Name', '2024-02-12 17:33:36.374000', 'tester2@gmail.com',
#         'https://images.unsplash.com/photo-1622016724812-b0d972e54791?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
#         'Test 2', '17038473921', 'INACTIVE', 'INDIVIDUAL'),
#        (104, 'Unique Street', '2024-02-12 19:25:19.686000', 'uniquee@gmail.com',
#         'https://images.unsplash.com/photo-1637579103895-9ba8218e9aca?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D',
#         'Unique Name', '17038278322', 'ACTIVE', 'INDIVIDUAL');

# INSERT INTO invoice
# VALUES (1, '2024-02-08 20:00:00.000000', 'QIVUGHOJ', '1 Limpieza Hogar 300, 2 Inspeccion Tecnica 200', 'PENDING', 500,
#         102),
#        (2, '2024-02-07 20:00:00.000000', '3L8GIB7D', '1 Limpieza General 200 bs.', 'OVERDUE', 200, 1),
#        (3, '2024-02-10 20:00:00.000000', '3RFGPCBV', '1 Limpieza Hogar 100, 2 Inspeccion Tecnica 100', 'PENDING', 200,
#         1),
#        (4, '2024-02-22 20:00:00.000000', 'OSWZI9EI', '1 Lava Auto 100bs', 'PENDING', 100, 1);

INSERT INTO AccountVerifications
VALUES (1, 1, 'http://localhost:8080/user/verify/account/986a31d0-a101-4844-a30e-f1bd9f0cf4c5'),
       (2, 2, 'http://localhost:8080/user/verify/account/921db99a-7fe5-4e9f-bff9-3d5867102d90'),
       (7, 7, 'http://localhost:8080/user/verify/account/b86930d2-1758-4553-8352-ef63b08366d8'),
       (13, 13, 'http://localhost:4200/user/verify/account/4adf3b8e-17bf-488e-8709-c98536348d92'),
       (14, 14, 'http://localhost:4200/user/verify/account/29dd15ec-b098-4b4b-8bec-4c9bf3535720');



INSERT INTO ResetPasswordVerification
VALUES (18, 13, 'http://localhost:4200/user/verify/password/ede1f8e3-22de-424b-bdb5-5ebebe20eaa1',
        '2024-02-27 18:44:16');


INSERT INTO UsersEvents
VALUES (1, 1, 1, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-12 13:44:24'),
       (2, 1, 2, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-12 13:44:26'),
       (3, 1, 1, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-12 17:21:33'),
       (4, 1, 2, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-12 17:21:35'),
       (5, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 17:29:45'),
       (6, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 17:29:46'),
       (7, 1, 5, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 17:29:57'),
       (8, 1, 5, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 17:30:00'),
       (9, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 18:23:31'),
       (10, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 18:23:32'),
       (11, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 19:33:04'),
       (12, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 19:33:05'),
       (13, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 21:03:58'),
       (14, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 21:03:59'),
       (15, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 22:02:56'),
       (16, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 22:02:57'),
       (17, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 22:39:15'),
       (18, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-12 22:39:17'),
       (19, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 02:41:51'),
       (20, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 02:41:54'),
       (21, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 03:28:44'),
       (22, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 03:28:46'),
       (23, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 18:23:16'),
       (24, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 18:23:17'),
       (25, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 18:56:48'),
       (26, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 18:56:49'),
       (27, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 20:58:19'),
       (28, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-13 20:58:20'),
       (29, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 15:31:17'),
       (30, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 15:31:20'),
       (31, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 16:01:30'),
       (32, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 16:01:31'),
       (33, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 16:36:00'),
       (34, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 16:36:01'),
       (35, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 17:12:49'),
       (36, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 17:12:50'),
       (37, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 20:16:42'),
       (38, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 20:16:43'),
       (39, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 20:51:59'),
       (40, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-14 20:52:00'),
       (41, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:11:09'),
       (42, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:11:12'),
       (43, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:42:28'),
       (44, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:42:29'),
       (45, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:55:09'),
       (46, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:55:10'),
       (47, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:56:28'),
       (48, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:56:30'),
       (49, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:56:36'),
       (50, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-15 17:56:38'),
       (51, 1, 1, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 19:51:11'),
       (52, 1, 2, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 19:51:12'),
       (53, 1, 1, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 19:53:23'),
       (54, 1, 2, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 19:53:24'),
       (55, 1, 1, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 20:29:54'),
       (56, 1, 2, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 20:29:56'),
       (57, 1, 1, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 20:45:00'),
       (58, 1, 2, 'PostmanRuntime - Postman Runtime', '0:0:0:0:0:0:0:1', '2024-02-16 20:45:01'),
       (59, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 20:54:07'),
       (60, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 20:54:08'),
       (61, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 21:42:29'),
       (62, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 21:42:30'),
       (63, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 22:13:08'),
       (64, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 22:13:09'),
       (65, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 22:43:22'),
       (66, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 22:43:23'),
       (67, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:44:59'),
       (68, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:45:00'),
       (69, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:51:15'),
       (70, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:51:17'),
       (71, 1, 4, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:51:29'),
       (72, 1, 4, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:51:54'),
       (73, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:51:58'),
       (74, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-16 23:51:59'),
       (75, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-17 14:33:18'),
       (76, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-17 14:33:19'),
       (77, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-17 16:17:17'),
       (78, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-17 16:17:19'),
       (79, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:05:57'),
       (80, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:05:59'),
       (81, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:54:51'),
       (82, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:54:52'),
       (83, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:54:54'),
       (84, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:54:55'),
       (85, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:55:40'),
       (86, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:55:40'),
       (87, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:55:52'),
       (88, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 21:55:53'),
       (89, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 23:45:12'),
       (90, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 23:45:13'),
       (91, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 23:45:16'),
       (92, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 23:45:17'),
       (93, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 23:45:58'),
       (94, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-19 23:45:59'),
       (97, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:10'),
       (98, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:11'),
       (99, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:17'),
       (100, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:18'),
       (101, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:28'),
       (102, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:28'),
       (103, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:33'),
       (104, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:34'),
       (105, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:49'),
       (106, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:48:50'),
       (107, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:49:40'),
       (108, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:49:41'),
       (109, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:51:36'),
       (110, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:51:37'),
       (113, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:51:51'),
       (114, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:51:52'),
       (115, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:51:58'),
       (116, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:51:59'),
       (117, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:52:03'),
       (118, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:52:04'),
       (123, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:53:06'),
       (124, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:53:07'),
       (127, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:54:18'),
       (128, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:54:19'),
       (129, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:54:32'),
       (130, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:54:33'),
       (131, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:55:06'),
       (132, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:55:07'),
       (137, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:55:52'),
       (138, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 02:55:53'),
       (139, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:21:31'),
       (140, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:21:34'),
       (143, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:22:05'),
       (144, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:22:07'),
       (145, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:26:57'),
       (146, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:26:58'),
       (147, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:31:51'),
       (148, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:31:52'),
       (151, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:50:50'),
       (152, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:50:51'),
       (157, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:52:00'),
       (158, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:52:02'),
       (161, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:52:42'),
       (162, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:52:44'),
       (165, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:58:47'),
       (166, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:58:48'),
       (167, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:58:53'),
       (168, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 13:58:54'),
       (173, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:19:42'),
       (174, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:19:43'),
       (175, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:19:47'),
       (176, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:19:49'),
       (179, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:22:13'),
       (180, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:22:14'),
       (181, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:22:30'),
       (182, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:22:30'),
       (183, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:26:07'),
       (184, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:26:08'),
       (185, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:26:12'),
       (186, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:26:13'),
       (189, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:37:32'),
       (190, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:37:33'),
       (191, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:52:10'),
       (192, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:52:11'),
       (193, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:52:29'),
       (194, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:52:30'),
       (195, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:52:36'),
       (196, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:52:36'),
       (197, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:53:19'),
       (198, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:53:19'),
       (199, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:53:26'),
       (200, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:53:27'),
       (201, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:59:06'),
       (202, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 14:59:07'),
       (205, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 15:42:24'),
       (206, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 15:42:25'),
       (209, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 15:42:47'),
       (210, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 15:42:48'),
       (211, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 16:37:22'),
       (212, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 16:37:23'),
       (215, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 17:54:17'),
       (216, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 17:54:18'),
       (217, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 19:45:46'),
       (218, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 19:45:48'),
       (221, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:17:14'),
       (222, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:17:15'),
       (223, 1, 5, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:25:05'),
       (224, 1, 5, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:25:17'),
       (225, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:25:51'),
       (226, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:25:52'),
       (227, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:29:21'),
       (228, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 20:29:22'),
       (235, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:16:53'),
       (236, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:16:54'),
       (237, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:17:06'),
       (238, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:17:07'),
       (239, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:18:19'),
       (240, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:18:20'),
       (243, 2, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:18:36'),
       (244, 2, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:18:37'),
       (276, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:29:07'),
       (277, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:29:08'),
       (278, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:29:19'),
       (279, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:29:20'),
       (280, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:29:54'),
       (281, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:29:55'),
       (282, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:32:08'),
       (283, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-22 21:32:09'),
       (291, 13, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:36:37'),
       (292, 13, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:36:38'),
       (293, 13, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:36:50'),
       (294, 13, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:36:51'),
       (295, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:37:00'),
       (296, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:37:01'),
       (297, 13, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:37:09'),
       (298, 13, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 20:37:10'),
       (299, 13, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 21:05:33'),
       (300, 13, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 21:05:34'),
       (301, 13, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 21:05:38'),
       (302, 13, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 21:05:39'),
       (303, 13, 9, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 21:05:52'),
       (304, 13, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 21:05:58'),
       (305, 13, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-24 21:05:59'),
       (306, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:29:44'),
       (307, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:29:45'),
       (308, 1, 6, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:37:39'),
       (309, 1, 6, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:37:45'),
       (310, 1, 7, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:37:53'),
       (311, 1, 7, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:37:55'),
       (312, 1, 7, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:37:57'),
       (313, 1, 9, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:38:06'),
       (314, 1, 9, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:38:16'),
       (315, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:40:07'),
       (316, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:40:26'),
       (317, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:41:43'),
       (318, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:41:55'),
       (319, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:42:09'),
       (320, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:42:09'),
       (321, 13, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:44:49'),
       (322, 13, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:44:50'),
       (323, 14, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:45:22'),
       (324, 14, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:45:22'),
       (325, 14, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:45:46'),
       (326, 14, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 18:45:47'),
       (327, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 19:13:18'),
       (328, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-26 19:13:19'),
       (329, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 15:57:52'),
       (330, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 15:57:53'),
       (331, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:06:03'),
       (332, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:06:05'),
       (333, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:06:07'),
       (334, 1, 8, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:06:10'),
       (335, 1, 7, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:06:35'),
       (336, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:07:04'),
       (337, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:07:05'),
       (338, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:51:25'),
       (339, 1, 3, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:51:26'),
       (340, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:51:44'),
       (341, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:51:44'),
       (342, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:52:18'),
       (343, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:52:19'),
       (344, 1, 6, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:54:58'),
       (345, 1, 6, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:55:01'),
       (346, 1, 9, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:55:09'),
       (347, 1, 9, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 16:55:24'),
       (348, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 22:09:28'),
       (349, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-02-29 22:09:28'),
       (350, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 10:35:02'),
       (351, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 10:35:03'),
       (352, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 14:17:07'),
       (353, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 14:17:07'),
       (354, 1, 1, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 15:15:59'),
       (355, 1, 2, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 15:16:00'),
       (356, 1, 4, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 15:16:25'),
       (357, 1, 6, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 15:16:32'),
       (358, 1, 6, 'Firefox - Desktop', '127.0.0.1', '2024-03-01 15:16:34');


INSERT INTO UsersRoles
VALUES (1, 1, 3),
       (2, 2, 1),
       (7, 7, 1),
       (13, 13, 1),
       (14, 14, 1);