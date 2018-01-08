# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table coin_prices (
  coin_price_id                 bigint auto_increment not null,
  coin_id                       varchar(255),
  price                         bigint,
  timestamp                     bigint,
  constraint pk_coin_prices primary key (coin_price_id)
);

create table markets (
  market_name                   varchar(255) not null,
  market_currency               varchar(255),
  base_currency                 varchar(255),
  min_trade_size                double,
  is_active                     tinyint(1) default 0,
  constraint pk_markets primary key (market_name)
);

create table user_purchases (
  user_purchase_id              bigint auto_increment not null,
  user_id                       bigint,
  coin_id                       varchar(255),
  amount                        bigint,
  unitprice                     bigint,
  exchange_id                   varchar(255),
  timestamp                     bigint,
  status                        integer,
  constraint pk_user_purchases primary key (user_purchase_id)
);


# --- !Downs

drop table if exists coin_prices;

drop table if exists markets;

drop table if exists user_purchases;

