create schema if not exists spring_security;
use spring_security;
insert into users values
('jsmith','$2a$10$IbOPwGGZDHql4sgEWsfk8.O8bCUDYWlbpQAKVa8/XLpIdn4rZkYku',1),/* CapstoneAdmin */
('mstark','$2a$10$O8ArnFu9nbQY.tZywK75eeObnETlBHAaxIVlkvN8gMSdb2QaYgADu',1),/* CapstoneManager */
('dwarner','$2a$10$lu6XjdYct2Pyr97Iogjow.Q5IL/lmEpmNLsu2k5LCWqkfFZEdK/qK',1),/* CapstoneTeamLead */
('bstokes','$2a$10$8fWiHZDoXWxXDDpst1XnreMXbTZb0TOSmUYPkhhO.ZgqX/tslkJp.',1);/* CapstoneEmployee */
insert into authorities values
('jsmith','ADMIN'),
('mstark','MANAGER'),
('dwarner','TEAM LEAD'),
('bstokes','EMPLOYEE');