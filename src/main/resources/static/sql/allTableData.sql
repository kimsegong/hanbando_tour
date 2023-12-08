-- ******************************************************************************
-- 시퀀스 삭제
DROP SEQUENCE USER_SEQ;

DROP SEQUENCE REGION_SEQ;
DROP SEQUENCE THEME_SEQ;
DROP SEQUENCE PACKAGE_SEQ;
DROP SEQUENCE PRODUCT_IMAGE_SEQ;

DROP SEQUENCE BANNER_SEQUENCE;

DROP SEQUENCE HOTEL_SEQUENCE;
DROP SEQUENCE ROOM_SEQUENCE;

DROP SEQUENCE PAYMENT_SEQ;
DROP SEQUENCE TOURIST_SEQ;
DROP SEQUENCE RESERVE_SEQ;

DROP SEQUENCE HEART_SEQ;

DROP SEQUENCE REVIEW_SEQ;

DROP SEQUENCE NOTICE_SEQ;
DROP SEQUENCE INQUIRY_SEQ;
DROP SEQUENCE INQUIRY_ANSWER_SEQ;
DROP SEQUENCE FAQ_CA_SEQ;
DROP SEQUENCE FAQ_SEQ;


-- ******************************************************************************
-- 시퀀스 생성
CREATE SEQUENCE USER_SEQ NOCACHE;

CREATE SEQUENCE REGION_SEQ  NOCACHE;
CREATE SEQUENCE THEME_SEQ   NOCACHE;
CREATE SEQUENCE PACKAGE_SEQ NOCACHE;
CREATE SEQUENCE PRODUCT_IMAGE_SEQ NOCACHE;

CREATE SEQUENCE BANNER_SEQUENCE NOCACHE;

CREATE SEQUENCE HOTEL_SEQUENCE NOCACHE;
CREATE SEQUENCE ROOM_SEQUENCE  NOCACHE;

CREATE SEQUENCE RESERVE_SEQ NOCACHE;
CREATE SEQUENCE TOURIST_SEQ NOCACHE;
CREATE SEQUENCE PAYMENT_SEQ NOCACHE;

CREATE SEQUENCE HEART_SEQ         NOCACHE;

CREATE SEQUENCE REVIEW_SEQ NOCACHE;

CREATE SEQUENCE NOTICE_SEQ         NOCACHE;
CREATE SEQUENCE INQUIRY_SEQ        NOCACHE; 
CREATE SEQUENCE INQUIRY_ANSWER_SEQ NOCACHE;
CREATE SEQUENCE FAQ_CA_SEQ         NOCACHE;
CREATE SEQUENCE FAQ_SEQ            NOCACHE;


-- ******************************************************************************
-- 테이블 삭제

-- 공지 테이블 삭제
DROP TABLE NOTICE_T;

-- 문의 관련 테이블 삭제
DROP TABLE FAQ_T;
DROP TABLE FAQ_CA_T;
DROP TABLE INQUIRY_ANSWER_T;
DROP TABLE INQUIRY_T;

-- 리뷰 테이블 삭제
DROP TABLE REVIEW_T;

-- 찜 테이블 삭제
DROP TABLE HEART_T;

-- 예약/결제 테이블 삭제
DROP TABLE PAYMENT_T;
DROP TABLE TOURIST_T;
DROP TABLE RESERVE_T;

-- 호텔 관련 테이블 삭제
DROP TABLE ROOMPRICE_T;
DROP TABLE ROOM_FEATURE_T;
DROP TABLE HOTEL_IMAGE_T;
DROP TABLE ROOMTYPE_T;	
DROP TABLE FACILITIES_T;		
DROP TABLE HOTEL_T;	

-- 베너 테이블 삭제
DROP TABLE BANNER_IMAGE_T;

-- 패키지 관련 테이블 삭제
DROP TABLE PRODUCT_IMAGE_T;
DROP TABLE PACKAGE_T;
DROP TABLE THEME_T;
DROP TABLE REGION_T;

-- 회원 관련 테이블 삭제
DROP TABLE INACTIVE_USER_T;
DROP TABLE LEAVE_USER_T;
DROP TABLE ACCESS_T;
DROP TABLE USER_T;


-- ******************************************************************************
-- 테이블 생성
-- ************************************ 회원 ************************************

-- 가입한 사용자
CREATE TABLE USER_T (
    USER_NO        NUMBER              NOT NULL,         -- 회원 번호 (PK)
    EMAIL          VARCHAR2(100 BYTE)  NOT NULL UNIQUE,  -- 이메일을 아이디로 사용
    PW             VARCHAR2(64 BYTE)   NULL,             -- SHA-256 암호화 방식 사용
    NAME           VARCHAR2(50 BYTE)   NULL,             -- 이름
    GENDER         VARCHAR2(2 BYTE)    NULL,             -- M, F, NO
    MOBILE         VARCHAR2(15 BYTE)   NULL,             -- 하이픈 제거 후 저장
    POSTCODE       VARCHAR2(5 BYTE)    NULL,             -- 우편번호
    ROAD_ADDRESS   VARCHAR2(100 BYTE)  NULL,             -- 도로명주소
    JIBUN_ADDRESS  VARCHAR2(100 BYTE)  NULL,             -- 지번주소
    DETAIL_ADDRESS VARCHAR2(100 BYTE)  NULL,             -- 상세주소
    AGREE          NUMBER              NOT NULL,         -- 서비스 동의 여부(0:필수, 1:이벤트)
    STATE          NUMBER              NULL,             -- (간편로그인)가입형태(0:일반, 1:네이버, 2:카카오)
    AUTH           NUMBER              NULL,             -- 사용자 권한 (0:관리자, 1:회원)
    PW_MODIFIED_AT VARCHAR2(30 BYTE)   NULL,             -- 비밀번호 수정일
    JOINED_AT      VARCHAR2(30 BYTE)   NULL,             -- 가입일
    CONSTRAINT PK_USER PRIMARY KEY(USER_NO)
);





-- 접속 기록
CREATE TABLE ACCESS_T (
    EMAIL    VARCHAR2(100 BYTE) NOT NULL,  -- 접속한 회원 (PK/FK)
    LOGIN_AT VARCHAR2(30 BYTE)  NULL,      -- 로그인 일시
    CONSTRAINT FK_USER_ACCESS FOREIGN KEY(EMAIL) REFERENCES USER_T(EMAIL) ON DELETE CASCADE
);
    
-- 탈퇴한 사용자
CREATE TABLE LEAVE_USER_T (
    EMAIL        VARCHAR2(100 BYTE) NOT NULL,  -- 탈퇴회원 이메일 (PK)
    JOINED_AT    VARCHAR2(30 BYTE)  NULL,      -- 가입일
    LEAVED_AT    VARCHAR2(30 BYTE)  NULL,      -- 탈퇴일
    CONSTRAINT PK_LEAVE PRIMARY KEY(EMAIL)
);

-- 휴면 사용자 (1년 이상 접속 기록이 없으면 휴면 처리)
CREATE TABLE INACTIVE_USER_T (
    USER_NO        NUMBER              NOT NULL,         -- 휴면회원 번호 (PK)
    EMAIL          VARCHAR2(100 BYTE)  NOT NULL UNIQUE,  -- 이메일을 아이디로 사용
    PW             VARCHAR2(64 BYTE)   NULL,             -- SHA-256 암호화 방식 사용
    NAME           VARCHAR2(50 BYTE)   NULL,             -- 이름
    GENDER         VARCHAR2(2 BYTE)    NULL,             -- M, F, NO
    MOBILE         VARCHAR2(15 BYTE)   NULL,             -- 하이픈 제거 후 저장
    POSTCODE       VARCHAR2(5 BYTE)    NULL,             -- 우편번호
    ROAD_ADDRESS   VARCHAR2(100 BYTE)  NULL,             -- 도로명주소
    JIBUN_ADDRESS  VARCHAR2(100 BYTE)  NULL,             -- 지번주소
    DETAIL_ADDRESS VARCHAR2(100 BYTE)  NULL,             -- 상세주소
    AGREE          NUMBER              NOT NULL,         -- 서비스 동의 여부(0:필수, 1:이벤트)
    STATE          NUMBER              NULL,             -- 가입형태(0:정상, 1:네이버, 2:카카오)
    AUTH           NUMBER              NULL,             -- 사용자 권한 (관리자:0, 회원:1)
    PW_MODIFIED_AT VARCHAR2(30 BYTE)   NULL,             -- 비밀번호 수정일
    JOINED_AT      VARCHAR2(30 BYTE)   NULL,             -- 가입일
    INACTIVED_AT   VARCHAR2(30 BYTE)   NULL,             -- 휴면처리일
    CONSTRAINT PK_INACTIVE_USER PRIMARY KEY(USER_NO)
);


-- ************************************ 지역 ************************************
-- 지역 카테고리 테이블
CREATE TABLE REGION_T (
    REGION_NO   NUMBER             NOT NULL,  -- 지역 번호 (PK)
    REGION_NAME VARCHAR2(255 BYTE) NULL,      -- 지역명
    CONSTRAINT PK_REGION PRIMARY KEY(REGION_NO)
);

-- ************************************ 테마 ************************************
-- 테마 카테고리 테이블
CREATE TABLE THEME_T (
    THEME_NO   NUMBER             NOT NULL,  -- 테마 번호 (PK)
    THEME_NAME VARCHAR2(255 BYTE) NULL,      -- 테마명
    CONSTRAINT PK_THEME PRIMARY KEY(THEME_NO)
);

