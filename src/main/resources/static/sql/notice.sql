-- 시퀀스 삭제
DROP SEQUENCE INQUIRY_SEQ;
DROP SEQUENCE INQUIRY_ANSWER_SEQ;
DROP SEQUENCE FAQ_CA_SEQ;
DROP SEQUENCE FAQ_SEQ;
DROP SEQUENCE NOTICE_SEQ;

-- 시퀀스 생성
CREATE SEQUENCE INQUIRY_SEQ        NOCACHE; 
CREATE SEQUENCE INQUIRY_ANSWER_SEQ NOCACHE;
CREATE SEQUENCE FAQ_CA_SEQ         NOCACHE;
CREATE SEQUENCE FAQ_SEQ            NOCACHE;
CREATE SEQUENCE NOTICE_SEQ         NOCACHE;

-- 테이블 삭제
DROP TABLE INQUIRY_T;
DROP TABLE INQUIRY_ANSWER_T;
DROP TABLE FAQ_CA_T;
DROP TABLE FAQ_T;
DROP TABLE NOTICE_T;

-- 테이블 생성

-- 1:1문의 테이블
CREATE TABLE INQUIRY_T(
  INQUIRY_NO    NUMBER              NOT NULL,           -- 문의 번호
  USER_NO       NUMBER              NOT NULL,           -- 회원 번호
  TITLE         VARCHAR2(100 BYTE) ,                    -- 제목
  CONTENTS      CLOB ,                                  -- 내용
  CREATED_AT    VARCHAR2(50 BYTE),                      -- 작성일
  SEPARATE      VARCHAR2(50 BYTE),                      -- 분류
  CONSTRAINT PK_INQUIRY_T PRIMARY KEY(INQUIRY_NO)
);

-- 1:1문의 답변테이블
CREATE TABLE INQUIRY_ANSWER_T(
  ANSWER_NO     NUMBER              NOT NULL,           -- 답변 번호
  USER_NO       NUMBER              NOT NULL,           -- 회원 번호
  INQUIRY_NO    NUMBER              NOT NULL,           -- 문의 번호
  CONTENTS       CLOB,                                  -- 내용
  CREATED_AT    VARCHAR2(50 BYTE),                      -- 작성일
  CONSTRAINT PK_INQUIRY_ANSWER_T PRIMARY KEY(ANSWER_NO)
);

-- 자주묻는 질문 카테고리 테이블
CREATE TABLE FAQ_CA_T(
  CA_NO         NUMBER              NOT NULL,           -- 카테고리 번호       
  CA_TITLE      VARCHAR2(100 BYTE),                     -- 카테고리명      
  CONSTRAINT PK_FAQ_CA_T PRIMARY KEY(CA_NO)
);

-- 자주묻는 질문 테이블
CREATE TABLE FAQ_T(
  FAQ_NO        NUMBER              NOT NULL,           -- 글번호
  CA_NO         NUMBER              NOT NULL,           -- 카테고리 번호
  TITLE         VARCHAR2(100 BYTE),                     -- 카테고리 제목
  CONTENTS      CLOB,                                   -- 내용
  CREATED_AT    VARCHAR2(50 BYTE),                      -- 작성일
  MODIFIED_AT   VARCHAR2(50 BYTE),                      -- 수정일
  CONSTRAINT PK_FAQ_T PRIMARY KEY(FAQ_NO)
);

-- 공지사항 테이블
CREATE TABLE NOTICE_T (
    NOTICE_NO       NUMBER               NOT NULL,      -- 공지 번호
    TITLE           VARCHAR2(100 BYTE),                 -- 공지 제목
    CONTENTS        CLOB,                               -- 공지 내용
    CREATED_AT      VARCHAR2(100 BYTE),                 -- 공지 작성일
    MODIFIED_AT     VARCHAR2(100 BYTE),                 -- 공지 수정일
    CONSTRAINT PK_NOTICE_T PRIMARY KEY(NOTICE_NO)
);






-- ****************************테스트다*************************************

--  문의 삽입 테스트 
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 11, '팔렸어요',   '나야나',   '2000/01/01', '패키지ㅇㅇ');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 12, '왜팔렸어요', '강다니엘', '2000/01/02', '패키지');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 13, ' 당장 내눈앞에', 'ㅎㅎ', '2000/01/03', '패키지');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 14, '가지고',         '조권', '2000/01/04', '패키지');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 14, '오세요',        '송지효', '2000/01/04', '패키지');



-- 문의 답변 테스트
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 21, 31, '배송중이에요', '2001/02/01/');
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 22, 32, '배송중이에요', '2001/03/01/');
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 23, 33, '배송중이에요', '2001/04/01/');
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 24, 34, '배송중이에요', '2001/05/01/');
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 25, 35, '배송중이에요', '2001/06/01/');

-- 자주묻는 질문 카테고리 테스트
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '솔드아웃인가요');
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '흐아아아아아아아아아');
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '현기증나요');
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '으아으ㅏ으아ㅡ아ㅡ아으ㅏ으ㅏ');
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '안돼에에에에에에에에에에[ㅇ');

-- 자주묻는 질문 테스트
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 41, '환불수수료가 어떻게되나요?', '출발 7일전....', '2002/01/01', '2003/01/01');
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 42, '지각할경우는요?',            '걸어오세요',     '2002/02/01', '2003/02/01');
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 43, '코로나걸렸을경우에는요',     '코로나일경우..', '2002/03/01', '2003/03/01');
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 44, '제일 럭셔리한 상품 추천해주세요',    '글쎼요', '2002/04/01', '2003/04/01');
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 45, '천재지변은요?',              '환불 쌉가능',    '2002/05/01', '2003/05/01');


-- 공지사항 테스트

INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '가산', '구디아카데미', TO_DATE('20100101','YYYY-MM-DD'), TO_DATE('20110101','YYYY-MM-DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '광명', '사거리'      , TO_DATE('20120101','YYYY-MM-DD'), TO_DATE('20130101','YYYY-MM-DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '가리', '봉',           TO_DATE('20140101','YYYY-MM-DD'), TO_DATE('20150101','YYYY-MM-DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '개봉', '사거리',       TO_DATE('20160101','YYYY-MM-DD'), TO_DATE('20170101','YYYY-MM-DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '부천', '성모병원',     TO_DATE('20180101','YYYY-MM-DD'), TO_DATE('20190101','YYYY-MM-DD')); 
COMMIT;


