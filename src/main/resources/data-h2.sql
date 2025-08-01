--- password lorenzo in bcrypt
insert into users (username, password, role, created, last_update) VALUES
('lorenzo', '$2a$10$CQViJR5.yIcVJzfeGPMRMeeb9aZbUOCga.MJhbKVaq1ooxIdrF4mm', 'admin', now(), now());

insert into courses values (1, 'Primi');
insert into courses values (2, 'Secondi');
insert into courses values (3, 'Panzanelle');
insert into courses values (4, 'Bar');

insert into departments values (1, 'Cucina');
insert into departments values (2, 'Griglia');
insert into departments values (3, 'Panzanelle');
insert into departments values (4, 'Bar');

insert into products (id, name, note, department_id, course_id, price, created, last_update, parent_id, sell_locked) VALUES
(1, 'Tordelli', null, 1, 1, 8, now(), now(), null, false),
(2, 'Tordelli con formaggio', null, 1, 1, 8, now(), now(), 1, false),
(3, 'Testaroli al pesto', null, 1, 1, 5, now(), now(), null, false),
(4, 'Testaroli al pesto con formaggio', null, 1, 1, 5, now(), now(), 3, false),
(5, 'Spaghetti di mare', null, 1, 1, 8, now(), now(), null, false),
(6, 'Frittura di pesce', null, 1, 2, 9, now(), now(), null, false),
(7, 'Grigliata Hamburger', null, 2, 2, 8, now(), now(), null, false),
(8, 'Grigliata Rosticciana', null, 2, 2, 7.5, now(), now(), null, false),
(9, 'Grigliata Pollo', null, 2, 2, 6.5, now(), now(), null, false),
(10, 'Grigliata Salsicce', null, 2, 2, 4, now(), now(), null, false),
(11, 'Grigliata Wurstel', null, 2, 2, 3, now(), now(), null, false),
(12, 'Baccalà Marinato', null, 1, 2, 8, now(), now(), null, false),
(13, 'Patate Fritte', null, 1, 2, 2.5, now(), now(), null, false),
(14, 'Ketchup', null, 1, 2, 0.5, now(), now(), null, false),
(15, 'Maionese', null, 1, 2, 0.5, now(), now(), null, false),
(16, 'Pomodori', null, 1, 2, 1.5, now(), now(), null, false),
(17, 'Pane (con i primi)', null, 1, 1, 0.5, now(), now(), null, false),
(18, 'Pane (con i secondi)', null, 1, 2, 0.5, now(), now(), 17, false),
(19, 'Panzanella', null, 3, 3, 0.8, now(), now(), null, false),
(20, 'Salumi', null, 3, 3, 4, now(), now(), null, false),
(21, 'Stracchino', null, 3, 3, 1.5, now(), now(), null, false),
(22, 'Nutellina', null, 3, 3, 0.5, now(), now(), null, false),
(23, 'Birra 200cc', null, 4, 4, 2, now(), now(), null, false),
(24, 'Birra 400cc', null, 4, 4, 4, now(), now(), null, false),
(25, 'Bevanda in lattina', null, 4, 4, 2, now(), now(), null, false),
(26, 'Estathè in brick', null, 4, 4, 1, now(), now(), null, false),
(27, 'Acqua frizzante/naturale 1.5L', null, 4, 4, 2, now(), now(), null, false),
(28, 'Acqua frizzante/naturale 0.5L', null, 4, 4, 1, now(), now(), null, false),
(29, 'Test prodotto bloccato', null, 4, 4, 1, now(), now(), null, true),
(30, 'Test prodotto terminato', null, 4, 4, 1, now(), now(), null, false),
(31, 'Test prodotto quasi terminato', null, 4, 4, 1, now(), now(), null, false)
;


insert into products_quantity(product_id, initial_quantity, available_quantity) VALUES
(1, 220, 216),
(2, 0, 0),
(3, 100, 98),
(4, 0, 0),
(5, 100, 99),
(6, 80, 77),
(7, 30, 29),
(8, 90, 90),
(9, 40, 38),
(10, 100, 100),
(11, 200, 200),
(12, 80, 80),
(13, 300, 298),
(14, 80, 80),
(15, 80, 80),
(16, 50, 49),
(17, 100, 100),
(18, 0, 0),
(19, 600, 591),
(20, 60, 58),
(21, 30, 30),
(22, 40, 40),
(23, 1000, 1000),
(24, 1000, 998),
(25, 100, 99),
(26, 30, 30),
(27, 1000, 998),
(28, 1000, 1000),
(29, 100, 100),
(30, 100, 0),
(31, 80, 9)
;


insert into discounts (name, rate) VALUES
('Sconto amici', 20.0),
('Pasto omaggio', 100.0)
;

INSERT INTO `orders` VALUES
(1,'Sandro Pertini',NULL,0,6,0.50,107.50,'lorenzo',NULL,'2025-07-25 07:50:39','2025-07-25 07:50:39'),
(2,'Sergio Mattarella',NULL,1,0,0.50,29.20,'lorenzo',20,'2025-07-25 07:52:08','2025-07-25 07:52:08')
;

INSERT INTO `orders_products` (order_id, product_id, price, quantity, note, idx) VALUES
(1,1,8.00,1,NULL,1),
(1,2,8.00,2,NULL,0),
(1,4,5.00,2,NULL,2),
(1,5,8.00,1,NULL,3),
(1,6,9.00,2,NULL,5),
(1,7,8.00,1,NULL,4),
(1,9,6.50,2,NULL,6),
(1,16,1.50,1,NULL,8),
(1,19,0.80,5,NULL,7),
(1,20,4.00,1,NULL,9),
(1,24,4.00,2,NULL,12),
(1,25,2.00,1,NULL,11),
(1,27,2.00,2,NULL,10),
(2,1,8.00,1,NULL,3),
(2,6,9.00,1,NULL,4),
(2,13,2.50,2,NULL,1),
(2,19,0.80,4,NULL,0),
(2,20,4.00,1,NULL,2)
;

insert into monitors (id, name) VALUES
(1, 'Cucina'),
(2, 'Griglia')
;

insert into monitors_products (monitor_id, product_id, idx) VALUES
(1, 1, 1),
(1, 3, 2),
(1, 5, 3),
(1, 6, 4),
(1, 13, 5),
(1, 19, 6),
(1, 20, 7),

(2, 8, 1),
(2, 9, 2),
(2, 10, 3),
(2, 7, 4),
(2, 11, 5)
;