-- ************************************ 패키지 상품 *****************************
-- 패키지 상품 테이블
CREATE TABLE PACKAGE_T (
	PACKAGE_NO	        NUMBER	            NOT NULL,  -- 패키지 번호              (PK)
	REGION_NO	        NUMBER	            NULL,      -- 지역 번호                (FK)
	THEME_NO	        NUMBER	            NULL,      -- 테마 번호                (FK)
	PACKAGE_TITLE	    VARCHAR2(150 BYTE)	NOT NULL,  -- 패키지 이름
    MINI_ONE	        VARCHAR2(150 BYTE)	NULL,      -- 패키지 작은 설명1
	MINI_TWO	        VARCHAR2(150 BYTE)	NULL,      -- 패키지 작은 설명2
	MINI_THREE	        VARCHAR2(150 BYTE)	NULL,      -- 패키지 작은 설명3
	PACKAGE_PLAN	    CLOB            	NULL,      -- 패키지 계획
	PACKAGE_CONTENTS	CLOB	            NULL,      -- 패키지 내용
	HOTEL_CONTENTS	    CLOB	            NULL,      -- 호텔 상세
	PRICE	            NUMBER	            NULL,      -- 가격
	DANGER	            CLOB             	NULL,      -- 여행참고사항
	CREATED_AT	        VARCHAR2(100 BYTE)	NULL,      -- 시작일
	MODIFIED_AT	        VARCHAR2(100 BYTE)	NULL,      -- 수정일
	HIT             	NUMBER	            DEFAULT 0, -- 조회수
	STATUS	            NUMBER	            NULL,      -- 상품 상태 (예약가능여부, 0:예약불가, 1:예약가능)
	MAX_PEOPLE	        NUMBER	            NULL,      -- 최대 인원수
	RECOMMEND_STATUS	NUMBER	            NULL,      -- 추천상태  (0:비추천, 1:추천)
    CONSTRAINT PK_PACKAGE          PRIMARY KEY(PACKAGE_NO),
    CONSTRAINT FK_REGION_PACKAGE   FOREIGN KEY(REGION_NO)   REFERENCES REGION_T(REGION_NO)     ON DELETE SET NULL,
    CONSTRAINT FK_THEME_PACKAGE    FOREIGN KEY(THEME_NO)    REFERENCES THEME_T(THEME_NO)       ON DELETE SET NULL
);

-- 패키지 상품 이미지 테이블
CREATE TABLE PRODUCT_IMAGE_T (
	IMAGE_NO	    NUMBER	            NOT NULL,  -- 이미지 번호  (PK)
	PACKAGE_NO	    NUMBER	            NULL,      -- 패키지 번호  (FK)
	THUMBNAIL	    NUMBER              DEFAULT 0, -- 썸네일
	FILESYSTEM_NAME	VARCHAR2(100 BYTE)	NOT NULL,  -- 파일이름
	IMAGE_PATH	    VARCHAR2(300 BYTE)	NOT NULL,  -- 파일경로
    CONSTRAINT PK_PRODUCT_IMAGE  PRIMARY KEY(IMAGE_NO),
    CONSTRAINT FK_PACKAGE_IMAGE  FOREIGN KEY(PACKAGE_NO)  REFERENCES PACKAGE_T(PACKAGE_NO) ON DELETE CASCADE
);

-- ************************************ 배너 ************************************
-- 메인 배너 테이블
CREATE TABLE BANNER_IMAGE_T (
   BANNER_NO        NUMBER                NOT NULL,    -- 배너 파일 번호  (PK)
   ORIGINAL_NAME    VARCHAR2(300 BYTE)    NOT NULL,    -- 원래 이름
   FILESYSTEM_NAME  VARCHAR2(300 BYTE)    NOT NULL,    -- 시스템 상 이름
   BANNER_PATH      VARCHAR2(300 BYTE)    NOT NULL,    -- 배너 경로 
   STATE            NUMBER                DEFAULT 0,   -- (0:게시 안함, 1:게시)
   LINKED_ADDRESS   VARCHAR2(300 BYTE)    DEFAULT '/', -- 클릭시 이동 경로 
   CONSTRAINT PK_BANNER PRIMARY KEY(BANNER_NO)
);


-- ************************************ 호텔 상품 *******************************
-- 호텔 테이블
CREATE TABLE HOTEL_T (
    HOTEL_NO           NUMBER               NOT NULL,  -- 호텔 번호  (PK)
    REGION_NO          NUMBER               NULL,      -- 지역 구분  (FK)
    HOTEL_NAME         VARCHAR2(150 BYTE)   NULL,      -- 호텔 명
    HOTEL_ADDRESS      VARCHAR2(255 BYTE)   NULL,      -- 주소
    LATITUDE           NUMBER               NULL,      -- 위도
    LONGITUDE          NUMBER               NULL,      -- 경도 
    HOTEL_DETAIL       CLOB                 NULL,      -- 설명
    PHONE_NUMBER       VARCHAR2(10 BYTE)    NULL,      -- 전화번호
    H_EMAIL            VARCHAR2(100 BYTE)   NULL,      -- 이메일
    CREATED_AT         VARCHAR2(100 BYTE)   NULL,      -- 작성일
    MODIFIED_AT        VARCHAR2(100 BYTE)   NULL,      -- 수정일
    HIT                NUMBER               DEFAULT 0, -- 조회수
    STATUS             NUMBER               NULL,      -- 판매중 (0:판매 안함, 1:판매)
    RECOMMEND_STATUS   NUMBER               NULL,      -- 추천 상태 (0:없음, 1:추천)
    CONSTRAINT PK_HOTEL        PRIMARY KEY(HOTEL_NO),
    CONSTRAINT FK_REGION_HOTEL FOREIGN KEY(REGION_NO) REFERENCES REGION_T(REGION_NO) ON DELETE SET NULL
);


-- 호텔 편의시설 테이블
CREATE TABLE FACILITIES_T (
    HOTEL_NO           NUMBER   NOT NULL, -- 호텔 번호   (PK/FK)
    POOL               NUMBER   NULL,     -- 수영장 (0:없음, 1:있음)
    MORNING            NUMBER   NULL,     -- 조식
    SAUNA              NUMBER   NULL,     -- 사우나
    LOUNGE             NUMBER   NULL,     -- 라운지
    ROOMSERVICE        NUMBER   NULL,     -- 룸서비스
    CONSTRAINT FK_HOFEL_FAC FOREIGN KEY(HOTEL_NO) REFERENCES HOTEL_T(HOTEL_NO) ON DELETE CASCADE
);


-- 호텔 객실타입 테이블
CREATE TABLE ROOMTYPE_T (
    ROOM_NO       NUMBER               NOT NULL,   -- 객실 번호  (PK)
    HOTEL_NO      NUMBER               NOT NULL,   -- 호텔 번호  (FK)
    ROOM_DETAIL   CLOB                 NULL,       -- 객실 설명
    ROOM_NAME     VARCHAR2(100 BYTE)   NULL,       -- 객실 이름
    ROOM_MANY     NUMBER               NULL,       -- 객실 개수 
    R_VIEW        VARCHAR2(100 BYTE)   NULL,       -- 시티뷰 오션뷰 
    BLEAKFAST     NUMBER               NULL,       -- 조식여부 
    SMOKE         NUMBER               NULL,       -- 흡연가능여부 (0:불가, 1:가능)
    PEOPLE        NUMBER               NULL,       -- 방 인원수  
    BED           VARCHAR2(100 BYTE)   NULL,       -- 침대 종류 
    SHOWER        VARCHAR2(100 BYTE)   NULL,       -- 샤워실인가 욕조인가 
    R_SIZE        NUMBER               NULL,       -- 방 크기
    CONSTRAINT PK_ROOM      PRIMARY KEY(ROOM_NO),
    CONSTRAINT FK_HOEL_ROOM FOREIGN KEY(HOTEL_NO) REFERENCES HOTEL_T(HOTEL_NO) ON DELETE CASCADE
);

-- 호텔, 객실 이미지 테이블
CREATE TABLE HOTEL_IMAGE_T (
    HOTEL_NO          NUMBER           NOT NULL,  -- 호텔 번호  (PK/FK)
    ROOM_NO           NUMBER           NULL,      -- 객실 번호  (FK)
    ORIGINAL_NAME     VARCHAR2(300)    NOT NULL,  -- 사진 원래 이름
    THUMBNAIL         NUMBER           NULL,      -- 썸네일 유무 (0:없음, 1:있음) 
    FILESYSTEM_NAME   VARCHAR2(300)    NOT NULL,  -- 시스템 이름
    IMAGE_PATH        VARCHAR2(300)    NOT NULL,  -- 경로 
    CONSTRAINT FK_HOTLE_IMGAE FOREIGN KEY(HOTEL_NO) REFERENCES HOTEL_T(HOTEL_NO) ON DELETE CASCADE,
    CONSTRAINT FK_ROOM_IMAGE  FOREIGN KEY(ROOM_NO) REFERENCES ROOMTYPE_T(ROOM_NO) ON DELETE CASCADE  
);

-- 객실 특성 테이블
CREATE TABLE ROOM_FEATURE_T (
    ROOM_NO    NUMBER      NOT NULL,  -- 객실 번호   (PK/FK)
    TOWEL      NUMBER      NULL,      -- 수건 (0:없음, 1:있음)
    WATER      NUMBER      NULL,      -- 생수
    COFFEE     NUMBER      NULL,      -- 커피/티
    DRIER      NUMBER      NULL,      -- 드라이기
    IRON       NUMBER      NULL,      -- 다리미
    MINIBAR    NUMBER      NULL,      -- 미니바
    CONSTRAINT FK_ROOM_FEATURE FOREIGN KEY(ROOM_NO) REFERENCES ROOMTYPE_T(ROOM_NO) ON DELETE CASCADE
);

-- 객실 기간 별 가격 테이블
CREATE TABLE ROOMPRICE_T (
    HOTEL_NO         NUMBER              NOT NULL,  -- 호텔번호    (FK)
    ROOM_NO          NUMBER              NOT NULL,  -- 객실번호    (FK)
    BI_PRICE         NUMBER              NULL,      -- 비성수기 금액
    BS_DATE          VARCHAR2(100 BYTE)  NULL,      -- 비성수기 시작
    BE_DATE          VARCHAR2(100 BYTE)  NULL,      -- 비성수기 끝
    JUN_PRICE        NUMBER              NULL,      -- 준성수기 금액
    JS_DATE          VARCHAR2(100 BYTE)  NULL,      -- 준성수기 시작
    JE_DATE          VARCHAR2(100 BYTE)  NULL,      -- 준성수기 끝
    SUNG_PRICE       NUMBER              NULL,      -- 비성수기 금액
    SS_DATE          VARCHAR2(100 BYTE)  NULL,      -- 성수기 시작
    SE_DATE          VARCHAR2(100 BYTE)  NULL,      -- 성수기 끝
    CONSTRAINT FK_HOTLE_PRICE FOREIGN KEY(HOTEL_NO) REFERENCES HOTEL_T(HOTEL_NO) ON DELETE CASCADE,
    CONSTRAINT FK_ROOM_PRICE FOREIGN KEY(ROOM_NO) REFERENCES ROOMTYPE_T(ROOM_NO) ON DELETE CASCADE
);

