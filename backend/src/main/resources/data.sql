INSERT INTO member(name, tag, image_url)
VALUES ('도도', '4e52d416', 'https://avatars.githubusercontent.com/u/79188587?v=4'),
       ('땡이', 'a582a275', 'https://avatars.githubusercontent.com/u/110461155?v=4'),
       ('트레', '68fc8014', ''),
       ('위브', '3dde7373', 'https://avatars.githubusercontent.com/u/28584160?v=4'),
       ('벼리', '525ec19f', ''),
       ('누누', '5f0f8307', ''),
       ('채드', '114d8979', 'https://avatars.githubusercontent.com/u/102402485?v=4'),
       ('에디', 'c065a053', '');

INSERT INTO device_token(member_id, device_token)
VALUES (1,'a'),
       (2,'a'),
       (3,'a'),
       (4,'a'),
       (5,'a'),
       (6,'a'),
       (7,'a'),
       (8,'a');

INSERT INTO footprint(member_id, latitude, longitude, walk_status, created_at, is_deleted)
VALUES (1, 37.5173316, 127.1011661, 'BEFORE', TIMESTAMPADD(MINUTE, -10, NOW()), FALSE),
       (4, 37.5136533, 127.0983182, 'BEFORE', TIMESTAMPADD(MINUTE, -40, NOW()), FALSE);

INSERT INTO footprint(member_id, latitude, longitude, walk_status, start_walk_time, created_at, is_deleted)
VALUES (2, 37.5185122, 127.098778, 'ONGOING', TIMESTAMPADD(MINUTE, -20, NOW()), TIMESTAMPADD(MINUTE, -30, NOW()), FALSE),
       (5, 37.5131474, 127.1042528, 'ONGOING', TIMESTAMPADD(MINUTE, -50, NOW()), TIMESTAMPADD(MINUTE, -70, NOW()), FALSE),
       (7, 37.516183, 127.1068874, 'ONGOING', TIMESTAMPADD(MINUTE, -70, NOW()), TIMESTAMPADD(MINUTE, -90, NOW()), FALSE);

INSERT INTO footprint(member_id, latitude, longitude, walk_status, start_walk_time, end_walk_time, created_at, is_deleted)
VALUES (3, 37.5191964, 127.1055562, 'AFTER', TIMESTAMPADD(MINUTE, -30, NOW()), TIMESTAMPADD(MINUTE, -20, NOW()), TIMESTAMPADD(MINUTE, -50, NOW()), FALSE),
       (6, 37.5171728, 127.1047797, 'AFTER', TIMESTAMPADD(MINUTE, -60, NOW()), TIMESTAMPADD(MINUTE, -20, NOW()), TIMESTAMPADD(MINUTE, -90, NOW()), FALSE);

