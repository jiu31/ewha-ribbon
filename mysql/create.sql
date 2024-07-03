drop database if exists  DB2024Team13;
create database DB2024Team13;
use DB2024Team13; # use database

# create table

create table DB2024_restaurant(
rest_name varchar(30) not null, #primary key
location varchar(50), #주소
category varchar(20), #한식, 중식, 일식, 양식, 분식, 아시안, 패스트푸드, 카페
breaktime boolean, 
section_name varchar(20), #정문, 후문, 공대쪽문, 북아현
eat_alone boolean,
primary key (rest_name)
);

create table DB2024_customer(
student_id varchar(20) not null, #primary key, 7자리 정수
nickname varchar(10),
signup_date date,
password varchar (50),
primary key(student_id)
);

create table DB2024_section(
building varchar(7) not null, #primary key, 학교 내 건물
section varchar(20), #정문, 후문, 공대쪽문, 북아현
primary key(building)
);

create table DB2024_menu(
rest_name varchar(30) not null, #primary key, foreign key
menu_name varchar(50) not null, #primary key
price int,
vegan boolean,
primary key (rest_name, menu_name),
foreign key (rest_name) references DB2024_restaurant(rest_name) on delete cascade
);

create table DB2024_order(
order_id int not null, #primary key
student_id varchar(20) not null, #foreign key
rest_name varchar(30) not null, #foreign key
menu_name varchar(50) not null, #foreign key
order_date_time datetime,
primary key (order_id),
foreign key (student_id) references DB2024_customer(student_id) on delete cascade,
foreign key (rest_name, menu_name) references DB2024_menu(rest_name, menu_name)
);

create table DB2024_review(
review_id int not null, #primary key
rest_name varchar(30) not null, #foreign key
student_id varchar(20) not null, #foreign key
star float, 
primary key (review_id),
foreign key (rest_name) references DB2024_restaurant(rest_name),
foreign key (student_id) references DB2024_customer(student_id)
);

create table DB2024_bookmark(
rest_name varchar(30) not null, #primary key, foreign key
student_id varchar(20) not null, #primary key, foreign key
primary key (rest_name, student_id),
foreign key (rest_name) references DB2024_restaurant(rest_name),
foreign key (student_id) references DB2024_customer(student_id)
);


# create view
# order 테이블에서 레스토랑별 주문수의 합을 계산하는 뷰 생성
CREATE VIEW DB2024_rest_order_count AS
SELECT 
    rest_name,
    COUNT(order_id) AS order_count
FROM 
    DB2024_order
GROUP BY 
    rest_name;

# 리뷰 테이블에서 레스토랑별 평균 평점을 계산하는 뷰 생성
CREATE VIEW DB2024_rest_avg_rating AS
SELECT 
    rest_name,
    AVG(star) AS avg_rating
FROM 
    DB2024_review
GROUP BY 
    rest_name;

# create index
CREATE INDEX idx_restaurant_rest_name ON DB2024_restaurant(rest_name);
CREATE INDEX idx_review_id ON DB2024_review(review_id);
CREATE INDEX idx_bookmark_student_rest ON DB2024_bookmark(student_id, rest_name);
CREATE INDEX idx_restaurant_section_name ON DB2024_restaurant(section_name);
CREATE INDEX idx_order_student_date ON DB2024_order(student_id, order_date_time);


# insert

# Restaurant table
# 북아현
insert into DB2024_restaurant values ('북아현로69초밥', '서울 서대문구 북아현로 69 1층', '일식', TRUE, '북아현', TRUE);
insert into DB2024_restaurant values ('쵸이피자', '서울 서대문구 북아현로 78', '패스트푸드', FALSE, '북아현', FALSE);
insert into DB2024_restaurant values ('구일당', '서울 서대문구 북아현로 71 1층 101호', '한식', FALSE, '북아현', FALSE);
insert into DB2024_restaurant values ('세아짬뽕', '서울 서대문구 이화여대8길 123 힐스테이트 상가동 2층 201호', '중식', FALSE, '북아현', TRUE);
insert into DB2024_restaurant values ('콘브리오', '서울 서대문구 북아현로 25길 12', '카페', FALSE, '북아현', TRUE);