-- ************************************ 예약 ************************************
-- 예약 테이블
CREATE TABLE RESERVE_T (
    RESERVE_NO     NUMBER             NOT NULL,  -- 예약번호 (PK)
    RESERVED_AT    VARCHAR2(50 BYTE)  NOT NULL,  -- 예약일자
    REQUESTED_TERM VARCHAR2(200 BYTE) NULL,      -- 요청사항
    AGREE          NUMBER             NULL,      -- 약관동의여부 (0:필수, 1:선택)
    DEPARTURE_LOC  VARCHAR2(50 BYTE)  NULL,      -- 출발지
    RESERVE_STATUS NUMBER             NULL,      -- 예약상태 (0:대기, 1:완료, 2:취소)
    RESERVE_START  VARCHAR2(30 BYTE)  NULL,      -- 예약일정 시작일
    RESERVE_FINISH VARCHAR2(30 BYTE)  NULL,      -- 예약일정 종료일   
    RESERVE_PERSON NUMBER             NULL,      -- 예약인원수
    RESERVE_PRICE  NUMBER             NULL,      -- 예약가격
    USER_NO        NUMBER             NOT NULL,  -- 회원번호   (FK)
    PACKAGE_NO     NUMBER             NULL,      -- 패키지번호 (FK)
    HOTEL_NO       NUMBER             NULL,      -- 호텔번호   (FK)
    ROOM_NO        NUMBER             NULL,      -- 객실번호   (FK)
    CONSTRAINT PK_RES       PRIMARY KEY(RESERVE_NO),
    CONSTRAINT FK_USER_RES  FOREIGN KEY(USER_NO)    REFERENCES USER_T(USER_NO) ON DELETE CASCADE,
    CONSTRAINT FK_PACK_RES  FOREIGN KEY(PACKAGE_NO) REFERENCES PACKAGE_T(PACKAGE_NO) ON DELETE SET NULL,
    CONSTRAINT FK_HOTEL_RES FOREIGN KEY(HOTEL_NO)   REFERENCES HOTEL_T(HOTEL_NO) ON DELETE SET NULL,
    CONSTRAINT FK_ROOM_RES  FOREIGN KEY(ROOM_NO)    REFERENCES ROOMTYPE_T(ROOM_NO) ON DELETE SET NULL
);

-- 여행객 테이블
CREATE TABLE TOURIST_T (
    TOURIST_NO NUMBER            NOT NULL,  -- 여행객번호 (PK)
    NAME       VARCHAR2(20 BYTE) NULL,      -- 여행객이름
    BIRTH_DATE VARCHAR2(30 BYTE) NULL,      -- 생년월일
    GENDER     VARCHAR2(2 BYTE)  NULL,      -- 성별
    MOBILE     VARCHAR2(15 BYTE) NULL,      -- 연락처
    AGE_CASE   NUMBER            NULL,      -- 연령대 (0:성인, 1:소아)
    RESERVE_NO NUMBER            NOT NULL,  -- 예약번호 (FK)
    CONSTRAINT PK_TOUR     PRIMARY KEY(TOURIST_NO),
    CONSTRAINT FK_RES_TOUR FOREIGN KEY(RESERVE_NO) REFERENCES RESERVE_T(RESERVE_NO) ON DELETE CASCADE
);


-- ************************************ 결제 ************************************
-- 결제 테이블
CREATE TABLE PAYMENT_T (
    PAYMENT_NO    NUMBER             NOT NULL,  -- 결제번호 (PK)
    IMP_UID       VARCHAR2(30 BYTE)  NULL,      -- 결제고유번호
    PAY_YN        VARCHAR2(10 BYTE)  NULL,      -- 결제성공여부
    PAY_METHOD    VARCHAR2(30 BYTE)  NULL,      -- 결제수단
    PAID_AMOUNT   NUMBER             NULL,      -- 결제금액
    PAID_AT       VARCHAR2(30 BYTE)  NULL,      -- 결제승인시각
    MERCHANT_UID  VARCHAR2(30 BYTE)  NULL,      -- 주문번호
    BUYER_NAME    VARCHAR2(20 BYTE)  NULL,      -- 구매자이름
    BUYER_EMAIL   VARCHAR2(100 BYTE) NULL,      -- 구매자이메일
    ERROR_MSG     VARCHAR2(300 BYTE) NULL,      -- 에러메시지
    PAY_STATUS    VARCHAR2(10 BYTE)  NULL,      -- 결제상태 ready,paid,failed(api응답으로 오는 값임)
    CANCEL_AMOUNT NUMBER             NULL,      -- 취소금액
    RESERVE_NO    NUMBER             NULL,      -- 예약번호 (FK)
    CONSTRAINT PK_PAY PRIMARY KEY(PAYMENT_NO),
    CONSTRAINT FK_RES_PAY FOREIGN KEY(RESERVE_NO) REFERENCES RESERVE_T(RESERVE_NO) ON DELETE SET NULL
);

-- ************************************ 찜 **************************************
-- 찜 테이블
CREATE TABLE HEART_T (
	USER_NO	    NUMBER	NULL,  -- 회원 번호   (FK)
	PACKAGE_NO	NUMBER	NULL,  -- 패키지 번호 (FK)
	HOTEL_NO	NUMBER	NULL,  -- 호텔 번호   (FK)
    CONSTRAINT FK_USER_HEART     FOREIGN KEY(USER_NO)     REFERENCES USER_T(USER_NO)         ON DELETE SET NULL,
    CONSTRAINT FK_PACKAGE_HEART  FOREIGN KEY(PACKAGE_NO)  REFERENCES PACKAGE_T(PACKAGE_NO)   ON DELETE SET NULL,
    CONSTRAINT FK_HOTEL_HEART    FOREIGN KEY(HOTEL_NO)    REFERENCES HOTEL_T(HOTEL_NO)       ON DELETE SET NULL
);

-- ************************************ 리뷰 ************************************
-- 리뷰 테이블
CREATE TABLE REVIEW_T (
	REVIEW_NO	    NUMBER	            NOT NULL,  -- 리뷰 번호   (PK)
	USER_NO	        NUMBER	            NOT NULL,  -- 회원 번호   (FK)
	PACKAGE_NO	    NUMBER	            NULL,      -- 패키지 번호 (FK)
	HOTEL_NO	    NUMBER	            NULL,      -- 호텔 번호   (FK)
	REVIEW_CONTENTS	VARCHAR2(300 BYTE)	NULL,      -- 리뷰내용
	STAR	        NUMBER	            NOT NULL,  -- 별점
	REGIST_AT	    VARCHAR2(100 BYTE)	NOT NULL,  -- 등록일
    CONSTRAINT PK_REVIEW          PRIMARY KEY(REVIEW_NO),
    CONSTRAINT FK_USER_REVIEW     FOREIGN KEY(USER_NO)     REFERENCES USER_T(USER_NO)        ON DELETE CASCADE,
    CONSTRAINT FK_PACKAGE_REVIEW  FOREIGN KEY(PACKAGE_NO)  REFERENCES PACKAGE_T(PACKAGE_NO)  ON DELETE SET NULL,
    CONSTRAINT FK_HOTEL_REVIEW    FOREIGN KEY(HOTEL_NO)    REFERENCES HOTEL_T(HOTEL_NO)      ON DELETE SET NULL
);

-- ************************************ 공지 ************************************
-- 공지사항 테이블
CREATE TABLE NOTICE_T (
    NOTICE_NO       NUMBER              NOT NULL,  -- 공지 번호  (PK)
    TITLE           VARCHAR2(100 BYTE)  NULL,      -- 공지 제목
    CONTENTS        CLOB                NULL,      -- 공지 내용
    CREATED_AT      VARCHAR2(100 BYTE)  NULL,      -- 공지 작성일
    MODIFIED_AT     VARCHAR2(100 BYTE)  NULL,      -- 공지 수정일
    CONSTRAINT PK_NOTICE PRIMARY KEY(NOTICE_NO)
);

-- ************************************ 문의 ************************************
-- 1:1문의 테이블
CREATE TABLE INQUIRY_T(
  INQUIRY_NO    NUMBER              NOT NULL,  -- 문의 번호  (PK)
  USER_NO       NUMBER              NOT NULL,  -- 회원 번호  (FK)
  TITLE         VARCHAR2(100 BYTE)  NULL,      -- 제목
  CONTENTS      CLOB                NULL,      -- 내용
  CREATED_AT    VARCHAR2(50 BYTE)   NULL,      -- 작성일
  SEPARATE      VARCHAR2(50 BYTE)   NULL,      -- 분류 (기타/패키지/호텔)
  CONSTRAINT PK_INQUIRY      PRIMARY KEY(INQUIRY_NO),
  CONSTRAINT FK_USER_INQUIRY FOREIGN KEY(USER_NO) REFERENCES USER_T(USER_NO) ON DELETE CASCADE
);

-- 1:1문의 답변 테이블
CREATE TABLE INQUIRY_ANSWER_T(
  ANSWER_NO     NUMBER            NOT NULL,  -- 답변 번호                (PK)
  USER_NO       NUMBER            NOT NULL,  -- 회원(답변한 관리자) 번호 (FK)
  INQUIRY_NO    NUMBER            NOT NULL,  -- 문의 번호                (FK)
  CONTENTS      CLOB              NULL,      -- 내용
  CREATED_AT    VARCHAR2(50 BYTE) NULL,      -- 작성일
  CONSTRAINT PK_ANSWER         PRIMARY KEY(ANSWER_NO),
  CONSTRAINT FK_USER_ANSWER    FOREIGN KEY(USER_NO)    REFERENCES USER_T(USER_NO)       ON DELETE CASCADE,
  CONSTRAINT FK_INQUIRY_ANSWER FOREIGN KEY(INQUIRY_NO) REFERENCES INQUIRY_T(INQUIRY_NO) ON DELETE CASCADE
);

-- 자주묻는질문 카테고리 테이블
CREATE TABLE FAQ_CA_T(
  CA_NO         NUMBER             NOT NULL,  -- 카테고리 번호  (PK)
  CA_TITLE      VARCHAR2(100 BYTE) NULL,      -- 카테고리명      
  CONSTRAINT PK_FAQ_CA PRIMARY KEY(CA_NO)
);

-- 자주묻는질문 테이블
CREATE TABLE FAQ_T(
  FAQ_NO        NUMBER             NOT NULL,  -- 글번호        (PK)
  CA_NO         NUMBER             NOT NULL,  -- 카테고리 번호 (FK)
  TITLE         VARCHAR2(100 BYTE) NULL,      -- 카테고리 제목
  CONTENTS      CLOB               NULL,      -- 내용
  CREATED_AT    VARCHAR2(50 BYTE)  NULL,      -- 작성일
  MODIFIED_AT   VARCHAR2(50 BYTE)  NULL,      -- 수정일
  CONSTRAINT PK_FAQ    PRIMARY KEY(FAQ_NO),
  CONSTRAINT FK_CA_FAQ FOREIGN KEY(CA_NO) REFERENCES FAQ_CA_T(CA_NO) ON DELETE CASCADE
);





