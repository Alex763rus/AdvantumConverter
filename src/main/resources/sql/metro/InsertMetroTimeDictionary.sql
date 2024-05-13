# Справочник временных окон приемки метро
-- #     ="select "&A2&", '"&B2&"', '"&C2&"', '"&D2&"', "&F2&" union all"
-- drop table metro_time_dictionary;
-- select * from metro_time_dictionary;
select count(*) from metro_time_dictionary;
REPLACE INTO metro_time_dictionary(time_id, code, time_start, time_end, priority)
select 10, 'FISH', '23:00', '03:00', 1 union all
select 10, 'ИКРА', '14:00', '06:00', 1 union all
select 10, 'MEAT', '23:00', '03:00', 2 union all
select 10, 'FV', '00:00', '03:00', 3 union all
select 10, 'FRESH', '23:00', '03:00', 3 union all
select 10, 'CHILLED', '00:00', '03:00', 3 union all
select 10, 'MILK', '23:00', '03:00', 3 union all
select 10, 'FROZEN', '23:00', '06:00', 4 union all
select 10, 'BB H', '14:00', '06:00', 5 union all
select 10, 'CD', '14:00', '06:00', 5 union all
select 10, 'ALC', '14:00', '06:00', 5 union all
select 10, 'IMPORT', '14:00', '06:00', 5 union all
select 10, 'BB A', '14:00', '06:00', 5 union all
select 11, 'FISH', '00:00', '02:00', 1 union all
select 11, 'ИКРА', '14:00', '06:00', 1 union all
select 11, 'MEAT', '00:00', '02:00', 2 union all
select 11, 'FV', '00:00', '02:00', 3 union all
select 11, 'FRESH', '00:00', '02:00', 3 union all
select 11, 'CHILLED', '00:00', '02:00', 3 union all
select 11, 'MILK', '00:00', '02:00', 3 union all
select 11, 'FROZEN', '03:00', '09:00', 4 union all
select 11, 'BB H', '14:00', '06:00', 5 union all
select 11, 'CD', '14:00', '06:00', 5 union all
select 11, 'ALC', '14:00', '06:00', 5 union all
select 11, 'IMPORT', '14:00', '06:00', 5 union all
select 11, 'BB A', '14:00', '06:00', 5 union all
select 12, 'FISH', '01:00', '03:00', 1 union all
select 12, 'ИКРА', '14:00', '06:00', 1 union all
select 12, 'MEAT', '01:00', '03:00', 2 union all
select 12, 'FV', '01:00', '03:00', 3 union all
select 12, 'FRESH', '01:00', '03:00', 3 union all
select 12, 'CHILLED', '01:00', '03:00', 3 union all
select 12, 'MILK', '01:00', '03:00', 3 union all
select 12, 'FROZEN', '00:00', '09:00', 4 union all
select 12, 'BB H', '14:00', '06:00', 5 union all
select 12, 'CD', '14:00', '06:00', 5 union all
select 12, 'ALC', '14:00', '06:00', 5 union all
select 12, 'IMPORT', '14:00', '06:00', 5 union all
select 12, 'BB A', '14:00', '06:00', 5 union all
select 1202, 'FISH', '08:00', '17:00', 1 union all
select 1202, 'ИКРА', '08:00', '18:00', 1 union all
select 1202, 'MEAT', '08:00', '17:00', 2 union all
select 1202, 'FV', '08:00', '17:00', 3 union all
select 1202, 'FRESH', '08:00', '17:00', 3 union all
select 1202, 'CHILLED', '08:00', '17:00', 3 union all
select 1202, 'MILK', '08:00', '17:00', 3 union all
select 1202, 'FROZEN', '08:00', '17:00', 4 union all
select 1202, 'IMPORT', '08:00', '17:00', 5 union all
select 1202, 'ALC', '08:00', '17:00', 5 union all
select 1202, 'BB A', '08:00', '17:00', 5 union all
select 1202, 'BB H', '08:00', '18:00', 5 union all
select 1202, 'CD', '08:00', '17:00', 5 union all
select 13, 'FISH', '01:00', '03:00', 1 union all
select 13, 'ИКРА', '14:00', '06:00', 1 union all
select 13, 'MEAT', '01:00', '03:00', 2 union all
select 13, 'FV', '01:00', '03:00', 3 union all
select 13, 'FRESH', '01:00', '03:00', 3 union all
select 13, 'CHILLED', '01:00', '03:00', 3 union all
select 13, 'MILK', '01:00', '03:00', 3 union all
select 13, 'FROZEN', '04:00', '06:00', 4 union all
select 13, 'BB H', '14:00', '06:00', 5 union all
select 13, 'CD', '14:00', '06:00', 5 union all
select 13, 'ALC', '14:00', '06:00', 5 union all
select 13, 'IMPORT', '14:00', '06:00', 5 union all
select 13, 'BB A', '14:00', '06:00', 5 union all
select 1304, 'FISH', '08:00', '12:00', 1 union all
select 1304, 'ИКРА', '09:00', '18:00', 1 union all
select 1304, 'MEAT', '09:00', '18:00', 2 union all
select 1304, 'FV', '09:00', '18:00', 3 union all
select 1304, 'FRESH', '09:00', '18:00', 3 union all
select 1304, 'CHILLED', '09:00', '18:00', 3 union all
select 1304, 'MILK', '09:00', '18:00', 3 union all
select 1304, 'FROZEN', '09:00', '18:00', 4 union all
select 1304, 'BB H', '09:00', '18:00', 5 union all
select 1304, 'CD', '09:00', '18:00', 5 union all
select 1304, 'ALC', '09:00', '18:00', 5 union all
select 1304, 'IMPORT', '09:00', '18:00', 5 union all
select 1304, 'BB A', '09:00', '18:00', 5 union all
select 1306, 'ИКРА', '07:00', '19:00', 1 union all
select 1306, 'MEAT', '07:00', '17:00', 2 union all
select 1306, 'FV', '07:00', '17:00', 3 union all
select 1306, 'FRESH', '07:00', '17:00', 3 union all
select 1306, 'CHILLED', '07:00', '17:00', 3 union all
select 1306, 'MILK', '07:00', '17:00', 3 union all
select 1306, 'FROZEN', '07:00', '19:00', 4 union all
select 1306, 'BB H', '07:00', '19:00', 5 union all
select 1306, 'CD', '07:00', '19:00', 5 union all
select 1306, 'ALC', '07:00', '19:00', 5 union all
select 1306, 'IMPORT', '07:00', '19:00', 5 union all
select 1306, 'BB A', '07:00', '19:00', 5 union all
select 1307, 'FISH', '20:00', '22:00', 1 union all
select 1307, 'ИКРА', '06:00', '22:30', 1 union all
select 1307, 'MEAT', '20:00', '22:00', 2 union all
select 1307, 'CHILLED', '20:00', '22:00', 3 union all
select 1307, 'FV', '20:00', '22:00', 3 union all
select 1307, 'FRESH', '20:00', '22:00', 3 union all
select 1307, 'MILK', '20:00', '22:00', 3 union all
select 1307, 'FROZEN', '06:00', '13:00', 4 union all
select 1307, 'BB H', '06:00', '22:30', 5 union all
select 1307, 'CD', '06:00', '22:30', 5 union all
select 1307, 'ALC', '06:00', '22:30', 5 union all
select 1307, 'IMPORT', '06:00', '22:30', 5 union all
select 1307, 'BB A', '06:00', '22:30', 5 union all
select 1308, 'FISH', '22:00', '04:00', 1 union all
select 1308, 'ИКРА', '22:00', '16:00', 1 union all
select 1308, 'MEAT', '22:00', '04:00', 2 union all
select 1308, 'FRESH', '22:00', '04:00', 3 union all
select 1308, 'FV', '22:00', '04:00', 3 union all
select 1308, 'CHILLED', '22:00', '04:00', 3 union all
select 1308, 'MILK', '01:00', '05:00', 3 union all
select 1308, 'FROZEN', '22:00', '04:00', 4 union all
select 1308, 'BB H', '22:00', '16:00', 5 union all
select 1308, 'CD', '22:00', '16:00', 5 union all
select 1308, 'ALC', '22:00', '16:00', 5 union all
select 1308, 'IMPORT', '22:00', '16:00', 5 union all
select 1308, 'BB A', '22:00', '16:00', 5 union all
select 1309, 'FISH', '08:00', '10:00', 1 union all
select 1309, 'ИКРА', '09:00', '18:00', 1 union all
select 1309, 'MEAT', '09:00', '15:00', 2 union all
select 1309, 'FRESH', '08:00', '10:00', 3 union all
select 1309, 'FV', '09:00', '15:00', 3 union all
select 1309, 'CHILLED', '09:00', '15:00', 3 union all
select 1309, 'MILK', '09:00', '15:00', 3 union all
select 1309, 'FROZEN', '09:00', '18:00', 4 union all
select 1309, 'BB H', '09:00', '18:00', 5 union all
select 1309, 'CD', '09:00', '18:00', 5 union all
select 1309, 'ALC', '09:00', '18:00', 5 union all
select 1309, 'IMPORT', '09:00', '18:00', 5 union all
select 1309, 'BB A', '09:00', '18:00', 5 union all
select 1310, 'FISH', '11:00', '18:00', 1 union all
select 1310, 'ИКРА', '09:00', '18:00', 1 union all
select 1310, 'MEAT', '11:00', '18:00', 2 union all
select 1310, 'FV', '08:00', '18:00', 3 union all
select 1310, 'FRESH', '11:00', '18:00', 3 union all
select 1310, 'CHILLED', '08:00', '18:00', 3 union all
select 1310, 'MILK', '08:00', '18:00', 3 union all
select 1310, 'FROZEN', '09:00', '13:00', 4 union all
select 1310, 'BB H', '09:00', '18:00', 5 union all
select 1310, 'CD', '09:00', '18:00', 5 union all
select 1310, 'ALC', '09:00', '18:00', 5 union all
select 1310, 'IMPORT', '09:00', '18:00', 5 union all
select 1310, 'BB A', '09:00', '18:00', 5 union all
select 1311, 'FISH', '08:00', '12:00', 1 union all
select 1311, 'ИКРА', '07:00', '20:00', 1 union all
select 1311, 'MEAT', '07:00', '20:00', 2 union all
select 1311, 'FV', '07:00', '20:00', 3 union all
select 1311, 'FRESH', '07:00', '20:00', 3 union all
select 1311, 'CHILLED', '07:00', '20:00', 3 union all
select 1311, 'MILK', '07:00', '20:00', 3 union all
select 1311, 'FROZEN', '07:00', '20:00', 4 union all
select 1311, 'BB H', '07:00', '20:00', 5 union all
select 1311, 'CD', '07:00', '20:00', 5 union all
select 1311, 'ALC', '07:00', '20:00', 5 union all
select 1311, 'IMPORT', '07:00', '20:00', 5 union all
select 1311, 'BB A', '07:00', '20:00', 5 union all
select 1317, 'FISH', '07:00', '15:00', 1 union all
select 1317, 'ИКРА', '07:00', '15:00', 1 union all
select 1317, 'MEAT', '10:00', '15:00', 2 union all
select 1317, 'FV', '07:00', '15:00', 3 union all
select 1317, 'FRESH', '07:00', '15:00', 3 union all
select 1317, 'CHILLED', '07:00', '15:00', 3 union all
select 1317, 'MILK', '07:00', '15:00', 3 union all
select 1317, 'FROZEN', '07:00', '15:00', 4 union all
select 1317, 'BB H', '07:00', '15:00', 5 union all
select 1317, 'CD', '07:00', '15:00', 5 union all
select 1317, 'ALC', '07:00', '15:00', 5 union all
select 1317, 'IMPORT', '07:00', '15:00', 5 union all
select 1317, 'BB A', '07:00', '15:00', 5 union all
select 1318, 'FISH', '04:00', '06:00', 1 union all
select 1318, 'ИКРА', '06:00', '16:00', 1 union all
select 1318, 'MEAT', '04:00', '06:00', 2 union all
select 1318, 'FV', '04:00', '06:00', 3 union all
select 1318, 'FRESH', '04:00', '06:00', 3 union all
select 1318, 'CHILLED', '04:00', '06:00', 3 union all
select 1318, 'MILK', '04:00', '06:00', 3 union all
select 1318, 'FROZEN', '07:00', '14:00', 4 union all
select 1318, 'BB H', '04:00', '06:00', 5 union all
select 1318, 'CD', '04:00', '06:00', 5 union all
select 1318, 'ALC', '04:00', '06:00', 5 union all
select 1318, 'IMPORT', '04:00', '06:00', 5 union all
select 1318, 'BB A', '04:00', '06:00', 5 union all
select 1320, 'FISH', '05:00', '06:00', 1 union all
select 1320, 'ИКРА', '11:00', '16:00', 1 union all
select 1320, 'MEAT', '05:00', '06:00', 2 union all
select 1320, 'FRESH', '05:00', '06:00', 3 union all
select 1320, 'FV', '06:30', '12:00', 3 union all
select 1320, 'CHILLED', '06:30', '12:00', 3 union all
select 1320, 'MILK', '06:00', '12:00', 3 union all
select 1320, 'FROZEN', '11:00', '16:00', 4 union all
select 1320, 'BB H', '11:00', '16:00', 5 union all
select 1320, 'CD', '11:00', '16:00', 5 union all
select 1320, 'ALC', '11:00', '16:00', 5 union all
select 1320, 'IMPORT', '11:00', '16:00', 5 union all
select 1320, 'BB A', '11:00', '16:00', 5 union all
select 1322, 'FISH', '04:00', '08:00', 1 union all
select 1322, 'ИКРА', '07:00', '17:00', 1 union all
select 1322, 'MEAT', '04:00', '08:00', 2 union all
select 1322, 'CHILLED', '04:00', '08:00', 3 union all
select 1322, 'FV', '04:00', '08:00', 3 union all
select 1322, 'FRESH', '04:00', '08:00', 3 union all
select 1322, 'MILK', '05:00', '07:00', 3 union all
select 1322, 'FROZEN', '04:00', '08:00', 4 union all
select 1322, 'CD', '04:00', '17:00', 5 union all
select 1322, 'BB H', '04:00', '17:00', 5 union all
select 1322, 'ALC', '04:00', '17:00', 5 union all
select 1322, 'IMPORT', '04:00', '17:00', 5 union all
select 1322, 'BB A', '04:00', '17:00', 5 union all
select 1336, 'FISH', '12:00', '18:00', 1 union all
select 1336, 'ИКРА', '07:00', '17:00', 1 union all
select 1336, 'MEAT', '12:00', '18:00', 2 union all
select 1336, 'FV', '14:00', '18:00', 3 union all
select 1336, 'FRESH', '14:00', '18:00', 3 union all
select 1336, 'CHILLED', '14:00', '18:00', 3 union all
select 1336, 'MILK', '14:00', '18:00', 3 union all
select 1336, 'FROZEN', '12:00', '18:00', 4 union all
select 1336, 'BB H', '07:00', '17:00', 5 union all
select 1336, 'CD', '07:00', '17:00', 5 union all
select 1336, 'ALC', '07:00', '17:00', 5 union all
select 1336, 'IMPORT', '07:00', '17:00', 5 union all
select 1336, 'BB A', '07:00', '17:00', 5 union all
select 1337, 'FISH', '15:00', '20:00', 1 union all
select 1337, 'ИКРА', '10:00', '16:00', 1 union all
select 1337, 'MEAT', '15:00', '20:00', 2 union all
select 1337, 'FV', '10:00', '15:00', 3 union all
select 1337, 'FRESH', '10:00', '15:00', 3 union all
select 1337, 'CHILLED', '10:00', '15:00', 3 union all
select 1337, 'MILK', '10:00', '15:00', 3 union all
select 1337, 'FROZEN', '10:00', '15:00', 4 union all
select 1337, 'BB H', '10:00', '16:00', 5 union all
select 1337, 'CD', '10:00', '16:00', 5 union all
select 1337, 'ALC', '10:00', '16:00', 5 union all
select 1337, 'IMPORT', '10:00', '16:00', 5 union all
select 1337, 'BB A', '10:00', '16:00', 5 union all
select 1340, 'FISH', '09:00', '18:00', 1 union all
select 1340, 'ИКРА', '09:00', '18:00', 1 union all
select 1340, 'MEAT', '09:00', '18:00', 2 union all
select 1340, 'FV', '09:00', '18:00', 3 union all
select 1340, 'FRESH', '09:00', '18:00', 3 union all
select 1340, 'CHILLED', '09:00', '18:00', 3 union all
select 1340, 'MILK', '09:00', '18:00', 3 union all
select 1340, 'FROZEN', '09:00', '18:00', 4 union all
select 1340, 'BB H', '09:00', '18:00', 5 union all
select 1340, 'CD', '09:00', '18:00', 5 union all
select 1340, 'ALC', '09:00', '18:00', 5 union all
select 1340, 'IMPORT', '09:00', '18:00', 5 union all
select 1340, 'BB A', '09:00', '18:00', 5 union all
select 1342, 'FISH', '14:00', '20:00', 1 union all
select 1342, 'ИКРА', '09:00', '18:00', 1 union all
select 1342, 'MEAT', '14:00', '20:00', 2 union all
select 1342, 'FV', '09:00', '18:00', 3 union all
select 1342, 'FRESH', '09:00', '18:00', 3 union all
select 1342, 'CHILLED', '09:00', '18:00', 3 union all
select 1342, 'MILK', '09:00', '18:00', 3 union all
select 1342, 'FROZEN', '09:00', '18:00', 4 union all
select 1342, 'BB H', '09:00', '18:00', 5 union all
select 1342, 'CD', '09:00', '18:00', 5 union all
select 1342, 'ALC', '09:00', '18:00', 5 union all
select 1342, 'IMPORT', '09:00', '18:00', 5 union all
select 1342, 'BB A', '09:00', '18:00', 5 union all
select 1356, 'FISH', '00:00', '06:00', 1 union all
select 1356, 'ИКРА', '23:00', '07:00', 1 union all
select 1356, 'MEAT', '00:00', '06:00', 2 union all
select 1356, 'FV', '03:00', '05:00', 3 union all
select 1356, 'FRESH', '03:00', '05:00', 3 union all
select 1356, 'CHILLED', '03:00', '05:00', 3 union all
select 1356, 'MILK', '03:00', '05:00', 3 union all
select 1356, 'FROZEN', '23:00', '07:00', 4 union all
select 1356, 'BB H', '23:00', '07:00', 5 union all
select 1356, 'CD', '23:00', '07:00', 5 union all
select 1356, 'ALC', '23:00', '07:00', 5 union all
select 1356, 'IMPORT', '23:00', '07:00', 5 union all
select 1356, 'BB A', '23:00', '07:00', 5 union all
select 1357, 'FISH', '07:00', '11:00', 1 union all
select 1357, 'ИКРА', '06:00', '14:00', 1 union all
select 1357, 'MEAT', '07:00', '11:00', 2 union all
select 1357, 'FV', '05:00', '13:00', 3 union all
select 1357, 'FRESH', '05:00', '13:00', 3 union all
select 1357, 'CHILLED', '05:00', '13:00', 3 union all
select 1357, 'MILK', '05:00', '13:00', 3 union all
select 1357, 'FROZEN', '06:00', '14:00', 4 union all
select 1357, 'BB H', '06:00', '14:00', 5 union all
select 1357, 'CD', '06:00', '14:00', 5 union all
select 1357, 'ALC', '06:00', '14:00', 5 union all
select 1357, 'IMPORT', '06:00', '14:00', 5 union all
select 1357, 'BB A', '06:00', '14:00', 5 union all
select 1363, 'FISH', '04:00', '08:00', 1 union all
select 1363, 'ИКРА', '06:30', '16:00', 1 union all
select 1363, 'MEAT', '04:00', '08:00', 2 union all
select 1363, 'FV', '04:00', '08:00', 3 union all
select 1363, 'FRESH', '04:00', '08:00', 3 union all
select 1363, 'CHILLED', '04:00', '08:00', 3 union all
select 1363, 'MILK', '04:00', '08:00', 3 union all
select 1363, 'FROZEN', '04:00', '08:00', 4 union all
select 1363, 'BB H', '04:00', '08:00', 5 union all
select 1363, 'CD', '04:00', '08:00', 5 union all
select 1363, 'ALC', '04:00', '08:00', 5 union all
select 1363, 'IMPORT', '04:00', '08:00', 5 union all
select 1363, 'BB A', '04:00', '08:00', 5 union all
select 14, 'FISH', '03:00', '06:00', 1 union all
select 14, 'ИКРА', '22:00', '17:00', 1 union all
select 14, 'MEAT', '00:00', '06:00', 2 union all
select 14, 'FV', '02:00', '04:00', 3 union all
select 14, 'FRESH', '02:00', '04:00', 3 union all
select 14, 'CHILLED', '02:00', '04:00', 3 union all
select 14, 'MILK', '02:00', '04:00', 3 union all
select 14, 'FROZEN', '03:00', '09:00', 4 union all
select 14, 'BB H', '22:00', '17:00', 5 union all
select 14, 'CD', '22:00', '17:00', 5 union all
select 14, 'ALC', '22:00', '17:00', 5 union all
select 14, 'IMPORT', '22:00', '17:00', 5 union all
select 14, 'BB A', '22:00', '17:00', 5 union all
select 1444, 'FISH', '00:00', '06:00', 1 union all
select 1444, 'ИКРА', '22:00', '06:00', 1 union all
select 1444, 'MEAT', '00:00', '06:00', 2 union all
select 1444, 'FV', '00:00', '03:00', 3 union all
select 1444, 'FRESH', '00:00', '03:00', 3 union all
select 1444, 'CHILLED', '00:00', '03:00', 3 union all
select 1444, 'MILK', '00:00', '03:00', 3 union all
select 1444, 'FROZEN', '22:00', '06:00', 4 union all
select 1444, 'BB H', '22:00', '06:00', 5 union all
select 1444, 'CD', '22:00', '06:00', 5 union all
select 1444, 'ALC', '22:00', '06:00', 5 union all
select 1444, 'IMPORT', '22:00', '06:00', 5 union all
select 1444, 'BB A', '22:00', '06:00', 5 union all
select 1491, 'FISH', '10:00', '13:00', 1 union all
select 1491, 'ИКРА', '09:00', '16:00', 1 union all
select 1491, 'MEAT', '10:00', '13:00', 2 union all
select 1491, 'FV', '09:00', '16:00', 3 union all
select 1491, 'FRESH', '09:00', '16:00', 3 union all
select 1491, 'CHILLED', '09:00', '16:00', 3 union all
select 1491, 'MILK', '09:00', '16:00', 3 union all
select 1491, 'FROZEN', '09:00', '16:00', 4 union all
select 1491, 'BB H', '09:00', '16:00', 5 union all
select 1491, 'CD', '09:00', '16:00', 5 union all
select 1491, 'ALC', '09:00', '16:00', 5 union all
select 1491, 'IMPORT', '09:00', '16:00', 5 union all
select 1491, 'BB A', '09:00', '16:00', 5 union all
select 1492, 'FISH', '12:00', '15:00', 1 union all
select 1492, 'ИКРА', '09:00', '16:00', 1 union all
select 1492, 'MEAT', '12:00', '15:00', 2 union all
select 1492, 'FV', '09:00', '16:00', 3 union all
select 1492, 'FRESH', '09:00', '16:00', 3 union all
select 1492, 'CHILLED', '09:00', '16:00', 3 union all
select 1492, 'MILK', '09:00', '16:00', 3 union all
select 1492, 'FROZEN', '09:00', '16:00', 4 union all
select 1492, 'BB H', '09:00', '16:00', 5 union all
select 1492, 'CD', '09:00', '16:00', 5 union all
select 1492, 'ALC', '09:00', '16:00', 5 union all
select 1492, 'IMPORT', '09:00', '16:00', 5 union all
select 1492, 'BB A', '09:00', '16:00', 5 union all
select 15, 'FISH', '22:00', '04:00', 1 union all
select 15, 'ИКРА', '07:00', '16:00', 1 union all
select 15, 'MEAT', '22:00', '04:00', 2 union all
select 15, 'CHILLED', '00:00', '08:00', 3 union all
select 15, 'FV', '00:00', '08:00', 3 union all
select 15, 'FRESH', '07:00', '16:00', 3 union all
select 15, 'MILK', '07:00', '16:00', 3 union all
select 15, 'FROZEN', '22:00', '04:00', 4 union all
select 15, 'IMPORT', '00:00', '08:00', 5 union all
select 15, 'ALC', '00:00', '08:00', 5 union all
select 15, 'BB H', '00:00', '08:00', 5 union all
select 15, 'BB A', '00:00', '08:00', 5 union all
select 15, 'CD', '07:00', '16:00', 5 union all
select 16, 'FISH', '22:00', '04:00', 1 union all
select 16, 'ИКРА', '07:00', '15:00', 1 union all
select 16, 'MEAT', '22:00', '04:00', 2 union all
select 16, 'FV', '00:00', '08:00', 3 union all
select 16, 'FRESH', '06:00', '14:00', 3 union all
select 16, 'CHILLED', '00:00', '08:00', 3 union all
select 16, 'MILK', '06:00', '14:00', 3 union all
select 16, 'FROZEN', '22:00', '04:00', 4 union all
select 16, 'CD', '00:00', '08:00', 5 union all
select 16, 'BB H', '00:00', '08:00', 5 union all
select 16, 'ALC', '00:00', '08:00', 5 union all
select 16, 'IMPORT', '00:00', '08:00', 5 union all
select 16, 'BB A', '00:00', '08:00', 5 union all
select 17, 'FISH', '00:00', '04:00', 1 union all
select 17, 'ИКРА', '14:00', '06:00', 1 union all
select 17, 'MEAT', '00:00', '06:00', 2 union all
select 17, 'FV', '03:00', '05:00', 3 union all
select 17, 'FRESH', '03:00', '05:00', 3 union all
select 17, 'CHILLED', '03:00', '05:00', 3 union all
select 17, 'MILK', '03:00', '05:00', 3 union all
select 17, 'FROZEN', '00:00', '06:00', 4 union all
select 17, 'BB H', '14:00', '06:00', 5 union all
select 17, 'CD', '14:00', '06:00', 5 union all
select 17, 'ALC', '14:00', '06:00', 5 union all
select 17, 'IMPORT', '14:00', '06:00', 5 union all
select 17, 'BB A', '14:00', '06:00', 5 union all
select 18, 'FISH', '00:00', '06:00', 1 union all
select 18, 'ИКРА', '14:00', '06:00', 1 union all
select 18, 'MEAT', '00:00', '06:00', 2 union all
select 18, 'FV', '23:00', '01:00', 3 union all
select 18, 'FRESH', '23:00', '01:00', 3 union all
select 18, 'CHILLED', '23:00', '01:00', 3 union all
select 18, 'MILK', '23:00', '01:00', 3 union all
select 18, 'FROZEN', '03:00', '09:00', 4 union all
select 18, 'BB H', '14:00', '06:00', 5 union all
select 18, 'CD', '14:00', '06:00', 5 union all
select 18, 'ALC', '14:00', '06:00', 5 union all
select 18, 'IMPORT', '14:00', '06:00', 5 union all
select 18, 'BB A', '14:00', '06:00', 5 union all
select 19, 'FISH', '00:00', '06:00', 1 union all
select 19, 'ИКРА', '22:00', '18:00', 1 union all
select 19, 'MEAT', '00:00', '06:00', 2 union all
select 19, 'FV', '03:00', '05:00', 3 union all
select 19, 'FRESH', '03:00', '05:00', 3 union all
select 19, 'CHILLED', '03:00', '05:00', 3 union all
select 19, 'MILK', '03:00', '05:00', 3 union all
select 19, 'FROZEN', '03:00', '09:00', 4 union all
select 19, 'BB H', '22:00', '18:00', 5 union all
select 19, 'CD', '22:00', '18:00', 5 union all
select 19, 'ALC', '22:00', '18:00', 5 union all
select 19, 'IMPORT', '22:00', '18:00', 5 union all
select 19, 'BB A', '22:00', '18:00', 5 union all
select 20, 'FISH', '22:00', '04:00', 1 union all
select 20, 'ИКРА', '07:00', '16:00', 1 union all
select 20, 'MEAT', '22:00', '04:00', 2 union all
select 20, 'FV', '00:00', '08:00', 3 union all
select 20, 'FRESH', '07:00', '16:00', 3 union all
select 20, 'CHILLED', '00:00', '08:00', 3 union all
select 20, 'MILK', '07:00', '16:00', 3 union all
select 20, 'FROZEN', '22:00', '04:00', 4 union all
select 20, 'BB H', '00:00', '08:00', 5 union all
select 20, 'CD', '00:00', '08:00', 5 union all
select 20, 'ALC', '00:00', '08:00', 5 union all
select 20, 'IMPORT', '00:00', '08:00', 5 union all
select 20, 'BB A', '00:00', '08:00', 5 union all
select 21, 'FISH', '10:00', '13:00', 1 union all
select 21, 'ИКРА', '09:00', '13:00', 1 union all
select 21, 'MEAT', '10:00', '13:00', 2 union all
select 21, 'FV', '08:00', '14:00', 3 union all
select 21, 'FRESH', '08:00', '14:00', 3 union all
select 21, 'CHILLED', '08:00', '14:00', 3 union all
select 21, 'MILK', '08:00', '14:00', 3 union all
select 21, 'FROZEN', '09:00', '13:00', 4 union all
select 21, 'BB H', '09:00', '13:00', 5 union all
select 21, 'CD', '09:00', '13:00', 5 union all
select 21, 'ALC', '09:00', '13:00', 5 union all
select 21, 'IMPORT', '09:00', '13:00', 5 union all
select 21, 'BB A', '09:00', '13:00', 5 union all
select 22, 'FISH', '06:30', '07:30', 1 union all
select 22, 'ИКРА', '06:30', '12:00', 1 union all
select 22, 'MEAT', '07:30', '10:00', 2 union all
select 22, 'FV', '06:30', '12:00', 3 union all
select 22, 'FRESH', '06:30', '12:00', 3 union all
select 22, 'CHILLED', '06:30', '12:00', 3 union all
select 22, 'MILK', '06:30', '12:00', 3 union all
select 22, 'FROZEN', '06:30', '12:00', 4 union all
select 22, 'BB H', '06:30', '12:00', 5 union all
select 22, 'CD', '06:30', '12:00', 5 union all
select 22, 'ALC', '06:30', '12:00', 5 union all
select 22, 'IMPORT', '06:30', '12:00', 5 union all
select 22, 'BB A', '06:30', '12:00', 5 union all
select 23, 'FISH', '17:00', '19:00', 1 union all
select 23, 'ИКРА', '06:30', '12:00', 1 union all
select 23, 'MEAT', '08:00', '14:00', 2 union all
select 23, 'FV', '08:00', '14:00', 3 union all
select 23, 'FRESH', '08:00', '14:00', 3 union all
select 23, 'CHILLED', '08:00', '14:00', 3 union all
select 23, 'MILK', '08:00', '14:00', 3 union all
select 23, 'FROZEN', '06:30', '12:00', 4 union all
select 23, 'BB H', '06:30', '12:00', 5 union all
select 23, 'CD', '06:30', '12:00', 5 union all
select 23, 'ALC', '06:30', '12:00', 5 union all
select 23, 'IMPORT', '06:30', '12:00', 5 union all
select 23, 'BB A', '06:30', '12:00', 5 union all
select 24, 'FISH', '05:00', '07:00', 1 union all
select 24, 'ИКРА', '08:00', '14:00', 1 union all
select 24, 'MEAT', '07:00', '10:00', 2 union all
select 24, 'FV', '08:00', '14:00', 3 union all
select 24, 'FRESH', '08:00', '14:00', 3 union all
select 24, 'CHILLED', '08:00', '14:00', 3 union all
select 24, 'MILK', '08:00', '14:00', 3 union all
select 24, 'FROZEN', '08:00', '14:00', 4 union all
select 24, 'BB H', '08:00', '14:00', 5 union all
select 24, 'CD', '08:00', '14:00', 5 union all
select 24, 'ALC', '08:00', '14:00', 5 union all
select 24, 'IMPORT', '08:00', '14:00', 5 union all
select 24, 'BB A', '08:00', '14:00', 5 union all
select 25, 'FISH', '12:00', '19:00', 1 union all
select 25, 'ИКРА', '10:00', '18:00', 1 union all
select 25, 'MEAT', '12:00', '19:00', 2 union all
select 25, 'FV', '08:00', '14:00', 3 union all
select 25, 'FRESH', '08:00', '14:00', 3 union all
select 25, 'CHILLED', '08:00', '14:00', 3 union all
select 25, 'MILK', '08:00', '14:00', 3 union all
select 25, 'FROZEN', '09:00', '18:00', 4 union all
select 25, 'BB H', '10:00', '18:00', 5 union all
select 25, 'CD', '10:00', '18:00', 5 union all
select 25, 'ALC', '10:00', '18:00', 5 union all
select 25, 'IMPORT', '10:00', '18:00', 5 union all
select 25, 'BB A', '10:00', '18:00', 5 union all
select 26, 'FISH', '05:00', '06:00', 1 union all
select 26, 'ИКРА', '10:00', '16:00', 1 union all
select 26, 'MEAT', '14:00', '20:00', 2 union all
select 26, 'FV', '10:00', '16:00', 3 union all
select 26, 'FRESH', '10:00', '16:00', 3 union all
select 26, 'CHILLED', '10:00', '16:00', 3 union all
select 26, 'MILK', '10:00', '16:00', 3 union all
select 26, 'FROZEN', '10:00', '16:00', 4 union all
select 26, 'BB H', '10:00', '16:00', 5 union all
select 26, 'CD', '10:00', '16:00', 5 union all
select 26, 'ALC', '10:00', '16:00', 5 union all
select 26, 'IMPORT', '10:00', '16:00', 5 union all
select 26, 'BB A', '10:00', '16:00', 5 union all
select 27, 'FISH', '04:00', '08:00', 1 union all
select 27, 'ИКРА', '07:00', '12:00', 1 union all
select 27, 'MEAT', '08:00', '14:00', 2 union all
select 27, 'FV', '08:00', '14:00', 3 union all
select 27, 'FRESH', '08:00', '14:00', 3 union all
select 27, 'CHILLED', '08:00', '14:00', 3 union all
select 27, 'MILK', '08:00', '14:00', 3 union all
select 27, 'FROZEN', '07:00', '12:00', 4 union all
select 27, 'BB H', '07:00', '12:00', 5 union all
select 27, 'CD', '07:00', '12:00', 5 union all
select 27, 'ALC', '07:00', '12:00', 5 union all
select 27, 'IMPORT', '07:00', '12:00', 5 union all
select 27, 'BB A', '07:00', '12:00', 5 union all
select 28, 'FISH', '07:00', '10:00', 1 union all
select 28, 'ИКРА', '06:30', '12:00', 1 union all
select 28, 'MEAT', '07:30', '13:00', 2 union all
select 28, 'FV', '07:30', '13:00', 3 union all
select 28, 'FRESH', '07:30', '13:00', 3 union all
select 28, 'CHILLED', '07:30', '13:00', 3 union all
select 28, 'MILK', '07:30', '13:00', 3 union all
select 28, 'FROZEN', '06:30', '12:00', 4 union all
select 28, 'BB H', '06:30', '12:00', 5 union all
select 28, 'CD', '06:30', '12:00', 5 union all
select 28, 'ALC', '06:30', '12:00', 5 union all
select 28, 'IMPORT', '06:30', '12:00', 5 union all
select 28, 'BB A', '06:30', '12:00', 5 union all
select 29, 'FISH', '12:00', '17:00', 1 union all
select 29, 'ИКРА', '10:00', '18:00', 1 union all
select 29, 'MEAT', '12:00', '17:00', 2 union all
select 29, 'FV', '06:30', '16:00', 3 union all
select 29, 'FRESH', '06:30', '16:00', 3 union all
select 29, 'CHILLED', '06:30', '16:00', 3 union all
select 29, 'MILK', '06:30', '16:00', 3 union all
select 29, 'FROZEN', '16:00', '21:00', 4 union all
select 29, 'BB H', '10:00', '18:00', 5 union all
select 29, 'CD', '10:00', '18:00', 5 union all
select 29, 'ALC', '10:00', '18:00', 5 union all
select 29, 'IMPORT', '10:00', '18:00', 5 union all
select 29, 'BB A', '10:00', '18:00', 5 union all
select 30, 'FISH', '08:00', '13:00', 1 union all
select 30, 'ИКРА', '16:00', '21:00', 1 union all
select 30, 'MEAT', '08:00', '13:00', 2 union all
select 30, 'FV', '06:30', '16:00', 3 union all
select 30, 'FRESH', '06:30', '16:00', 3 union all
select 30, 'CHILLED', '06:30', '16:00', 3 union all
select 30, 'MILK', '06:30', '16:00', 3 union all
select 30, 'FROZEN', '16:00', '21:00', 4 union all
select 30, 'BB H', '16:00', '21:00', 5 union all
select 30, 'CD', '16:00', '21:00', 5 union all
select 30, 'ALC', '16:00', '21:00', 5 union all
select 30, 'IMPORT', '16:00', '21:00', 5 union all
select 30, 'BB A', '16:00', '21:00', 5 union all
select 31, 'FISH', '13:00', '18:00', 1 union all
select 31, 'ИКРА', '16:00', '22:00', 1 union all
select 31, 'MEAT', '12:00', '18:00', 2 union all
select 31, 'FV', '12:00', '18:00', 3 union all
select 31, 'FRESH', '12:00', '18:00', 3 union all
select 31, 'CHILLED', '12:00', '18:00', 3 union all
select 31, 'MILK', '12:00', '18:00', 3 union all
select 31, 'FROZEN', '16:00', '22:00', 4 union all
select 31, 'BB H', '16:00', '22:00', 5 union all
select 31, 'CD', '16:00', '22:00', 5 union all
select 31, 'ALC', '16:00', '22:00', 5 union all
select 31, 'IMPORT', '16:00', '22:00', 5 union all
select 31, 'BB A', '16:00', '22:00', 5 union all
select 32, 'FISH', '05:00', '06:00', 1 union all
select 32, 'ИКРА', '08:00', '14:00', 1 union all
select 32, 'MEAT', '12:00', '16:00', 2 union all
select 32, 'FV', '12:00', '16:00', 3 union all
select 32, 'FRESH', '12:00', '16:00', 3 union all
select 32, 'CHILLED', '12:00', '16:00', 3 union all
select 32, 'MILK', '12:00', '16:00', 3 union all
select 32, 'FROZEN', '08:00', '14:00', 4 union all
select 32, 'BB H', '08:00', '14:00', 5 union all
select 32, 'CD', '08:00', '14:00', 5 union all
select 32, 'ALC', '08:00', '14:00', 5 union all
select 32, 'IMPORT', '08:00', '14:00', 5 union all
select 32, 'BB A', '08:00', '14:00', 5 union all
select 33, 'FISH', '06:30', '12:00', 1 union all
select 33, 'ИКРА', '15:00', '21:00', 1 union all
select 33, 'MEAT', '08:00', '14:00', 2 union all
select 33, 'FV', '08:00', '14:00', 3 union all
select 33, 'FRESH', '08:00', '14:00', 3 union all
select 33, 'CHILLED', '08:00', '14:00', 3 union all
select 33, 'MILK', '08:00', '14:00', 3 union all
select 33, 'FROZEN', '15:00', '21:00', 4 union all
select 33, 'BB H', '15:00', '21:00', 5 union all
select 33, 'CD', '15:00', '21:00', 5 union all
select 33, 'ALC', '15:00', '21:00', 5 union all
select 33, 'IMPORT', '15:00', '21:00', 5 union all
select 33, 'BB A', '15:00', '21:00', 5 union all
select 34, 'FISH', '20:00', '22:00', 1 union all
select 34, 'ИКРА', '08:00', '14:00', 1 union all
select 34, 'MEAT', '08:00', '14:00', 2 union all
select 34, 'FV', '08:00', '14:00', 3 union all
select 34, 'FRESH', '08:00', '14:00', 3 union all
select 34, 'CHILLED', '08:00', '14:00', 3 union all
select 34, 'MILK', '08:00', '14:00', 3 union all
select 34, 'FROZEN', '08:00', '14:00', 4 union all
select 34, 'BB H', '08:00', '14:00', 5 union all
select 34, 'CD', '08:00', '14:00', 5 union all
select 34, 'ALC', '08:00', '14:00', 5 union all
select 34, 'IMPORT', '08:00', '14:00', 5 union all
select 34, 'BB A', '08:00', '14:00', 5 union all
select 35, 'FISH', '09:00', '12:00', 1 union all
select 35, 'ИКРА', '11:00', '16:00', 1 union all
select 35, 'MEAT', '06:30', '12:00', 2 union all
select 35, 'FV', '06:30', '12:00', 3 union all
select 35, 'FRESH', '06:30', '12:00', 3 union all
select 35, 'CHILLED', '06:30', '12:00', 3 union all
select 35, 'MILK', '06:30', '12:00', 3 union all
select 35, 'FROZEN', '11:00', '16:00', 4 union all
select 35, 'BB H', '11:00', '16:00', 5 union all
select 35, 'CD', '11:00', '16:00', 5 union all
select 35, 'ALC', '11:00', '16:00', 5 union all
select 35, 'IMPORT', '11:00', '16:00', 5 union all
select 35, 'BB A', '11:00', '16:00', 5 union all
select 36, 'FISH', '07:00', '11:00', 1 union all
select 36, 'ИКРА', '09:00', '13:00', 1 union all
select 36, 'MEAT', '07:00', '11:00', 2 union all
select 36, 'FV', '06:30', '12:00', 3 union all
select 36, 'FRESH', '06:30', '12:00', 3 union all
select 36, 'CHILLED', '06:30', '12:00', 3 union all
select 36, 'MILK', '06:30', '12:00', 3 union all
select 36, 'FROZEN', '09:00', '13:00', 4 union all
select 36, 'BB H', '09:00', '13:00', 5 union all
select 36, 'CD', '09:00', '13:00', 5 union all
select 36, 'ALC', '09:00', '13:00', 5 union all
select 36, 'IMPORT', '09:00', '13:00', 5 union all
select 36, 'BB A', '09:00', '13:00', 5 union all
select 37, 'FISH', '07:00', '10:00', 1 union all
select 37, 'ИКРА', '09:00', '13:00', 1 union all
select 37, 'MEAT', '09:00', '12:00', 2 union all
select 37, 'FV', '06:30', '12:00', 3 union all
select 37, 'FRESH', '06:30', '12:00', 3 union all
select 37, 'CHILLED', '06:30', '12:00', 3 union all
select 37, 'MILK', '06:30', '12:00', 3 union all
select 37, 'FROZEN', '09:00', '13:00', 4 union all
select 37, 'BB H', '09:00', '13:00', 5 union all
select 37, 'CD', '09:00', '13:00', 5 union all
select 37, 'ALC', '09:00', '13:00', 5 union all
select 37, 'IMPORT', '09:00', '13:00', 5 union all
select 37, 'BB A', '09:00', '13:00', 5 union all
select 38, 'FISH', '06:00', '08:00', 1 union all
select 38, 'ИКРА', '06:30', '12:00', 1 union all
select 38, 'MEAT', '08:00', '15:00', 2 union all
select 38, 'FV', '06:30', '12:00', 3 union all
select 38, 'FRESH', '06:30', '12:00', 3 union all
select 38, 'CHILLED', '06:30', '12:00', 3 union all
select 38, 'MILK', '06:30', '12:00', 3 union all
select 38, 'FROZEN', '06:30', '12:00', 4 union all
select 38, 'BB H', '06:30', '12:00', 5 union all
select 38, 'CD', '06:30', '12:00', 5 union all
select 38, 'ALC', '06:30', '12:00', 5 union all
select 38, 'IMPORT', '06:30', '12:00', 5 union all
select 38, 'BB A', '06:30', '12:00', 5 union all
select 39, 'FISH', '12:00', '17:00', 1 union all
select 39, 'ИКРА', '09:00', '13:00', 1 union all
select 39, 'MEAT', '12:00', '17:00', 2 union all
select 39, 'FV', '06:30', '12:00', 3 union all
select 39, 'FRESH', '06:30', '12:00', 3 union all
select 39, 'CHILLED', '06:30', '12:00', 3 union all
select 39, 'MILK', '06:30', '12:00', 3 union all
select 39, 'FROZEN', '09:00', '13:00', 4 union all
select 39, 'BB H', '09:00', '13:00', 5 union all
select 39, 'CD', '09:00', '13:00', 5 union all
select 39, 'ALC', '09:00', '13:00', 5 union all
select 39, 'IMPORT', '09:00', '13:00', 5 union all
select 39, 'BB A', '09:00', '13:00', 5 union all
select 40, 'FISH', '06:00', '10:00', 1 union all
select 40, 'ИКРА', '08:00', '14:00', 1 union all
select 40, 'MEAT', '06:00', '10:00', 2 union all
select 40, 'FV', '06:30', '10:00', 3 union all
select 40, 'FRESH', '06:30', '10:00', 3 union all
select 40, 'CHILLED', '06:30', '10:00', 3 union all
select 40, 'MILK', '06:30', '10:00', 3 union all
select 40, 'FROZEN', '08:00', '14:00', 4 union all
select 40, 'BB H', '08:00', '14:00', 5 union all
select 40, 'CD', '08:00', '14:00', 5 union all
select 40, 'ALC', '08:00', '14:00', 5 union all
select 40, 'IMPORT', '08:00', '14:00', 5 union all
select 40, 'BB A', '08:00', '14:00', 5 union all
select 41, 'FISH', '06:00', '08:30', 1 union all
select 41, 'ИКРА', '08:00', '14:00', 1 union all
select 41, 'MEAT', '08:00', '16:00', 2 union all
select 41, 'FV', '10:00', '16:00', 3 union all
select 41, 'FRESH', '10:00', '16:00', 3 union all
select 41, 'CHILLED', '10:00', '16:00', 3 union all
select 41, 'MILK', '10:00', '16:00', 3 union all
select 41, 'FROZEN', '08:00', '14:00', 4 union all
select 41, 'BB H', '08:00', '14:00', 5 union all
select 41, 'CD', '08:00', '14:00', 5 union all
select 41, 'ALC', '08:00', '14:00', 5 union all
select 41, 'IMPORT', '08:00', '14:00', 5 union all
select 41, 'BB A', '08:00', '14:00', 5 union all
select 42, 'FISH', '06:00', '08:00', 1 union all
select 42, 'ИКРА', '06:30', '10:30', 1 union all
select 42, 'MEAT', '12:00', '15:00', 2 union all
select 42, 'FV', '06:30', '12:00', 3 union all
select 42, 'FRESH', '06:30', '12:00', 3 union all
select 42, 'CHILLED', '06:30', '12:00', 3 union all
select 42, 'MILK', '06:30', '12:00', 3 union all
select 42, 'FROZEN', '06:30', '12:00', 4 union all
select 42, 'BB H', '06:30', '10:30', 5 union all
select 42, 'CD', '06:30', '10:30', 5 union all
select 42, 'ALC', '06:30', '10:30', 5 union all
select 42, 'IMPORT', '06:30', '10:30', 5 union all
select 42, 'BB A', '06:30', '10:30', 5 union all
select 43, 'FISH', '22:00', '23:00', 1 union all
select 43, 'ИКРА', '14:00', '20:00', 1 union all
select 43, 'MEAT', '12:00', '18:00', 2 union all
select 43, 'FV', '12:00', '18:00', 3 union all
select 43, 'FRESH', '12:00', '18:00', 3 union all
select 43, 'CHILLED', '12:00', '18:00', 3 union all
select 43, 'MILK', '12:00', '18:00', 3 union all
select 43, 'FROZEN', '14:00', '20:00', 4 union all
select 43, 'BB H', '14:00', '20:00', 5 union all
select 43, 'CD', '14:00', '20:00', 5 union all
select 43, 'ALC', '14:00', '20:00', 5 union all
select 43, 'IMPORT', '14:00', '20:00', 5 union all
select 43, 'BB A', '14:00', '20:00', 5 union all
select 44, 'FISH', '08:00', '12:00', 1 union all
select 44, 'ИКРА', '07:00', '20:00', 1 union all
select 44, 'MEAT', '07:00', '20:00', 2 union all
select 44, 'FV', '07:00', '20:00', 3 union all
select 44, 'FRESH', '07:00', '20:00', 3 union all
select 44, 'CHILLED', '07:00', '20:00', 3 union all
select 44, 'MILK', '07:00', '20:00', 3 union all
select 44, 'FROZEN', '07:00', '20:00', 4 union all
select 44, 'BB H', '07:00', '20:00', 5 union all
select 44, 'CD', '07:00', '20:00', 5 union all
select 44, 'ALC', '07:00', '20:00', 5 union all
select 44, 'IMPORT', '07:00', '20:00', 5 union all
select 44, 'BB A', '07:00', '20:00', 5 union all
select 45, 'FISH', '08:00', '12:00', 1 union all
select 45, 'ИКРА', '07:00', '18:00', 1 union all
select 45, 'MEAT', '07:00', '18:00', 2 union all
select 45, 'FV', '07:00', '18:00', 3 union all
select 45, 'FRESH', '07:00', '18:00', 3 union all
select 45, 'CHILLED', '07:00', '18:00', 3 union all
select 45, 'MILK', '07:00', '18:00', 3 union all
select 45, 'FROZEN', '07:00', '18:00', 4 union all
select 45, 'BB H', '07:00', '18:00', 5 union all
select 45, 'CD', '07:00', '18:00', 5 union all
select 45, 'ALC', '07:00', '18:00', 5 union all
select 45, 'IMPORT', '07:00', '18:00', 5 union all
select 45, 'BB A', '07:00', '18:00', 5 union all
select 46, 'ИКРА', '07:00', '20:00', 1 union all
select 46, 'MEAT', '07:00', '20:00', 2 union all
select 46, 'FV', '07:00', '20:00', 3 union all
select 46, 'FRESH', '07:00', '20:00', 3 union all
select 46, 'CHILLED', '07:00', '20:00', 3 union all
select 46, 'MILK', '07:00', '20:00', 3 union all
select 46, 'FROZEN', '07:00', '20:00', 4 union all
select 46, 'BB H', '07:00', '20:00', 5 union all
select 46, 'CD', '07:00', '20:00', 5 union all
select 46, 'ALC', '07:00', '20:00', 5 union all
select 46, 'IMPORT', '07:00', '20:00', 5 union all
select 46, 'BB A', '07:00', '20:00', 5 union all
select 47, 'FISH', '14:00', '16:00', 1 union all
select 47, 'ИКРА', '10:00', '16:00', 1 union all
select 47, 'MEAT', '08:00', '14:00', 2 union all
select 47, 'FV', '08:00', '14:00', 3 union all
select 47, 'FRESH', '08:00', '14:00', 3 union all
select 47, 'CHILLED', '08:00', '14:00', 3 union all
select 47, 'MILK', '08:00', '14:00', 3 union all
select 47, 'FROZEN', '10:00', '16:00', 4 union all
select 47, 'BB H', '10:00', '16:00', 5 union all
select 47, 'CD', '10:00', '16:00', 5 union all
select 47, 'ALC', '10:00', '16:00', 5 union all
select 47, 'IMPORT', '10:00', '16:00', 5 union all
select 47, 'BB A', '10:00', '16:00', 5 union all
select 48, 'FISH', '01:00', '03:00', 1 union all
select 48, 'ИКРА', '07:00', '15:00', 1 union all
select 48, 'MEAT', '01:00', '03:00', 2 union all
select 48, 'FV', '01:00', '03:00', 3 union all
select 48, 'FRESH', '01:00', '03:00', 3 union all
select 48, 'CHILLED', '01:00', '03:00', 3 union all
select 48, 'MILK', '01:00', '03:00', 3 union all
select 48, 'FROZEN', '01:00', '03:00', 4 union all
select 48, 'BB H', '01:00', '03:00', 5 union all
select 48, 'CD', '01:00', '03:00', 5 union all
select 48, 'ALC', '01:00', '03:00', 5 union all
select 48, 'IMPORT', '01:00', '03:00', 5 union all
select 48, 'BB A', '01:00', '03:00', 5 union all
select 49, 'FISH', '00:00', '06:00', 1 union all
select 49, 'ИКРА', '06:00', '18:00', 1 union all
select 49, 'MEAT', '00:00', '06:00', 2 union all
select 49, 'FV', '05:00', '12:00', 3 union all
select 49, 'FRESH', '05:00', '12:00', 3 union all
select 49, 'CHILLED', '05:00', '12:00', 3 union all
select 49, 'MILK', '05:00', '12:00', 3 union all
select 49, 'FROZEN', '03:00', '12:00', 4 union all
select 49, 'BB H', '06:00', '18:00', 5 union all
select 49, 'CD', '06:00', '18:00', 5 union all
select 49, 'ALC', '06:00', '18:00', 5 union all
select 49, 'IMPORT', '06:00', '18:00', 5 union all
select 49, 'BB A', '06:00', '18:00', 5 union all
select 50, 'FISH', '12:00', '15:00', 1 union all
select 50, 'ИКРА', '10:00', '15:00', 1 union all
select 50, 'MEAT', '10:00', '14:00', 2 union all
select 50, 'FV', '08:00', '14:00', 3 union all
select 50, 'FRESH', '08:00', '14:00', 3 union all
select 50, 'CHILLED', '08:00', '14:00', 3 union all
select 50, 'MILK', '08:00', '14:00', 3 union all
select 50, 'FROZEN', '10:00', '15:00', 4 union all
select 50, 'BB H', '10:00', '15:00', 5 union all
select 50, 'CD', '10:00', '15:00', 5 union all
select 50, 'ALC', '10:00', '15:00', 5 union all
select 50, 'IMPORT', '10:00', '15:00', 5 union all
select 50, 'BB A', '10:00', '15:00', 5 union all
select 51, 'FISH', '09:00', '16:00', 1 union all
select 51, 'ИКРА', '12:00', '18:00', 1 union all
select 51, 'MEAT', '09:00', '16:00', 2 union all
select 51, 'FV', '13:00', '18:00', 3 union all
select 51, 'FRESH', '13:00', '18:00', 3 union all
select 51, 'CHILLED', '13:00', '18:00', 3 union all
select 51, 'MILK', '13:00', '18:00', 3 union all
select 51, 'FROZEN', '12:00', '18:00', 4 union all
select 51, 'BB H', '12:00', '18:00', 5 union all
select 51, 'CD', '12:00', '18:00', 5 union all
select 51, 'ALC', '12:00', '18:00', 5 union all
select 51, 'IMPORT', '12:00', '18:00', 5 union all
select 51, 'BB A', '12:00', '18:00', 5 union all
select 52, 'FISH', '18:00', '21:00', 1 union all
select 52, 'ИКРА', '10:00', '16:00', 1 union all
select 52, 'MEAT', '18:00', '21:00', 2 union all
select 52, 'FV', '06:30', '12:00', 3 union all
select 52, 'FRESH', '06:30', '12:00', 3 union all
select 52, 'CHILLED', '06:30', '12:00', 3 union all
select 52, 'MILK', '06:30', '12:00', 3 union all
select 52, 'FROZEN', '10:00', '16:00', 4 union all
select 52, 'BB H', '10:00', '16:00', 5 union all
select 52, 'CD', '10:00', '16:00', 5 union all
select 52, 'ALC', '10:00', '16:00', 5 union all
select 52, 'IMPORT', '10:00', '16:00', 5 union all
select 52, 'BB A', '10:00', '16:00', 5 union all
select 53, 'FISH', '17:00', '19:00', 1 union all
select 53, 'ИКРА', '08:00', '14:00', 1 union all
select 53, 'MEAT', '06:00', '10:00', 2 union all
select 53, 'FV', '06:00', '10:00', 3 union all
select 53, 'FRESH', '17:00', '19:00', 3 union all
select 53, 'CHILLED', '06:00', '10:00', 3 union all
select 53, 'MILK', '06:00', '10:00', 3 union all
select 53, 'FROZEN', '08:00', '14:00', 4 union all
select 53, 'BB H', '08:00', '14:00', 5 union all
select 53, 'CD', '08:00', '14:00', 5 union all
select 53, 'ALC', '08:00', '14:00', 5 union all
select 53, 'IMPORT', '08:00', '14:00', 5 union all
select 53, 'BB A', '08:00', '14:00', 5 union all
select 54, 'ИКРА', '07:00', '18:00', 1 union all
select 54, 'MEAT', '09:00', '17:00', 2 union all
select 54, 'FV', '07:00', '18:00', 3 union all
select 54, 'FRESH', '07:00', '18:00', 3 union all
select 54, 'CHILLED', '07:00', '18:00', 3 union all
select 54, 'MILK', '07:00', '18:00', 3 union all
select 54, 'FROZEN', '07:00', '18:00', 4 union all
select 54, 'BB H', '07:00', '18:00', 5 union all
select 54, 'CD', '07:00', '18:00', 5 union all
select 54, 'ALC', '07:00', '18:00', 5 union all
select 54, 'IMPORT', '07:00', '18:00', 5 union all
select 54, 'BB A', '07:00', '18:00', 5 union all
select 55, 'FISH', '07:00', '10:00', 1 union all
select 55, 'ИКРА', '10:00', '16:00', 1 union all
select 55, 'MEAT', '08:00', '12:00', 2 union all
select 55, 'FV', '08:00', '14:00', 3 union all
select 55, 'FRESH', '08:00', '14:00', 3 union all
select 55, 'CHILLED', '08:00', '14:00', 3 union all
select 55, 'MILK', '08:00', '14:00', 3 union all
select 55, 'FROZEN', '10:00', '16:00', 4 union all
select 55, 'BB H', '10:00', '16:00', 5 union all
select 55, 'CD', '10:00', '16:00', 5 union all
select 55, 'ALC', '10:00', '16:00', 5 union all
select 55, 'IMPORT', '10:00', '16:00', 5 union all
select 55, 'BB A', '10:00', '16:00', 5 union all
select 56, 'FISH', '14:00', '18:00', 1 union all
select 56, 'ИКРА', '08:00', '18:00', 1 union all
select 56, 'MEAT', '14:00', '18:00', 2 union all
select 56, 'FV', '12:00', '16:00', 3 union all
select 56, 'FRESH', '12:00', '16:00', 3 union all
select 56, 'CHILLED', '12:00', '16:00', 3 union all
select 56, 'MILK', '12:00', '16:00', 3 union all
select 56, 'FROZEN', '08:00', '18:00', 4 union all
select 56, 'BB H', '08:00', '18:00', 5 union all
select 56, 'CD', '08:00', '18:00', 5 union all
select 56, 'ALC', '08:00', '18:00', 5 union all
select 56, 'IMPORT', '08:00', '18:00', 5 union all
select 56, 'BB A', '08:00', '18:00', 5 union all
select 57, 'ИКРА', '07:00', '20:00', 1 union all
select 57, 'MEAT', '07:00', '20:00', 2 union all
select 57, 'FV', '07:00', '20:00', 3 union all
select 57, 'FRESH', '07:00', '20:00', 3 union all
select 57, 'CHILLED', '07:00', '20:00', 3 union all
select 57, 'MILK', '07:00', '20:00', 3 union all
select 57, 'FROZEN', '02:00', '12:00', 4 union all
select 57, 'BB H', '07:00', '20:00', 5 union all
select 57, 'CD', '07:00', '20:00', 5 union all
select 57, 'ALC', '07:00', '20:00', 5 union all
select 57, 'IMPORT', '07:00', '20:00', 5 union all
select 57, 'BB A', '07:00', '20:00', 5 union all
select 58, 'FISH', '16:00', '17:00', 1 union all
select 58, 'ИКРА', '10:00', '16:00', 1 union all
select 58, 'MEAT', '07:00', '10:00', 2 union all
select 58, 'FV', '10:00', '16:00', 3 union all
select 58, 'FRESH', '10:00', '16:00', 3 union all
select 58, 'CHILLED', '10:00', '16:00', 3 union all
select 58, 'MILK', '10:00', '16:00', 3 union all
select 58, 'FROZEN', '10:00', '16:00', 4 union all
select 58, 'BB H', '10:00', '16:00', 5 union all
select 58, 'CD', '10:00', '16:00', 5 union all
select 58, 'ALC', '10:00', '16:00', 5 union all
select 58, 'IMPORT', '10:00', '16:00', 5 union all
select 58, 'BB A', '10:00', '16:00', 5 union all
select 59, 'FISH', '08:00', '10:00', 1 union all
select 59, 'ИКРА', '11:00', '16:00', 1 union all
select 59, 'MEAT', '09:00', '12:00', 2 union all
select 59, 'FV', '06:30', '12:00', 3 union all
select 59, 'FRESH', '06:30', '12:00', 3 union all
select 59, 'CHILLED', '06:30', '12:00', 3 union all
select 59, 'MILK', '06:30', '12:00', 3 union all
select 59, 'FROZEN', '11:00', '16:00', 4 union all
select 59, 'BB H', '11:00', '16:00', 5 union all
select 59, 'CD', '11:00', '16:00', 5 union all
select 59, 'ALC', '11:00', '16:00', 5 union all
select 59, 'IMPORT', '11:00', '16:00', 5 union all
select 59, 'BB A', '11:00', '16:00', 5 union all
select 60, 'FISH', '08:00', '12:00', 1 union all
select 60, 'ИКРА', '07:00', '20:00', 1 union all
select 60, 'MEAT', '07:00', '20:00', 2 union all
select 60, 'FV', '07:00', '20:00', 3 union all
select 60, 'FRESH', '07:00', '20:00', 3 union all
select 60, 'CHILLED', '07:00', '20:00', 3 union all
select 60, 'MILK', '07:00', '20:00', 3 union all
select 60, 'FROZEN', '05:00', '13:00', 4 union all
select 60, 'BB H', '07:00', '20:00', 5 union all
select 60, 'CD', '07:00', '20:00', 5 union all
select 60, 'ALC', '07:00', '20:00', 5 union all
select 60, 'IMPORT', '07:00', '20:00', 5 union all
select 60, 'BB A', '07:00', '20:00', 5 union all
select 61, 'FISH', '06:00', '12:00', 1 union all
select 61, 'ИКРА', '07:00', '14:00', 1 union all
select 61, 'MEAT', '06:00', '12:00', 2 union all
select 61, 'FV', '06:00', '15:00', 3 union all
select 61, 'FRESH', '06:00', '15:00', 3 union all
select 61, 'CHILLED', '06:00', '15:00', 3 union all
select 61, 'MILK', '06:00', '15:00', 3 union all
select 61, 'FROZEN', '06:00', '15:00', 4 union all
select 61, 'BB H', '07:00', '14:00', 5 union all
select 61, 'CD', '07:00', '14:00', 5 union all
select 61, 'ALC', '07:00', '14:00', 5 union all
select 61, 'IMPORT', '07:00', '14:00', 5 union all
select 61, 'BB A', '07:00', '14:00', 5 union all
select 63, 'FISH', '06:00', '08:00', 1 union all
select 63, 'ИКРА', '08:00', '14:00', 1 union all
select 63, 'MEAT', '08:00', '11:00', 2 union all
select 63, 'FV', '05:00', '08:00', 3 union all
select 63, 'FRESH', '05:00', '08:00', 3 union all
select 63, 'CHILLED', '05:00', '08:00', 3 union all
select 63, 'MILK', '05:00', '08:00', 3 union all
select 63, 'FROZEN', '08:00', '14:00', 4 union all
select 63, 'BB H', '08:00', '14:00', 5 union all
select 63, 'CD', '08:00', '14:00', 5 union all
select 63, 'ALC', '08:00', '14:00', 5 union all
select 63, 'IMPORT', '08:00', '14:00', 5 union all
select 63, 'BB A', '08:00', '14:00', 5 union all
select 64, 'FISH', '06:00', '07:00', 1 union all
select 64, 'ИКРА', '10:00', '16:00', 1 union all
select 64, 'MEAT', '10:00', '16:00', 2 union all
select 64, 'FV', '10:00', '16:00', 3 union all
select 64, 'FRESH', '10:00', '16:00', 3 union all
select 64, 'CHILLED', '10:00', '16:00', 3 union all
select 64, 'MILK', '10:00', '16:00', 3 union all
select 64, 'FROZEN', '10:00', '16:00', 4 union all
select 64, 'BB H', '10:00', '16:00', 5 union all
select 64, 'CD', '10:00', '16:00', 5 union all
select 64, 'ALC', '10:00', '16:00', 5 union all
select 64, 'IMPORT', '10:00', '16:00', 5 union all
select 64, 'BB A', '10:00', '16:00', 5 union all
select 65, 'FISH', '16:00', '20:00', 1 union all
select 65, 'ИКРА', '10:00', '18:00', 1 union all
select 65, 'MEAT', '16:00', '20:00', 2 union all
select 65, 'FV', '09:00', '18:00', 3 union all
select 65, 'FRESH', '09:00', '18:00', 3 union all
select 65, 'CHILLED', '09:00', '18:00', 3 union all
select 65, 'MILK', '09:00', '18:00', 3 union all
select 65, 'FROZEN', '16:00', '21:00', 4 union all
select 65, 'BB H', '10:00', '18:00', 5 union all
select 65, 'CD', '10:00', '18:00', 5 union all
select 65, 'ALC', '10:00', '18:00', 5 union all
select 65, 'IMPORT', '10:00', '18:00', 5 union all
select 65, 'BB A', '10:00', '18:00', 5 union all
select 66, 'ИКРА', '07:00', '20:00', 1 union all
select 66, 'MEAT', '08:00', '16:00', 2 union all
select 66, 'FV', '07:00', '20:00', 3 union all
select 66, 'FRESH', '07:00', '20:00', 3 union all
select 66, 'CHILLED', '07:00', '20:00', 3 union all
select 66, 'MILK', '07:00', '20:00', 3 union all
select 66, 'FROZEN', '07:00', '20:00', 4 union all
select 66, 'BB H', '07:00', '20:00', 5 union all
select 66, 'CD', '07:00', '20:00', 5 union all
select 66, 'ALC', '07:00', '20:00', 5 union all
select 66, 'IMPORT', '07:00', '20:00', 5 union all
select 66, 'BB A', '07:00', '20:00', 5 union all
select 67, 'FISH', '23:00', '01:00', 1 union all
select 67, 'ИКРА', '06:00', '16:00', 1 union all
select 67, 'MEAT', '23:00', '01:00', 2 union all
select 67, 'FV', '23:00', '01:00', 3 union all
select 67, 'FRESH', '23:00', '01:00', 3 union all
select 67, 'CHILLED', '23:00', '01:00', 3 union all
select 67, 'MILK', '23:00', '01:00', 3 union all
select 67, 'FROZEN', '06:00', '13:00', 4 union all
select 67, 'BB H', '06:00', '16:00', 5 union all
select 67, 'CD', '06:00', '16:00', 5 union all
select 67, 'ALC', '06:00', '16:00', 5 union all
select 67, 'IMPORT', '06:00', '16:00', 5 union all
select 67, 'BB A', '06:00', '16:00', 5 union all
select 68, 'ИКРА', '07:00', '16:00', 1 union all
select 68, 'MEAT', '06:30', '14:00', 2 union all
select 68, 'FV', '06:30', '12:00', 3 union all
select 68, 'FRESH', '06:30', '12:00', 3 union all
select 68, 'CHILLED', '06:30', '12:00', 3 union all
select 68, 'MILK', '06:30', '12:00', 3 union all
select 68, 'FROZEN', '16:00', '23:00', 4 union all
select 68, 'BB H', '07:00', '16:00', 5 union all
select 68, 'CD', '07:00', '16:00', 5 union all
select 68, 'ALC', '07:00', '16:00', 5 union all
select 68, 'IMPORT', '07:00', '16:00', 5 union all
select 68, 'BB A', '07:00', '16:00', 5 union all
select 69, 'FISH', '12:00', '18:00', 1 union all
select 69, 'ИКРА', '09:00', '18:00', 1 union all
select 69, 'MEAT', '12:00', '18:00', 2 union all
select 69, 'FV', '10:00', '16:00', 3 union all
select 69, 'FRESH', '10:00', '16:00', 3 union all
select 69, 'CHILLED', '10:00', '16:00', 3 union all
select 69, 'MILK', '10:00', '16:00', 3 union all
select 69, 'FROZEN', '09:00', '15:00', 4 union all
select 69, 'BB H', '09:00', '18:00', 5 union all
select 69, 'CD', '09:00', '18:00', 5 union all
select 69, 'ALC', '09:00', '18:00', 5 union all
select 69, 'IMPORT', '09:00', '18:00', 5 union all
select 69, 'BB A', '09:00', '18:00', 5 union all
select 70, 'FISH', '07:00', '08:00', 1 union all
select 70, 'ИКРА', '09:00', '18:00', 1 union all
select 70, 'MEAT', '16:00', '19:00', 2 union all
select 70, 'FV', '08:00', '14:00', 3 union all
select 70, 'FRESH', '08:00', '14:00', 3 union all
select 70, 'CHILLED', '08:00', '14:00', 3 union all
select 70, 'MILK', '08:00', '14:00', 3 union all
select 70, 'FROZEN', '09:00', '15:00', 4 union all
select 70, 'BB H', '09:00', '18:00', 5 union all
select 70, 'CD', '09:00', '18:00', 5 union all
select 70, 'ALC', '09:00', '18:00', 5 union all
select 70, 'IMPORT', '09:00', '18:00', 5 union all
select 70, 'BB A', '09:00', '18:00', 5 union all
select 71, 'FISH', '05:00', '07:00', 1 union all
select 71, 'ИКРА', '09:00', '18:00', 1 union all
select 71, 'MEAT', '16:00', '22:00', 2 union all
select 71, 'FV', '16:00', '22:00', 3 union all
select 71, 'FRESH', '16:00', '22:00', 3 union all
select 71, 'CHILLED', '16:00', '22:00', 3 union all
select 71, 'MILK', '16:00', '22:00', 3 union all
select 71, 'FROZEN', '10:00', '16:00', 4 union all
select 71, 'BB H', '09:00', '18:00', 5 union all
select 71, 'CD', '09:00', '18:00', 5 union all
select 71, 'ALC', '09:00', '18:00', 5 union all
select 71, 'IMPORT', '09:00', '18:00', 5 union all
select 71, 'BB A', '09:00', '18:00', 5 union all
select 72, 'FISH', '08:00', '10:00', 1 union all
select 72, 'ИКРА', '09:00', '18:00', 1 union all
select 72, 'MEAT', '07:00', '10:00', 2 union all
select 72, 'FV', '10:00', '16:00', 3 union all
select 72, 'FRESH', '10:00', '16:00', 3 union all
select 72, 'CHILLED', '10:00', '16:00', 3 union all
select 72, 'MILK', '10:00', '16:00', 3 union all
select 72, 'FROZEN', '09:00', '15:00', 4 union all
select 72, 'BB H', '09:00', '18:00', 5 union all
select 72, 'CD', '09:00', '18:00', 5 union all
select 72, 'ALC', '09:00', '18:00', 5 union all
select 72, 'IMPORT', '09:00', '18:00', 5 union all
select 72, 'BB A', '09:00', '18:00', 5 union all
select 73, 'FISH', '03:00', '05:00', 1 union all
select 73, 'ИКРА', '06:30', '17:00', 1 union all
select 73, 'MEAT', '07:00', '11:00', 2 union all
select 73, 'FV', '06:00', '12:00', 3 union all
select 73, 'FRESH', '06:00', '12:00', 3 union all
select 73, 'CHILLED', '06:00', '12:00', 3 union all
select 73, 'MILK', '06:00', '12:00', 3 union all
select 73, 'FROZEN', '07:00', '16:00', 4 union all
select 73, 'BB H', '06:30', '17:00', 5 union all
select 73, 'CD', '06:30', '17:00', 5 union all
select 73, 'ALC', '06:30', '17:00', 5 union all
select 73, 'IMPORT', '06:30', '17:00', 5 union all
select 73, 'BB A', '06:30', '17:00', 5 union all
select 74, 'FISH', '16:00', '18:00', 1 union all
select 74, 'ИКРА', '09:00', '18:00', 1 union all
select 74, 'MEAT', '08:00', '14:00', 2 union all
select 74, 'FV', '08:00', '14:00', 3 union all
select 74, 'FRESH', '08:00', '14:00', 3 union all
select 74, 'CHILLED', '08:00', '14:00', 3 union all
select 74, 'MILK', '08:00', '14:00', 3 union all
select 74, 'FROZEN', '06:00', '12:00', 4 union all
select 74, 'BB H', '09:00', '18:00', 5 union all
select 74, 'CD', '09:00', '18:00', 5 union all
select 74, 'ALC', '09:00', '18:00', 5 union all
select 74, 'IMPORT', '09:00', '18:00', 5 union all
select 74, 'BB A', '09:00', '18:00', 5 union all
select 76, 'FISH', '08:00', '12:00', 1 union all
select 76, 'ИКРА', '07:00', '20:00', 1 union all
select 76, 'MEAT', '08:00', '19:00', 2 union all
select 76, 'FV', '07:00', '20:00', 3 union all
select 76, 'FRESH', '07:00', '20:00', 3 union all
select 76, 'CHILLED', '07:00', '20:00', 3 union all
select 76, 'MILK', '07:00', '20:00', 3 union all
select 76, 'FROZEN', '07:00', '20:00', 4 union all
select 76, 'BB H', '07:00', '20:00', 5 union all
select 76, 'CD', '07:00', '20:00', 5 union all
select 76, 'ALC', '07:00', '20:00', 5 union all
select 76, 'IMPORT', '07:00', '20:00', 5 union all
select 76, 'BB A', '07:00', '20:00', 5 union all
select 77, 'FISH', '04:00', '08:00', 1 union all
select 77, 'ИКРА', '06:30', '14:00', 1 union all
select 77, 'MEAT', '04:00', '08:00', 2 union all
select 77, 'FV', '04:00', '08:00', 3 union all
select 77, 'FRESH', '04:00', '08:00', 3 union all
select 77, 'CHILLED', '04:00', '08:00', 3 union all
select 77, 'MILK', '04:00', '08:00', 3 union all
select 77, 'FROZEN', '04:00', '08:00', 4 union all
select 77, 'CD', '04:00', '08:00', 5 union all
select 77, 'BB H', '04:00', '08:00', 5 union all
select 77, 'ALC', '04:00', '08:00', 5 union all
select 77, 'IMPORT', '04:00', '08:00', 5 union all
select 77, 'BB A', '04:00', '08:00', 5 union all
select 78, 'FISH', '07:00', '11:00', 1 union all
select 78, 'ИКРА', '09:00', '18:00', 1 union all
select 78, 'MEAT', '12:00', '18:00', 2 union all
select 78, 'FV', '12:00', '18:00', 3 union all
select 78, 'FRESH', '12:00', '18:00', 3 union all
select 78, 'CHILLED', '12:00', '18:00', 3 union all
select 78, 'MILK', '12:00', '18:00', 3 union all
select 78, 'FROZEN', '07:00', '16:00', 4 union all
select 78, 'BB H', '09:00', '18:00', 5 union all
select 78, 'CD', '09:00', '18:00', 5 union all
select 78, 'ALC', '09:00', '18:00', 5 union all
select 78, 'IMPORT', '09:00', '18:00', 5 union all
select 78, 'BB A', '09:00', '18:00', 5 union all
select 82, 'FISH', '07:00', '10:00', 1 union all
select 82, 'ИКРА', '10:00', '18:00', 1 union all
select 82, 'MEAT', '07:00', '10:00', 2 union all
select 82, 'FV', '10:00', '18:00', 3 union all
select 82, 'FRESH', '10:00', '18:00', 3 union all
select 82, 'CHILLED', '10:00', '18:00', 3 union all
select 82, 'MILK', '10:00', '18:00', 3 union all
select 82, 'FROZEN', '10:00', '18:00', 4 union all
select 82, 'BB H', '10:00', '18:00', 5 union all
select 82, 'CD', '10:00', '18:00', 5 union all
select 82, 'ALC', '10:00', '18:00', 5 union all
select 82, 'IMPORT', '10:00', '18:00', 5 union all
select 82, 'BB A', '10:00', '18:00', 5 union all
select 83, 'ИКРА', '09:00', '18:00', 1 union all
select 83, 'MEAT', '09:00', '18:00', 2 union all
select 83, 'FV', '09:00', '18:00', 3 union all
select 83, 'FRESH', '09:00', '18:00', 3 union all
select 83, 'CHILLED', '09:00', '18:00', 3 union all
select 83, 'MILK', '09:00', '18:00', 3 union all
select 83, 'FROZEN', '09:00', '18:00', 4 union all
select 83, 'BB H', '09:00', '18:00', 5 union all
select 83, 'CD', '09:00', '18:00', 5 union all
select 83, 'ALC', '09:00', '18:00', 5 union all
select 83, 'IMPORT', '09:00', '18:00', 5 union all
select 83, 'BB A', '09:00', '18:00', 5 union all
select 86, 'FISH', '06:30', '13:00', 1 union all
select 86, 'ИКРА', '17:00', '22:00', 1 union all
select 86, 'MEAT', '06:30', '13:00', 2 union all
select 86, 'FV', '12:00', '18:00', 3 union all
select 86, 'FRESH', '12:00', '18:00', 3 union all
select 86, 'CHILLED', '12:00', '18:00', 3 union all
select 86, 'MILK', '12:00', '18:00', 3 union all
select 86, 'FROZEN', '14:00', '18:00', 4 union all
select 86, 'BB H', '17:00', '22:00', 5 union all
select 86, 'CD', '17:00', '22:00', 5 union all
select 86, 'ALC', '17:00', '22:00', 5 union all
select 86, 'IMPORT', '17:00', '22:00', 5 union all
select 86, 'BB A', '17:00', '22:00', 5 union all
select 88, 'FISH', '09:00', '12:00', 1 union all
select 88, 'ИКРА', '08:00', '15:00', 1 union all
select 88, 'MEAT', '08:00', '15:00', 2 union all
select 88, 'FV', '08:00', '15:00', 3 union all
select 88, 'FRESH', '08:00', '15:00', 3 union all
select 88, 'CHILLED', '08:00', '15:00', 3 union all
select 88, 'MILK', '08:00', '15:00', 3 union all
select 88, 'FROZEN', '08:00', '18:00', 4 union all
select 88, 'BB H', '08:00', '15:00', 5 union all
select 88, 'CD', '08:00', '15:00', 5 union all
select 88, 'ALC', '08:00', '15:00', 5 union all
select 88, 'IMPORT', '08:00', '15:00', 5 union all
select 88, 'BB A', '08:00', '15:00', 5 union all
select 94, 'FISH', '12:00', '13:00', 1 union all
select 94, 'ИКРА', '15:00', '22:00', 1 union all
select 94, 'MEAT', '19:00', '23:00', 2 union all
select 94, 'FV', '12:00', '18:00', 3 union all
select 94, 'FRESH', '12:00', '18:00', 3 union all
select 94, 'CHILLED', '12:00', '18:00', 3 union all
select 94, 'MILK', '12:00', '18:00', 3 union all
select 94, 'FROZEN', '14:00', '18:00', 4 union all
select 94, 'BB H', '15:00', '22:00', 5 union all
select 94, 'CD', '15:00', '22:00', 5 union all
select 94, 'ALC', '15:00', '22:00', 5 union all
select 94, 'IMPORT', '15:00', '22:00', 5 union all
select 94, 'BB A', '15:00', '22:00', 5 union all
select 95, 'ИКРА', '07:00', '20:00', 1 union all
select 95, 'MEAT', '07:00', '20:00', 2 union all
select 95, 'FV', '07:00', '20:00', 3 union all
select 95, 'FRESH', '07:00', '20:00', 3 union all
select 95, 'CHILLED', '07:00', '20:00', 3 union all
select 95, 'MILK', '07:00', '20:00', 3 union all
select 95, 'FROZEN', '07:00', '20:00', 4 union all
select 95, 'BB H', '07:00', '20:00', 5 union all
select 95, 'CD', '07:00', '20:00', 5 union all
select 95, 'ALC', '07:00', '20:00', 5 union all
select 95, 'IMPORT', '07:00', '20:00', 5 union all
select 95, 'BB A', '07:00', '20:00', 5 union all
select 97, 'FISH', '21:00', '06:00', 1 union all
select 97, 'ИКРА', '21:00', '06:00', 1 union all
select 97, 'MEAT', '21:00', '06:00', 2 union all
select 97, 'FV', '21:00', '06:00', 3 union all
select 97, 'FRESH', '21:00', '06:00', 3 union all
select 97, 'CHILLED', '21:00', '06:00', 3 union all
select 97, 'MILK', '21:00', '06:00', 3 union all
select 97, 'FROZEN', '21:00', '06:00', 4 union all
select 97, 'BB H', '21:00', '06:00', 5 union all
select 97, 'CD', '21:00', '06:00', 5 union all
select 97, 'ALC', '21:00', '06:00', 5 union all
select 97, 'IMPORT', '21:00', '06:00', 5 union all
select 97, 'BB A', '21:00', '06:00', 5
;
