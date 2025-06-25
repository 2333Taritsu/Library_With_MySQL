create database Book_manager;
use Book_manager;

create table books(
	id varchar(20) primary key,
    title varchar (100) Not NULL,
    author varchar(50) not NULL,
    price Decimal (10,2) Not NULL
);
create table users(
	id varchar(15) primary key,
    nick_name varchar(20) Not NULL,
	balance Decimal (20,2) Not NULL
);