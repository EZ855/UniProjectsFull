-- COMP3311 21T3 Exam Q1
-- Properties most recently sold; date, price and type of each
-- Ordered by price, then property ID if prices are equal

create or replace view q1(date, price, type)
as
select P.sold_date, P.sold_price, P.ptype
from Properties P
where P.sold_date='2021-11-09'
order by P.sold_price ASC
;

