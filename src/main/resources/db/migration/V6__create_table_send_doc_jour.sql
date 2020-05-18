create table if not exists send_doc_jur (
send_doc_id int not null,
auction_number varchar (100) not null,
doc_step varchar (500) not null,
sucessful_step boolean,
doc_send_comment varchar (1000),
insert_doc_date timestamp default current_timestamp
);