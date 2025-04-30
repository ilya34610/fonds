CREATE TABLE public.roles (
	id int2 NOT NULL,
	"name" varchar(30) NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (id)
);

CREATE TABLE public.users (
	id serial NOT NULL,
	id_roles int2 NOT NULL,
	fio varchar(100) NOT NULL,
	login varchar(20) NOT NULL,
    password varchar(60) NOT NULL,
	phone varchar(20) NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_roles_fk FOREIGN KEY (id_roles) REFERENCES public.roles(id) ON DELETE CASCADE
);

CREATE TABLE public.countries (
	id serial NOT NULL,
	"name" varchar(100) NOT NULL,
	CONSTRAINT countries_pk PRIMARY KEY (id)
);

CREATE TABLE public.donation_types (
	id smallserial NOT NULL,
	"name" varchar(50) NOT NULL,
	CONSTRAINT donation_types_pk PRIMARY KEY (id)
);


CREATE TABLE public.categories (
	id smallserial NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT categories_pk PRIMARY KEY (id)
);

CREATE TABLE public.currency_type (
	id smallserial NOT NULL,
	"name" varchar(50) NOT NULL,
	CONSTRAINT currency_type_pk PRIMARY KEY (id)
);

CREATE TABLE public.cities (
	id bigserial NOT NULL,
	"name" varchar(100) NOT NULL,
	id_country integer NOT NULL,
	CONSTRAINT cities_pk PRIMARY KEY (id),
	CONSTRAINT cities_countries_fk FOREIGN KEY (id_country) REFERENCES public.countries(id) ON DELETE CASCADE
);

CREATE TABLE public.citizens (
	id bigserial NOT NULL,
	id_city int8 NOT NULL,
	birth_date date NOT NULL,
	id_user int4 NOT NULL,
	sum numeric(12, 2) NOT NULL,
	CONSTRAINT citizens_pk PRIMARY KEY (id),
	CONSTRAINT citizens_cities_fk FOREIGN KEY (id_city) REFERENCES public.cities(id) ON DELETE CASCADE,
	CONSTRAINT citizens_users_fk FOREIGN KEY (id_user) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE public.fond_expenses (
	id serial8 NOT NULL,
	sum numeric(12, 2) NOT NULL,
	id_citizen int2 NOT NULL,
	CONSTRAINT fond_expenses_pk PRIMARY KEY (id),
	CONSTRAINT fond_expenses_citizen_fk FOREIGN KEY (id_citizen) REFERENCES public.citizens(id) ON DELETE CASCADE
);

CREATE TABLE public.fonds (
	id serial8 NOT NULL,
	name varchar(100) NOT NULL,
	id_city bigint NOT NULL,
	creation_date date NOT NULL,
	phone varchar(20) NOT NULL,
	id_user int4 NOT NULL,
	sum numeric(12, 2) NOT NULL,
	CONSTRAINT fonds_pk PRIMARY KEY (id),
	CONSTRAINT fonds_cities_fk FOREIGN KEY (id_city) REFERENCES public.cities(id) ON DELETE CASCADE,
	CONSTRAINT fonds_users_fk FOREIGN KEY (id_user) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE public.fonds_fond_expenses (
	id_fonds int4 NOT NULL,
	id_fond_expenses int4 NOT NULL,
	CONSTRAINT fonds_fonds_expenses_fonds_fk FOREIGN KEY (id_fonds) REFERENCES public.fonds(id) ON DELETE CASCADE,
	CONSTRAINT fonds_fonds_expenses_fond_expenses_fk FOREIGN KEY (id_fond_expenses) REFERENCES public.fond_expenses(id) ON DELETE CASCADE
);


CREATE TABLE public.capital_sources (
	id bigserial NOT NULL,
	sum numeric(12, 2) NOT NULL,
	id_currency_type smallint NOT NULL,
	donation_date date NOT NULL,
	id_user int4 NOT NULL,
	CONSTRAINT capital_sources_pk PRIMARY KEY (id),
	CONSTRAINT capital_sources_currency_type_fk FOREIGN KEY (id_currency_type) REFERENCES public.currency_type(id) ON DELETE CASCADE,
	CONSTRAINT capital_sources_users_fk FOREIGN KEY (id_user) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE public.receipts (
	id serial8 NOT NULL,
	sum numeric(12, 2) NOT NULL,
	id_fond int4 NOT NULL,
	CONSTRAINT receipts_pk PRIMARY KEY (id),
	CONSTRAINT receipts_fonds_fk FOREIGN KEY (id_fond) REFERENCES public.fonds(id) ON DELETE CASCADE
);

CREATE TABLE public.capital_sources_receipts (
	id_capital_source bigint NOT NULL,
	id_receipt bigint NOT NULL,
	CONSTRAINT capital_sources_receipts_capital_sources_fk FOREIGN KEY (id_capital_source) REFERENCES public.capital_sources(id) ON DELETE CASCADE,
	CONSTRAINT capital_sources_receipts_receipts_fk FOREIGN KEY (id_receipt) REFERENCES public.receipts(id) ON DELETE CASCADE
);

CREATE TABLE public.capital_sources_fonds (
	id_capital_source bigint NOT NULL,
	id_fond bigint NOT NULL,
	CONSTRAINT capital_sources_fonds_capital_sources_fk FOREIGN KEY (id_capital_source) REFERENCES public.capital_sources(id) ON DELETE CASCADE,
	CONSTRAINT capital_sources_fonds_fonds_fk FOREIGN KEY (id_fond) REFERENCES public.fonds(id) ON DELETE CASCADE
);

CREATE TABLE public.capital_sources_donation_types (
	id_capital_source bigint NOT NULL,
	id_donation_type bigint NOT NULL,
	CONSTRAINT capital_sources_donation_types_capital_sources_fk FOREIGN KEY (id_capital_source) REFERENCES public.capital_sources(id) ON DELETE CASCADE,
	CONSTRAINT capital_sources_donation_types_donation_types_fk FOREIGN KEY (id_donation_type) REFERENCES public.donation_types(id) ON DELETE CASCADE
);

CREATE TABLE public.citizens_categories (
	id_citizen bigint NOT NULL,
	id_category bigint NOT NULL,
	CONSTRAINT citizens_categories_categories_fk FOREIGN KEY (id_category) REFERENCES public.categories(id) ON DELETE CASCADE,
	CONSTRAINT citizens_categories_citizens_fk FOREIGN KEY (id_citizen) REFERENCES public.citizens(id) ON DELETE CASCADE
);

CREATE TABLE public.citizens_fonds (
	id_citizen int8 NULL,
	id_fond bigint NULL,
	CONSTRAINT citizens_fonds_citizens_fk FOREIGN KEY (id_citizen) REFERENCES public.citizens(id) ON DELETE CASCADE,
	CONSTRAINT citizens_fonds_fonds_fk FOREIGN KEY (id_fond) REFERENCES public.fonds(id) ON DELETE CASCADE
);

