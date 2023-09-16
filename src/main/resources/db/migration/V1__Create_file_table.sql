-- -------------------------------------------------- --
-- Схема базы данных на языке SQL диалекта PostgreSQL --
-- -------------------------------------------------- --

CREATE TABLE "categories" (
    "id"          SERIAL       PRIMARY KEY,
    "name"        VARCHAR(64)  NOT NULL,
    "description" VARCHAR(256) NULL,

    CONSTRAINT "unique_name" UNIQUE ("name")
);


CREATE TABLE "expenses" (
    "id"          SERIAL        PRIMARY KEY,
    "date"        DATE          NOT NULL,
    "category_id" INTEGER       NOT NULL,
    "cost"        NUMERIC(12,2) NOT NULL,
    "state"       VARCHAR(32)   NULL,

    CONSTRAINT "positive_cost" CHECK ("cost" >= 0),
    CONSTRAINT "fk_category"   FOREIGN KEY ("category_id") REFERENCES "categories" ("id")
);
