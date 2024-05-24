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

-- Функция для генерации случайных строк из списка слов
CREATE OR REPLACE FUNCTION random_string_from_list(words TEXT[]) RETURNS TEXT AS $$
BEGIN
    RETURN words[(trunc(random() * array_length(words, 1) + 1))::integer];
END;
$$ LANGUAGE plpgsql;

-- Функция для генерации случайного имени пользователя
CREATE OR REPLACE FUNCTION three_random_string_from_list(words TEXT[]) RETURNS TEXT AS $$
DECLARE
    result TEXT := '';
BEGIN
    result := words[(trunc(random() * array_length(words, 1) + 1))::integer] || ' ' ||
              words[(trunc(random() * array_length(words, 1) + 1))::integer] || ' ' ||
              words[(trunc(random() * array_length(words, 1) + 1))::integer];
    RETURN result;
END;
$$ LANGUAGE plpgsql;

--генерация 100000 случайных значений
DO $$
DECLARE
    i INTEGER;
BEGIN
    FOR i IN 1..100000 LOOP
        INSERT INTO fonds_json (data)
        VALUES (
            json_build_object(
                'fond', json_build_object(
                    'id', i,
                    'name', three_random_string_from_list('{Надежда, для, всех, Сердца, помощи, фонд, Лучи, добра, организация, Руки, помощи, ассоциация}'),
                    'sum', random() * 1000,
                    'creation_date', random_date('2000-01-01'::DATE, '2024-05-20'::DATE),
                    'phone', '+1234567890'
                ),
                'city', json_build_object(
                    'id', i,
                    'name', random_string_from_list('{Название города1, Название города2, Название города3, Название города4, Название города5, Название города6, Название города7, Название города8, Название города9, Название города10}')
                ),
                'country', json_build_object(
                    'id', i,
                    'name', random_string_from_list('{Название страны1, Название страны2, Название страны3, Название страны4, Название страны5, Название страны6, Название страны7, Название страны8, Название страны9, Название страны10}')
                ),
                'capital_sources', json_build_object(
                    'id', i,
                    'sum', random() * 500,
                    'donation_date', random_date('2000-05-01'::DATE, '2024-05-20'::DATE)
                ),
                'donation_types', json_build_object(
                    'id', i,
                    'name', random_string_from_list('{Пожертвования частных лиц, Пожертвования организаций, Государственная помощь, Пожертвования церкви}')
                ),
                'currency_type', json_build_object(
                    'id', i,
                    'name', random_string_from_list('{RUB, USD, EUR, GBP, JPY}')
                )
            )
        );
    END LOOP;
END;
$$ LANGUAGE plpgsql;
