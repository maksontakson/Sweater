insert into usr(id, user_name, password, active)
    values (1,'admin', '123', true);
insert into user_roles (user_id, roles)
    values (1, 'USER'), (1, 'ADMIN');