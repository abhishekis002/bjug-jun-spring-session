drop table line_items if exists;
drop table orders if exists;
drop table products if exists;

create table products (id integer identity primary key, 
	name varchar(255), price decimal(8,2), available_quantity integer);
	
create table orders (id integer identity primary key, 
	cost decimal(8,2));
	
create table line_items (id integer identity primary key,
	product_id integer, quantity integer, order_id integer);
