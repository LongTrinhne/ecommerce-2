create table category
(
    level              int          not null,
    category_id        bigint auto_increment
        primary key,
    parent_category_id bigint       null,
    category_name      varchar(255) not null,
    constraint FKs2ride9gvilxy2tcuv7witnxc
        foreign key (parent_category_id) references category (category_id)
)
    collate = utf8mb4_0900_ai_ci;

create table product
(
    discounted_percent double       null,
    quantity           int          null,
    category_id        bigint       not null,
    created_at         datetime(6)  null,
    discounted_price   bigint       null,
    price              bigint       not null,
    product_id         bigint auto_increment
        primary key,
    description        varchar(255) not null,
    image_url          varchar(255) null,
    title              varchar(255) not null,
    constraint FK1mtsbur82frn64de7balymq9s
        foreign key (category_id) references category (category_id)
)
    collate = utf8mb4_0900_ai_ci;

create table user
(
    created_at   datetime(6)            null,
    user_id      bigint auto_increment
        primary key,
    email        varchar(255)           not null,
    first_name   varchar(255)           not null,
    last_name    varchar(255)           not null,
    password     varchar(255)           not null,
    phone_number varchar(255)           null,
    role         enum ('ADMIN', 'USER') not null,
    constraint UK_4bgmpi98dylab6qdvf9xyaxu4
        unique (phone_number),
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email)
)
    collate = utf8mb4_0900_ai_ci;

create table address
(
    address_id   bigint auto_increment
        primary key,
    user_id      bigint       null,
    district     varchar(255) not null,
    first_name   varchar(255) not null,
    last_name    varchar(255) not null,
    phone_number varchar(255) not null,
    province     varchar(255) not null,
    street       varchar(255) not null,
    constraint UK_24yhkmn5jfqan5w1k1tpf1j3n
        unique (phone_number),
    constraint FKda8tuywtf0gb6sedwk7la1pgi
        foreign key (user_id) references user (user_id)
)
    collate = utf8mb4_0900_ai_ci;

create table _order
(
    total_items                 int          null,
    created_at                  datetime(6)  null,
    delivery_date               datetime(6)  null,
    discount                    bigint       null,
    order_date                  datetime(6)  null,
    order_id                    bigint auto_increment
        primary key,
    shipping_address_address_id bigint       null,
    total_discounted_price      bigint       null,
    total_price                 bigint       null,
    user_user_id                bigint       null,
    order_status                varchar(255) null,
    constraint UK_3x2ub9c33m45dqnhaa3orauj9
        unique (shipping_address_address_id),
    constraint FKktqkl7kou3pykq8fnm4rr9jmy
        foreign key (user_user_id) references user (user_id),
    constraint FKr93qsf8iayxg5l5f3x0oly3hm
        foreign key (shipping_address_address_id) references address (address_id)
)
    collate = utf8mb4_0900_ai_ci;

create table _order_item
(
    quantity           int         null,
    delivery_date      datetime(6) null,
    discounted_price   bigint      null,
    order_item_id      bigint auto_increment
        primary key,
    order_order_id     bigint      null,
    price              bigint      null,
    product_product_id bigint      null,
    user_id            bigint      null,
    constraint FK1fsi9b645ltxm4pe2deaghdgp
        foreign key (order_order_id) references _order (order_id),
    constraint FKgbtl81dj653udvtiyn2cxkt87
        foreign key (product_product_id) references product (product_id)
)
    collate = utf8mb4_0900_ai_ci;

create table cart
(
    total_item             int    not null,
    cart_id                bigint auto_increment
        primary key,
    discount               bigint null,
    total_discounted_price bigint null,
    total_price            bigint null,
    user_id                bigint not null,
    constraint UK_9emlp6m95v5er2bcqkjsw48he
        unique (user_id),
    constraint FKl70asp4l4w0jmbm1tqyofho4o
        foreign key (user_id) references user (user_id)
)
    collate = utf8mb4_0900_ai_ci;

create table cart_item
(
    quantity           int    null,
    cart_cart_id       bigint null,
    cart_item_id       bigint auto_increment
        primary key,
    discounted_price   bigint null,
    price              bigint null,
    product_product_id bigint null,
    user_id            bigint null,
    constraint FK7vsju2wftw7285wp7maxhi2pd
        foreign key (cart_cart_id) references cart (cart_id),
    constraint FKf67ig6902so02qgbxfiufjmid
        foreign key (product_product_id) references product (product_id)
)
    collate = utf8mb4_0900_ai_ci;

create table rating
(
    rating     double      null,
    created_at datetime(6) null,
    product_id bigint      not null,
    rating_id  bigint auto_increment
        primary key,
    user_id    bigint      not null,
    constraint FKlkuwie0au2dru36asng9nywmh
        foreign key (product_id) references product (product_id),
    constraint FKpn05vbx6usw0c65tcyuce4dw5
        foreign key (user_id) references user (user_id)
)
    collate = utf8mb4_0900_ai_ci;

create table review
(
    created_at datetime(6)  null,
    product_id bigint       null,
    review_id  bigint auto_increment
        primary key,
    user_id    bigint       null,
    review     varchar(255) null,
    constraint FKiyf57dy48lyiftdrf7y87rnxi
        foreign key (user_id) references user (user_id),
    constraint FKiyof1sindb9qiqr9o8npj8klt
        foreign key (product_id) references product (product_id)
)
    collate = utf8mb4_0900_ai_ci;


