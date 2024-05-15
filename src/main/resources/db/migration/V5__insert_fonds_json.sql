-- вставка 5 +- адекватных записей
--INSERT INTO fonds_json (data)
--VALUES
--('{"fond": {"id": 1, "name": "Фонд 1", "city": {"id": 1, "name": "Город 1", "country": {"id": 1, "name": "Страна 1"}}, "creation_date": "2024-05-20", "phone": "+1234567890", "user": {"id": 1}, "sum": 1000.00, "capital_sources": [{"id": 1, "sum": 500.00, "currency_type": {"id": 1, "name": "Валюта 1"}, "donation_date": "2024-05-20", "user": {"id": 1}, "donation_types": [{"id": 1, "name": "Тип пожертвования 1"}]}}}'::json),
--('{"fond": {"id": 2, "name": "Фонд 2", "city": {"id": 2, "name": "Город 2", "country": {"id": 2, "name": "Страна 2"}}, "creation_date": "2024-05-21", "phone": "+1234567891", "user": {"id": 2}, "sum": 2000.00, "capital_sources": [{"id": 2, "sum": 1000.00, "currency_type": {"id": 2, "name": "Валюта 2"}, "donation_date": "2024-05-21", "user": {"id": 2}, "donation_types": [{"id": 2, "name": "Тип пожертвования 2"}]}}}'::json),
--('{"fond": {"id": 3, "name": "Фонд 3", "city": {"id": 3, "name": "Город 3", "country": {"id": 3, "name": "Страна 3"}}, "creation_date": "2024-05-22", "phone": "+1234567892", "user": {"id": 3}, "sum": 3000.00, "capital_sources": [{"id": 3, "sum": 1500.00, "currency_type": {"id": 3, "name": "Валюта 3"}, "donation_date": "2024-05-22", "user": {"id": 3}, "donation_types": [{"id": 3, "name": "Тип пожертвования 3"}]}}}'::json),
--('{"fond": {"id": 4, "name": "Фонд 4", "city": {"id": 4, "name": "Город 4", "country": {"id": 4, "name": "Страна 4"}}, "creation_date": "2024-05-23", "phone": "+1234567893", "user": {"id": 4}, "sum": 4000.00, "capital_sources": [{"id": 4, "sum": 2000.00, "currency_type": {"id": 4, "name": "Валюта 4"}, "donation_date": "2024-05-23", "user": {"id": 4}, "donation_types": [{"id": 4, "name": "Тип пожертвования 4"}]}}}'::json),
--('{"fond": {"id": 5, "name": "Фонд 5", "city": {"id": 5, "name": "Город 5", "country": {"id": 5, "name": "Страна 5"}}, "creation_date": "2024-05-24", "phone": "+1234567894", "user": {"id": 5}, "sum": 5000.00, "capital_sources": [{"id": 5, "sum": 2500.00, "currency_type": {"id": 5, "name": "Валюта 5"}, "donation_date": "2024-05-24", "user": {"id": 5}, "donation_types": [{"id": 5, "name": "Тип пожертвования 5"}]}}}'::json);



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
                    'creation_date', random_date('2024-05-01'::DATE, '2024-05-20'::DATE),
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
                    'donation_date', random_date('2024-05-01'::DATE, '2024-05-20'::DATE)
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
