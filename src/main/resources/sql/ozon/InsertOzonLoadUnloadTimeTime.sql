# Транзитное время и нормы загрузок озон, погрузочно-разгрузочные работы
/*
   ="select '"&A2&"',"&B2&","&C2&" union all"
   drop ozon_load_unload_time car;
   truncate ozon_load_unload_time;
   select * from ozon_load_unload_time;
 */
REPLACE INTO ozon_load_unload_time(arrival, unload_time, load_time)
select 'FRESH_MAX_ВЛГ_ЗЕМЛЯЧКИ',4,15 union all
select 'FRESH_MAX_КЗН_ГОРЬКОВСКИЙ',6,15 union all
select 'FRESH_MAX_КЗН_ОРЕНБУРГСКИЙ',8,15 union all
select 'FRESH_MAX_НБЧ_МЕНЗЕЛИНСКИЙ',5,15 union all
select 'FRESH_MAX_КДР_МЕДИА_ПЛАЗА',3,15 union all
select 'FRESH_MAX_КДР_ПОНОМАРЕНКО',9,15 union all
select 'FRESH_MAX_СОЧИ_ТРАНСПОРТНАЯ',6,15 union all
select 'FRESH_MAX_СПБ_БЕСТУЖЕВСКИЙ',10,15 union all
select 'FRESH_MAX_СПБ_ВОЛХОНСКИЙ',5,15 union all
select 'FRESH_MAX_СПБ_ЛИГОВСКИЙ',4,15 union all
select 'FRESH_MAX_СПБ_МЕБЕЛЬНАЯ',3,15 union all
select 'FRESH_MAX_СПБ_РАУМ_БУГРЫ',4,15 union all
select 'FRESH_СПБ_ТРОИЦКИЙ',6,15 union all
select 'FRESH_MAX_СПБ_ЛИГОВСКИЙ_2',4,15 union all
select 'FRESH_MAX_СПБ_ЛОГОПАРК',3,15 union all
select 'FRESH_MAX_РСТ_ЗАПАДНЫЙ',6,15 union all
select 'FRESH_MAX_РСТ_ШОЛОХОВО',9,15 union all
select 'FRESH_MAX_ТВЕРЬ_КАЛИНИНА',3,15 union all
select 'FRESH_MAX_МСК_АВТОГРАД',4,15 union all
select 'FRESH_АЛТУФЬЕВО',3,15 union all
select 'FRESH_MAX_МСК_АТРИУМ',10,15 union all
select 'FRESH_MAX_МСК_БЫКОВО',6,15 union all
select 'FRESH_MAX_МСК_ЕВРОПОЛИС',3,15 union all
select 'FRESH_MAX_МСК_ЕРЕМИНО',10,15 union all
select 'FRESH_MAX_МСК_ЗЕЛЕНОГРАД',9,15 union all
select 'FRESH_MAX_МСК_КАШИРСКИЙ_ДВОР',6,15 union all
select 'FRESH_MAX_МСК_КРАСНАЯ_ПРЕСНЯ',7,15 union all
select 'FRESH_MAX_МСК_КРЫМСКАЯ',5,15 union all
select 'FRESH_MAX_МСК_КУНЦЕВО',5,15 union all
select 'FRESH_MAX_МСК_ЛОБАЧЕВСКОГО',4,15 union all
select 'FRESH_MAX_МСК_МИТИНО',5,15 union all
select 'FRESH_MAX_МСК_МОСКВА_СИТИ',5,15 union all
select 'FRESH_MAX_МСК_ЛЮБЕРЦЫ',5,15 union all
select 'FRESH_MAX_МСК_МЫТИЩИ',4,15 union all
select 'FRESH_MAX_МСК_НОВАЯ_РИГА',5,15 union all
select 'FRESH_MAX_МСК_ОДИНЦОВО',3,15 union all
select 'FRESH_MAX_МСК_ПАРК_ХАУС',5,15 union all
select 'FRESH_MAX_МСК_ПОДОЛЬСК',5,15 union all
select 'FRESH_MAX_МСК_ПРАВОБЕРЕЖНЫЙ',6,15 union all
select 'FRESH_MAX_МСК_ПРИВОЛЬНАЯ',5,15 union all
select 'FRESH_MAX_МСК_ПУШКИНО',6,15 union all
select 'FRESH_MAX_МСК_РЕУТОВ',5,15 union all
select 'FRESH_MAX_МСК_САВЕЛОВСКИЙ',4,15 union all
select 'FRESH_MAX_МСК_САЛАРЬЕВО',9,15 union all
select 'FRESH_MAX_МСК_ТРОИЦК',4,15 union all
select 'FRESH_MAX_МСК_ТЮМЕНСКИЙ',7,15 union all
select 'FRESH_MAX_МСК_ШАРИКОПОДШИПНИКОВСКАЯ',4,15 union all
select 'FRESH_МСК_ВАРШАВСКИЙ',6,15 union all
select 'FRESH_MAX_МСК_НОВЫЙ_АРБАТ',3,15 union all
select 'FRESH_MAX_МСК_ЩУКИНО',7,15 union all
select 'FRESH_MAX_МСК_ЭЛЕКТРОЗАВОДСКАЯ',6,15 union all
select 'FRESH_MAX_МСК_КАШИРСКИЙ_ДВОР_2',6,15 union all
select 'FRESH_MAX_МСК_КОНЬКОВО',3,15 union all
select 'ХОРУГВИНО_КГТ',6,15 union all
select 'ПУШКИНО_2_РФЦ',6,15 union all
select 'ЖУКОВСКИЙ_РФЦ',6,15 union all
select 'СПБ_БУГРЫ_РФЦ',6,15 union all
select 'САНКТ-ПЕТЕРБУРГ_РФЦ',6,15 union all
select 'ПАВЛО_СЛОБОДСКОЕ_кроссдокинг',6,15 union all
select 'МО_ЧЕРНАЯ_ГРЯЗЬ_ХАБ',6,15 union all
select 'FRESH_MAX_МСК_КОСИНСКАЯ',6,15 union all
select 'FRESH_MAX_МСК_ДМИТРОВСКИЙ',10,15 union all
select 'FRESH_МСК_ДМИТРОВСКИЙ',10,15 union all
select 'FRESH_ЧЛБ_ХУДЯКОВА',5,15 union all
select 'FRESH_НСК_КОРОЛЕВА',10,15 union all
select 'FRESH_GRC_МСК_ЧЕРТАНОВСКАЯ',6,15 union all
select 'FRESH_GRC_МСК_НАРОДНОЕ_ОПОЛЧЕНИЕ',6,15 union all
select 'FRESH_GRC_МСК_ЛАЗОРЕВЫЙ',4,15 union all
select 'FRESH_MAX_МСК_СМОЛЬНАЯ',5,15 union all
select 'FRESH_DC_МСК_РЯБИНОВАЯ',3,15 union all
select 'ПУШКИНО_1_РФЦ',6,15 union all
select 'ХОРУГВИНО_РФЦ',6,15 union all
select 'ПЕТРОВСКОЕ_РФЦ',6,15 union all
select 'Fresh_Фуд-сити',6,15 union all
select 'FRESH_MAX_МСК_АЛТУФЬЕВО',3,15 union all
select 'FRESH_GRC_МСК_КРЫМСКАЯ',5,15 union all
select 'FRESH_GRC_МСК_АТРИУМ',10,15 union all
select 'FRESH_GRC_МСК_НОВЫЙ_АРБАТ',3,15 union all
select 'FRESH_GRC_МСК_МОСКВА_СИТИ',5,15 union all
select 'FRESH_GRC_МСК_АЛТУФЬЕВО',3,15 union all
select 'FRESH_GRC_МСК_ДМИТРОВСКИЙ',10,15 union all
select 'FRESH_GRC_МСК_СМОЛЬНАЯ',5,15 union all
select 'FRESH_GRC_МСК_КОНЬКОВО',3,15 union all
select 'FRESH_GRC_МСК_ЩУКИНО',7,15 union all
select 'FRESH_GRC_МСК_СВОБОДЫ',7,15 union all
select 'FRESH_GRC_МСК_АЛТАЙСКАЯ',7,15 union all
select 'FRESH_GRC_МСК_ПЛЮЩИХА',3,15 union all
select 'FRESH_GRC_МСК_ВВЕДЕНСКОГО',5,15 union all
select 'FRESH_GRC_МСК_ТИХАЯ',5,15 union all
select 'FRESH_GRC_МСК_КВЕСИССКАЯ',5,15 union all
select 'FRESH_GRC_МСК_ЩЁЛКОВСКОЕ ШОССЕ',5,15 union all
select 'FRESH_GRC_МСК_ТАЛЛИНСКАЯ',5,15 union all
select 'FRESH_GRC_МСК_ЩЕЛКОВСКАЯ',5,15 union all
select 'FRESH_GRC_МСК_СТАРОКАЛУЖСКОЕ',5,15 union all
select 'FRESH_GRC_МСК_КРАСНОГОРСКИЙ_БУЛЬВАР',5,15 union all
select 'FRESH_GRC_МСК_КРАСНОГОРСК_ПИОНЕРСКАЯ',5,15 union all
select 'FRESH_GRC_МСК_ГЕНЕРАЛА_БЕЛОВА',5,15 ;