# 정문
insert into DB2024_restaurant values ('사장님돈까스', '서울 서대문구 이화여대7길 11 1층', '분식', FALSE, '정문', TRUE);
insert into DB2024_restaurant values ('파파노다이닝', '서울 서대문구 이화여대길 88-14', '일식', TRUE, '정문', TRUE);
insert into DB2024_restaurant values ('리또리또', '서울 서대문구 이화여대길 52-33', '양식', FALSE, '정문', TRUE);
insert into DB2024_restaurant values ('반타이쏘이54', '서울 서대문구 이화여대5길 7-1, 1층', '아시안', TRUE, '정문', TRUE);
insert into DB2024_restaurant values ('그릭데이', '서울 서대문구 신촌역로 22-8', '카페', FALSE, '정문', TRUE);
insert into DB2024_restaurant values ('아콘스톨', '서울 서대문구 신촌역로 17 1층 110호', '분식', FALSE, '정문', TRUE);
insert into DB2024_restaurant values ('마더린러베이글', '서울 서대문구 이화여대5길 5 1층', '카페', FALSE, '정문', FALSE);
insert into DB2024_restaurant values ('이화김밥', '서울 서대문구 이화여대길 52 ECC B417호', '분식', TRUE, '정문', TRUE);
insert into DB2024_restaurant values ('가미분식', '서울 서대문구 이화여대8길 2 무궁화상가아파트', '한식', FALSE, '정문', TRUE);
insert into DB2024_restaurant values ('포포나무', '서울 서대문구 이화여대2가길 24', '양식', TRUE, '정문', TRUE);
insert into DB2024_restaurant values  ('심플리스트', '서울 서대문구 이화여대길 24 2층', '양식', TRUE, '정문', TRUE);
insert into DB2024_restaurant values ('김폴폴', '서울 서대문구 이화여대길 52 이화여자대학교 국제교육관', '한식', TRUE, '정문', TRUE);

# 공대쪽문
insert into DB2024_restaurant values ('존재의 이유', '서울 서대문구 성산로 553','한식', FALSE, '공대쪽문', TRUE);
insert into DB2024_restaurant values ('헐리우드', '서울 서대문구 성산로 551 1층', '양식', FALSE, '공대쪽문', TRUE);
insert into DB2024_restaurant values ('스타벅스 연대동문점', '서울 서대문구 성산로 565', '카페', FALSE, '공대쪽문', TRUE);
insert into DB2024_restaurant values ('위블레', '서울 서대문구 연대동문길 71 1층', '카페', FALSE, '공대쪽문', TRUE);

# 후문
insert into DB2024_restaurant values ('스튜디오웝', '서울 서대문구 성산로 539 1층', '카페', FALSE, '후문', TRUE);
insert into DB2024_restaurant values ('스탠바이키친', '서울 서대문구 연대동문길 49', '양식', TRUE, '후문', TRUE);
insert into DB2024_restaurant values ('하노이의 아침', '서울 서대문구 연대동문길 45 3층', '아시안', FALSE, '후문', TRUE);
insert into DB2024_restaurant values ('아비꼬 연대동문점', '서울 서대문구 연대동문길 45 2층', '일식', FALSE, '후문', TRUE);
insert into DB2024_restaurant values ('딸기골', '서울 서대문구 연대동문길 29', '한식', FALSE, '후문', TRUE);
insert into DB2024_restaurant values ('다미손칼국수', '서울 서대문구 성산로 537', '한식', TRUE, '후문', TRUE);


# Customer table

insert into DB2024_customer values ('1111', '디버깅', '2019-05-06', '2222');
insert into DB2024_customer values ('1971028', '범범카', '2019-05-06', '0506');
insert into DB2024_customer values ('2051013', '뵤', '2020-11-04', 'hamsterk');
insert into DB2024_customer values ('2131007', '믕지', '2021-08-18', 'meung0506');
insert into DB2024_customer values ('2261026', '뿅', '2022-07-28', 'longfor_923');
insert into DB2024_customer values ('2371031', '용데이', '2023-09-23', 'yeongday00');
insert into DB2024_customer values ('2176201', '배고파', '2021-04-21', 'mingming21');
insert into DB2024_customer values ('1836021', '찹쌀', '2019-01-05', 'nevergetit02');
insert into DB2024_customer values ('2451033', '우리집초코', '2024-03-31', 'iforgot!333');
insert into DB2024_customer values ('2241078', '네온', '2023-09-12', 'romancat');
insert into DB2024_customer values ('2033056', 'I는대문자', '2022-11-07', 'buzzcut03');
insert into DB2024_customer values ('2171046', '토마토', '2021-10-31','tomatorunstheworld');
insert into DB2024_customer values ('2103090', '그린그린', '2021-03-07','iamgreen');
insert into DB2024_customer values ('2450021', '새내깅', '2024-04-08','asdf1234');
insert into DB2024_customer values ('2305100', '바밤바', '2023-07-26','babamba12');
insert into DB2024_customer values ('2032005', '냠냠', '2020-05-05','ewhaewha22');
INSERT INTO DB2024_customer VALUES ('2123402', '김밥러버', '2024-03-04', 'gimbablover');
INSERT INTO DB2024_customer VALUES ('2208201', '자취러', '2024-03-04', 'thdms');
INSERT INTO DB2024_customer VALUES ('2123910', '민트초코', '2024-03-04', 'mincho');
INSERT INTO DB2024_customer VALUES ('2024902', '음대생', '2024-02-21', 'violinviolin');
INSERT INTO DB2024_customer VALUES ('2324290', '고구마라떼', '2021-02-22', 'dlghk');

# 관리자 
insert into DB2024_customer values ('0000', '관리자', '2017-12-31','1234');


# Section table