-- ******************************************************************************



-- 테스트 데이터 등록
-- ******************************************************************************

-- 관리자 등록 ******************************************************************
INSERT INTO USER_T (USER_NO, EMAIL, PW, NAME, AGREE, AUTH) VALUES(USER_SEQ.NEXTVAL, 'admin', STANDARD_HASH('1', 'SHA256'), '관리자admin', 0, 0);
INSERT INTO USER_T (USER_NO, EMAIL, PW, NAME, AGREE, AUTH) VALUES(USER_SEQ.NEXTVAL, 'master', STANDARD_HASH('1', 'SHA256'), '관리자master', 0, 0);
COMMIT;

-- 회원 등록 ********************************************************************
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user1@naver.com', STANDARD_HASH('1111', 'SHA256'), '사용자1', 'M', '01011111111', '11111', '디지털로', '가산동', '101동 1호', 0, 1, 1, TO_CHAR(TO_DATE('20230111'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230111'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user2@naver.com', STANDARD_HASH('1111', 'SHA256'), '사용자2', 'F', '01022222222', '22222', '디지털로', '가산동', '102동 2호', 0, 1, 1, TO_CHAR(TO_DATE('20230110'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230110'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user3@naver.com', STANDARD_HASH('1111', 'SHA256'), '사용자3', 'M', '01033333333', '33333', '디지털로', '가산동', '103동 3호', 0, 1, 1, TO_CHAR(TO_DATE('20230109'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230109'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user4@naver.com', STANDARD_HASH('1111', 'SHA256'), '사용자4', 'F', '01044444444', '44444', '디지털로', '가산동', '104동 4호', 0, 1, 1, TO_CHAR(TO_DATE('20230108'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230108'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user5@daum.net', STANDARD_HASH('1111', 'SHA256'), '사용자5', 'M', '01011111111', '11111', '디지털로', '가산동', '101동 5호', 0, 2, 1, TO_CHAR(TO_DATE('20230107'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230107'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user6@daum.net', STANDARD_HASH('1111', 'SHA256'), '사용자6', 'F', '01022222222', '22222', '디지털로', '가산동', '102동 6호', 0, 2, 1, TO_CHAR(TO_DATE('20230106'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230106'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user7@daum.net', STANDARD_HASH('1111', 'SHA256'), '사용자7', 'M', '01033333333', '33333', '디지털로', '가산동', '103동 7호', 0, 2, 1, TO_CHAR(TO_DATE('20230105'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230105'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user8@daum.net', STANDARD_HASH('1111', 'SHA256'), '사용자8', 'F', '01044444444', '44444', '디지털로', '가산동', '104동 8호', 0, 2, 1, TO_CHAR(TO_DATE('20230104'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230104'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user9@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자9', 'M', '01011111111', '11111', '디지털로', '가산동', '101동 9호', 0, 0, 1, TO_CHAR(TO_DATE('20230103'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230103'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user10@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자10', 'F', '01022222222', '22222', '디지털로', '가산동', '102동 10호', 0, 0, 1, TO_CHAR(TO_DATE('20230102'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230102'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user11@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자11', 'M', '01033333333', '33333', '디지털로', '가산동', '103동 11호', 0, 0, 1, TO_CHAR(TO_DATE('20230101'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230101'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user12@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자12', 'F', '01044444444', '44444', '디지털로', '가산동', '104동 12호', 0, 0, 1, TO_CHAR(TO_DATE('20200112'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200112'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user13@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자13', 'M', '01011111111', '11111', '디지털로', '가산동', '101동 13호', 0, 0, 1, TO_CHAR(TO_DATE('20200111'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200111'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user15@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자15', 'M', '01033333333', '33333', '디지털로', '가산동', '103동 15호', 0, 0, 1, TO_CHAR(TO_DATE('20200110'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200110'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user16@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자16', 'F', '01044444444', '44444', '디지털로', '가산동', '104동 16호', 0, 0, 1, TO_CHAR(TO_DATE('20200109'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200109'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user17@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자17', 'M', '01011111111', '11111', '디지털로', '가산동', '101동 17호', 0, 0, 1, TO_CHAR(TO_DATE('20200108'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200108'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user18@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자18', 'F', '01022222222', '22222', '디지털로', '가산동', '102동 18호', 0, 0, 1, TO_CHAR(TO_DATE('20200107'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200107'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user19@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자19', 'NO', '01033333333', '33333', '디지털로', '가산동', '103동 19호', 0, 0, 1, TO_CHAR(TO_DATE('20200106'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200106'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user20@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자20', 'NO', '01044444444', '44444', '디지털로', '가산동', '104동 20호', 0, 0, 1, TO_CHAR(TO_DATE('20200105'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200105'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user21@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자21', 'NO', '01011111111', '11111', '디지털로', '가산동', '101동 21호', 0, 0, 1, TO_CHAR(TO_DATE('20200104'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200104'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user22@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자22', 'F', '01022222222', '22222', '디지털로', '가산동', '102동 22호', 0, 0, 1, TO_CHAR(TO_DATE('20200103'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200103'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user23@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자23', 'M', '01033333333', '33333', '디지털로', '가산동', '103동 23호', 0, 0, 1, TO_CHAR(TO_DATE('20200102'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200102'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user24@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자24', 'F', '01044444444', '44444', '디지털로', '가산동', '104동 24호', 0, 0, 1, TO_CHAR(TO_DATE('20200101'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200101'), 'YYYY/MM/DD'));
COMMIT;

