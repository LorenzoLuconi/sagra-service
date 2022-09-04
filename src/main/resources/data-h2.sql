--- password lorenzo in bcrypt
insert into utenti values ('lorenzo', '$2a$10$CQViJR5.yIcVJzfeGPMRMeeb9aZbUOCga.MJhbKVaq1ooxIdrF4mm', now(), now());

insert into menu values (1, 'Primi');
insert into menu values (2, 'Secondi');

insert into reparti values (1, 'Cucina');

insert into prodotti (id, nome, note, reparto, menu, prezzo, created, last_update) VALUES
(1, 'Tordelli', null, 1, 1, 7, now(), now())
;
