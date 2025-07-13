--- password lorenzo in bcrypt
insert into users (username, password, role, created, last_update) VALUES
('lorenzo', '$2a$10$CQViJR5.yIcVJzfeGPMRMeeb9aZbUOCga.MJhbKVaq1ooxIdrF4mm', 'admin', now(), now());

insert into courses values (1, 'Primi');
insert into courses values (2, 'Secondi');
insert into courses values (3, 'Panzanelle');
insert into courses values (4, 'Bar');
insert into courses values (5, 'Test Test');

insert into departments values (1, 'Cucina');
insert into departments values (2, 'Griglia');
insert into departments values (3, 'Bar');
insert into departments values (4, 'Test Test');

insert into products (id, name, note, department_id, course_id, price, created, last_update, parent_id, sell_locked) VALUES
(1, 'Tordelli', null, 1, 1, 8, now(), now(), null, false),
(2, 'Panzanelle', null, 1, 3, 0.8, now(), now(), null, false),
(3, 'Grigliata Salsicce', null, 2, 2, 4.5, now(), now(), null, false),
(4, 'Grigliata Rosticciana', null, 2, 2, 6.5, now(), now(), null, false),
(5, 'Stracchino', null, 1, 3, 1.5, now(), now(), null, false),
(6, 'Bottiglia Acqua 1.5 litri', null, 3, 4, 2.0, now(), now(), null, false),
(7, 'Tordelli con formaggio', null, 3, 4, 2.0, now(), now(), 1, false),
(8, 'Sell Locked', null, 3, 4, 2.0, now(), now(), null, true),
(9, 'Bottiglia Acqua 0.5 litri', null, 3, 3, 1.0, now(), now(), null, false)

;

insert into products_quantity(product_id, quantity) VALUES
(1, 200),
(2, 500),
(3, 75),
(4, 100),
(5, 30),
(6, 1000 ),
(7, 0),
(8, 100),
(9, 1000)
;


insert into discounts (name, rate) VALUES
('Sconto 20% amici', 20.0),
('Sconto 100% amici speciali', 100.0)
;

insert into orders (id, username, customer, note, take_away, service_number, service_cost, total_amount, created, last_update) VALUES
(1, 'lorenzo', 'Lorenzo Luconi', null, false, 6, 3.0, 34.4, '2025-07-10 09:49:00', '2025-07-10 09:50:01'),
(2, 'lorenzo', 'Sandro Pertini', 'test', true, 0, 0, 4.0, '2025-07-10 09:49:00', '2025-07-10 09:50:01')
;

insert into orders_products (order_id, product_id, price, quantity, note) VALUES
(1, 2, 0.8, 3, null),
(1, 1, 8, 3, null),
(1, 5, 1.5, 2, null),
(1, 6, 2.0, 1, null),
(2, 2, 0.8, 5, null),
(2, 5, 1.5, 5, null)

;

insert into monitors (id, name) VALUES
(1, 'Cucina'),
(2, 'Test')
;

insert into monitors_products (monitor_id, product_id, priority) VALUES
(1, 1, 2),
(1, 4, 1),
(1, 3, 3),
(2, 1, 1)
;