INSERT INTO pet(member_id, name, description, birth_date, size_type, gender, image_url)
VALUES (1, '피스타', '곰돌이 컷 원조가 저에요~!', '2010-04-01', 'SMALL', 'MALE_NEUTERED',
        'https://flexible.img.hani.co.kr/flexible/normal/960/960/imgdb/resize/2019/0121/00501111_20190121.JPG'),
       (1, '치오', '간식 주면 달려가요', '2015-04-01', 'SMALL', 'FEMALE',
        'https://i.pinimg.com/564x/d1/62/cb/d162cb12dfa0011a7bd67188a14d661c.jpg'),
       (2, '치즈', '첫째!', '2011-05-01', 'MEDIUM', 'FEMALE',
        'https://product.cdn.cevaws.com/var/storage/images/media/adaptil-2017/images/www-ww/shutterstock_395310793-3-2/3547034-1-www-WW/shutterstock_395310793-3-2.jpg'),
       (2, '진돌이', '둘째!', '2019-06-01', 'MEDIUM', 'MALE',
        'https://i.pinimg.com/564x/96/d4/43/96d443c92059f2b3a240a7ff74692bbf.jpg'),
       (2, '진솔이', '셋째!', '2020-07-01', 'MEDIUM', 'MALE',
        'https://i.pinimg.com/564x/08/01/67/080167a359545bc40068045f984a9994.jpg'),
       (3, '라떼', '겁이 많아요! 개껌 좋아해요', '2021-11-01', 'LARGE', 'MALE_NEUTERED',
        'https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg'),
       (4, '누누', '완전E에요 친구만들고싶어해요!', '2013-08-01', 'SMALL', 'MALE',
        'https://shop.peopet.co.kr/data/goods/388/2022/06/_temp_16557127733930view.jpg'),
       (4, '푸푸', '가끔 빤히 쳐다봐요', '2016-08-01', 'SMALL', 'MALE_NEUTERED',
        'https://i.pinimg.com/564x/f0/82/3c/f0823c54bb92aec76f2959d88a0c42a5.jpg'),
       (5, '두부', '만나면 인사해주세요!', '2014-09-01', 'MEDIUM', 'FEMALE_NEUTERED',
        'https://mblogthumb-phinf.pstatic.net/MjAxNzA0MjhfMTU5/MDAxNDkzMzQzOTgwNjI2.Da3oF-lT1Tq8Y2zhYhlGi6kvIjZa6YOgYy-zVWfjwBkg.r-zq73hAyuL5rkP1B8J2qveAQLapdfGiuNlxEDlVH84g.PNG.fjvfeewt/20170428_104410.png?type=w800'),
       (6, '호두', '달리기를 정말 좋아해요', '2015-10-01', 'LARGE', 'MALE',
        'https://cdn.pixabay.com/photo/2015/03/14/05/37/portrait-672799_1280.jpg'),
       (6, '땅콩', '땅콩 좋아해요', '2015-10-01', 'LARGE', 'MALE',
        'https://i.pinimg.com/564x/f4/18/f2/f418f20d23c55a6522a712d46f06267e.jpg'),
       (6, '밤', '밤을 좋아해요', '2015-10-01', 'LARGE', 'MALE',
        'https://i.pinimg.com/564x/6e/a1/27/6ea127df2927d71a4224a04e3da882fc.jpg'),
       (7, '모카', '어려서 여러강아지 만나고싶어요', '2022-11-01', 'SMALL', 'MALE_NEUTERED',
        'https://mblogthumb-phinf.pstatic.net/MjAxNzA0MjhfMjg5/MDAxNDkzMzQzOTgxMDQy.4u283s2rrib7xSWR5IdL1r_yiipEITnM6VofjaJsZPUg.oTUM6w8LeF-orpapq_S8j45mjRxjofwrpq8jhQ5LC5Eg.PNG.fjvfeewt/20170428_104509.png?type=w800'),
       (8, '초코', '매일 저녁에 나와요!', '2017-12-01', 'MEDIUM', 'FEMALE',
        'https://cdn.pixabay.com/photo/2019/12/08/21/09/animal-4682251_960_720.jpg');

INSERT INTO chat_room (member_capacity, chat_room_type)
VALUES (5, 'GROUP'),
       (5, 'GROUP'),
       (2, 'PRIVATE'), -- 개인 채팅방
       (2, 'PRIVATE'); -- 개인 채팅방

INSERT INTO chat_room_member (chat_room_id, member_id, created_at)
VALUES (3, 1, '2023-08-03 12:00:00'),
       (3, 2, '2023-08-03 12:00:00'),
       (4, 3, '2023-08-03 12:00:00'),
       (4, 4, '2023-08-03 12:00:00');

INSERT INTO club (title, content, member_capacity, province,city,village, image_url, created_at, status, chat_room_id)
VALUES ('전국구 강아지 모임', '주먹이 가장 매운 강이지 모임', 3, '서울특별시', '송파구', '신천동', 'https://cdn.imweb.me/thumbnail/20240214/0b725e3e8a959.jpg', '2023-08-01 12:00:00', 'OPEN', 1),
       ('미녀 강아지 모임', '예쁜 강아지 모임', 5, '서울특별시', '송파구', '신천동', 'https://images.mypetlife.co.kr/content/uploads/2023/04/18094901/xuan-nguyen-zr0beNrnvgQ-unsplash-768x512-1.jpg', '2023-07-31 01:00:00', 'OPEN', 2);

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

INSERT INTO playground (latitude, longitude)
VALUES (37.514062, 127.100972),
        (37.520740, 127.121328);

INSERT INTO playground_member (playground_id, member_id, message, is_inside, exit_time)
VALUES (1, 2, '강아지 3마리 보유', true, null),
        (1, 1, '강아지 2마리 보유', false, null),
        (2, 3, '강아지 1마리 보유' ,true, null);