insert into DB2024_section values ('ECC', '정문');
insert into DB2024_section values ('조형예술관', '정문');
insert into DB2024_section values ('음악관', '정문');
insert into DB2024_section values ('국제교육관', '정문');
insert into DB2024_section values ('신세계관', '정문');
insert into DB2024_section values ('SK텔레콤관', '정문');
insert into DB2024_section values ('체육관', '정문');
insert into DB2024_section values ('헬렌관', '정문');

insert into DB2024_section values ('포스코관', '후문');
insert into DB2024_section values ('학관', '후문');
insert into DB2024_section values ('학생문화관', '후문');
insert into DB2024_section values ('생활관', '후문');
insert into DB2024_section values ('약학관', '후문');
insert into DB2024_section values ('인문관', '후문');
insert into DB2024_section values ('교육관', '후문');

insert into DB2024_section values ('아산공학관', '공대쪽문');
insert into DB2024_section values ('신공학관', '공대쪽문');
insert into DB2024_section values ('종합과학관', '공대쪽문');
insert into DB2024_section values ('연구협력관', '공대쪽문');

insert into DB2024_section values ('한우리집', '북아현');
insert into DB2024_section values ('이하우스', '북아현');


# Menu table

insert into DB2024_menu values ('북아현로69초밥', '스페셜초밥(14pcs)', 28000, FALSE);
insert into DB2024_menu values ('북아현로69초밥', '연어초밥(10pcs)', 20000, FALSE);
insert into DB2024_menu values ('북아현로69초밥', '소고기초밥(5pcs)', 12000, FALSE);

insert into DB2024_menu values ('쵸이피자', '프리미엄피자L 2판', 23900, FALSE);
insert into DB2024_menu values ('쵸이피자', '포테이토골드피자(L)', 20900, FALSE);
insert into DB2024_menu values ('쵸이피자', '크림스파게티', 7000, FALSE);

insert into DB2024_menu values ('구일당', '닭볶음탕(중)', 32000, FALSE);
insert into DB2024_menu values ('구일당', '닭냉채', 25000, FALSE);
insert into DB2024_menu values ('구일당', '계란말이', 11000, FALSE);

insert into DB2024_menu values ('세아짬뽕', '세아짬뽕', 9000, FALSE);
insert into DB2024_menu values ('세아짬뽕', '차돌짬뽕', 13000, FALSE);
insert into DB2024_menu values ('세아짬뽕', '백짬뽕', 13000, FALSE);

insert into DB2024_menu values ('콘브리오', '콘브리오 샌드위치', 8000, FALSE);
insert into DB2024_menu values ('콘브리오', '레몬청에이드', 5000, FALSE);
insert into DB2024_menu values ('콘브리오', '스모키얼그레이라떼', 5000, FALSE);

insert into DB2024_menu values ('마더린러베이글', '베이글(개당)', 3200, FALSE);
insert into DB2024_menu values ('마더린러베이글', '크림치즈(65g)', 3500, FALSE);
insert into DB2024_menu values ('마더린러베이글', '훈제연어샌드위치', 8900, FALSE);

insert into DB2024_menu values ('사장님돈까스', '고구마치즈돈까스', 11500, FALSE);
insert into DB2024_menu values ('사장님돈까스', '초계물냉면', 9500, FALSE);
insert into DB2024_menu values ('사장님돈까스', '냉모밀돈까스세트', 12000, FALSE);

insert into DB2024_menu values ('파파노다이닝', '와후함바그정식', 12000, FALSE);
insert into DB2024_menu values ('파파노다이닝', '아게다시도후정식', 12000, TRUE);
insert into DB2024_menu values ('파파노다이닝', '가라아게정식', 12000, FALSE);

insert into DB2024_menu values ('리또리또', '닭부리또', 5500, FALSE);
insert into DB2024_menu values ('리또리또', '콩부리또', 5500, TRUE);
insert into DB2024_menu values ('리또리또', '소부리또', 6500, FALSE);

insert into DB2024_menu values ('반타이쏘이54', '새우팟타이', 10500, FALSE);
insert into DB2024_menu values ('반타이쏘이54', '뿌팟퐁카리(소프트크랩)', 23000, FALSE);
insert into DB2024_menu values ('반타이쏘이54', '팟카파우무쌉(다진돼지고기덮밥)', 9500, FALSE);

insert into DB2024_menu values ('그릭데이', '런던블루(딸기,블루베리,바나나,건무화과,쌀크런치,메이플귀리)', 6200, FALSE);
insert into DB2024_menu values ('그릭데이', '베리그린(딸기,골드키위,블루베리,바나나,메이플귀리)', 5900, FALSE);
insert into DB2024_menu values ('그릭데이', '베리스칼렛(딸기,바나나,오레오,딸기잼,쌀크런치)', 5900, FALSE);

insert into DB2024_menu values ('아콘스톨', '참치김밥', 4500, FALSE);
insert into DB2024_menu values ('아콘스톨', '야채(비트)김밥', 3000, TRUE);
insert into DB2024_menu values ('아콘스톨', '순대떡볶음', 3900, FALSE);

insert into DB2024_menu values ('존재의 이유', '가정식백반', 8000, FALSE);
insert into DB2024_menu values ('존재의 이유', '낙지볶음 정식', 12000, FALSE);
insert into DB2024_menu values ('존재의 이유', '갈치조림 정식', 13000, FALSE);

