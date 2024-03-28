CREATE TABLE public.countries (
	id serial NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT countries_pk PRIMARY KEY (id)
);

CREATE TABLE public.donation_types (
	id smallserial NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT donation_types_pk PRIMARY KEY (id)
);


CREATE TABLE public.categories (
	id smallserial NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT categories_pk PRIMARY KEY (id)
);

CREATE TABLE public.donations (
	sum money NOT NULL,
	"date" date NOT NULL,
	id bigserial NOT NULL,
	CONSTRAINT donations_pk PRIMARY KEY (id)
);

CREATE TABLE public.currency_type (
	id smallserial NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT currency_type_pk PRIMARY KEY (id)
);

CREATE TABLE public.cities (
	id bigserial NOT NULL,
	"name" varchar NOT NULL,
	id_country integer NOT NULL,
	CONSTRAINT cities_pk PRIMARY KEY (id),
	CONSTRAINT cities_countries_fk FOREIGN KEY (id_country) REFERENCES public.countries(id)
);

CREATE TABLE public.citizens (
	id bigserial NOT NULL,
	fio varchar NOT NULL,
	id_city bigint NOT NULL,
	birth_date date NOT NULL,
	CONSTRAINT citizens_pk PRIMARY KEY (id),
	CONSTRAINT citizens_cities_fk FOREIGN KEY (id_city) REFERENCES public.cities(id)
);

CREATE TABLE public.citizens_donations (
	id_citizen bigint NOT NULL,
	id_donation bigint NULL,
	CONSTRAINT citizens_donations_citizens_fk FOREIGN KEY (id_citizen) REFERENCES public.citizens(id),
	CONSTRAINT citizens_donations_donations_fk FOREIGN KEY (id_donation) REFERENCES public.donations(id)
);

CREATE TABLE public.fonds (
	id serial NOT NULL,
	name varchar NOT NULL,
	id_city bigint NOT NULL,
	creation_date date NOT NULL,
	phone varchar NOT NULL,
	CONSTRAINT fonds_pk PRIMARY KEY (id),
	CONSTRAINT fonds_cities_fk FOREIGN KEY (id_city) REFERENCES public.cities(id)
);

CREATE TABLE public.capital_sources (
	id bigserial NOT NULL,
	sum money NOT NULL,
	id_currency_type smallint NOT NULL,
	phone varchar NULL,
	donation_date date NOT NULL,
	CONSTRAINT capital_sources_pk PRIMARY KEY (id),
	CONSTRAINT capital_sources_currency_type_fk FOREIGN KEY (id_currency_type) REFERENCES public.currency_type(id)
);

CREATE TABLE public.capital_sources_fonds (
	id_capital_source bigint NOT NULL,
	id_fond integer NOT NULL,
	CONSTRAINT capital_sources_fonds_capital_sources_fk FOREIGN KEY (id_capital_source) REFERENCES public.capital_sources(id),
	CONSTRAINT capital_sources_fonds_fonds_fk FOREIGN KEY (id_fond) REFERENCES public.fonds(id)
);

CREATE TABLE public.capital_sources_donation_types (
	id_capital_source bigint NOT NULL,
	id_donation_type smallint NOT NULL,
	CONSTRAINT capital_sources_donation_types_capital_sources_fk FOREIGN KEY (id_capital_source) REFERENCES public.capital_sources(id),
	CONSTRAINT capital_sources_donation_types_donation_types_fk FOREIGN KEY (id_donation_type) REFERENCES public.donation_types(id)
);

CREATE TABLE public.citizens_categories (
	id_citizen bigint NOT NULL,
	id_category smallint NOT NULL,
	CONSTRAINT citizens_categories_categories_fk FOREIGN KEY (id_category) REFERENCES public.categories(id),
	CONSTRAINT citizens_categories_citizens_fk FOREIGN KEY (id_citizen) REFERENCES public.citizens(id)
);

CREATE TABLE public.citizens_fonds (
	id_citizen int8 NULL,
	id_fonds int4 NULL,
	CONSTRAINT citizens_fonds_citizens_fk FOREIGN KEY (id_citizen) REFERENCES public.citizens(id),
	CONSTRAINT citizens_fonds_fonds_fk FOREIGN KEY (id_fonds) REFERENCES public.fonds(id)
);