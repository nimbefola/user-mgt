
CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

DROP TABLE IF EXISTS  account;
create TABLE account (
    "id" VARCHAR(255) NOT NULL,
    "created" TIMESTAMP NOT NULL,
    "updated" TIMESTAMP NOT NULL,
    "version" INT8 DEFAULT 0,
    "name" VARCHAR(255) NOT NULL,
    "business_name" VARCHAR(255) NULL,
    "email" VARCHAR(255) NOT NULL,
    "username" VARCHAR(255) NOT NULL,
    "password" VARCHAR(255) NOT NULL,
    "pin" VARCHAR(255) NOT NULL,
    "profile_picture_url" VARCHAR(255) NULL,
    "msisdn" VARCHAR(255) NOT NULL,
    "activation_otp" VARCHAR(255) NULL,
    "status" VARCHAR(255) NOT NULL,
    "account_type" VARCHAR(255) NOT NULL,
    "service_description" VARCHAR(255) NULL,
    "address_id" VARCHAR(255) NOT NULL,
    "bank_detail_id" VARCHAR(255) NOT NULL,
    "balance" VARCHAR(255) NOT NULL,
    primary key (id)
);

DROP TABLE IF EXISTS  bank_detail;
create TABLE bank_detail (
    "id" VARCHAR(255) NOT NULL,
    "created" TIMESTAMP NOT NULL,
    "updated" TIMESTAMP NOT NULL,
    "version" INT8 DEFAULT 0,
    "account_number" VARCHAR(255) NULL,
    "account_name" VARCHAR(255) NULL,
    "cbn_bank_code" VARCHAR(255) NULL,
    "bank_code" VARCHAR(255) NULL,
    "bank_name" VARCHAR(255) NULL,
    primary key (id)
);

DROP TABLE IF EXISTS  address;
create TABLE address (
    "id" VARCHAR(255) NOT NULL,
    "created" TIMESTAMP NOT NULL,
    "updated" TIMESTAMP NOT NULL,
    "version" INT8 DEFAULT 0,
    "line1" VARCHAR(255) NOT NULL,
    "line2" VARCHAR(255) NULL,
    "state" VARCHAR(255) NOT NULL,
    "country" VARCHAR(255) NOT NULL,
    primary key (id)
);

DROP TABLE IF EXISTS  service;
create TABLE service (
    "id" VARCHAR(255) NOT NULL,
    "created" TIMESTAMP NOT NULL,
    "updated" TIMESTAMP NOT NULL,
    "version" INT8 DEFAULT 0,
    "title" VARCHAR(255) NOT NULL,
    "description" VARCHAR(255) NOT NULL,
    "service_image_url" VARCHAR(255) NULL,
    primary key (id)
);

DROP TABLE IF EXISTS  account_service;
create TABLE account_service (
    "account_id" VARCHAR(255) NOT NULL,
    "service_id" VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS bank;
CREATE TABLE bank (
    "id" VARCHAR(255) NOT NULL,
    "created" TIMESTAMP NOT NULL,
    "updated" TIMESTAMP NOT NULL,
    "version" INT8 DEFAULT 0,
    "cbn_bank_code" VARCHAR(20) NOT NULL,
    "bank_code" VARCHAR(50) NOT NULL,
    "bank_name" VARCHAR(255) NOT NULL,
    primary key (id)
);


alter table account add constraint uk_email unique (email);
alter table account add constraint uk_username unique (username);
alter table account add constraint uk_msisdn unique (msisdn);
alter table bank add constraint uk_bank_cbn_bank_code_bank_code unique (cbn_bank_code,bank_code);
alter table bank_detail add constraint uk_account_number unique (account_number);