--- password lorenzo in bcrypt
insert into users values ('lorenzo', '$2a$10$CQViJR5.yIcVJzfeGPMRMeeb9aZbUOCga.MJhbKVaq1ooxIdrF4mm', now(), now());

insert into menu values (1, 'Primi');
insert into menu values (2, 'Secondi');
insert into menu values (3, 'Panzanelle');
insert into menu values (4, 'Bar');

insert into departments values (1, 'Cucina');
insert into departments values (2, 'Griglia');
insert into departments values (3, 'Bar');

insert into products (id, name, note, department, menu, price, created, last_update) VALUES
(1, 'Tordelli', null, 1, 1, 8, now(), now()),
(2, 'Panzanelle', null, 1, 3, 0.8, now(), now()),
(3, 'Grigliata Salsicce', null, 2, 1, 4.5, now(), now()),
(4, 'Grigliata Rosticciana', null, 2, 1, 6.5, now(), now()),
(5, 'Stracchino', null, 1, 3, 1.5, now(), now()),
(6, 'Bottiglia Acqua 1.5 litri', null, 3, 4, 2.0, now(), now())
;

insert into products_quantity(product_id, quantity) VALUES
(1, 200),
(2, 500),
(3, 75),
(4, 100),
(5, 30),
(6, 1000 )
;


insert into orders (id, username, customer, note, take_away, service_number, service_cost, total_amount, created, last_update) VALUES
(1, 'lorenzo', 'Lorenzo Luconi', null, false, 6, 3.0, 34.4, '2025-07-10 09:49:00', '2025-07-10 09:50:01')
;

insert into orders_products (id, order_id, product_id, price, quantity, note, created, last_update) VALUES
(1, 1, 2, 0.8, 3, null, '2025-07-10 09:49:00', '2025-07-10 09:49:00'),
(2, 1, 1, 8, 3, null, '2025-07-10 09:49:00', '2025-07-10 09:49:00'),
(3, 1, 5, 1.5, 2, null, '2025-07-10 09:49:00', '2025-07-10 09:49:00'),
(4, 1, 6, 2.0, 1, null, '2025-07-10 09:49:00', '2025-07-10 09:49:00')
;
