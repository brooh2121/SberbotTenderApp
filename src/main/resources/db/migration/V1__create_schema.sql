create table if not exists public.auctions (
auction_number varchar (100) not null,
organization_name varchar(1000) not null,
tender_name varchar (1000) not null,
publication_date varchar(100) not null,
auction_sum varchar(100) not null
);