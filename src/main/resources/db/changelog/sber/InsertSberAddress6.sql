-- адреса сбера
insert into sber_address(city, address, city_and_region)
select 'Новосибирск, Красноярск', '660118, г. Красноярск ул., Полигонная зд. 8Д', 'Красноярск'
;

CREATE UNIQUE INDEX IF NOT EXISTS idx_sber_address_unique
    ON sber_address (city, address, city_and_region);