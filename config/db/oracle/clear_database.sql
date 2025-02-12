-- alm_qort tables
DROP TABLE qort_deal;
DROP TABLE qort_deal_payment;

--alm_store_tables
DROP TABLE dict_counterparty;
DROP TABLE dict_currency;

-- alm_qort sequences
DROP SEQUENCE qort_deal_seq;
DROP_SEQUENCE qort_deal_payment_seq;

-- alm_store sequences
DROP SEQUENCE dict_counterparty_seq;
DROP SEQUENCE dict_currency_seq;

-- liquibase
DROP TABLE databasechangelog;
DROP TABLE databasechangeloglock;
