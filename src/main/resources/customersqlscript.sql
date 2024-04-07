-- sets the value of status to 'ACTIVE' for rows where the ID of the customer is even 
-- divisible by 2 without a remainder
UPDATE customer SET status = 'ACTIVE' WHERE MOD(id, 2) = 0;

-- sets the value of status to 'INACTIVE' for rows where the ID of the customer is odd 
-- not divisible by 2 a remainder
UPDATE customer SET status = 'INACTIVE' WHERE MOD(id, 2) = 1;

UPDATE customer SET type = 'INDIVIDUAL';

UPDATE customer SET created_date = CURRENT_TIMESTAMP WHERE id > 2;

UPDATE customer 
SET created_date = DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 WEEK)
WHERE id > 2 AND id NOT IN (103, 104);

SHOW VARIABLES LIKE 'sql_safe_updates';

SET SQL_SAFE_UPDATES = FALSE;
SET SQL_SAFE_UPDATES = TRUE;



insert into customer (name, email, address, phone, image_url) values ('Sukey', 'sdashwood1@microsoft.com', '91551 Vidon Drive', '745-619-0337', 'http://dummyimage.com/249x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Angelique', 'awinson2@amazon.com', '115 Florence Street', '485-909-6034', 'http://dummyimage.com/241x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Harwilll', 'hbaynard3@guardian.co.uk', '7 Dahle Terrace', '886-429-1918', 'http://dummyimage.com/183x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Aida', 'astabbins4@nhs.uk', '35990 Union Court', '879-794-9479', 'http://dummyimage.com/239x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Cary', 'cmoriarty5@mapquest.com', '03 Kim Point', '117-931-7153', 'http://dummyimage.com/142x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Charin', 'carter6@globo.com', '34 La Follette Place', '463-659-9921', 'http://dummyimage.com/128x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Ros', 'reversfield7@tinyurl.com', '71 Forster Lane', '463-802-9564', 'http://dummyimage.com/165x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Allianora', 'ameneghelli8@xing.com', '21896 Arkansas Road', '529-679-5762', 'http://dummyimage.com/176x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Wilbur', 'wbryde9@princeton.edu', '62 Judy Center', '856-987-6591', 'http://dummyimage.com/248x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Lib', 'lbrunrotha@barnesandnoble.com', '68028 Cody Way', '811-763-4435', 'http://dummyimage.com/158x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Laurena', 'lruttb@t.co', '336 Merrick Court', '410-506-2356', 'http://dummyimage.com/208x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Kessia', 'kbranchec@dropbox.com', '951 Kropf Pass', '691-307-2447', 'http://dummyimage.com/172x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Kennedy', 'khamprechtd@hao123.com', '97 Oak Valley Circle', '713-651-7079', 'http://dummyimage.com/212x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Eamon', 'ebollone@ucoz.ru', '71085 Manitowish Way', '914-691-4410', 'http://dummyimage.com/131x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Joann', 'jpevsnerf@cornell.edu', '3 Hintze Plaza', '347-532-9386', 'http://dummyimage.com/173x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Hilly', 'hbenasikg@delicious.com', '1925 Derek Trail', '643-159-3861', 'http://dummyimage.com/208x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Pru', 'pdalglieshh@wp.com', '9905 Autumn Leaf Way', '338-130-1433', 'http://dummyimage.com/149x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Missie', 'mswynfeni@devhub.com', '2327 Nancy Alley', '825-715-5870', 'http://dummyimage.com/160x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Martelle', 'mtilerj@wired.com', '2 Dayton Terrace', '994-506-0987', 'http://dummyimage.com/143x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Verine', 'vcollomossek@yolasite.com', '110 Carioca Plaza', '311-368-4945', 'http://dummyimage.com/117x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Josselyn', 'jantonil@barnesandnoble.com', '4 Melody Way', '498-549-4855', 'http://dummyimage.com/183x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Letisha', 'lpetrushkam@sun.com', '10 Shelley Crossing', '545-484-6271', 'http://dummyimage.com/190x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Abeu', 'acousinsn@mac.com', '2270 Arkansas Hill', '952-490-4372', 'http://dummyimage.com/216x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Ardelis', 'astanelando@wired.com', '5222 Nova Point', '975-789-5446', 'http://dummyimage.com/124x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Rubina', 'rbertomeup@histats.com', '18549 Darwin Court', '992-936-8769', 'http://dummyimage.com/189x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Thaine', 'tbennerq@163.com', '51726 Mifflin Terrace', '644-135-4703', 'http://dummyimage.com/166x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Verge', 'vcreaghr@google.com', '1808 Havey Point', '909-994-2289', 'http://dummyimage.com/193x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Calv', 'cwestmerlands@archive.org', '67147 Lotheville Place', '468-414-8138', 'http://dummyimage.com/196x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Olwen', 'ogovenlockt@opensource.org', '12026 Warrior Center', '698-122-6453', 'http://dummyimage.com/234x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Celisse', 'cbrunsdenu@xinhuanet.com', '7710 Transport Court', '221-719-1146', 'http://dummyimage.com/103x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Otho', 'osemainev@house.gov', '0843 5th Court', '530-528-8789', 'http://dummyimage.com/144x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Danya', 'dfranew@earthlink.net', '93 Glendale Lane', '722-699-0909', 'http://dummyimage.com/141x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Sigismund', 'slangshawx@webmd.com', '630 Eagan Court', '188-550-4822', 'http://dummyimage.com/154x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Amandy', 'aleytony@flickr.com', '522 Bultman Road', '343-687-8075', 'http://dummyimage.com/212x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Maddy', 'mfrottonz@apache.org', '697 Hoard Way', '523-955-5240', 'http://dummyimage.com/104x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Julee', 'jsisland10@surveymonkey.com', '68369 Merry Lane', '984-997-8386', 'http://dummyimage.com/147x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Leontine', 'lpatience11@facebook.com', '96766 Lakewood Gardens Parkway', '720-957-6373', 'http://dummyimage.com/134x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Fred', 'fthoma12@cisco.com', '860 Westport Alley', '481-742-6919', 'http://dummyimage.com/130x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Ky', 'kfaivre13@indiegogo.com', '71 Mariners Cove Trail', '945-993-5520', 'http://dummyimage.com/210x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Enrica', 'ebucktharp14@behance.net', '64 Clove Center', '943-410-1418', 'http://dummyimage.com/175x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Gannie', 'gaspall15@dell.com', '2316 Rockefeller Court', '685-678-3214', 'http://dummyimage.com/223x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Sonia', 'sdisbury16@joomla.org', '2 Waywood Way', '970-434-0794', 'http://dummyimage.com/134x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Mozes', 'mguyonnet17@delicious.com', '7398 Banding Road', '987-254-9816', 'http://dummyimage.com/239x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Ric', 'rbengtsson18@miitbeian.gov.cn', '90789 Butterfield Place', '133-928-2873', 'http://dummyimage.com/189x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Kilian', 'kissatt19@youku.com', '87781 Sunbrook Way', '680-354-9156', 'http://dummyimage.com/231x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Corri', 'cgarbutt1a@netvibes.com', '0 Ronald Regan Alley', '865-490-4379', 'http://dummyimage.com/205x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Lotta', 'lkamienski1b@cnn.com', '9 Sunbrook Court', '770-294-4654', 'http://dummyimage.com/232x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Arleen', 'ahaddrill1c@sbwire.com', '91081 Boyd Plaza', '783-197-1813', 'http://dummyimage.com/107x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Bord', 'bhardware1d@globo.com', '6729 Oakridge Way', '560-693-7448', 'http://dummyimage.com/234x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Dietrich', 'descale1e@mac.com', '80 Mcguire Park', '978-156-9012', 'http://dummyimage.com/112x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Benedikta', 'bphippen1f@livejournal.com', '2 Morningstar Parkway', '753-360-4646', 'http://dummyimage.com/193x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Claus', 'clonergan1g@ehow.com', '205 Pankratz Alley', '705-864-4661', 'http://dummyimage.com/188x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Leigha', 'lbaldock1h@instagram.com', '18 Shoshone Center', '515-805-9225', 'http://dummyimage.com/203x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Aldous', 'asynnot1i@sbwire.com', '78 Esch Way', '853-434-7802', 'http://dummyimage.com/130x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Gav', 'garundel1j@blinklist.com', '02 Larry Terrace', '933-801-8305', 'http://dummyimage.com/183x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Delphinia', 'dabrahams1k@arizona.edu', '578 Anthes Circle', '565-164-0311', 'http://dummyimage.com/247x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Baird', 'bpattle1l@virginia.edu', '208 Duke Point', '718-761-6062', 'http://dummyimage.com/108x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Englebert', 'epyrah1m@nationalgeographic.com', '3 Scott Terrace', '308-517-9653', 'http://dummyimage.com/169x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Shay', 'sattenbrow1n@auda.org.au', '31872 Delaware Road', '640-520-5593', 'http://dummyimage.com/142x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Ramsey', 'rtarling1o@51.la', '45 Vermont Terrace', '677-873-8833', 'http://dummyimage.com/193x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Leicester', 'loshiel1p@over-blog.com', '3428 Bartillon Place', '717-324-4250', 'http://dummyimage.com/215x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Gelya', 'gcordelette1q@netvibes.com', '020 Summerview Street', '902-971-1530', 'http://dummyimage.com/250x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Megan', 'mgogarty1r@seesaa.net', '3 Lakewood Circle', '362-991-6550', 'http://dummyimage.com/178x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Margie', 'mabela1s@wikimedia.org', '44785 Talmadge Center', '206-787-0460', 'http://dummyimage.com/228x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Sabrina', 'sdecent1t@blogtalkradio.com', '44 Iowa Alley', '755-639-4583', 'http://dummyimage.com/185x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Ellsworth', 'eyemm1u@psu.edu', '37856 Hoffman Alley', '684-369-4908', 'http://dummyimage.com/178x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Imogene', 'imanifold1v@indiatimes.com', '477 Hintze Circle', '932-347-4709', 'http://dummyimage.com/209x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Tilly', 'tdreini1w@dailymail.co.uk', '8774 Vera Alley', '866-625-1531', 'http://dummyimage.com/181x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Lutero', 'lkrauss1x@howstuffworks.com', '6 Dorton Plaza', '741-798-1681', 'http://dummyimage.com/136x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Upton', 'uperceval1y@lulu.com', '0420 Glendale Center', '756-803-0652', 'http://dummyimage.com/144x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Brittany', 'bbreens1z@mayoclinic.com', '79 Garrison Junction', '399-954-0598', 'http://dummyimage.com/135x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Daveta', 'dbeardshall20@google.it', '7 Jay Alley', '286-965-8143', 'http://dummyimage.com/229x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Preston', 'pnaden21@uiuc.edu', '74715 Donald Court', '993-821-3196', 'http://dummyimage.com/211x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Yvor', 'ytyzack22@opensource.org', '2 Kensington Pass', '795-315-8664', 'http://dummyimage.com/249x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Julianna', 'jzmitrovich23@about.com', '2595 Calypso Point', '341-744-1422', 'http://dummyimage.com/167x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Osborn', 'oosman24@si.edu', '98 Haas Hill', '841-913-5204', 'http://dummyimage.com/129x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Drucie', 'dlaudham25@gizmodo.com', '76 Mayer Pass', '331-923-2128', 'http://dummyimage.com/110x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Rusty', 'rsemper26@odnoklassniki.ru', '7409 Maple Park', '941-914-0738', 'http://dummyimage.com/167x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Andy', 'abailiss27@gnu.org', '1 Petterle Junction', '161-111-4366', 'http://dummyimage.com/174x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Loise', 'ldevuyst28@tiny.cc', '01 Waubesa Street', '981-552-7564', 'http://dummyimage.com/248x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Colin', 'cvarfalameev29@linkedin.com', '07461 Talmadge Place', '273-493-2099', 'http://dummyimage.com/224x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Jamie', 'jbaudi2a@yale.edu', '142 Mitchell Plaza', '349-145-3526', 'http://dummyimage.com/171x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Raddie', 'rskirlin2b@e-recht24.de', '2421 Southridge Street', '292-734-8652', 'http://dummyimage.com/204x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Candace', 'ctrewhela2c@narod.ru', '360 Center Point', '292-121-4356', 'http://dummyimage.com/137x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Robbie', 'rshoebotham2d@apple.com', '9 Grim Hill', '399-948-6261', 'http://dummyimage.com/238x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Gray', 'gadamsky2e@aol.com', '2543 Veith Court', '937-600-3465', 'http://dummyimage.com/181x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Sigismond', 'skharchinski2f@imdb.com', '42 Kensington Center', '287-970-3258', 'http://dummyimage.com/241x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Lexi', 'lstreets2g@epa.gov', '1044 Quincy Junction', '375-752-0320', 'http://dummyimage.com/183x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Misha', 'mblaxland2h@wired.com', '525 Milwaukee Plaza', '103-549-9422', 'http://dummyimage.com/182x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Raddie', 'rmccullough2i@rakuten.co.jp', '00 Sycamore Trail', '918-499-9361', 'http://dummyimage.com/174x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Estel', 'edootson2j@vkontakte.ru', '7 Harbort Road', '580-109-6275', 'http://dummyimage.com/102x100.png/cc0000/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Mycah', 'mhanford2k@google.pl', '60 Oriole Circle', '812-818-3065', 'http://dummyimage.com/230x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Rosemarie', 'rkrochmann2l@ning.com', '1 School Court', '435-284-8167', 'http://dummyimage.com/114x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Vivyan', 'vjansen2m@npr.org', '87665 Cardinal Alley', '541-740-0444', 'http://dummyimage.com/191x100.png/dddddd/000000');
insert into customer (name, email, address, phone, image_url) values ('Mallory', 'mpeltz2n@rambler.ru', '52 Fairfield Center', '177-773-4282', 'http://dummyimage.com/138x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Rosina', 'rgiacopello2o@state.tx.us', '2 Del Mar Avenue', '302-759-7432', 'http://dummyimage.com/191x100.png/ff4444/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Ulrikaumeko', 'ublasli2p@sfgate.com', '5890 Merrick Hill', '487-387-1532', 'http://dummyimage.com/139x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Desiree', 'dtincknell2q@utexas.edu', '44 East Parkway', '163-799-2665', 'http://dummyimage.com/116x100.png/5fa2dd/ffffff');
insert into customer (name, email, address, phone, image_url) values ('Aristotle', 'afrizzell2r@berkeley.edu', '6195 Dapin Terrace', '435-839-7976', 'http://dummyimage.com/228x100.png/cc0000/ffffff');