-- 탈퇴회원 등록 ****************************************************************
INSERT INTO LEAVE_USER_T VALUES('user123@gmail.com', TO_CHAR(TO_DATE('20100101'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20100201'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user456@gmail.com', TO_CHAR(TO_DATE('20100102'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20101102'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user789@gmail.com', TO_CHAR(TO_DATE('20100103'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20100203'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user987@gmail.com', TO_CHAR(TO_DATE('20100110'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20101110'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user654@gmail.com', TO_CHAR(TO_DATE('20011010'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20011110'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user321@gmail.com', TO_CHAR(TO_DATE('20100923'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20101023'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user147@gmail.com', TO_CHAR(TO_DATE('20230101'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20230201'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user258@gmail.com', TO_CHAR(TO_DATE('20220103'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20221103'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user369@gmail.com', TO_CHAR(TO_DATE('20101101'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20101201'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user963@gmail.com', TO_CHAR(TO_DATE('20170801'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20170901'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user852@gmail.com', TO_CHAR(TO_DATE('20180515'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20180615'),'YYYY/MM/DD'));
INSERT INTO LEAVE_USER_T VALUES('user741@gmail.com', TO_CHAR(TO_DATE('20200815'),'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200915'),'YYYY/MM/DD'));
COMMIT;


-- ******************************************************************************
-- 지역 카테고리 등록 ***********************************************************
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '선택안함'); -- 1
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '전라도');   -- 2
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '충청도');   -- 3
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '경상도');   -- 4
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '제주도');   -- 5
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '서울');     -- 6
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '경기도');   -- 7
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '인천');     -- 8
INSERT INTO REGION_T VALUES(REGION_SEQ.NEXTVAL, '강원도');   -- 9
COMMIT; 

-- 테마 카테고리 등록 ***********************************************************
INSERT INTO THEME_T VALUES(THEME_SEQ.NEXTVAL, '선택안함');   -- 1
INSERT INTO THEME_T VALUES(THEME_SEQ.NEXTVAL, '골프여행');   -- 2
INSERT INTO THEME_T VALUES(THEME_SEQ.NEXTVAL, '단풍여행');   -- 3
INSERT INTO THEME_T VALUES(THEME_SEQ.NEXTVAL, '식도락여행'); -- 4
INSERT INTO THEME_T VALUES(THEME_SEQ.NEXTVAL, '기차여행');   -- 5
INSERT INTO THEME_T VALUES(THEME_SEQ.NEXTVAL, '등산여행');   -- 6
COMMIT;

-- 패키지 상품 등록 *************************************************************
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 9, 4, '강원도로 놀러가요', '예약가능', '인기폭발', '항공없음', '강원도에서 먹고자고싸고', '강원도는 이래저래', '호텔정보는 이렇습니다!', 56000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 2, 1, '전라도로 놀러가요', '예약가능', '강추', '항공없음', '전라도에서 먹고자고싸고', '전라도는 이래저래', '호텔정보는 이렇습니다!', 66000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 3, 2, '골프여행 놀러가요', '예약가능', '가족과함께 추천', '항공없음', '골프에서 먹고자고싸고', '골프는 이래저래', '호텔정보는 이렇습니다!', 126000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 1, 6, '등산할래요 놀러가요', '예약가능', '연인과 추천', '항공없음', '등산에서 먹고자고싸고', '등산는 이래저래', '호텔정보는 이렇습니다!', 46000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 8, 4, '인천으로 놀러가요', '예약가능', '효도여행 강추', '항공없음', '인천에서 먹고자고싸고', '인천는 이래저래', '호텔정보는 이렇습니다!', 226000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 1, 5, '기차타고 놀러가요', '예약가능', '연인과 추천', '항공없음', '기차에서 먹고자고싸고', '기차는 이래저래', NULL, 169000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 4, 4, '경상도로 놀러가요', '예약가능', '가족과함께 추천', '항공없음', '경상도에서 먹고자고싸고', '경상도는 이래저래', '호텔정보는 이렇습니다!', 129000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 6, 3, '서울 단풍보러 놀러가요', '예약가능', '효도여행 강추', '항공없음', '서울에서 먹고자고싸고', '단풍은 이래저래', '호텔정보는 이렇습니다!', 219000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 5, 6, '제주도로 등산가요', '예약가능', '강추', '항공없음', '제주도에서 먹고자고싸고', '제주도 등산은 이래저래', '호텔정보는 이렇습니다!', 136000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 7, 3, '경기도 단풍보러 놀러가요', '예약가능', '인기폭발', '항공없음', '경기도에서 먹고자고싸고', '경기도 단풍은 이래저래', '호텔정보는 이렇습니다!', 45000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 1, 1, '인기 없는 묻지마여행1', '예약가능', '추천', '항공없음', '아무데서 먹고자고싸고', '묻지마여행은 이래저래', '호텔정보는 이렇습니다!', 55000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 0);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 1, 1, '인기 없는 묻지마여행2', '예약가능', '강추', '항공없음', '아무데서 먹고자고싸고', '묻지마여행은 이래저래', '호텔정보는 이렇습니다!', 55000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 0);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 1, 1, '인기 많은묻지마여행1', '예약가능', '친구와 함께 추천', '항공없음', '아무데서 먹고자고싸고', '묻지마여행은 이래저래', '호텔정보는 이렇습니다!', 55000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
INSERT INTO PACKAGE_T VALUES(PACKAGE_SEQ.NEXTVAL, 1, 1, '인기 많은묻지마여행2', '예약가능', '친구와 함께 추천', '항공없음', '아무데서 먹고자고싸고', '묻지마여행은 이래저래', '호텔정보는 이렇습니다!', 55000, '위험한사항이있어요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 0, 1, 30, 1);
COMMIT;

-- ******************************************************************************
-- 배너 등록 ********************************************************************
INSERT INTO BANNER_IMAGE_T (BANNER_NO, ORIGINAL_NAME, FILESYSTEM_NAME, BANNER_PATH, STATE, LINKED_ADDRESS)
VALUES (BANNER_SEQUENCE.NEXTVAL, 'Banner1.jpg', 'Banner1_file', '/images/banners/banner1.jpg', 1, '/promotion1');

INSERT INTO BANNER_IMAGE_T (BANNER_NO, ORIGINAL_NAME, FILESYSTEM_NAME, BANNER_PATH, STATE, LINKED_ADDRESS)
VALUES (BANNER_SEQUENCE.NEXTVAL, 'Banner2.jpg', 'Banner2_file', '/images/banners/banner2.jpg', 1, '/promotion2');

INSERT INTO BANNER_IMAGE_T (BANNER_NO, ORIGINAL_NAME, FILESYSTEM_NAME, BANNER_PATH, STATE, LINKED_ADDRESS)
VALUES (BANNER_SEQUENCE.NEXTVAL, 'Banner3.jpg', 'Banner3_file', '/images/banners/banner3.jpg', 0, '/promotion3');

COMMIT;

-- ******************************************************************************
-- 호텔 상품 등록 ***************************************************************
INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 1, '호텔 서울', '서울 강남구 역삼동', 37.507457, 127.035358, '서울의 중심에 위치한 편안한 호텔입니다.', '010123456', 'hotel1@example.com', '2023/01/01', '2023/01/05', 100, 1, 1);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 1, '해운대 호텔', '부산 해운대구 해운대해변로', 35.165298, 129.165198, '해운대에 자리한 바다가 보이는 호텔입니다.', '0109876543', 'hotel2@example.com', '2023/02/01', '2023/02/10', 150, 1, 1);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 2, '경주 풀빌라', '경주시 보문로', 35.856876, 129.224778, '역사와 자연이 어우러진 풀빌라입니다.', '010555555', 'hotel3@example.com', '2023/03/01', '2023/03/15', 120, 1, 1);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 2, '제주 리조트', '제주시 중앙로', 33.500090, 126.531188, '자연 속에서 휴식을 즐길 수 있는 제주 리조트입니다.', '010444444', 'hotel4@example.com', '2023/04/01', '2023/04/20', 80, 1, 0);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 2, '강릉 펜션', '강원도 강릉시 난설헌로', 37.793653, 128.904917, '강릉의 아름다운 풍경을 즐길 수 있는 펜션입니다.', '010666666', 'hotel5@example.com', '2023/05/01', '2023/05/10', 110, 1, 1);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 3, '여수 해수욕장 호텔', '전라남도 여수시 해안로', 34.760761, 127.647621, '여수의 맑은 바다가 보이는 호텔입니다.', '010333333', 'hotel6@example.com', '2023/06/01', '2023/06/15', 90, 1, 0);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 3, '대구 비즈니스 호텔', '대구 중구 서문로', 35.866079, 128.595136, '대구 시내에 위치한 편리한 비즈니스 호텔입니다.', '010222222', 'hotel7@example.com', '2023/07/01', '2023/07/20', 130, 1, 1);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 4, '인천 공항 트랜짓 호텔', '인천 중구 인천대로', 37.457156, 126.445415, '인천 공항에서 편리하게 이용할 수 있는 호텔입니다.', '010999999', 'hotel8@example.com', '2023/08/01', '2023/08/10', 70, 1, 1);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 4, '울산 리버뷰 호텔', '울산 남구 산업로', 35.539429, 129.318182, '울산 강변에서 멋진 뷰를 즐길 수 있는 호텔입니다.', '0109999888', 'hotel9@example.com', '2023/09/01', '2023/09/10', 75, 1, 1);

INSERT INTO HOTEL_T (HOTEL_NO, REGION_NO, HOTEL_NAME, HOTEL_ADDRESS, LATITUDE, LONGITUDE, HOTEL_DETAIL, PHONE_NUMBER, H_EMAIL, CREATED_AT, MODIFIED_AT, HIT, STATUS, RECOMMEND_STATUS)
VALUES (HOTEL_SEQUENCE.NEXTVAL, 4, '전주 한옥스테이', '전주시 완산구 한옥마을', 35.814510, 127.152756, '전주의 전통과 아름다움을 느낄 수 있는 한옥스테이입니다.', '0108888222', 'hotel10@example.com', '2023/10/01', '2023/10/15', 80, 1, 0);

COMMIT;

-- 호텔 시설 등록 ***************************************************************
INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (1, 1, 1, 1, 1, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (2, 1, 1, 0, 1, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (3, 1, 1, 1, 0, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (4, 0, 1, 0, 1, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (5, 1, 1, 1, 1, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (6, 0, 0, 1, 1, 0);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (7, 1, 1, 1, 1, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (8, 0, 1, 0, 1, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (9, 1, 1, 0, 0, 1);

INSERT INTO FACILITIES_T (HOTEL_NO, POOL, MORNING, SAUNA, LOUNGE, ROOMSERVICE)
VALUES (10, 0, 1, 0, 1, 0);

-- 호텔 객실 등록 ***************************************************************
-- 호텔 1의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 1, '편안하고 모던한 디자인의 객실입니다.', '스탠다드 더블', 3, '시티뷰', 1, 0, 2, '더블', '샤워실', 25);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 1, '넓은 창문으로 햇살 가득한 객실입니다.', '스탠다드 트윈', 2, '오션뷰', 1, 0, 2, '트윈', '샤워실', 28);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 1, '풍부한 공간과 편리한 시설이 구비된 스위트 룸입니다.', '스위트 룸', 1, '오션뷰', 1, 0, 4, '킹 사이즈', '욕조', 40);

COMMIT;

-- 호텔 2의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 2, '고급스러운 분위기의 객실입니다.', '디럭스 더블', 2, '시티뷰', 1, 1, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 2, '넓은 공간과 특별한 서비스가 제공되는 스위트 룸입니다.', '스위트 룸', 1, '오션뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 2, '가족이 함께 머물기에 적합한 패밀리 룸입니다.', '패밀리 룸', 2, '시티뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

COMMIT;

-- 호텔 3의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 3, '전통적인 한옥 스타일의 고급 객실입니다.', '한옥 스위트', 2, '정원뷰', 1, 0, 2, '킹 사이즈', '욕조', 40);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 3, '모던한 디자인과 편안한 공간을 제공하는 디럭스 더블룸입니다.', '디럭스 더블', 1, '시티뷰', 1, 1, 2, '더블', '샤워실', 32);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 3, '심플하면서도 편안한 객실입니다.', '스탠다드 트윈', 3, '정원뷰', 1, 0, 2, '트윈', '샤워실', 28);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 4, '휴식과 힐링을 위한 공간이 마련된 디럭스 객실입니다.', '디럭스 더블', 2, '시티뷰', 1, 0, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 4, '화려하고 세련된 분위기의 스위트 룸입니다.', '스위트 룸', 1, '오션뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 4, '편안함과 모던한 디자인이 어우러진 패밀리 룸입니다.', '패밀리 룸', 3, '시티뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

COMMIT;

-- 호텔 5의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 5, '고급스러움과 편안함이 느껴지는 디럭스 객실입니다.', '디럭스 더블', 2, '시티뷰', 1, 0, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 5, '모던한 인테리어와 넓은 공간이 특징인 스위트 룸입니다.', '스위트 룸', 1, '오션뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 5, '가족이 함께 머물기에 이상적인 패밀리 룸입니다.', '패밀리 룸', 2, '시티뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

COMMIT;

-- 호텔 6의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 6, '풍부한 공간과 편안한 분위기의 디럭스 객실입니다.', '디럭스 더블', 2, '오션뷰', 1, 0, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 6, '러그와 가구가 조화를 이루는 스위트 룸입니다.', '스위트 룸', 1, '시티뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 6, '편안하고 따뜻한 분위기의 패밀리 룸입니다.', '패밀리 룸', 3, '오션뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 7, '화려한 조명과 럭셔리한 분위기의 디럭스 객실입니다.', '디럭스 더블', 2, '시티뷰', 1, 0, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 7, '아늑하고 세련된 인테리어의 스위트 룸입니다.', '스위트 룸', 1, '오션뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 7, '가족 모두가 만족할 수 있는 패밀리 룸입니다.', '패밀리 룸', 3, '시티뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

COMMIT;

-- 호텔 8의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 8, '푸른 바다가 내려다보이는 디럭스 객실입니다.', '디럭스 더블', 2, '시티뷰', 1, 0, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 8, '포근한 분위기의 스위트 룸입니다.', '스위트 룸', 1, '오션뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 8, '가족 모두가 편안하게 머물 수 있는 패밀리 룸입니다.', '패밀리 룸', 3, '시티뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

COMMIT;

-- 호텔 9의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 9, '화려한 조명과 세련된 분위기의 디럭스 객실입니다.', '디럭스 더블', 2, '오션뷰', 1, 0, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 9, '편안하면서도 세련된 인테리어의 스위트 룸입니다.', '스위트 룸', 1, '시티뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 9, '가족 모두가 만족할 수 있는 패밀리 룸입니다.', '패밀리 룸', 3, '오션뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

COMMIT;

-- 호텔 10의 객실
INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 10, '푸르른 바다와 하늘을 닮은 디럭스 객실입니다.', '디럭스 더블', 2, '오션뷰', 1, 0, 2, '더블', '샤워실', 30);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 10, '우아하고 세련된 분위기의 스위트 룸입니다.', '스위트 룸', 1, '시티뷰', 1, 0, 4, '킹 사이즈', '욕조', 45);

INSERT INTO ROOMTYPE_T (ROOM_NO, HOTEL_NO, ROOM_DETAIL, ROOM_NAME, ROOM_MANY, R_VIEW, BLEAKFAST, SMOKE, PEOPLE, BED, SHOWER, R_SIZE)
VALUES (ROOM_SEQUENCE.NEXTVAL, 10, '가족 모두가 함께 머물기에 좋은 패밀리 룸입니다.', '패밀리 룸', 3, '오션뷰', 1, 0, 4, '더블+트윈', '샤워실', 35);

COMMIT;

-- 호텔 이미지 등록 *************************************************************
-- 호텔 1 이미지
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (1, 'hotel1_thumbnail.jpg', 1, 'hotel1_thumbnail.jpg',  '/images/hotel1/thumbnail/hotel1_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (1, 1, 'hotel1_thumbnail.jpg', 0, 'hotel1_room1_1.jpg', '/images/hotel1/room1/hotel1_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (1, 1, 'hotel1_thumbnail.jpg', 0, 'hotel1_room1_2.jpg', '/images/hotel1/room1/hotel1_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (1, 2, 'hotel1_thumbnail.jpg', 0, 'hotel1_thumbnail.jpg', '/images/hotel1/thumbnail/hotel1_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (1, 2, 'hotel1_thumbnail.jpg',  0, 'hotel1_room2_1.jpg', '/images/hotel1/room2/hotel1_room2_1.jpg');

COMMIT;

-- 호텔 2 이미지
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (2, 'hotel2_thumbnail.jpg', 1, 'hotel2_thumbnail.jpg', '/images/hotel2/thumbnail/hotel2_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (2, 1, 'hotel2_thumbnail.jpg', 0, 'hotel2_room1_1.jpg', '/images/hotel2/room1/hotel2_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (2, 1, 'hotel2_thumbnail.jpg',0, 'hotel2_room1_2.jpg', '/images/hotel2/room1/hotel2_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (2, 2, 'hotel2_thumbnail.jpg', 0, 'hotel2_thumbnail.jpg', '/images/hotel2/thumbnail/hotel2_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (2, 2,  'hotel2_thumbnail.jpg', 0, 'hotel2_room2_1.jpg', '/images/hotel2/room2/hotel2_room2_1.jpg');

COMMIT;

-- 호텔 3 이미지
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (3, 'hotel2_thumbnail.jpg', 1, 'hotel3_thumbnail.jpg', '/images/hotel3/thumbnail/hotel3_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (3, 1, 'hotel2_thumbnail.jpg', 0, 'hotel3_room1_1.jpg', '/images/hotel3/room1/hotel3_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (3, 1, 'hotel2_thumbnail.jpg', 0, 'hotel3_room1_2.jpg', '/images/hotel3/room1/hotel3_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (3, 2, 'hotel2_thumbnail.jpg', 0, 'hotel3_thumbnail.jpg', '/images/hotel3/thumbnail/hotel3_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (3, 2, 'hotel2_thumbnail.jpg', 0, 'hotel3_room2_1.jpg', '/images/hotel3/room2/hotel3_room2_1.jpg');

COMMIT;

-- 호텔 4 이미지
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (4, 'hotel4_room1_1.jpg', 1, 'hotel4_thumbnail.jpg', '/images/hotel4/thumbnail/hotel4_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (4, 1, 'hotel4_room1_1.jpg', 0, 'hotel4_room1_1.jpg', '/images/hotel4/room1/hotel4_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (4, 1, 'hotel4_room1_1.jpg', 0, 'hotel4_room1_2.jpg', '/images/hotel4/room1/hotel4_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (4, 2, 'hotel4_room1_1.jpg', 0, 'hotel4_thumbnail.jpg', '/images/hotel4/thumbnail/hotel4_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (4, 2, 'hotel4_room1_1.jpg', 0, 'hotel4_room2_1.jpg', '/images/hotel4/room2/hotel4_room2_1.jpg');

COMMIT;

-- 호텔 5 이미지
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (5, 'hotel4_room1_1.jpg', 1, 'hotel5_thumbnail.jpg', '/images/hotel5/thumbnail/hotel5_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (5, 1, 'hotel4_room1_1.jpg', 0, 'hotel5_room1_1.jpg', '/images/hotel5/room1/hotel5_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (5, 1, 'hotel4_room1_1.jpg', 0, 'hotel5_room1_2.jpg', '/images/hotel5/room1/hotel5_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (5, 2, 'hotel4_room1_1.jpg', 0, 'hotel5_room2_1.jpg', '/images/hotel5/room2/hotel5_room2_1.jpg');

COMMIT;

-- 호텔 6
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (6, 'hotel4_room1_1.jpg', 1, 'hotel6_thumbnail.jpg', '/images/hotel6/thumbnail/hotel6_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (6, 1,'hotel4_room1_1.jpg',  0, 'hotel6_room1_1.jpg', '/images/hotel6/room1/hotel6_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (6, 1, 'hotel4_room1_1.jpg', 0, 'hotel6_room1_2.jpg', '/images/hotel6/room1/hotel6_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (6, 2,'hotel4_room1_1.jpg',  0, 'hotel6_room2_1.jpg', '/images/hotel6/room2/hotel6_room2_1.jpg');

COMMIT;

-- 호텔 7
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (7, 'hotel4_room1_1.jpg', 1, 'hotel6_thumbnail.jpg', '/images/hotel6/thumbnail/hotel6_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (7, 1, 'hotel4_room1_1.jpg', 0, 'hotel6_room1_1.jpg', '/images/hotel6/room1/hotel6_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (7, 1, 'hotel4_room1_1.jpg', 0, 'hotel6_room1_2.jpg', '/images/hotel6/room1/hotel6_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (7, 2, 'hotel4_room1_1.jpg', 0, 'hotel6_room2_1.jpg', '/images/hotel6/room2/hotel6_room2_1.jpg');

COMMIT;

-- 호텔 8
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (8, 'hotel4_room1_1.jpg', 1, 'hotel6_thumbnail.jpg', '/images/hotel6/thumbnail/hotel6_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (8, 1, 'hotel4_room1_1.jpg', 0, 'hotel6_room1_1.jpg', '/images/hotel6/room1/hotel6_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (8, 1, 'hotel4_room1_1.jpg', 0, 'hotel6_room1_2.jpg', '/images/hotel6/room1/hotel6_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (8, 2, 'hotel4_room1_1.jpg', 0, 'hotel6_room2_1.jpg', '/images/hotel6/room2/hotel6_room2_1.jpg');

-- 호텔 9
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (9, 'hotel6_thumbnail.jpg', 1, 'hotel6_thumbnail.jpg', '/images/hotel6/thumbnail/hotel6_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (9, 1,'hotel6_room1_1.jpg', 0, 'hotel6_room1_1.jpg', '/images/hotel6/room1/hotel6_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (9, 1,'hotel6_room1_1.jpg', 0, 'hotel6_room1_2.jpg', '/images/hotel6/room1/hotel6_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (9, 2,'hotel6_room1_1.jpg', 0, 'hotel6_room2_1.jpg', '/images/hotel6/room2/hotel6_room2_1.jpg');

COMMIT;

-- 호텔 10
INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (10, 'hotel4_room1_1.jpg', 1, 'hotel6_thumbnail.jpg', '/images/hotel6/thumbnail/hotel6_thumbnail.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (10, 1, 'hotel4_room1_1.jpg', 0, 'hotel6_room1_1.jpg', '/images/hotel6/room1/hotel6_room1_1.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (10, 1, 'hotel4_room1_1.jpg', 0, 'hotel6_room1_2.jpg', '/images/hotel6/room1/hotel6_room1_2.jpg');

INSERT INTO HOTEL_IMAGE_T (HOTEL_NO, ROOM_NO, ORIGINAL_NAME, THUMBNAIL,  FILESYSTEM_NAME, IMAGE_PATH)
VALUES (10, 2, 'hotel4_room1_1.jpg', 0, 'hotel6_room2_1.jpg', '/images/hotel6/room2/hotel6_room2_1.jpg');
COMMIT;


-- 객실 특성 등록 ***************************************************************
-- 호텔 1 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (1, 1, 1, 1, 1, 0, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (2, 1, 1, 1, 1, 0, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (3, 1, 1, 1, 1, 0, 1);

COMMIT;

-- 호텔 2 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (4, 1, 1, 1, 0, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (5, 1, 1, 1, 0, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (6, 1, 1, 1, 0, 1, 1);

COMMIT;

-- 호텔 3 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (7, 1, 1, 0, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (8, 1, 1, 0, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (9, 1, 1, 0, 1, 1, 1);

COMMIT;

-- 호텔 4 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (10, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (11, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (12, 1, 0, 1, 1, 1, 1);

COMMIT;

-- 호텔 5 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (13, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (14, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (15, 1, 0, 1, 1, 1, 1);

COMMIT;

-- 호텔 6 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (16, 1, 1, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (17, 1, 1, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (18, 1, 1, 1, 1, 1, 1);

COMMIT;

-- 호텔 7 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (19, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (20, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (21, 1, 0, 1, 1, 1, 1);

COMMIT;

-- 호텔 8 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (22, 1, 1, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (23, 1, 1, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (24, 1, 1, 1, 1, 1, 1);

COMMIT;

-- 호텔 9 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (25, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (26, 1, 0, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (27, 1, 0, 1, 1, 1, 1);

COMMIT;

-- 호텔 10 방 특징
INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (28, 1, 1, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (29, 1, 1, 1, 1, 1, 1);

INSERT INTO ROOM_FEATURE_T (ROOM_NO, TOWEL, WATER, COFFEE, DRIER, IRON, MINIBAR)
VALUES (30, 1, 1, 1, 1, 1, 1);

COMMIT;

-- 객실 기간별 가격 등록 ********************************************************
-- 호텔 1 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (1, 1, 100, '2024/01/01', '2024/03/31', 150, '2024/04/01', '2024/06/30', 200, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (1, 2, 120, '2024/01/01', '2024/03/31', 180, '2024/04/01', '2024/06/30', 230, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (1, 3, 80, '2024/01/01', '2024/03/31', 120, '2024/04/01', '2024/06/30', 160, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 2 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (2, 4, 90, '2024/01/01', '2024/03/31', 130, '2024/04/01', '2024/06/30', 180, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (2, 5, 110, '2024/01/01', '2024/03/31', 160, '2024/04/01', '2024/06/30', 210, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (2, 6, 70, '2024/01/01', '2024/03/31', 110, '2024/04/01', '2024/06/30', 150, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 3 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (3, 7, 120, '2024/01/01', '2024/03/31', 180, '2024/04/01', '2024/06/30', 230, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (3, 8, 150, '2024/01/01', '2024/03/31', 200, '2024/04/01', '2024/06/30', 250, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (3, 9, 100, '2024/01/01', '2024/03/31', 150, '2024/04/01', '2024/06/30', 200, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 4 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (4, 10, 80, '2024/01/01', '2024/03/31', 120, '2024/04/01', '2024/06/30', 160, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (4, 11, 100, '2024/01/01', '2024/03/31', 150, '2024/04/01', '2024/06/30', 200, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (4, 12, 60, '2024/01/01', '2024/03/31', 100, '2024/04/01', '2024/06/30', 140, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 5 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (5, 13, 70, '2024/01/01', '2024/03/31', 110, '2024/04/01', '2024/06/30', 150, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (5, 14, 90, '2024/01/01', '2024/03/31', 130, '2024/04/01', '2024/06/30', 170, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (5, 15, 50, '2024/01/01', '2024/03/31', 80, '2024/04/01', '2024/06/30', 120, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 6 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (6, 16, 110, '2024/01/01', '2024/03/31', 160, '2024/04/01', '2024/06/30', 200, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (6, 17, 130, '2024/01/01', '2024/03/31', 180, '2024/04/01', '2024/06/30', 220, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (6, 18, 90, '2024/01/01', '2024/03/31', 120, '2024/04/01', '2024/06/30', 160, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 7 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (7, 19, 120, '2024/01/01', '2024/03/31', 180, '2024/04/01', '2024/06/30', 230, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (7, 20, 150, '2024/01/01', '2024/03/31', 200, '2024/04/01', '2024/06/30', 250, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (7, 21, 100, '2024/01/01', '2024/03/31', 150, '2024/04/01', '2024/06/30', 200, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 8 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (8, 22, 80, '2024/01/01', '2024/03/31', 120, '2024/04/01', '2024/06/30', 160, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (8, 23, 100, '2024/01/01', '2024/03/31', 150, '2024/04/01', '2024/06/30', 200, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (8, 24, 60, '2024/01/01', '2024/03/31', 100, '2024/04/01', '2024/06/30', 140, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 9 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (9, 25, 70, '2024/01/01', '2024/03/31', 110, '2024/04/01', '2024/06/30', 150, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (9, 26, 90, '2024/01/01', '2024/03/31', 130, '2024/04/01', '2024/06/30', 170, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (9, 27, 50, '2024/01/01', '2024/03/31', 80, '2024/04/01', '2024/06/30', 120, '2023/11/01', '2023/12/31');

COMMIT;

-- 호텔 10 객실 가격
INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (10, 28, 110, '2024/01/01', '2024/03/31', 160, '2024/04/01', '2024/06/30', 200, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (10, 29, 130, '2024/01/01', '2024/03/31', 180, '2024/04/01', '2024/06/30', 220, '2023/11/01', '2023/12/31');

INSERT INTO ROOMPRICE_T (HOTEL_NO, ROOM_NO, BI_PRICE, BS_DATE, BE_DATE, JUN_PRICE, JS_DATE, JE_DATE, SUNG_PRICE, SS_DATE, SE_DATE)
VALUES (10, 30, 90, '2024/01/01', '2024/03/31', 120, '2024/04/01', '2024/06/30', 160, '2023/11/01', '2023/12/31');

COMMIT;

-- 상품 사진 등록****************************************************************
INSERT INTO PRODUCT_IMAGE_T VALUES(PRODUCT_IMAGE_SEQ.NEXTVAL, 1, 0, '콘텐츠사진', '경로입니다');
INSERT INTO PRODUCT_IMAGE_T VALUES(PRODUCT_IMAGE_SEQ.NEXTVAL, 2, 0, '콘텐츠사진', '경로입니다');
INSERT INTO PRODUCT_IMAGE_T VALUES(PRODUCT_IMAGE_SEQ.NEXTVAL, 3, 0, '콘텐츠사진', '경로입니다');
INSERT INTO PRODUCT_IMAGE_T VALUES(PRODUCT_IMAGE_SEQ.NEXTVAL, 4, 0, '콘텐츠사진', '경로입니다');
INSERT INTO PRODUCT_IMAGE_T VALUES(PRODUCT_IMAGE_SEQ.NEXTVAL, 5, 0, '콘텐츠사진', '경로입니다');
COMMIT;

-- ******************************************************************************

-- 예약 등록
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'Jamsil', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 112000, 3, 1, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'seoulStation', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 56000, 4, 1, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'DDP', 0, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 3, 168000, 5, 1, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'seoulStation', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 110000, 6, 13, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'Jamsil', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 3, 165000, 7, 13, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'seoulStation', 0, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 242000, 8, 9, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 0, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 80, 7, NULL, 1, 3);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 100, 8, NULL, 1, 1);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 90, 9, NULL, 2, 4);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 70, 10, NULL, 2, 6);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'DDP', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 70, 11, NULL, 5, 13);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 110, 4, NULL, 10, 28);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 130, 5, NULL, 10, 29);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 3, 90, 6, NULL, 10, 30);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'Jamsil', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 112000, 3, 1, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'seoulStation', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 56000, 4, 1, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'DDP', 0, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 3, 168000, 5, 1, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'seoulStation', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 110000, 6, 13, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'Jamsil', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 3, 165000, 7, 13, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'seoulStation', 0, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 242000, 8, 9, NULL, NULL);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 0, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 80, 7, NULL, 1, 3);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 100, 8, NULL, 1, 1);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 90, 9, NULL, 2, 4);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'Jamsil', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 70, 10, NULL, 2, 6);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 70, 11, NULL, 5, 13);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'seoulStation', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 2, 110, 4, NULL, 10, 28);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 1, 130, 5, NULL, 10, 29);
INSERT INTO RESERVE_T VALUES(RESERVE_SEQ.NEXTVAL, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '이것저것 요청합니다.', 0, 'byOwn', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'), 3, 90, 6, NULL, 10, 30);
COMMIT;

-- 여행객 등록
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김성인', '19900101', 'M', '01012341234', 0, 1);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김소아', '20200101', 'M', '01012341234', 1, 1);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '나성인', '19900101', 'F', '01011112222', 0, 2);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '박성인', '19900101', 'F', '01033334444', 0, 3);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '이성인', '19900101', 'F', '01044445555', 0, 3);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '홍성인', '19900101', 'F', '01055556666', 0, 3);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '장성인', '19900101', 'F', '01055556666', 0, 4);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '장소아', '19900101', 'M', '01066667777', 1, 4);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '박성인', '19900101', 'F', '01077778888', 0, 5);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '조성인', '19900101', 'M', '01088889999', 0, 5);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '이소아', '19900101', 'F', '01099991010', 1, 5);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김성인', '19900101', 'M', '01022221111', 0, 6);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김성인', '19900101', 'F', '01033332222', 0, 6);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김성인', '19900101', 'M', '01012341234', 0, 15);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김소아', '20200101', 'M', '01012341234', 1, 15);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '나성인', '19900101', 'F', '01011112222', 0, 16);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '박성인', '19900101', 'F', '01033334444', 0, 17);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '이성인', '19900101', 'F', '01044445555', 0, 17);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '홍성인', '19900101', 'F', '01055556666', 0, 17);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '장성인', '19900101', 'F', '01055556666', 0, 18);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '장소아', '19900101', 'M', '01066667777', 1, 18);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '박성인', '19900101', 'F', '01077778888', 0, 19);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '조성인', '19900101', 'M', '01088889999', 0, 19);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '이소아', '19900101', 'F', '01099991010', 1, 19);

INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김성인', '19900101', 'M', '01022221111', 0, 20);
INSERT INTO TOURIST_T VALUES(TOURIST_SEQ.NEXTVAL, '김성인', '19900101', 'F', '01033332222', 0, 20);

COMMIT;

-- 결제 등록
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 112000, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자1', '구매자1@이메일', '에러메시지', 'paid', 0, 1);
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 110000, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자2', '구매자2@이메일', '에러메시지', 'paid', 0, 4);
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 165000, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자3', '구매자3@이메일', '에러메시지', 'paid', 0, 5);
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 90, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자4', '구매자4@이메일', '에러메시지', 'paid', 0, 9);
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 70, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자5', '구매자5@이메일', '에러메시지', 'paid', 0, 10);
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 70, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자6', '구매자6@이메일', '에러메시지', 'paid', 0, 11);
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 110, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자7', '구매자7@이메일', '에러메시지', 'paid', 0, 12);
INSERT INTO PAYMENT_T VALUES(PAYMENT_SEQ.NEXTVAL, '결제고유번호', 'Y', '결제수단', 130, TO_CHAR(SYSDATE,'YYYY/MM/DD'), '주문번호', '구매자8', '구매자8@이메일', '에러메시지', 'paid', 0, 13);
COMMIT;

-- ******************************************************************************
-- 리뷰 등록 ********************************************************************
-- 패키지 리뷰
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 3, 1, NULL, '너무 행복했습니다!', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 4, 1, NULL, '너무 좋았습니다!', 3, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 5, 1, NULL, '별로습니다!', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 6, 2, NULL, '너무 행복했습니다!', 4, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 7, 3, NULL, '너무 즐거웠습니다!', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 8, 13, NULL, '너무 재밌었어요!', 4, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 3, 13, NULL, '너무 행복했습니다!', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 4, 12, NULL, '와 진짜 별로!', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 5, 10, NULL, '강추강추!', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
COMMIT;

-- 호텔 리뷰
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 3, NULL, 10, '와! 벌써 또 가고싶어요', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 4, NULL, 10, '제가 가본 호텔중 최고에요', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 5, NULL, 9, '좋았어요. 깔끔해요!', 4, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 6, NULL, 9, '꽤 괜찮았어요~', 3, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 7, NULL, 8, '솔직히 또 가고싶진 않네요', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 8, NULL, 8, '음~ 나쁘진 않아요..', 3, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 9, NULL, 8, '객실 상태는 괜찮은데 서비스가 영..', 3, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 6, NULL, 7, '여긴 진자 레전드 별 다섯개', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 10, NULL, 7, '전반적으로 만족스러웠습니다~', 4, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 3, NULL, 7, '가족들이랑 내년에 또 가기로 했습니다~', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 5, NULL, 7, '재방문 의사 있어요 굿~', 4, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 5, NULL, 4, '잘 지내다 왔어요~', 4, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 7, NULL, 3, '잘 지내다 왔지만.. 또 가진 않을듯..', 3, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 11, NULL, 3, '솔직히 위생이 의심스러움', 2, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 12, NULL, 2, '담에도 여기 이용하려구요~', 5, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 3, NULL, 1, '잘 지내고 잘 놀다 왔습니다~', 4, TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO REVIEW_T VALUES(REVIEW_SEQ.NEXTVAL, 4, NULL, 1, '와.. 객실상태.. 복불복인가봐요.. 관리를 뭐 이런식으로..', 1, TO_CHAR(SYSDATE,'YYYY/MM/DD'));

COMMIT;

-- ******************************************************************************
-- 찜 등록
-- user1@naver.com
INSERT INTO HEART_T VALUES(3, 1, NULL);
INSERT INTO HEART_T VALUES(3, 2, NULL);
INSERT INTO HEART_T VALUES(3, 3, NULL);
INSERT INTO HEART_T VALUES(3, 4, NULL);
INSERT INTO HEART_T VALUES(3, 5, NULL);
INSERT INTO HEART_T VALUES(3, 6, NULL);
INSERT INTO HEART_T VALUES(3, NULL, 1);
INSERT INTO HEART_T VALUES(3, NULL, 2);
INSERT INTO HEART_T VALUES(3, NULL, 4);
INSERT INTO HEART_T VALUES(3, NULL, 10);
INSERT INTO HEART_T VALUES(3, NULL, 9);
INSERT INTO HEART_T VALUES(3, NULL, 7);
INSERT INTO HEART_T VALUES(3, NULL, 2);
COMMIT;

-- user2@naver.com
INSERT INTO HEART_T VALUES(4, 10, NULL);
INSERT INTO HEART_T VALUES(4, NULL, 3);
COMMIT;

-- user5@daum.net
INSERT INTO HEART_T VALUES(7, NULL, 10);
INSERT INTO HEART_T VALUES(7, NULL, 9);
INSERT INTO HEART_T VALUES(7, NULL, 8);
INSERT INTO HEART_T VALUES(7, NULL, 7);
INSERT INTO HEART_T VALUES(7, 2, NULL);
INSERT INTO HEART_T VALUES(7, 1, NULL);
COMMIT;

-- user9@gmail.com
INSERT INTO HEART_T VALUES(11, 1, NULL);
INSERT INTO HEART_T VALUES(11, 2, NULL);
INSERT INTO HEART_T VALUES(11, NULL, 10);
INSERT INTO HEART_T VALUES(11, NULL, 6);
COMMIT;

-- ******************************************************************************

-- 공지사항 등록
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '패키지 화장실 이용 안내', '화장실 이용이 가능한 곳마다 가이드가 안내할 예정입니다. 그 외 이용하고 싶으신 분은 당일에 따로 담당 가이드에게 말씀 부탁드립니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '패키지 예약완료 미결제 안내', '예약 후 결제 확인이 되지 않은 분들은 현장에서 현금결제만 가능합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '호텔 서비스 이용 추가금 안내', '따로 상품 소개에 안내되지 않은 서비스를 이용시 추가금액이 발생하며, 이용한 서비스에 대한 환불은 불가능 합니다. 사용 가능 서비스를 미리 숙지 부탁드립니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '호텔 객실내 금연 안내', '모든 호텔은 객실내 금연구역이며, 이를 어겨서 생기는 불상사에 해당 여행사는 책임지지 않습니다. 주의 부탁드립니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '패키지 환불/노쇼 안내', '결제 완료된 모든 상품은 출발 3일전까지 전액 환불 가능하며, 1일전까지 50%, 당일 혹은 사전 연락 없는 노쇼의 경우엔 환불이 불가능 합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '호텔 환불 안내', '각 호텔의 이용 요금 환불 기준은 해당 호텔의 기준과 상이하게 적용됩니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD')); 
INSERT INTO NOTICE_T VALUES(NOTICE_SEQ.NEXTVAL, '패키지 출발지 추가 안내', '출발지에 잠실, 고속버스터미널이 추가되었습니다. 많은 이용 부탁드립니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD')); 
COMMIT;

-- 자주묻는질문 카테고리 등록
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '패키지');    -- 1
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '호텔');      -- 2
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '예약/결제'); -- 3
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '회원안내');  -- 4
INSERT INTO FAQ_CA_T VALUES(FAQ_CA_SEQ.NEXTVAL, '기타');      -- 5
COMMIT;

-- 자주묻는질문 등록
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 3, '환불수수료가 어떻게되나요?', '출발 3일전까지 연락 주실 경우 수수료 없이 전액 환불 가능하며, 이후 50%의 수수료가 발생합니다. 당일 환불은 불가능 합니다. 관련된 상세 사항은 공지사항에서도 확인 가능합니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 1, '패키지 단체 예약은 어떻게 하나요?', '단체예약 관련하여 한반도 여행사 고객센터(02-0000-0000)로 연락주시면 상세히 안내해 드리겠습니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 2, '호텔 인원수 변경 가능한가요?', '호텔 예약 인원 초과 숙박 시 당일 호텔측에 문의하여 주시고, 추가금액이 발생할 수 있습니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 3, '예약은 어떻게 하나요?', '예약을 원하시는 상품의 상세페이지에서 이용 가능합니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 3, '결제는 어떻게 하나요?', '예약 완료 후 결제 가능합니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 4, '회원가입은 어떻게 하나요?', '우측 상단에서 회원가입 서비스가 이용 가능합니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 1, '천재지변시 취소는 어떻게 되나요?', '천재지변으로 인한 패키지 여행이 취소되는 경우 전액 환불해드립니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 1, '여행자보험이 포함인가요?', '2014년 8월7일부터 모든 국내여행상품은 개인정보 보호법(주민등록번호 처리의제한)등 관련법률에 의거하여 개인정보(주민번호)수집이 불가함에 따라 국내여행자보험가입이 불포함으로 변경되었습니다. 여행자보험 가입을 원하실 경우에는 개별가입을 권장합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 3, '현금영수증 해주세요!', '현금영수증은 해당 여행 종료 후 발행됩니다. 발행 원하시는 번호를 남겨주시면, 여행 종료 후 발행해드립니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 5, '코로나에 걸렸어요.', '코로나로인한 환불 및 예약취소의 경우 확진이 증명되는 서류를 첨부해주셔야 하며 추가 문의는 한반도 여행사 고객센터(02-0000-0000)로 연락 부탁드립니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 2, '호텔에서 추가 서비스를 이용하면 추가 금액이 발생하나요?', '제공해드리는 기본 서비스 외 추가 서비스 이용시 해당 호텔의 규정대로 추가금이 발생할 수 있습니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 1, '출발 장소에 지각했어요!', '모든 패키지 여행은 정시에 출발하며, 개인적인 사유로인한 미탑승시 자가용 또는 대중교통을 이용하여 여행지까지 오셔야 합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 2, '호텔에 물건을 두고왔어요!', '해당 호텔의 분실물 센터로 연락 바랍니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 5, '여행 중 물건을 잃어버렸어요!', '분실문 관련 문의는 한반도 여행사 고객센터(02-0000-0000)으로 연락 바랍니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO FAQ_T VALUES(FAQ_SEQ.NEXTVAL, 4, '개명했는데 회원정보에 이름변경은 어떻게 하나요?', '개명 사실이 확인되는 개명확인서, 주민등록 초본, 이메일, 전화번호, 변경 전 성함, 변경 후 성함을 첨부하여 한반도 여행사 이메일(hanbando@hanbando.com)로 발송해주시면 처리해드리겠습니다. 회원정보 변경 완료까지 7일정도 소요될 수 있습니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'), TO_CHAR(SYSDATE,'YYYY/MM/DD'));
COMMIT;

-- 1:1 문의 등록(회원)
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 3, '쩌기요', '저 패키지 여행 안 갔으니까 환불해달라고요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), '패키지');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 4, '다른 호텔은 예약이 안 되나요?', '호텔 상품 좀 추가해주세요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), '호텔');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 6, '아니 가이드가', '가이드가 말을 왜 그런식으로 해요? 진자 어이없네', TO_CHAR(SYSDATE,'YYYY/MM/DD'), '패키지');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 9, '호텔 인원이 추가됐는데요', '추가금 꼭 내야하나요? 카드결제 되나요?', TO_CHAR(SYSDATE,'YYYY/MM/DD'), '호텔');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 4, '아니 호텔 상태가 왜이래요', '진심 더러움,, 이거는 환불 해줘야 하는거 아닌가요?', TO_CHAR(SYSDATE,'YYYY/MM/DD'), '호텔');
INSERT INTO INQUIRY_T VALUES(INQUIRY_SEQ.NEXTVAL, 10, '저 개명했는데요', '이름 변경 해주세요', TO_CHAR(SYSDATE,'YYYY/MM/DD'), '기타');
COMMIT;

-- 1:1 문의-답변 등록(관리자)
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 1, 1, '안녕하세요. 한반도 여행사입니다. 문의주신 내용 관련하여 확인해보니 당일 노쇼로 확인되어 환불이 어렵습니다. 죄송합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 2, 2, '안녕하세요. 한반도 여행사입니다. 현재 예약 가능한 상품 외에는 별도로 예약하셔야 합니다. 말씀해주신 부분 참고하여 더 좋은 상품들로 찾아뵙도록 하겠습니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 1, 3, '안녕하세요. 한반도 여행사입니다. 우선 불편을 드려 진심으로 죄송하단 말씀 드립니다. 어떤 가이드와 문제가 있었는지 더 상세히 말씀해주신다면 조속히 조치하도록 하겠습니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 2, 4, '안녕하세요. 한반도 여행사입니다. 호텔 숙박 인원 추가시 추가금이 발생할 수 있으며 추가금 관련된 상세 문의는 해당 호텔측으로 연락 부탁드립니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'));
INSERT INTO INQUIRY_ANSWER_T VALUES(INQUIRY_ANSWER_SEQ.NEXTVAL, 1, 5, '안녕하세요. 한반도 여행사입니다. 우선 불편을 드려 진심으로 죄송하단 말씀 드립니다. 객실 상태 관련된 민원은 해당 호텔측으로 연락 부탁드립니다. 감사합니다.', TO_CHAR(SYSDATE,'YYYY/MM/DD'));
COMMIT;

-- ******************************************************************************
