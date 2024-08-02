INSERT INTO member(name, tag, email, image_url)
VALUES ('도도', '4e52d416', 'dodo@test.com', 'https://picsum.photos/100'),
       ('땡이', 'a582a275', 'jiho@test.com', 'https://picsum.photos/100'),
       ('트레', '68fc8014', 'tre@test.com', null),
       ('위브', '3dde7373', 'wiib@test.com', 'https://picsum.photos/100'),
       ('벼리', '525ec19f', 'byeori@test.com', null),
       ('누누', '5f0f8307', 'nunu@test.com', null),
       ('채드', '114d8979', 'ched@test.com', 'https://picsum.photos/100'),
       ('에디', 'c065a053', 'edy@test.com', null);

INSERT INTO footprint(member_id, latitude, longitude, walk_status, created_at, is_deleted)
VALUES (1, 37.5173316, 127.1011661, 'BEFORE', TIMESTAMPADD(MINUTE, -10, NOW()), FALSE),
       (2, 37.5185122, 127.098778, 'BEFORE', TIMESTAMPADD(MINUTE, -20, NOW()), FALSE),
       (3, 37.5191964, 127.1055562, 'BEFORE', TIMESTAMPADD(MINUTE, -30, NOW()), FALSE),
       (4, 37.5136533, 127.0983182, 'BEFORE', TIMESTAMPADD(MINUTE, -40, NOW()), FALSE),
       (5, 37.5131474, 127.1042528, 'BEFORE', TIMESTAMPADD(MINUTE, -50, NOW()), FALSE),
       (6, 37.5171728, 127.1047797, 'BEFORE', TIMESTAMPADD(MINUTE, -60, NOW()), FALSE),
       (7, 37.516183, 127.1068874, 'BEFORE', TIMESTAMPADD(MINUTE, -70, NOW()), FALSE);

INSERT INTO pet(member_id, name, description, birth_date, size_type, gender, image_url)
VALUES (1, '부', '곰돌이 컷 원조가 저에요~!', '2010-04-01', 'SMALL', 'MALE_NEUTERED',
        'https://flexible.img.hani.co.kr/flexible/normal/960/960/imgdb/resize/2019/0121/00501111_20190121.JPG'),
       (2, '치즈', '사랑받는 걸 좋아해요!인사해주세요!', '2011-05-01', 'MEDIUM', 'FEMALE',
        'https://product.cdn.cevaws.com/var/storage/images/media/adaptil-2017/images/www-ww/shutterstock_395310793-3-2/3547034-1-www-WW/shutterstock_395310793-3-2.jpg'),
       (3, '라떼', '겁이 많아요! 개껌 좋아해요', '2021-11-01', 'LARGE', 'MALE_NEUTERED',
        'https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg'),
       (4, '누누', '완전E에요 친구만들고싶어해요!', '2013-08-01', 'SMALL', 'MALE',
        'https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg'),
       (5, '두부', '만나면 인사해주세요!', '2014-09-01', 'MEDIUM', 'FEMALE_NEUTERED',
        'https://mblogthumb-phinf.pstatic.net/MjAxNzA0MjhfMTU5/MDAxNDkzMzQzOTgwNjI2.Da3oF-lT1Tq8Y2zhYhlGi6kvIjZa6YOgYy-zVWfjwBkg.r-zq73hAyuL5rkP1B8J2qveAQLapdfGiuNlxEDlVH84g.PNG.fjvfeewt/20170428_104410.png?type=w800'),
       (6, '호두', '달리기를 정말 좋아해요', '2015-10-01', 'LARGE', 'MALE',
        'https://cdn.pixabay.com/photo/2015/03/14/05/37/portrait-672799_1280.jpg'),
       (7, '모카', '어려서 여러강아지 만나고싶어요', '2022-11-01', 'SMALL', 'MALE_NEUTERED',
        'https://mblogthumb-phinf.pstatic.net/MjAxNzA0MjhfMjg5/MDAxNDkzMzQzOTgxMDQy.4u283s2rrib7xSWR5IdL1r_yiipEITnM6VofjaJsZPUg.oTUM6w8LeF-orpapq_S8j45mjRxjofwrpq8jhQ5LC5Eg.PNG.fjvfeewt/20170428_104509.png?type=w800'),
       (8, '초코', '매일 저녁에 나와요!', '2017-12-01', 'MEDIUM', 'FEMALE',
        'https://cdn.pixabay.com/photo/2019/12/08/21/09/animal-4682251_960_720.jpg');

INSERT INTO chat_room (id)
VALUES (1),
       (2);

INSERT INTO chat_room_member (chat_room_id, member_id, created_at)
VALUES (1, 6, '2023-08-01 12:00:00'),
       (2, 5, '2023-08-02 12:00:00'),
       (2, 7, '2023-08-03 12:00:00');

INSERT INTO club (title, content, member_capacity, address, image_url, created_at, status, chat_room_id)
VALUES ('전국구 강아지 모임', '주먹이 가장 매운 강이지 모임', 3, '서울 송파구 신천동', 'http://example.com/image.jpg',
        '2023-08-01 12:00:00', 'OPEN', 1),
       ('미녀 강아지 모임', '예쁜 강아지 모임', 5, '서울 송파구 신천동', 'http://example.com/image.jpg',
        '2023-07-31 01:00:00', 'OPEN', 2);

INSERT INTO club_gender (club_id, allowed_gender)
VALUES (1, 'MALE'),
       (1, 'FEMALE'),
       (2, 'MALE_NEUTERED'),
       (2, 'FEMALE_NEUTERED');

INSERT INTO club_size (club_id, allowed_size)
VALUES (1, 'LARGE'),
       (2, 'SMALL'),
       (2, 'MEDIUM');

INSERT INTO club_member (club_id, member_id, created_at)
VALUES (1, 6, '2023-07-31 01:00:00'),
       (2, 5, '2023-07-31 01:00:00'),
       (2, 7, '2023-07-31 02:00:00');

INSERT INTO club_pet (club_id, pet_id)
VALUES (1, 6),
       (2, 5),
       (2, 7);
