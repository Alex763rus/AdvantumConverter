INSERT INTO user_tbl(chat_id, first_name, registered_at, user_name, user_role, company_id)
SELECT
    chat_id,
    first_name,
    CAST(registered_at AS TIMESTAMP),
    user_name,
    user_role,
    company_id
FROM (
         SELECT 188745369 as chat_id, 'Darya' as first_name, '2024-02-07 17:49:31.645000' as registered_at, 'Dashkochan' as user_name, 4 as user_role, 2 as company_id UNION ALL
         SELECT 448764395, 'Vitaliy', '2023-06-02 15:18:19.266000', 'Vitaliych', 4, 2 UNION ALL
         SELECT 799008767, 'Григорьев Алексей', '2023-06-01 18:56:42.357000', 'grigorievap', 5, 2 UNION ALL
         SELECT 857117770, 'Oksana', '2023-06-05 15:54:47.744000', 'tasmanOksi', 4, 2 UNION ALL
         SELECT 858742686, 'Мария', '2023-06-05 15:53:41.620000', 'PankinaMari', 4, 2 UNION ALL
         SELECT 968518965, 'Алексей', '2023-06-05 16:02:13.907000', 'aleksey7276', 4, 2 UNION ALL
         SELECT 1041167280, 'Анна', '2023-06-05 16:53:02.089000', 'shmakova_it', 4, 2 UNION ALL
         SELECT 1279017832, 'Ксюша', '2023-06-05 15:58:43.627000', 'Ksenia_0_97', 4, 2 UNION ALL
         SELECT 1885275370, 'Tata', '2023-06-05 18:53:35.411000', 'TTata88', 4, 2 UNION ALL
         SELECT 1951378202, 'Софья', '2023-06-05 16:15:03.563000', 'sofya_ekkert', 4, 2 UNION ALL
         SELECT 6166184036, 'Логисты', '2024-10-22 15:16:36.890000', null, 4, 8 UNION ALL
         SELECT 6247615297, 'Alexey', '2023-06-02 10:06:51.289000', 'tgMasterAlexey', 5, 2
     ) AS data
    ON CONFLICT (chat_id)
DO UPDATE SET
    first_name = EXCLUDED.first_name,
           registered_at = EXCLUDED.registered_at,
           user_name = EXCLUDED.user_name,
           user_role = EXCLUDED.user_role,
           company_id = EXCLUDED.company_id;