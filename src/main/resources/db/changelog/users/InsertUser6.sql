INSERT INTO user_tbl(chat_id, first_name, last_name, registered_at, user_name, user_role, company_id, user_role_text)
select 22058039,
       '24668505',
       NULL,
       '2026-03-24 11:51:04.352'::timestamp,
       'Диспетчер Тула',
       4,
       9,
       'SUPPORT'
union all
select 8368719832,
       'Диспетчер',
       'Тула',
       '2026-03-23 07:57:27.666',
       null,
       4,
       9,
       'SUPPORT'
union all
select 941511135,
       'Saveliy',
       'Larichkin',
       '2026-03-19 10:26:15.463',
       'Steeeewie2k',
       4,
       9,
       'SUPPORT'
;