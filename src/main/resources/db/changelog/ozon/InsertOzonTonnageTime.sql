-- Время на разгрузку тоннажа
/*
   ="select "&A1&", '"&СЖПРОБЕЛЫ(ТЕКСТ(B1;"[ч]:мм:сс"))&"' union all"
   drop ozon_tonnage_time car;
   truncate ozon_tonnage_time;
   select * from ozon_tonnage_time;
 */
INSERT INTO ozon_tonnage_time(tonnage, time)
select 20000, '1:30:00' union all
select 10000, '1:10:00' union all
select 5000, '1:00:00' union all
select 1500, '1:00:00' union all
select 1200, '1:00:00' union all
select 1000, '1:00:00' union all
select 1300, '1:00:00' union all
select 7000, '1:00:00'

;