insert into DB2024_menu values ('헐리우드', '파마산치킨파스타', 12000, FALSE);
insert into DB2024_menu values ('헐리우드', '필리치즈 스테이크 샌드위치', 8000, FALSE);
insert into DB2024_menu values ('헐리우드', '까르보나라', 12000, FALSE);

insert into DB2024_menu values ('스타벅스 연대동문점', '카페 아메리카노', 4500, TRUE);
insert into DB2024_menu values ('스타벅스 연대동문점', '쿨 라임 피지오', 5900, TRUE);
insert into DB2024_menu values ('스타벅스 연대동문점', '플랫 화이트', 5600, FALSE);

insert into DB2024_menu values ('위블레', '크로아상', 4000, FALSE);
insert into DB2024_menu values ('위블레', '아메리카노', 4200, FALSE);
insert into DB2024_menu values ('위블레', '치아바타', 3000, FALSE);

insert into DB2024_menu values ('스튜디오웝', '밀크티', 5500, FALSE);
insert into DB2024_menu values ('스튜디오웝', '월트라떼', 5000, FALSE);
insert into DB2024_menu values ('스튜디오웝', '콘스콘', 4000, FALSE);

insert into DB2024_menu values ('스탠바이키친', '베이컨 양상추 토마토 샌드위치', 7300, FALSE);
insert into DB2024_menu values ('스탠바이키친', '베트남 치킨 반미', 7800, FALSE);
insert into DB2024_menu values ('스탠바이키친', '퀴노아 샐러드', 9800, TRUE);

insert into DB2024_menu values ('하노이의 아침', '양지쌀국수', 13000, FALSE);
insert into DB2024_menu values ('하노이의 아침', '짜조', 17000, FALSE);
insert into DB2024_menu values ('하노이의 아침', '파인볶음밥', 15500, FALSE);

insert into DB2024_menu values ('아비꼬 연대동문점', '100시간카레', 7200, FALSE);
insert into DB2024_menu values ('아비꼬 연대동문점', '수제함박카레', 11500, FALSE);
insert into DB2024_menu values ('아비꼬 연대동문점', '쉬림프카레', 11800, FALSE);

insert into DB2024_menu values ('딸기골', '치즈김치순두부', 5500, FALSE);
insert into DB2024_menu values ('딸기골', '김치찌개', 5000, FALSE);
insert into DB2024_menu values ('딸기골', '돌솥비빔밥', 5500, FALSE);

insert into DB2024_menu values ('다미손칼국수', '칼국수', 8500, FALSE);
insert into DB2024_menu values ('다미손칼국수', '칼만두', 8500, FALSE);
insert into DB2024_menu values ('다미손칼국수', '사골우거지탕', 9500, FALSE);

insert into DB2024_menu values ('이화김밥', '이화김밥', '4000', FALSE);
insert into DB2024_menu values ('이화김밥', '비건유부김밥', '3500', TRUE);
insert into DB2024_menu values ('이화김밥', '샐러드김밥', '5200', TRUE);

insert into DB2024_menu values ('가미분식', '가미우동', '7500', FALSE);
insert into DB2024_menu values ('가미분식', '닭칼국수', '9000', FALSE);
insert into DB2024_menu values ('가미분식', '비빔국수', '7500', FALSE);

insert into DB2024_menu values ('포포나무', '떡갈비 스테이크', '7500', FALSE);
insert into DB2024_menu values ('포포나무', '데리야끼 치킨 스테이크', '8000', FALSE);
insert into DB2024_menu values ('포포나무', '함박 스테이크', '7900', FALSE);

insert into DB2024_menu values ('심플리스트', '심플파이롤', '14000', FALSE);
insert into DB2024_menu values ('심플리스트', '심플파이치즈롤', '15000', FALSE);
insert into DB2024_menu values ('심플리스트', '홍새우 파스타', '23000', FALSE);

insert into DB2024_menu values ('김폴폴', '반상A', '7700', FALSE);
insert into DB2024_menu values ('김폴폴', '반상B', '8800', FALSE);
insert into DB2024_menu values ('김폴폴', '반상C', '9900', FALSE);


# Order table

