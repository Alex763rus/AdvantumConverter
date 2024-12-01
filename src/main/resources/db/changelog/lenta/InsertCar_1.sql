insert into car(car_name, pallet, tonnage, temperature_min, temperature_max)
select 'РЕФ_12_ТОНН_2_4_C_ГИДРОБОРТ', 77777, 12000, 2, 4  union all
select 'РЕФ_5_ТОНН_2_4_C_ГИДРОБОРТ', 77777, 5000, 2, 4
;