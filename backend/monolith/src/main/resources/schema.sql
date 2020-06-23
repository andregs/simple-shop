create table users (
    id bigint identity,
    username varchar(255) unique not null,
    password varchar(255) not null,
    role varchar(8) not null,
    unique(username)
);

create table product (
    id bigint identity,
    name varchar(255) unique not null,
    price decimal(19,2) not null,
    current_amount int not null,
    unique(name)
);

create table cart (
    id bigint identity,
    user_id bigint not null,
    product_id bigint not null,
    amount int not null,
    price decimal(19,2) not null,
    foreign key (user_id) references users(id),
    foreign key (product_id) references product(id),
    unique(user_id, product_id)
);

create table orders (
    id bigint identity,
    user_id bigint not null
);

create table order_line (
    id bigint identity,
    order_id bigint not null,
    product_id bigint not null,
    amount int not null,
    price decimal(19,2) not null,
    foreign key (order_id) references orders(id),
    foreign key (product_id) references product(id),
    unique(order_id, product_id)
);
