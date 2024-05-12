INSERT INTO public.countries ("name") VALUES
    ('Россия'),
    ('США'),
    ('Канада'),
    ('Германия'),
    ('Франция'),
    ('Япония');

INSERT INTO public.donation_types("name") VALUES
    ('Пожертвования частных лиц'),
    ('Пожертвования организаций'),
    ('Государственная помощь'),
    ('Пожертвования церкви');

INSERT INTO public.categories("name") VALUES
    ('Ветеран войны'),
    ('Инвалид войны'),
    ('Инвалид труда'),
    ('Инвалид детства'),
    ('Многодетная семья');

INSERT INTO public.currency_type("name") VALUES
    ('RUB'),
    ('USD'),
    ('EUR'),
    ('GBP'),
    ('JPY');

INSERT INTO public.cities ("name", id_country) VALUES
    ('Москва', (SELECT id FROM public.countries WHERE "name" = 'Россия')),
    ('Санкт-Петербург', (SELECT id FROM public.countries WHERE "name" = 'Россия')),
    ('Нью-Йорк', (SELECT id FROM public.countries WHERE "name" = 'США')),
    ('Лос-Анджелес', (SELECT id FROM public.countries WHERE "name" = 'США')),
    ('Торонто', (SELECT id FROM public.countries WHERE "name" = 'Канада')),
    ('Монреаль', (SELECT id FROM public.countries WHERE "name" = 'Канада')),
    ('Берлин', (SELECT id FROM public.countries WHERE "name" = 'Германия')),
    ('Париж', (SELECT id FROM public.countries WHERE "name" = 'Франция')),
    ('Токио', (SELECT id FROM public.countries WHERE "name" = 'Япония'));

INSERT INTO public.citizens (id_city, birth_date, id_user, sum ) VALUES
    (1,'2000-03-05', 4, 150000.00),
    (3,'2001-12-05', 5, 190000.00),
    (5,'2002-04-05', 6, 200000.00);

INSERT INTO public.fonds ("name", id_city, creation_date, phone, id_user, sum) VALUES
    ('Фонд "Развития"', 1, '2020-05-11', '+123456789', 2,  1000000.00),
    ('Фонд "Благо"', 2, '2021-05-11', '+987654321', 2,  500000.00),
    ('Фонд "Помощь"', 3, '2019-05-11', '+111222333', 2, 200000.00);

INSERT INTO public.receipts (sum, id_fond) VALUES
    (50000.00, 1),
    (80000.00, 2),
    (35000.00, 3);

INSERT INTO public.capital_sources (sum, id_currency_type, donation_date, id_user) VALUES
    (50000.00, 1,'2024-03-10',7),
    (80000.00, 1, '2022-03-12',7),
    (35000.00, 1, '2020-03-18',7);

INSERT INTO public.fond_expenses (sum, id_citizen) VALUES
    (50000.00, 1),
    (80000.00, 2),
    (35000.00, 3);

INSERT INTO public.fonds_fond_expenses (id_fonds, id_fond_expenses) VALUES
    (1, 1),
    (2, 2),
    (3, 3);

INSERT INTO public.capital_sources_donation_types (id_capital_source, id_donation_type) VALUES
    (1, 1),
    (2, 1),
    (3, 1);

INSERT INTO public.capital_sources_fonds (id_capital_source, id_fond) VALUES
    (1, 1),
    (2, 2),
    (3, 3);

INSERT INTO public.capital_sources_receipts(id_capital_source, id_receipt) VALUES
    (1, 1),
    (2, 2),
    (3, 3);


INSERT INTO public.citizens_categories (id_citizen, id_category) VALUES
    (1, 1),
    (2, 2),
    (3, 3);

INSERT INTO public.citizens_fonds (id_citizen, id_fond) VALUES
    (1, 1),
    (2, 2),
    (3, 3);



