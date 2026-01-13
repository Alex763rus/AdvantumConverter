INSERT INTO user_tbl(chat_id, first_name, last_name, registered_at, user_name, user_role, company_id, user_role_text)
SELECT
    chat_id,
    first_name,
    last_name,
    registered_at::TIMESTAMP,  -- Преобразование строки в TIMESTAMP
        user_name,
    user_role,
    company_id,
    user_role_text
FROM (
         SELECT 1010052269 as chat_id, 'Анастасия' as first_name, 'Зуевич' as last_name, '2025-09-19 13:11:14.851000' as registered_at, 'Anastasiya_Zuevich' as user_name, 4 as user_role, 7 as company_id, 'SUPPORT' as user_role_text UNION ALL
         SELECT 6619162062, 'Александр', NULL, '2025-02-19 23:32:50.357000', NULL, 4, 7, 'SUPPORT' UNION ALL
         SELECT 6678935766, 'Елена', 'Кашура', '2025-09-19 13:25:54.733000', 'ekashura', 4, 7, 'SUPPORT'
     ) AS data
    ON CONFLICT (chat_id)
DO UPDATE SET
    first_name = EXCLUDED.first_name,
           last_name = EXCLUDED.last_name,
           registered_at = EXCLUDED.registered_at,
           user_name = EXCLUDED.user_name,
           user_role = EXCLUDED.user_role,
           company_id = EXCLUDED.company_id,
           user_role_text = EXCLUDED.user_role_text;