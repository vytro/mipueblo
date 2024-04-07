package bytron.mipueblo.configuration;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SqlDataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public SqlDataInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {

        String customerCountQuery = "SELECT COUNT(*) FROM customer";
        String invoiceCountQuery = "SELECT COUNT(*) FROM invoice";

        Integer customerCount = jdbcTemplate.queryForObject(customerCountQuery, Integer.class);
        Integer invoiceCount = jdbcTemplate.queryForObject(invoiceCountQuery, Integer.class);

        if (customerCount != null && customerCount > 0 && invoiceCount != null && invoiceCount > 0) {
            return;
        }

        insertCustomers();
        insertInvoices();
    }

    //better for security
//    private void insertCustomers() {
//        jdbcTemplate.update("INSERT INTO customer (id, address, created_date, email, image_url, name, phone, status, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                1, "Cameroon Street", "2024-02-12 17:31:36", "kyle@gmail.com",
//                "https://images.unsplash.com/photo-1480365334925-2aee561aa28e?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDh8dG93SlpGc2twR2d8fGVufDB8fHx8fA%3D%3D",
//                "Kyle Quest", "17038450194", "INACTIVE", "INDIVIDUAL");
//        jdbcTemplate.update("INSERT INTO customer (id, address, created_date, email, image_url, name, phone, status, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
//                102, "No Name Street", "2024-02-02 20:44:26.000000", "newcomment@gmail.com",
//                "https://images.unsplash.com/photo-1637579103895-9ba8218e9aca?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
//                "New Comment", "17038496524", "ACTIVE", "INDIVIDUAL");
//    }

    private void insertCustomers() {
        String sql = "INSERT INTO customer (id, address, created_date, email, image_url, name, phone, status, type) VALUES" +
                "(1, 'Cameroon Street', '2024-02-12 17:31:36.374000', 'kyle@gmail.com'," +
                "'https://images.unsplash.com/photo-1480365334925-2aee561aa28e?w=600&auto=format&fit=crop&q=60&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHx0b3BpYy1mZWVkfDh8dG93SlpGc2twR2d8fGVufDB8fHx8fA%3D%3D'," +
                "'Kyle Quest', '17038450194', 'INACTIVE', 'INDIVIDUAL')," +
                "(2, 'Wallstreet', '2024-02-12 17:32:47.884000', 'tester@gmail.com'," +
                "'https://images.unsplash.com/photo-1622016724812-b0d972e54791?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'," +
                "'Test 1', '17034829444', 'ACTIVE', 'INDIVIDUAL')," +
                "(3, '91551 Vidon Drive', '2024-02-02 20:44:26.000000', 'sdashwood1@microsoft.com'," +
                "'http://dummyimage.com/249x100.png/5fa2dd/ffffff', 'Sukey', '745-619-0337', 'INACTIVE', 'INDIVIDUAL')," +
                "(4, '115 Florence Street', '2024-02-02 20:44:26.000000', 'awinson2@amazon.com'," +
                "'http://dummyimage.com/241x100.png/cc0000/ffffff', 'Angelique', '485-909-6034', 'ACTIVE', 'INDIVIDUAL')," +
                "(5, '7 Dahle Terrace', '2024-02-02 20:44:26.000000', 'hbaynard3@guardian.co.uk'," +
                "'http://dummyimage.com/183x100.png/cc0000/ffffff', 'Harwilll', '886-429-1918', 'INACTIVE', 'INDIVIDUAL')," +
                "(6, '35990 Union Court', '2024-02-02 20:44:26.000000', 'astabbins4@nhs.uk'," +
                "'http://dummyimage.com/239x100.png/ff4444/ffffff', 'Aida', '879-794-9479', 'ACTIVE', 'INDIVIDUAL')," +
                "(7, '03 Kim Point', '2024-02-02 20:44:26.000000', 'cmoriarty5@mapquest.com'," +
                "'http://dummyimage.com/142x100.png/ff4444/ffffff', 'Cary', '117-931-7153', 'INACTIVE', 'INDIVIDUAL')," +
                "(8, '34 La Follette Place', '2024-02-02 20:44:26.000000', 'carter6@globo.com'," +
                "'http://dummyimage.com/128x100.png/ff4444/ffffff', 'Charin', '463-659-9921', 'ACTIVE', 'INDIVIDUAL')," +
                "(9, '71 Forster Lane', '2024-02-02 20:44:26.000000', 'reversfield7@tinyurl.com'," +
                "'http://dummyimage.com/165x100.png/5fa2dd/ffffff', 'Ros', '463-802-9564', 'INACTIVE', 'INDIVIDUAL')," +
                "(10, '21896 Arkansas Road', '2024-02-02 20:44:26.000000', 'ameneghelli8@xing.com'," +
                "'http://dummyimage.com/176x100.png/cc0000/ffffff', 'Allianora', '529-679-5762', 'ACTIVE', 'INDIVIDUAL')," +
                "(11, '62 Judy Center', '2024-02-02 20:44:26.000000', 'wbryde9@princeton.edu'," +
                "'http://dummyimage.com/248x100.png/cc0000/ffffff', 'Wilbur', '856-987-6591', 'INACTIVE', 'INDIVIDUAL')," +
                "(12, '68028 Cody Way', '2024-02-02 20:44:26.000000', 'lbrunrotha@barnesandnoble.com'," +
                "'http://dummyimage.com/158x100.png/dddddd/000000', 'Lib', '811-763-4435', 'ACTIVE', 'INDIVIDUAL')," +
                "(13, '336 Merrick Court', '2024-02-02 20:44:26.000000', 'lruttb@t.co'," +
                "'http://dummyimage.com/208x100.png/cc0000/ffffff', 'Laurena', '410-506-2356', 'INACTIVE', 'INDIVIDUAL')," +
                "(14, '951 Kropf Pass', '2024-02-02 20:44:26.000000', 'kbranchec@dropbox.com'," +
                "'http://dummyimage.com/172x100.png/5fa2dd/ffffff', 'Kessia', '691-307-2447', 'ACTIVE', 'INDIVIDUAL')," +
                "(15, '97 Oak Valley Circle', '2024-02-02 20:44:26.000000', 'khamprechtd@hao123.com'," +
                "'http://dummyimage.com/212x100.png/cc0000/ffffff', 'Kennedy', '713-651-7079', 'INACTIVE', 'INDIVIDUAL')," +
                "(16, '71085 Manitowish Way', '2024-02-02 20:44:26.000000', 'ebollone@ucoz.ru'," +
                "'http://dummyimage.com/131x100.png/dddddd/000000', 'Eamon', '914-691-4410', 'ACTIVE', 'INDIVIDUAL')," +
                "(17, '3 Hintze Plaza', '2024-02-02 20:44:26.000000', 'jpevsnerf@cornell.edu'," +
                "'http://dummyimage.com/173x100.png/5fa2dd/ffffff', 'Joann', '347-532-9386', 'INACTIVE', 'INDIVIDUAL')," +
                "(18, '1925 Derek Trail', '2024-02-02 20:44:26.000000', 'hbenasikg@delicious.com'," +
                "'http://dummyimage.com/208x100.png/ff4444/ffffff', 'Hilly', '643-159-3861', 'ACTIVE', 'INDIVIDUAL')," +
                "(19, '9905 Autumn Leaf Way', '2024-02-02 20:44:26.000000', 'pdalglieshh@wp.com'," +
                "'http://dummyimage.com/149x100.png/dddddd/000000', 'Pru', '338-130-1433', 'INACTIVE', 'INDIVIDUAL')," +
                "(20, '2327 Nancy Alley', '2024-02-02 20:44:26.000000', 'mswynfeni@devhub.com'," +
                "'http://dummyimage.com/160x100.png/ff4444/ffffff', 'Missie', '825-715-5870', 'ACTIVE', 'INDIVIDUAL')," +
                "(21, '2 Dayton Terrace', '2024-02-02 20:44:26.000000', 'mtilerj@wired.com'," +
                "'http://dummyimage.com/143x100.png/cc0000/ffffff', 'Martelle', '994-506-0987', 'INACTIVE', 'INDIVIDUAL')," +
                "(22, '110 Carioca Plaza', '2024-02-02 20:44:26.000000', 'vcollomossek@yolasite.com'," +
                "'http://dummyimage.com/117x100.png/cc0000/ffffff', 'Verine', '311-368-4945', 'ACTIVE', 'INDIVIDUAL')," +
                "(23, '4 Melody Way', '2024-02-02 20:44:26.000000', 'jantonil@barnesandnoble.com'," +
                "'http://dummyimage.com/183x100.png/ff4444/ffffff', 'Josselyn', '498-549-4855', 'INACTIVE', 'INDIVIDUAL')," +
                "(24, '10 Shelley Crossing', '2024-02-02 20:44:26.000000', 'lpetrushkam@sun.com'," +
                "'http://dummyimage.com/190x100.png/5fa2dd/ffffff', 'Letisha', '545-484-6271', 'ACTIVE', 'INDIVIDUAL')," +
                "(25, '2270 Arkansas Hill', '2024-02-02 20:44:26.000000', 'acousinsn@mac.com'," +
                "'http://dummyimage.com/216x100.png/5fa2dd/ffffff', 'Abeu', '952-490-4372', 'INACTIVE', 'INDIVIDUAL')," +
                "(26, '5222 Nova Point', '2024-02-02 20:44:26.000000', 'astanelando@wired.com'," +
                "'http://dummyimage.com/124x100.png/dddddd/000000', 'Ardelis', '975-789-5446', 'ACTIVE', 'INDIVIDUAL')," +
                "(27, '18549 Darwin Court', '2024-02-02 20:44:26.000000', 'rbertomeup@histats.com'," +
                "'http://dummyimage.com/189x100.png/ff4444/ffffff', 'Rubina', '992-936-8769', 'INACTIVE', 'INDIVIDUAL')," +
                "(28, '51726 Mifflin Terrace', '2024-02-02 20:44:26.000000', 'tbennerq@163.com'," +
                "'http://dummyimage.com/166x100.png/5fa2dd/ffffff', 'Thaine', '644-135-4703', 'ACTIVE', 'INDIVIDUAL')," +
                "(29, '1808 Havey Point', '2024-02-02 20:44:26.000000', 'vcreaghr@google.com'," +
                "'http://dummyimage.com/193x100.png/5fa2dd/ffffff', 'Verge', '909-994-2289', 'INACTIVE', 'INDIVIDUAL')," +
                "(30, '67147 Lotheville Place', '2024-02-02 20:44:26.000000', 'cwestmerlands@archive.org'," +
                "'http://dummyimage.com/196x100.png/cc0000/ffffff', 'Calv', '468-414-8138', 'ACTIVE', 'INDIVIDUAL')," +
                "(31, '12026 Warrior Center', '2024-02-02 20:44:26.000000', 'ogovenlockt@opensource.org'," +
                "'http://dummyimage.com/234x100.png/5fa2dd/ffffff', 'Olwen', '698-122-6453', 'INACTIVE', 'INDIVIDUAL')," +
                "(32, '7710 Transport Court', '2024-02-02 20:44:26.000000', 'cbrunsdenu@xinhuanet.com'," +
                "'http://dummyimage.com/103x100.png/cc0000/ffffff', 'Celisse', '221-719-1146', 'ACTIVE', 'INDIVIDUAL')," +
                "(33, '0843 5th Court', '2024-02-02 20:44:26.000000', 'osemainev@house.gov'," +
                "'http://dummyimage.com/144x100.png/cc0000/ffffff', 'Otho', '530-528-8789', 'INACTIVE', 'INDIVIDUAL')," +
                "(34, '93 Glendale Lane', '2024-02-02 20:44:26.000000', 'dfranew@earthlink.net'," +
                "'http://dummyimage.com/141x100.png/dddddd/000000', 'Danya', '722-699-0909', 'ACTIVE', 'INDIVIDUAL')," +
                "(35, '630 Eagan Court', '2024-02-02 20:44:26.000000', 'slangshawx@webmd.com'," +
                "'http://dummyimage.com/154x100.png/ff4444/ffffff', 'Sigismund', '188-550-4822', 'INACTIVE', 'INDIVIDUAL')," +
                "(36, '522 Bultman Road', '2024-02-02 20:44:26.000000', 'aleytony@flickr.com'," +
                "'http://dummyimage.com/212x100.png/dddddd/000000', 'Amandy', '343-687-8075', 'ACTIVE', 'INDIVIDUAL')," +
                "(37, '697 Hoard Way', '2024-02-02 20:44:26.000000', 'mfrottonz@apache.org'," +
                "'http://dummyimage.com/104x100.png/ff4444/ffffff', 'Maddy', '523-955-5240', 'INACTIVE', 'INDIVIDUAL')," +
                "(38, '68369 Merry Lane', '2024-02-02 20:44:26.000000', 'jsisland10@surveymonkey.com'," +
                "'http://dummyimage.com/147x100.png/dddddd/000000', 'Julee', '984-997-8386', 'ACTIVE', 'INDIVIDUAL')," +
                "(39, '96766 Lakewood Gardens Parkway', '2024-02-02 20:44:26.000000', 'lpatience11@facebook.com'," +
                "'http://dummyimage.com/134x100.png/5fa2dd/ffffff', 'Leontine', '720-957-6373', 'INACTIVE', 'INDIVIDUAL')," +
                "(40, '860 Westport Alley', '2024-02-02 20:44:26.000000', 'fthoma12@cisco.com'," +
                "'http://dummyimage.com/130x100.png/dddddd/000000', 'Fred', '481-742-6919', 'ACTIVE', 'INDIVIDUAL')," +
                "(41, '71 Mariners Cove Trail', '2024-02-02 20:44:26.000000', 'kfaivre13@indiegogo.com'," +
                "'http://dummyimage.com/210x100.png/ff4444/ffffff', 'Ky', '945-993-5520', 'INACTIVE', 'INDIVIDUAL')," +
                "(42, '64 Clove Center', '2024-02-02 20:44:26.000000', 'ebucktharp14@behance.net'," +
                "'http://dummyimage.com/175x100.png/5fa2dd/ffffff', 'Enrica', '943-410-1418', 'ACTIVE', 'INDIVIDUAL')," +
                "(43, '2316 Rockefeller Court', '2024-02-02 20:44:26.000000', 'gaspall15@dell.com'," +
                "'http://dummyimage.com/223x100.png/dddddd/000000', 'Gannie', '685-678-3214', 'INACTIVE', 'INDIVIDUAL')," +
                "(44, '2 Waywood Way', '2024-02-02 20:44:26.000000', 'sdisbury16@joomla.org'," +
                "'http://dummyimage.com/134x100.png/5fa2dd/ffffff', 'Sonia', '970-434-0794', 'ACTIVE', 'INDIVIDUAL')," +
                "(45, '7398 Banding Road', '2024-02-02 20:44:26.000000', 'mguyonnet17@delicious.com'," +
                "'http://dummyimage.com/239x100.png/dddddd/000000', 'Mozes', '987-254-9816', 'INACTIVE', 'INDIVIDUAL')," +
                "(46, '90789 Butterfield Place', '2024-02-02 20:44:26.000000', 'rbengtsson18@miitbeian.gov.cn'," +
                "'http://dummyimage.com/189x100.png/ff4444/ffffff', 'Ric', '133-928-2873', 'ACTIVE', 'INDIVIDUAL')," +
                "(47, '87781 Sunbrook Way', '2024-02-02 20:44:26.000000', 'kissatt19@youku.com'," +
                "'http://dummyimage.com/231x100.png/dddddd/000000', 'Kilian', '680-354-9156', 'INACTIVE', 'INDIVIDUAL')," +
                "(48, '0 Ronald Regan Alley', '2024-02-02 20:44:26.000000', 'cgarbutt1a@netvibes.com'," +
                "'http://dummyimage.com/205x100.png/5fa2dd/ffffff', 'Corri', '865-490-4379', 'ACTIVE', 'INDIVIDUAL')," +
                "(49, '9 Sunbrook Court', '2024-02-02 20:44:26.000000', 'lkamienski1b@cnn.com'," +
                "'http://dummyimage.com/232x100.png/5fa2dd/ffffff', 'Lotta', '770-294-4654', 'INACTIVE', 'INDIVIDUAL')," +
                "(50, '91081 Boyd Plaza', '2024-02-02 20:44:26.000000', 'ahaddrill1c@sbwire.com'," +
                "'http://dummyimage.com/107x100.png/dddddd/000000', 'Arleen', '783-197-1813', 'ACTIVE', 'INDIVIDUAL')," +
                "(51, '6729 Oakridge Way', '2024-02-02 20:44:26.000000', 'bhardware1d@globo.com'," +
                "'http://dummyimage.com/234x100.png/ff4444/ffffff', 'Bord', '560-693-7448', 'INACTIVE', 'INDIVIDUAL')," +
                "(52, '80 Mcguire Park', '2024-02-02 20:44:26.000000', 'descale1e@mac.com'," +
                "'http://dummyimage.com/112x100.png/cc0000/ffffff', 'Dietrich', '978-156-9012', 'ACTIVE', 'INDIVIDUAL')," +
                "(53, '2 Morningstar Parkway', '2024-02-02 20:44:26.000000', 'bphippen1f@livejournal.com'," +
                "'http://dummyimage.com/193x100.png/dddddd/000000', 'Benedikta', '753-360-4646', 'INACTIVE', 'INDIVIDUAL')," +
                "(54, '205 Pankratz Alley', '2024-02-02 20:44:26.000000', 'clonergan1g@ehow.com'," +
                "'http://dummyimage.com/188x100.png/cc0000/ffffff', 'Claus', '705-864-4661', 'ACTIVE', 'INDIVIDUAL')," +
                "(55, '18 Shoshone Center', '2024-02-02 20:44:26.000000', 'lbaldock1h@instagram.com'," +
                "'http://dummyimage.com/203x100.png/5fa2dd/ffffff', 'Leigha', '515-805-9225', 'INACTIVE', 'INDIVIDUAL')," +
                "(56, '78 Esch Way', '2024-02-02 20:44:26.000000', 'asynnot1i@sbwire.com'," +
                "'http://dummyimage.com/130x100.png/dddddd/000000', 'Aldous', '853-434-7802', 'ACTIVE', 'INDIVIDUAL')," +
                "(57, '02 Larry Terrace', '2024-02-02 20:44:26.000000', 'garundel1j@blinklist.com'," +
                "'http://dummyimage.com/183x100.png/dddddd/000000', 'Gav', '933-801-8305', 'INACTIVE', 'INDIVIDUAL')," +
                "(58, '578 Anthes Circle', '2024-02-02 20:44:26.000000', 'dabrahams1k@arizona.edu'," +
                "'http://dummyimage.com/247x100.png/dddddd/000000', 'Delphinia', '565-164-0311', 'ACTIVE', 'INDIVIDUAL')," +
                "(59, '208 Duke Point', '2024-02-02 20:44:26.000000', 'bpattle1l@virginia.edu'," +
                "'http://dummyimage.com/108x100.png/ff4444/ffffff', 'Baird', '718-761-6062', 'INACTIVE', 'INDIVIDUAL')," +
                "(60, '3 Scott Terrace', '2024-02-02 20:44:26.000000', 'epyrah1m@nationalgeographic.com'," +
                "'http://dummyimage.com/169x100.png/dddddd/000000', 'Englebert', '308-517-9653', 'ACTIVE', 'INDIVIDUAL')," +
                "(61, '31872 Delaware Road', '2024-02-02 20:44:26.000000', 'sattenbrow1n@auda.org.au'," +
                "'http://dummyimage.com/142x100.png/cc0000/ffffff', 'Shay', '640-520-5593', 'INACTIVE', 'INDIVIDUAL')," +
                "(62, '45 Vermont Terrace', '2024-02-02 20:44:26.000000', 'rtarling1o@51.la'," +
                "'http://dummyimage.com/193x100.png/ff4444/ffffff', 'Ramsey', '677-873-8833', 'ACTIVE', 'INDIVIDUAL')," +
                "(63, '3428 Bartillon Place', '2024-02-02 20:44:26.000000', 'loshiel1p@over-blog.com'," +
                "'http://dummyimage.com/215x100.png/cc0000/ffffff', 'Leicester', '717-324-4250', 'INACTIVE', 'INDIVIDUAL')," +
                "(64, '020 Summerview Street', '2024-02-02 20:44:26.000000', 'gcordelette1q@netvibes.com'," +
                "'http://dummyimage.com/250x100.png/cc0000/ffffff', 'Gelya', '902-971-1530', 'ACTIVE', 'INDIVIDUAL')," +
                "(65, '3 Lakewood Circle', '2024-02-02 20:44:26.000000', 'mgogarty1r@seesaa.net'," +
                "'http://dummyimage.com/178x100.png/dddddd/000000', 'Megan', '362-991-6550', 'INACTIVE', 'INDIVIDUAL')," +
                "(66, '44785 Talmadge Center', '2024-02-02 20:44:26.000000', 'mabela1s@wikimedia.org'," +
                "'http://dummyimage.com/228x100.png/dddddd/000000', 'Margie', '206-787-0460', 'ACTIVE', 'INDIVIDUAL')," +
                "(67, '44 Iowa Alley', '2024-02-02 20:44:26.000000', 'sdecent1t@blogtalkradio.com'," +
                "'http://dummyimage.com/185x100.png/cc0000/ffffff', 'Sabrina', '755-639-4583', 'INACTIVE', 'INDIVIDUAL')," +
                "(68, '37856 Hoffman Alley', '2024-02-02 20:44:26.000000', 'eyemm1u@psu.edu'," +
                "'http://dummyimage.com/178x100.png/cc0000/ffffff', 'Ellsworth', '684-369-4908', 'ACTIVE', 'INDIVIDUAL')," +
                "(69, '477 Hintze Circle', '2024-02-02 20:44:26.000000', 'imanifold1v@indiatimes.com'," +
                "'http://dummyimage.com/209x100.png/5fa2dd/ffffff', 'Imogene', '932-347-4709', 'INACTIVE', 'INDIVIDUAL')," +
                "(70, '8774 Vera Alley', '2024-02-02 20:44:26.000000', 'tdreini1w@dailymail.co.uk'," +
                "'http://dummyimage.com/181x100.png/5fa2dd/ffffff', 'Tilly', '866-625-1531', 'ACTIVE', 'INDIVIDUAL')," +
                "(71, '6 Dorton Plaza', '2024-02-02 20:44:26.000000', 'lkrauss1x@howstuffworks.com'," +
                "'http://dummyimage.com/136x100.png/5fa2dd/ffffff', 'Lutero', '741-798-1681', 'INACTIVE', 'INDIVIDUAL')," +
                "(72, '0420 Glendale Center', '2024-02-02 20:44:26.000000', 'uperceval1y@lulu.com'," +
                "'http://dummyimage.com/144x100.png/5fa2dd/ffffff', 'Upton', '756-803-0652', 'ACTIVE', 'INDIVIDUAL')," +
                "(73, '79 Garrison Junction', '2024-02-02 20:44:26.000000', 'bbreens1z@mayoclinic.com'," +
                "'http://dummyimage.com/135x100.png/ff4444/ffffff', 'Brittany', '399-954-0598', 'INACTIVE', 'INDIVIDUAL')," +
                "(74, '7 Jay Alley', '2024-02-02 20:44:26.000000', 'dbeardshall20@google.it'," +
                "'http://dummyimage.com/229x100.png/ff4444/ffffff', 'Davetaa', '286-965-8143', 'ACTIVE', 'INDIVIDUAL')," +
                "(75, '74715 Donald Court', '2024-02-02 20:44:26.000000', 'pnaden21@uiuc.edu'," +
                "'http://dummyimage.com/211x100.png/ff4444/ffffff', 'Preston', '993-821-3196', 'INACTIVE', 'INDIVIDUAL')," +
                "(76, '2 Kensington Pass', '2024-02-02 20:44:26.000000', 'ytyzack22@opensource.org'," +
                "'http://dummyimage.com/249x100.png/dddddd/000000', 'Yvor', '795-315-8664', 'ACTIVE', 'INDIVIDUAL')," +
                "(77, '2595 Calypso Point', '2024-02-02 20:44:26.000000', 'jzmitrovich23@about.com'," +
                "'http://dummyimage.com/167x100.png/dddddd/000000', 'Julianna', '341-744-1422', 'INACTIVE', 'INDIVIDUAL')," +
                "(78, '98 Haas Hill', '2024-02-02 20:44:26.000000', 'oosman24@si.edu'," +
                "'http://dummyimage.com/129x100.png/cc0000/ffffff', 'Osborn', '841-913-5204', 'ACTIVE', 'INDIVIDUAL')," +
                "(79, '76 Mayer Pass', '2024-02-02 20:44:26.000000', 'dlaudham25@gizmodo.com'," +
                "'http://dummyimage.com/110x100.png/ff4444/ffffff', 'Drucie', '331-923-2128', 'INACTIVE', 'INDIVIDUAL')," +
                "(80, '7409 Maple Park', '2024-02-02 20:44:26.000000', 'rsemper26@odnoklassniki.ru'," +
                "'http://dummyimage.com/167x100.png/dddddd/000000', 'Rusty', '941-914-0738', 'ACTIVE', 'INDIVIDUAL')," +
                "(81, '1 Petterle Junction', '2024-02-02 20:44:26.000000', 'abailiss27@gnu.org'," +
                "'http://dummyimage.com/174x100.png/dddddd/000000', 'Andy', '161-111-4366', 'INACTIVE', 'INDIVIDUAL')," +
                "(82, '01 Waubesa Street', '2024-02-02 20:44:26.000000', 'ldevuyst28@tiny.cc'," +
                "'http://dummyimage.com/248x100.png/cc0000/ffffff', 'Loise', '981-552-7564', 'ACTIVE', 'INDIVIDUAL')," +
                "(83, '07461 Talmadge Place', '2024-02-02 20:44:26.000000', 'cvarfalameev29@linkedin.com'," +
                "'http://dummyimage.com/224x100.png/ff4444/ffffff', 'Colin', '273-493-2099', 'INACTIVE', 'INDIVIDUAL')," +
                "(84, '142 Mitchell Plaza', '2024-02-02 20:44:26.000000', 'jbaudi2a@yale.edu'," +
                "'http://dummyimage.com/171x100.png/dddddd/000000', 'Jamie', '349-145-3526', 'ACTIVE', 'INDIVIDUAL')," +
                "(85, '2421 Southridge Street', '2024-02-02 20:44:26.000000', 'rskirlin2b@e-recht24.de'," +
                "'http://dummyimage.com/204x100.png/5fa2dd/ffffff', 'Raddie', '292-734-8652', 'INACTIVE', 'INDIVIDUAL')," +
                "(86, '360 Center Point', '2024-02-02 20:44:26.000000', 'ctrewhela2c@narod.ru'," +
                "'http://dummyimage.com/137x100.png/5fa2dd/ffffff', 'Candace', '292-121-4356', 'ACTIVE', 'INDIVIDUAL')," +
                "(87, '9 Grim Hill', '2024-02-02 20:44:26.000000', 'rshoebotham2d@apple.com'," +
                "'http://dummyimage.com/238x100.png/dddddd/000000', 'Robbie', '399-948-6261', 'INACTIVE', 'INDIVIDUAL')," +
                "(88, '2543 Veith Court', '2024-02-02 20:44:26.000000', 'gadamsky2e@aol.com'," +
                "'http://dummyimage.com/181x100.png/5fa2dd/ffffff', 'Gray', '937-600-3465', 'ACTIVE', 'INDIVIDUAL')," +
                "(89, '42 Kensington Center', '2024-02-02 20:44:26.000000', 'skharchinski2f@imdb.com'," +
                "'http://dummyimage.com/241x100.png/dddddd/000000', 'Sigismond', '287-970-3258', 'INACTIVE', 'INDIVIDUAL')," +
                "(90, '1044 Quincy Junction', '2024-02-02 20:44:26.000000', 'lstreets2g@epa.gov'," +
                "'http://dummyimage.com/183x100.png/cc0000/ffffff', 'Lexi', '375-752-0320', 'ACTIVE', 'INDIVIDUAL')," +
                "(91, '525 Milwaukee Plaza', '2024-02-02 20:44:26.000000', 'mblaxland2h@wired.com'," +
                "'http://dummyimage.com/182x100.png/dddddd/000000', 'Misha', '103-549-9422', 'INACTIVE', 'INDIVIDUAL')," +
                "(92, '00 Sycamore Trail', '2024-02-02 20:44:26.000000', 'rmccullough2i@rakuten.co.jp'," +
                "'http://dummyimage.com/174x100.png/cc0000/ffffff', 'Raddie', '918-499-9361', 'ACTIVE', 'INDIVIDUAL')," +
                "(93, '7 Harbort Road', '2024-02-02 20:44:26.000000', 'edootson2j@vkontakte.ru'," +
                "'http://dummyimage.com/102x100.png/cc0000/ffffff', 'Estel', '580-109-6275', 'INACTIVE', 'INDIVIDUAL')," +
                "(94, '60 Oriole Circle', '2024-02-02 20:44:26.000000', 'mhanford2k@google.pl'," +
                "'http://dummyimage.com/230x100.png/5fa2dd/ffffff', 'Mycah', '812-818-3065', 'ACTIVE', 'INDIVIDUAL')," +
                "(95, '1 School Court', '2024-02-02 20:44:26.000000', 'rkrochmann2l@ning.com'," +
                "'http://dummyimage.com/114x100.png/5fa2dd/ffffff', 'Rosemarie', '435-284-8167', 'INACTIVE', 'INDIVIDUAL')," +
                "(96, '87665 Cardinal Alley', '2024-02-02 20:44:26.000000', 'vjansen2m@npr.org'," +
                "'http://dummyimage.com/191x100.png/dddddd/000000', 'Vivyan', '541-740-0444', 'ACTIVE', 'INDIVIDUAL')," +
                "(97, '52 Fairfield Center', '2024-02-02 20:44:26.000000', 'mpeltz2n@rambler.ru'," +
                "'http://dummyimage.com/138x100.png/ff4444/ffffff', 'Mallory', '177-773-4282', 'INACTIVE', 'INDIVIDUAL')," +
                "(98, '2 Del Mar Avenue', '2024-02-02 20:44:26.000000', 'rgiacopello2o@state.tx.us'," +
                "'http://dummyimage.com/191x100.png/ff4444/ffffff', 'Rosina', '302-759-7432', 'ACTIVE', 'INDIVIDUAL')," +
                "(99, '5890 Merrick Hill', '2024-02-02 20:44:26.000000', 'ublasli2p@sfgate.com'," +
                "'http://dummyimage.com/139x100.png/5fa2dd/ffffff', 'Ulrikaumeko', '487-387-1532', 'INACTIVE', 'INDIVIDUAL')," +
                "(100, '44 East Parkway', '2024-02-02 20:44:26.000000', 'dtincknell2q@utexas.edu'," +
                "'http://dummyimage.com/116x100.png/5fa2dd/ffffff', 'Desiree', '163-799-2665', 'ACTIVE', 'INDIVIDUAL')," +
                "(101, '6195 Dapin Terrace', '2024-02-02 20:44:26.000000', 'afrizzell2r@berkeley.edu'," +
                "'http://dummyimage.com/228x100.png/cc0000/ffffff', 'Aristotle', '435-839-7976', 'INACTIVE', 'INDIVIDUAL')," +
                "(102, 'No Name Street', '2024-02-02 20:44:26.000000', 'newcomment@gmail.com'," +
                "'https://images.unsplash.com/photo-1637579103895-9ba8218e9aca?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'," +
                "'New Comment', '17038496524', 'ACTIVE', 'INDIVIDUAL')," +
                "(103, 'A Street Name', '2024-02-12 17:33:36.374000', 'tester2@gmail.com'," +
                "'https://images.unsplash.com/photo-1622016724812-b0d972e54791?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'," +
                "'Test 2', '17038473921', 'INACTIVE', 'INDIVIDUAL')," +
                "(104, 'Unique Street', '2024-02-12 19:25:19.686000', 'uniquee@gmail.com'," +
                "'https://images.unsplash.com/photo-1637579103895-9ba8218e9aca?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D'," +
                "'Unique Name', '17038278322', 'ACTIVE', 'INDIVIDUAL')";
        jdbcTemplate.update(sql);
    }


    //better for security