INSERT INTO DB2024_order VALUES (1, '1971028', '북아현로69초밥', '스페셜초밥(14pcs)', '2024-05-20 12:34:56');
INSERT INTO DB2024_order VALUES (2, '2171046', '그릭데이', '런던블루(딸기,블루베리,바나나,건무화과,쌀크런치,메이플귀리)', '2024-05-15 06:34:50');
INSERT INTO DB2024_order VALUES (3, '2032005', '포포나무', '함박 스테이크', '2024-01-15 10:12:34');
INSERT INTO DB2024_order VALUES (4, '2024902', '사장님돈까스', '고구마치즈돈까스', '2024-02-10 06:34:50');
INSERT INTO DB2024_order VALUES (5, '2131007', '북아현로69초밥', '소고기초밥(5pcs)', '2024-03-10 10:12:34');
INSERT INTO DB2024_order VALUES (6, '2450021', '스탠바이키친', '베트남 치킨 반미', '2024-03-05 08:00:12');
INSERT INTO DB2024_order VALUES (7, '2451033', '구일당', '닭냉채', '2024-03-25 05:23:40');
INSERT INTO DB2024_order VALUES (8, '2261026', '스타벅스 연대동문점', '쿨 라임 피지오', '2024-02-25 05:23:40');
INSERT INTO DB2024_order VALUES (9, '2103090', '스탠바이키친', '베이컨 양상추 토마토 샌드위치', '2024-04-10 09:01:23');
INSERT INTO DB2024_order VALUES (10, '2241078', '이화김밥', '샐러드김밥', '2024-02-15 04:12:30');
INSERT INTO DB2024_order VALUES (11, '2451033', '이화김밥', '비건유부김밥', '2024-03-25 05:23:40');
INSERT INTO DB2024_order VALUES (12, '2324290', '헐리우드', '파마산치킨파스타', '2024-01-05 09:01:23');
INSERT INTO DB2024_order VALUES (13, '2024902', '아비꼬 연대동문점', '수제함박카레', '2024-02-10 02:00:10');
INSERT INTO DB2024_order VALUES (14, '2123910', '존재의 이유', '낙지볶음 정식', '2024-03-15 11:23:45');
INSERT INTO DB2024_order VALUES (15, '2033056', '세아짬뽕', '세아짬뽕', '2024-01-05 03:01:20');
INSERT INTO DB2024_order VALUES (16, '2261026', '파파노다이닝', '가라아게정식', '2024-02-25 01:45:00');
INSERT INTO DB2024_order VALUES (17, '1836021', '구일당', '닭볶음탕(중)', '2024-04-05 06:34:50');
INSERT INTO DB2024_order VALUES (18, '2450021', '콘브리오', '콘브리오 샌드위치', '2024-03-05 12:34:56');
INSERT INTO DB2024_order VALUES (19, '2032005', '콘브리오', '스모키얼그레이라떼', '2024-01-15 10:12:34');
INSERT INTO DB2024_order VALUES (20, '2241078', '스튜디오웝', '밀크티', '2024-02-15 12:34:56');
INSERT INTO DB2024_order VALUES (21, '2123910', '마더린러베이글', '훈제연어샌드위치', '2024-03-15 07:45:01');
INSERT INTO DB2024_order VALUES (22, '2033056', '반타이쏘이54', '팟카파우무쌉(다진돼지고기덮밥)', '2024-01-05 07:45:01');
INSERT INTO DB2024_order VALUES (23, '2032005', '하노이의 아침', '양지쌀국수', '2024-01-15 06:34:50');
INSERT INTO DB2024_order VALUES (24, '2032005', '아콘스톨', '야채(비트)김밥', '2024-01-15 02:00:10');
INSERT INTO DB2024_order VALUES (25, '2123402', '아콘스톨', '순대떡볶음', '2024-05-01 01:45:00');
INSERT INTO DB2024_order VALUES (26, '2171046', '가미분식', '닭칼국수', '2024-05-15 02:00:10');
INSERT INTO DB2024_order VALUES (27, '2305100', '포포나무', '데리야끼 치킨 스테이크', '2024-02-20 11:23:45');
INSERT INTO DB2024_order VALUES (28, '1971028', '딸기골', '치즈김치순두부', '2024-05-25 12:34:56');
INSERT INTO DB2024_order VALUES (29, '2131007', '딸기골', '돌솥비빔밥', '2024-03-10 10:12:34');
INSERT INTO DB2024_order VALUES (30, '2208201', '존재의 이유', '가정식백반', '2024-04-20 12:34:56');
INSERT INTO DB2024_order VALUES (31, '2261026', '쵸이피자', '프리미엄피자L 2판', '2024-02-25 09:01:23');
INSERT INTO DB2024_order VALUES (32, '1836021', '이화김밥', '이화김밥', '2024-04-05 06:34:50');
INSERT INTO DB2024_order VALUES (33, '2131007', '스타벅스 연대동문점', '카페 아메리카노', '2024-03-10 06:34:50');
INSERT INTO DB2024_order VALUES (34, '2371031', '다미손칼국수', '칼만두', '2024-01-20 08:00:12');
INSERT INTO DB2024_order VALUES (35, '2305100', '콘브리오', '레몬청에이드', '2024-02-20 11:23:45');
INSERT INTO DB2024_order VALUES (36, '2103090', '그릭데이', '베리그린(딸기,골드키위,블루베리,바나나,메이플귀리)', '2024-04-10 05:23:40');
INSERT INTO DB2024_order VALUES (37, '2033056', '스튜디오웝', '월트라떼', '2024-01-05 11:23:45');
INSERT INTO DB2024_order VALUES (38, '2024902', '존재의 이유', '갈치조림 정식', '2024-02-10 10:12:34');
INSERT INTO DB2024_order VALUES (39, '2051013', '딸기골', '김치찌개', '2024-04-15 11:23:45');
INSERT INTO DB2024_order VALUES (40, '2131007', '파파노다이닝', '아게다시도후정식', '2024-03-10 02:00:10');
INSERT INTO DB2024_order VALUES (41, '2024902', '김폴폴', '반상A', '2024-02-10 06:34:50');
INSERT INTO DB2024_order VALUES (42, '2208201', '마더린러베이글', '크림치즈(65g)', '2024-04-20 08:00:12');
INSERT INTO DB2024_order VALUES (43, '1836021', '리또리또', '소부리또', '2024-04-05 10:12:34');
INSERT INTO DB2024_order VALUES (44, '2324290', '김폴폴', '반상B', '2024-01-05 05:23:40');
INSERT INTO DB2024_order VALUES (45, '2451033', '반타이쏘이54', '새우팟타이', '2024-03-25 09:01:23');
INSERT INTO DB2024_order VALUES (46, '1971028', '사장님돈까스', '냉모밀돈까스세트', '2024-05-25 04:12:30');
INSERT INTO DB2024_order VALUES (47, '2241078', '구일당', '계란말이', '2024-02-15 04:12:30');
INSERT INTO DB2024_order VALUES (48, '2176201', '리또리또', '콩부리또', '2024-05-10 11:23:45');
INSERT INTO DB2024_order VALUES (49, '2451033', '위블레', '치아바타', '2024-03-25 01:45:00');
INSERT INTO DB2024_order VALUES (50, '2176201', '다미손칼국수', '사골우거지탕', '2024-05-10 07:45:01');
INSERT INTO DB2024_order VALUES (51, '2305100', '스탠바이키친', '퀴노아 샐러드', '2024-02-20 07:45:01');
INSERT INTO DB2024_order VALUES (52, '2208201', '하노이의 아침', '파인볶음밥', '2024-04-20 04:12:30');
INSERT INTO DB2024_order VALUES (53, '2261026', '다미손칼국수', '칼국수', '2024-02-25 09:01:23');
INSERT INTO DB2024_order VALUES (54, '2371031', '스타벅스 연대동문점', '플랫 화이트', '2024-01-20 04:12:30');
INSERT INTO DB2024_order VALUES (55, '2241078', '반타이쏘이54', '뿌팟퐁카리(소프트크랩)', '2024-02-15 08:00:12');
INSERT INTO DB2024_order VALUES (56, '2171046', '스튜디오웝', '콘스콘', '2024-05-15 10:12:34');
INSERT INTO DB2024_order VALUES (57, '2450021', '포포나무', '떡갈비 스테이크', '2024-03-05 12:34:56');
INSERT INTO DB2024_order VALUES (58, '2103090', '가미분식', '비빔국수', '2024-04-10 01:45:00');
INSERT INTO DB2024_order VALUES (59, '2123910', '심플리스트', '홍새우 파스타', '2024-03-15 07:45:01');
INSERT INTO DB2024_order VALUES (60, '2051013', '북아현로69초밥', '연어초밥(10pcs)', '2024-04-15 11:23:45');
INSERT INTO DB2024_order VALUES (61, '2123402', '심플리스트', '심플파이롤', '2024-05-01 09:01:23');
INSERT INTO DB2024_order VALUES (62, '2123402', '마더린러베이글', '베이글(개당)', '2024-05-01 09:01:23');
INSERT INTO DB2024_order VALUES (63, '2208201', '심플리스트', '심플파이치즈롤', '2024-04-20 08:00:12');
INSERT INTO DB2024_order VALUES (64, '2371031', '리또리또', '닭부리또', '2024-01-20 12:34:56');
INSERT INTO DB2024_order VALUES (65, '2176201', '쵸이피자', '크림스파게티', '2024-05-10 07:45:01');
INSERT INTO DB2024_order VALUES (66, '2051013', '헐리우드', '까르보나라', '2024-04-15 07:45:01');
INSERT INTO DB2024_order VALUES (67, '1971028', '김폴폴', '반상C', '2024-05-25 04:12:30');
INSERT INTO DB2024_order VALUES (68, '1971028', '헐리우드', '필리치즈 스테이크 샌드위치', '2024-05-25 08:00:12');
INSERT INTO DB2024_order VALUES (69, '2123910', '아비꼬 연대동문점', '100시간카레', '2024-03-15 03:01:20');
INSERT INTO DB2024_order VALUES (70, '2033056', '가미분식', '가미우동', '2024-01-05 03:01:20');
INSERT INTO DB2024_order VALUES (71, '2123402', '하노이의 아침', '짜조', '2024-05-01 05:23:40');
INSERT INTO DB2024_order VALUES (72, '2305100', '아콘스톨', '참치김밥', '2024-02-20 03:01:20');
INSERT INTO DB2024_order VALUES (73, '2103090', '세아짬뽕', '백짬뽕', '2024-04-10 01:45:00');
INSERT INTO DB2024_order VALUES (74, '2171046', '세아짬뽕', '차돌짬뽕', '2024-05-15 02:00:10');
INSERT INTO DB2024_order VALUES (75, '1836021', '위블레', '아메리카노', '2024-04-05 02:00:10');
INSERT INTO DB2024_order VALUES (76, '2324290', '아비꼬 연대동문점', '쉬림프카레', '2024-01-05 01:45:00');
INSERT INTO DB2024_order VALUES (77, '2051013', '파파노다이닝', '와후함바그정식', '2024-04-15 03:01:20');
INSERT INTO DB2024_order VALUES (78, '2176201', '위블레', '크로아상', '2024-05-10 03:01:20');
INSERT INTO DB2024_order VALUES (79, '2371031', '쵸이피자', '포테이토골드피자(L)', '2024-01-20 08:00:12');
INSERT INTO DB2024_order VALUES (80, '2450021', '그릭데이', '베리스칼렛(딸기,바나나,오레오,딸기잼,쌀크런치)', '2024-03-05 04:12:30');
INSERT INTO DB2024_order VALUES (81, '2324290', '사장님돈까스', '초계물냉면', '2024-01-05 05:23:40');
INSERT INTO DB2024_order VALUES (82, '2171046', '북아현로69초밥', '스페셜초밥(14pcs)', '2024-01-08 19:23:40');
INSERT INTO DB2024_order VALUES (83, '2450021', '쵸이피자', '프리미엄피자L 2판', '2024-03-11 09:59:22');
INSERT INTO DB2024_order VALUES (84, '2450021', '파파노다이닝', '가라아게정식', '2024-05-08 20:01:53');
INSERT INTO DB2024_order VALUES (85, '2032005', '리또리또', '콩부리또', '2024-01-31 14:37:00');
INSERT INTO DB2024_order VALUES (86, '2123402', '아콘스톨', '야채(비트)김밥', '2024-02-11 15:11:29');
INSERT INTO DB2024_order VALUES (87, '2123910', '쵸이피자', '크림스파게티', '2024-02-26 16:10:44');
INSERT INTO DB2024_order VALUES (88, '2324290', '아콘스톨', '참치김밥', '2024-03-21 20:01:32');
INSERT INTO DB2024_order VALUES (89, '2241078', '아콘스톨', '참치김밥', '2024-03-16 12:57:18');
INSERT INTO DB2024_order VALUES (90, '2241078', '아콘스톨', '순대떡볶음', '2024-03-18 11:42:07');
INSERT INTO DB2024_order VALUES (91, '1836021', '스타벅스 연대동문점', '카페 아메리카노', '2024-01-22 08:20:55');
INSERT INTO DB2024_order VALUES (92, '2371031', '북아현로69초밥', '소고기초밥(5pcs)', '2024-01-28 10:37:24');
INSERT INTO DB2024_order VALUES (93, '2131007', '스타벅스 연대동문점', '카페 아메리카노', '2024-03-08 07:23:55');
INSERT INTO DB2024_order VALUES (94, '1971028', '북아현로69초밥', '연어초밥(10pcs)', '2024-03-11 13:45:38');
INSERT INTO DB2024_order VALUES (95, '2051013', '아콘스톨', '참치김밥', '2024-03-19 15:31:59');
INSERT INTO DB2024_order VALUES (96, '2261026', '사장님돈까스', '초계물냉면', '2024-04-04 16:16:22');
INSERT INTO DB2024_order VALUES (97, '2451033', '스타벅스 연대동문점', '쿨 라임 피지오', '2024-04-17 17:55:30');
INSERT INTO DB2024_order VALUES (98, '2033056', '아콘스톨', '순대떡볶음', '2024-04-20 18:38:25');
INSERT INTO DB2024_order VALUES (99, '2103090', '스타벅스 연대동문점', '플랫 화이트', '2024-04-11 07:11:58');


