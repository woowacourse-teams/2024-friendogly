INSERT INTO member(name, tag, email)
VALUES ('도도', '4e52d416', 'dodo@test.com'),
       ('땡이', 'a582a275', 'jiho@test.com'),
       ('트레', '68fc8014', 'tre@test.com'),
       ('위브', '3dde7373', 'wiib@test.com'),
       ('벼리', '525ec19f', 'byeori@test.com'),
       ('누누', '5f0f8307', 'nunu@test.com'),
       ('채드', '114d8979', 'ched@test.com'),
       ('에디', 'c065a053', 'edy@test.com');

INSERT INTO footprint(member_id, latitude, longitude, created_at, is_deleted)
VALUES (1, 37.5173316, 127.1011661, TIMESTAMPADD(MINUTE, -10, NOW()), FALSE),
       (2, 37.5185122, 127.098778, TIMESTAMPADD(MINUTE, -20, NOW()), FALSE),
       (3, 37.5191964, 127.1055562, TIMESTAMPADD(MINUTE, -30, NOW()), FALSE),
       (4, 37.5136533, 127.0983182, TIMESTAMPADD(MINUTE, -40, NOW()), FALSE),
       (5, 37.5131474, 127.1042528, TIMESTAMPADD(MINUTE, -50, NOW()), FALSE),
       (6, 37.5171728, 127.1047797, TIMESTAMPADD(MINUTE, -60, NOW()), FALSE),
       (7, 37.516183, 127.1068874, TIMESTAMPADD(MINUTE, -70, NOW()), FALSE);

INSERT INTO pet(member_id, name, description, birth_date, size_type, gender, image_url)
VALUES (1, '귀요미', '제 이름은 귀요미입니다', '2010-04-01', 'SMALL', 'MALE_NEUTERED', 'https://pds.joongang.co.kr/news/component/htmlphoto_mmdata/201901/20/28017477-0365-4a43-b546-008b603da621.jpg'),
       (2, '멍뭉이', '우리 멍뭉이 귀엽죠?', '2011-05-01', 'MEDIUM', 'FEMALE', 'https://img.freepik.com/premium-photo/cute-puppy_976589-177.jpg?w=1060'),
       (3, '보리', '보리보리 쌀', '2021-11-01', 'LARGE', 'MALE_NEUTERED', 'https://img.extmovie.com/files/attach/images/148/921/189/074/4e27f0d91051df9ba456302557c78977.jpg'),
       (4, '누누', '우리 누누는 최고야', '2013-08-01', 'SMALL', 'MALE', 'https://img.extmovie.com/files/attach/images/148/921/189/074/636c2d09b8613b2562070e0cff068875.jpg'),
       (5, '윌럼프', '누누의 펫입니다', '2014-09-01', 'MEDIUM', 'FEMALE_NEUTERED', 'https://img.extmovie.com/files/attach/images/148/921/189/074/067bc1139ae789ca1fb8d7776787afd1.jpg'),
       (6, '밥톨이', '밥토리토리', '2015-10-01', 'LARGE', 'MALE', 'https://img.extmovie.com/files/attach/images/148/921/189/074/e3e776af3209b60f127d6b393b8e5600.jpg'),
       (7, '케이틀린', '헤드샷', '2016-11-01', 'SMALL', 'MALE_NEUTERED', 'https://img.extmovie.com/files/attach/images/148/921/189/074/20449117cdbb0d551bce93f1d67833e2.jpg'),
       (8, '이즈리얼', 'ezreal', '2017-12-01', 'MEDIUM', 'FEMALE', 'https://sitem.ssgcdn.com/13/03/75/item/1000438750313_i1_1200.jpg');
