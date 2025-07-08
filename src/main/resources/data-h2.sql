--- password lorenzo in bcrypt
insert into users values (1, 'lorenzo', '$2a$10$CQViJR5.yIcVJzfeGPMRMeeb9aZbUOCga.MJhbKVaq1ooxIdrF4mm', now(), now());

insert into menu values (1, 'Primi');
insert into menu values (2, 'Secondi');

insert into departments values (1, 'Cucina');

insert into products (id, name, note, department, menu, price, created, last_update) VALUES
(1, 'Tordelli', null, 1, 1, 7, now(), now()),
(2, 'Panzanelle', null, 1, 1, 0.5, now(), now())
;

insert into products_quantity(product, quantity) VALUES
(1, 200),
(2, 200)
