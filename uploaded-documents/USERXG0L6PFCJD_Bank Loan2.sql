select * from login;
delete from login where email like 'johndoe@gmail.com';
select * from roles;
select * from users;
select * from managers;
select * from banks;
select * from loan_types;
select * from loan_requests;
select * from address;
select * from occupation;
select * from account;

update users set credit_score=3800 where first_name='Preetham';


SELECT * FROM loan_requests lr
JOIN loan_types lt ON lr.loan_type_id = lt.loan_type_id
WHERE lt.bank_id = 'BANFJF9D9K3JF' AND lr.status = 'Approved';


insert into roles values('ROLE7DF98D8SK2', 'BANK');
insert into roles values('ROLE7DF9J3SSK2', 'MANAGER');
insert into roles values('ROLEFDK28D8SK2', 'USER')

insert into login values('LOGF873HFKNX8', 'sbi@gmail.com', 'Sbi@123', 'ROLE7DF98D8SK2');
insert into banks values('BANF87DF2K984', 'SBI', 'Garudacharpalya', '7635798121', 'LOGF873HFKNX8');