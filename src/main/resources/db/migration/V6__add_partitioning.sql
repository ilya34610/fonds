CREATE TABLE public.fonds_part (
    id serial4 NOT NULL,
    "name" varchar(100) NOT NULL,
    id_city int8 NOT NULL,
    creation_date date NOT NULL,
    phone varchar(20) NOT NULL,
    id_user int4 NOT NULL,
    sum numeric(12, 2) NOT NULL
) PARTITION BY LIST ("id_city");

CREATE TABLE fonds_part_city1 PARTITION OF fonds_part FOR VALUES IN (1);
CREATE TABLE fonds_part_city2 PARTITION OF fonds_part FOR VALUES IN (2);
CREATE TABLE fonds_part_city3 PARTITION OF fonds_part FOR VALUES IN (3);
CREATE TABLE fonds_part_city4 PARTITION OF fonds_part FOR VALUES IN (4);
CREATE TABLE fonds_part_city_default PARTITION OF fonds_part DEFAULT;
CREATE INDEX ON fonds_part (id_city);

DO $$
DECLARE
    i INTEGER;
BEGIN
    FOR i IN 1..500000 LOOP
INSERT INTO public.fonds_part (id, "name", id_city, creation_date,phone,id_user, sum)
VALUES (
    i,
    substring(md5(random()::text) FROM 1 FOR 10),
    trunc(random() * 10 + 1),
    random_date('2000-01-01'::DATE, '2024-05-20'::DATE),
    trunc(random() * 9 + 1),
    trunc(random() * 4 + 1),
    trunc(random() * 9 + 1)
);
    END LOOP;
END;
$$ LANGUAGE plpgsql;


SELECT
(SELECT COUNT(*) FROM fonds_part_city1 ) as fonds_part_city1,
(SELECT COUNT(*) FROM fonds_part_city2 ) as fonds_part_city2,
(SELECT COUNT(*) FROM fonds_part_city3 ) as fonds_part_city3,
(SELECT COUNT(*) FROM fonds_part_city4 ) as fonds_part_city4,
(SELECT COUNT(*) FROM fonds_part_city_default ) as fonds_part_city_default;



CREATE TABLE public.fonds_part_1 (
    id serial4 NOT NULL,
    "name" varchar(100) NOT NULL,
    id_city int8 NOT NULL,
    creation_date date NOT NULL,
    phone varchar(20) NOT NULL,
    id_user int4 NOT NULL,
    sum numeric(12, 2) NOT NULL
) PARTITION BY RANGE (creation_date);


CREATE TABLE fonds_part_2005_2010 PARTITION OF public.fonds_part_1
FOR VALUES FROM ('2005-01-01') TO ('2010-01-01');

CREATE TABLE fonds_part_2010_2015 PARTITION OF public.fonds_part_1
FOR VALUES FROM ('2010-01-02') TO ('2015-01-01');

CREATE TABLE fonds_part_2015_2020 PARTITION OF public.fonds_part_1
FOR VALUES FROM ('2015-01-02') TO ('2020-01-01');

CREATE TABLE fonds_part_2020_2022 PARTITION OF public.fonds_part_1
FOR VALUES FROM ('2020-01-02') TO ('2022-01-01');

CREATE TABLE fonds_part_default PARTITION OF fonds_part_1 DEFAULT;

CREATE TABLE fonds_part_2005_2010_user PARTITION OF public.fonds_part_1
FOR VALUES FROM ('2022-01-02') TO ('2024-01-01')
PARTITION BY LIST (id_user);

CREATE INDEX ON public.fonds_part_1 (creation_date);


--вложенные
CREATE TABLE fonds_part_2005_2010_user1 PARTITION OF fonds_part_2005_2010_user FOR VALUES IN (1);
CREATE TABLE fonds_part_2005_2010_user2 PARTITION OF fonds_part_2005_2010_user FOR VALUES IN (2);
CREATE TABLE fonds_part_2005_2010_user3 PARTITION OF fonds_part_2005_2010_user FOR VALUES IN (3);
CREATE TABLE fonds_part_2005_2010_user4 PARTITION OF fonds_part_2005_2010_user FOR VALUES IN (4);



INSERT INTO public.fonds_part_1 (id, "name", id_city, creation_date, phone, id_user, sum)
SELECT id, "name", id_city, creation_date, phone, id_user, sum
FROM public.fonds_part;


CREATE TABLE public.fonds_part_copy (
    id serial4 NOT NULL,
    name varchar(100) NOT NULL,
    id_city int8 NOT NULL,
    creation_date date NOT NULL,
    phone varchar(20) NOT NULL,
    id_user int4 NOT NULL,
    sum numeric(12, 2) NOT NULL
);

INSERT INTO public.fonds_part_copy (id, "name", id_city, creation_date, phone, id_user, sum)
SELECT id, "name", id_city, creation_date, phone, id_user, sum
FROM public.fonds_part;

--индексы к обычной таблице:
CREATE INDEX idx_fonds_id_city ON fonds_part_copy (id_city);
CREATE INDEX idx_fonds_creation_date ON fonds_part_copy (creation_date);
CREATE INDEX idx_fonds_phone ON fonds_part_copy (phone);
CREATE INDEX idx_fonds_id_user ON fonds_part_copy (id_user);

--индексы к секционированным таблицам:
CREATE INDEX ON fonds_part (id_city);
CREATE INDEX ON fonds_part (creation_date);
CREATE INDEX ON fonds_part (phone);
CREATE INDEX ON fonds_part (id_user);

CREATE INDEX ON fonds_part_1 (id_city);
CREATE INDEX ON fonds_part_1 (creation_date);
CREATE INDEX ON fonds_part_1 (phone);
CREATE INDEX ON fonds_part_1 (id_user);

explain (analyze) select * from fonds_part;


