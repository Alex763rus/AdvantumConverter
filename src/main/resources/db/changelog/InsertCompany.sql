-- # 3) с листа Адреса РЦ
-- ="select "&A2&",'"&B2&"',"&1&",'"&СЖПРОБЕЛЫ(ТЕКСТ(C2;"ЧЧ:ММ"))&"', '"&СЖПРОБЕЛЫ(ТЕКСТ(D2;"ЧЧ:ММ"))&"', '"&СЖПРОБЕЛЫ(ТЕКСТ(E2;"ЧЧ:ММ"))&"' union all"
--     drop table lenta_dictionary;
REPLACE INTO company(company_id, company_name)
select 1, 'Компания отсутствует' union all
select 2, 'Адвантум' union all
select 3, 'Лента' union all
select 4, 'Буш-Автопром' union all
select 5, 'Озон' union all
select 6, 'Метро' union all
select 7, 'СберЛогистика' union all
select 8, 'ArtFruit'
;