# Review table

INSERT INTO DB2024_review(review_id, rest_name, student_id, star) VALUES
(1, '북아현로69초밥', '1971028', 5.0),
(2, '그릭데이', '2171046', 4.3),
(3, '포포나무', '2032005', 4.6),
(4, '사장님돈까스', '2024902', 4.8),
(5, '북아현로69초밥', '2131007', 4.5),
(6, '스탠바이키친', '2450021', 5.0),
(7, '구일당', '2451033', 4.0),
(8, '스타벅스 연대동문점', '2261026', 3.8),
(9, '스탠바이키친', '2103090', 4.4),
(10, '이화김밥', '2241078', 4.2),
(11, '이화김밥', '2451033', 5.0),
(12, '헐리우드', '2324290', 5.0),
(13, '아비꼬 연대동문점', '2024902', 4.5),
(14, '존재의 이유', '2123910', 4.8),
(15, '세아짬뽕', '2033056', 3.3),
(16, '파파노다이닝', '2261026', 1.0),
(17, '구일당', '1836021', 2.5),
(18, '콘브리오', '2450021', 3.5),
(19, '콘브리오', '2032005', 4.5),
(20, '스튜디오웝', '2241078', 3.8),
(21, '마더린러베이글', '2123910', 4.0),
(22, '반타이쏘이54', '2033056', 5.0),
(23, '하노이의 아침', '2032005', 0.5),
(24, '아콘스톨', '2032005', 5.0),
(25, '아콘스톨', '2123402', 4.6),
(26, '가미분식', '2171046', 4.2),
(27, '포포나무', '2305100', 5.0),
(28, '딸기골', '1971028', 5.0),
(29, '딸기골', '2131007', 4.0),
(30, '존재의 이유', '2208201', 4.1),
(31, '쵸이피자', '2261026', 5.0),
(32, '이화김밥', '1836021', 2.0),
(33, '스타벅스 연대동문점', '2131007', 4.5),
(34, '다미손칼국수', '2371031', 0.5),
(35, '콘브리오', '2305100', 3.7),
(36, '그릭데이', '2103090', 3.9),
(37, '스튜디오웝', '2033056', 5.0),
(38, '존재의 이유', '2024902', 3.5),
(39, '딸기골', '2051013', 4.4),
(40, '파파노다이닝', '2131007', 3.0),
(41, '김폴폴', '2024902', 3.0),
(42, '마더린러베이글', '2208201', 5.0),
(43, '리또리또', '1836021', 5.0),
(44, '김폴폴', '2324290', 3.6),
(45, '반타이쏘이54', '2451033', 4.3),
(46, '사장님돈까스', '1971028', 4.5),
(47, '구일당', '2241078', 5.0),
(48, '리또리또', '2176201', 3.0),
(49, '위블레', '2451033', 5.0),
(50, '다미손칼국수', '2176201', 4.0),
(51, '스탠바이키친', '2305100', 4.5),
(52, '하노이의 아침', '2208201', 4.0),
(53, '다미손칼국수', '2261026', 5.0),
(54, '스타벅스 연대동문점', '2371031', 2.0),
(55, '반타이쏘이54', '2241078', 4.8),
(56, '스튜디오웝', '2171046', 4.1),
(57, '포포나무', '2450021', 4.5),
(58, '가미분식', '2103090', 5.0),
(59, '심플리스트', '2123910', 5.0),
(60, '북아현로69초밥', '2051013', 5.0),
(61, '심플리스트', '2123402', 4.5),
(62, '마더린러베이글', '2123402', 4.0),
(63, '심플리스트', '2208201', 4.8),
(64, '리또리또', '2371031', 4.3),
(65, '쵸이피자', '2176201', 5.0),
(66, '헐리우드', '2051013', 4.5),
(67, '김폴폴', '1971028', 3.7),
(68, '헐리우드', '1971028', 4.9),
(69, '아비꼬 연대동문점', '2123910', 4.0),
(70, '가미분식', '2033056', 3.5),
(71, '하노이의 아침', '2123402', 5.0),
(72, '아콘스톨', '2305100', 5.0),
(73, '세아짬뽕', '2103090', 1.5),
(74, '세아짬뽕', '2171046', 4.0),
(75, '위블레', '1836021', 4.5),
(76, '아비꼬 연대동문점', '2324290', 3.8),
(77, '파파노다이닝', '2051013', 5.0),
(78, '위블레', '2176201', 5.0),
(79, '쵸이피자', '2371031', 4.8),
(80, '그릭데이', '2450021', 5.0),
(81, '사장님돈까스', '2324290', 4.6),
(82, '북아현로69초밥', '2171046', 4.4),
(83, '쵸이피자', '2450021', 5.0),
(84, '파파노다이닝', '2450021', 3.5),
(85, '리또리또', '2032005', 3.8),
(86, '아콘스톨', '2123402', 5.0),
(87, '쵸이피자', '2123910', 4.5),
(88, '아콘스톨', '2324290', 4.5),
(89, '아콘스톨', '2241078', 5.0),
(90, '아콘스톨', '2241078', 4.8),
(91, '스타벅스 연대동문점', '1836021', 3.3),
(92, '북아현로69초밥', '2371031', 4.3),
(93, '스타벅스 연대동문점', '2131007', 4.5),
(94, '북아현로69초밥', '1971028', 5.0),
(95, '아콘스톨', '2051013', 4.6),
(96, '사장님돈까스', '2261026', 4.5),
(97, '스타벅스 연대동문점', '2451033', 5.0),
(98, '아콘스톨', '2033056', 5.0),
(99, '스타벅스 연대동문점', '2103090', 4.2);


# Bookmark table

insert into DB2024_bookmark values('심플리스트', '1971028');
insert into DB2024_bookmark values('세아짬뽕', '2171046');
insert into DB2024_bookmark values('위블레', '1836021');

insert into DB2024_bookmark values('쵸이피자', '2371031');
insert into DB2024_bookmark values('반타이쏘이54', '2241078');
insert into DB2024_bookmark values('가미분식', '2033056');

insert into DB2024_bookmark values('북아현로69초밥', '2051013');
insert into DB2024_bookmark values('구일당', '2451033');
insert into DB2024_bookmark values('포포나무', '2032005');

insert into DB2024_bookmark values('스탠바이키친', '2450021');
insert into DB2024_bookmark values('딸기골', '2131007');
insert into DB2024_bookmark values('존재의 이유', '2208201');