//    private void insertInvoices() {
//        jdbcTemplate.update("INSERT INTO invoice (id, date, invoice_number, services, status, total, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
//                1, "2024-02-08 20:00:00", "QIVUGHOJ", "1 Limpieza Hogar 300, 2 Inspeccion Tecnica 200", "PENDING", 500, 102);
//        jdbcTemplate.update("INSERT INTO invoice (id, date, invoice_number, services, status, total, customer_id) VALUES (?, ?, ?, ?, ?, ?, ?)",
//                2, "2024-02-07 20:00:00.000000", "3L8GIB7D", "1 Limpieza General 200 bs.", "OVERDUE", 200, 1);
//    }

    private void insertInvoices() {
        String sql = "INSERT INTO invoice (id, date, invoice_number, services, status, total, customer_id) VALUES " +
                "(1, '2024-02-08 20:00:00', 'QIVUGHOJ', '1 Limpieza Hogar 300, 2 Inspeccion Tecnica 200', 'PENDING', 500, 102), " +
                "(2, '2024-02-07 20:00:00.000000', '3L8GIB7D', '1 Limpieza General 200 bs.', 'OVERDUE', 200, 1), " +
                "(3, '2024-02-10 20:00:00.000000', '3RFGPCBV', '1 Limpieza Hogar 100, 2 Inspeccion Tecnica 100', 'PENDING', 200, 1), " +
                "(4, '2024-02-22 20:00:00.000000', 'OSWZI9EI', '1 Lava Auto 100bs', 'PENDING', 100, 1)";
        jdbcTemplate.update(sql);
    }


}
