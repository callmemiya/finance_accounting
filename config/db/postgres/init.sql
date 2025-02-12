-- Удаляем объекты в случае повторного использования скрипта
DROP SCHEMA IF EXISTS finance_acc CASCADE;
DROP USER IF EXISTS finance_acc;

-- Создаем владельца схемы
CREATE USER finance_acc WITH password 'finance_acc';

-- Переключаемся на созданную базу и создаем схему
CREATE SCHEMA finance_acc AUTHORIZATION finance_acc;
GRANT USAGE ON SCHEMA finance_acc TO finance_acc;

ALTER DEFAULT PRIVILEGES FOR USER finance_acc IN SCHEMA finance_acc GRANT USAGE ON SEQUENCES TO finance_acc;
ALTER DEFAULT PRIVILEGES FOR USER finance_acc IN SCHEMA finance_acc GRANT EXECUTE ON FUNCTIONS TO finance_acc;

ALTER USER finance_acc SET SEARCH_PATH = 'finance_acc';
