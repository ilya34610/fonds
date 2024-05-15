-- Функция для генерации случайных строк
CREATE OR REPLACE FUNCTION random_string(length INTEGER) RETURNS TEXT AS $$
DECLARE
    characters TEXT := 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz';
    result TEXT := '';
    i INTEGER;
BEGIN
    FOR i IN 1..length LOOP
        result := result || substr(characters, (trunc(random() * length(characters) + 1))::integer, 1);
    END LOOP;
    RETURN result;
END;
$$ LANGUAGE plpgsql;

-- Функция для генерации случайной даты
CREATE OR REPLACE FUNCTION random_date(start_date DATE, end_date DATE) RETURNS DATE AS $$
BEGIN
    RETURN start_date + (random() * (end_date - start_date))::int;
END;
$$ LANGUAGE plpgsql;

DO $$
DECLARE
    i INTEGER;
    random_city TEXT;
    random_fio TEXT;
    random_login TEXT;
    random_role TEXT;
    random_currency TEXT;
    random_donation_type TEXT;
    fund JSONB;
BEGIN
    FOR i IN 1..100000 LOOP
        random_city := random_string(10);
        random_fio := random_string(12);
        random_login := random_string(8);
        random_role := random_string(5);
        random_currency := random_string(3);
        random_donation_type := random_string(6);

        fund := jsonb_build_object(
            'id', i,
            'name', random_string(10),
            'city', jsonb_build_object(
                'id', i % 100 + 1,
                'name', random_city
            ),
            'creation_date', random_date('2020-01-01'::date, '2023-01-01'::date),
            'phone', random_string(10),
            'user', jsonb_build_object(
                'id', i % 1000 + 1,
                'fio', random_fio,
                'login', random_login,
                'roles', jsonb_build_object(
                    'id', i % 10 + 1,
                    'name', random_role
                )
            ),
            'sum', round((random() * 100000)::numeric, 2),
            'capital_sources', jsonb_build_array(
                jsonb_build_object(
                    'id', i % 500 + 1,
                    'sum', round((random() * 50000)::numeric, 2),
                    'currency_type', jsonb_build_object(
                        'id', i % 5 + 1,
                        'name', random_currency
                    ),
                    'donation_date', random_date('2020-01-01'::date, '2023-01-01'::date),
                    'user', jsonb_build_object(
                        'id', i % 1000 + 2,
                        'fio', random_fio,
                        'login', random_login,
                        'roles', jsonb_build_object(
                            'id', i % 10 + 2,
                            'name', random_role
                        )
                    ),
                    'donation_types', jsonb_build_array(
                        jsonb_build_object(
                            'id', i % 10 + 1,
                            'name', random_donation_type
                        ),
                        jsonb_build_object(
                            'id', i % 10 + 2,
                            'name', random_donation_type
                        )
                    )
                )
            ),
            'citizens', jsonb_build_array(
                jsonb_build_object(
                    'id', i % 1000 + 3,
                    'city', jsonb_build_object(
                        'id', i % 100 + 2,
                        'name', random_city
                    ),
                    'birth_date', random_date('1980-01-01'::date, '2000-01-01'::date),
                    'user', jsonb_build_object(
                        'id', i % 1000 + 4,
                        'fio', random_fio,
                        'login', random_login,
                        'roles', jsonb_build_object(
                            'id', i % 10 + 3,
                            'name', random_role
                        )
                    ),
                    'sum', round((random() * 10000)::numeric, 2)
                )
            )
        );

        INSERT INTO public.fonds_json (data) VALUES (fund);
    END LOOP;
END;
$$;
