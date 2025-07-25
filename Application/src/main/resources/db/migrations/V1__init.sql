create table public.users
(
    userid     bigserial
        primary key,
    age        integer      not null,
    gender     varchar(255) not null,
    hair_color varchar(255)
        constraint users_hair_color_check
            check ((hair_color)::text = ANY
                   ((ARRAY ['RED'::character varying, 'ORANGE'::character varying, 'YELLOW'::character varying, 'GREEN'::character varying, 'BLUE'::character varying, 'VIOLET'::character varying, 'BLACK'::character varying, 'BLONDE'::character varying])::text[])),
    login      varchar(255) not null,
    name       varchar(255) not null
);

alter table public.users
    owner to postgres;

create table public.user_friends
(
    user_id   bigint not null
        constraint fkk08ugelrh9cea1oew3hgxryw2
            references public.users,
    friend_id bigint
);

alter table public.user_friends
    owner to postgres;

create table public.accounts
(
    accountid bigserial
        primary key,
    balance   double precision not null,
    user_id   bigint           not null
);

alter table public.accounts
    owner to postgres;

create table public.transactions
(
    transactionid    bigserial
        primary key,
    amount           double precision not null,
    from_account_id  bigint           not null,
    to_account_id    bigint           not null,
    type_transaction varchar(255)     not null
        constraint transactions_type_transaction_check
            check ((type_transaction)::text = ANY
                   ((ARRAY ['REPLENISHMENT'::character varying, 'WITHDRAWAL'::character varying, 'TRANSFER'::character varying])::text[]))
);

alter table public.transactions
    owner to postgres;


CREATE INDEX IF NOT EXISTS idx_login ON public.users(login);
CREATE INDEX IF NOT EXISTS idx_name ON public.users(name);
CREATE INDEX IF NOT EXISTS idx_user_id ON public.accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_from_account_id ON public.transactions(from_account_id);
CREATE INDEX IF NOT EXISTS idx_to_account_id ON public.transactions(to_account_id);

