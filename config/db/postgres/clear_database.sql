-- set schema
SET SCHEMA 'finance_acc';

-- alm_qort tables
DROP TABLE IF EXISTS qort_deal;
DROP TABLE IF EXISTS qort_deal_payment;

--alm_store_tables
DROP TABLE IF EXISTS dict_counterparty;
DROP TABLE IF EXISTS dict_currency;

-- alm_qort sequences
DROP SEQUENCE IF EXISTS qort_deal_seq;
DROP_SEQUENCE IF EXISTS qort_deal_payment_seq;

-- alm_store sequences
DROP SEQUENCE IF EXISTS dict_counterparty_seq;
DROP SEQUENCE IF EXISTS dict_currency_seq;

-- liquibase
DROP TABLE IF EXISTS databasechangelog;
DROP TABLE IF EXISTS databasechangeloglock;
