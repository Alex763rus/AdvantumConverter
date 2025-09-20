replace into user_tbl(chat_id, first_name, last_name, registered_at, user_name, user_role, company_id, user_role_text)
select '1010052269', 'Анастасия', 'Зуевич', '2025-09-19 13:11:14.851000', 'Anastasiya_Zuevich', 4, 7, 'SUPPORT'
union all select '6619162062', 'Александр', NULL, '2025-02-19 23:32:50.357000', NULL, 4, 7, 'SUPPORT'
union all select '6678935766', 'Елена', 'Кашура', '2025-09-19 13:25:54.733000', 'ekashura', 4, 7, 'SUPPORT'
;