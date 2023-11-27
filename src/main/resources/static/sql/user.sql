-- ******************************************************************************
-- 시퀀스 삭제
DROP SEQUENCE USER_SEQ;

-- *****************************************************************************
-- 시퀀스 생성
CREATE SEQUENCE USER_SEQ NOCACHE;


-- ******************************************************************************
-- 테이블 삭제

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
    STATE          NUMBER              NULL,             -- (간편로그인)가입형태(0:정상, 1:네이버, 2:카카오)
    AUTH           NUMBER              NULL,             -- 사용자 권한 (관리자:0, 회원:1)
    PW_MODIFIED_AT VARCHAR2(30 BYTE)   NULL,             -- 비밀번호 수정일
    JOINED_AT      VARCHAR2(30 BYTE)   NULL,             -- 가입일
    CONSTRAINT PK_USER PRIMARY KEY(USER_NO)
);

-- 접속 기록
CREATE TABLE ACCESS_T (
    EMAIL    VARCHAR2(100 BYTE) NOT NULL,  -- 접속한 회원 (FK)
    LOGIN_AT VARCHAR2(30 BYTE)  NULL,      -- 로그인 일시
    CONSTRAINT FK_USER_ACCESS FOREIGN KEY(EMAIL) REFERENCES USER_T(EMAIL) ON DELETE CASCADE
);
    
-- 탈퇴한 사용자
CREATE TABLE LEAVE_USER_T (
    LEAVED_EMAIL VARCHAR2(100 BYTE) NOT NULL,  -- 탈퇴회원 이메일 (PK)
    JOINED_AT    VARCHAR2(30 BYTE)  NULL,      -- 가입일
    LEAVED_AT    VARCHAR2(30 BYTE)  NULL,      -- 탈퇴일
    CONSTRAINT PK_LEAVE PRIMARY KEY(LEAVED_EMAIL)
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
    STATE          NUMBER,                               -- 가입형태(0:정상, 1:네이버, 2:카카오)
    AUTH           NUMBER,                               -- 사용자 권한 (관리자:0, 회원:1)
    PW_MODIFIED_AT VARCHAR2(30 BYTE)   NULL,             -- 비밀번호 수정일
    JOINED_AT      VARCHAR2(30 BYTE)   NULL,             -- 가입일
    INACTIVED_AT   VARCHAR2(30 BYTE)   NULL,             -- 휴면처리일
    CONSTRAINT PK_INACTIVE_USER PRIMARY KEY(USER_NO)
);


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
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user19@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자19', 'M', '01033333333', '33333', '디지털로', '가산동', '103동 19호', 0, 0, 1, TO_CHAR(TO_DATE('20200106'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200106'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user20@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자20', 'F', '01044444444', '44444', '디지털로', '가산동', '104동 20호', 0, 0, 1, TO_CHAR(TO_DATE('20200105'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200105'), 'YYYY/MM/DD'));
INSERT INTO USER_T VALUES(USER_SEQ.NEXTVAL, 'user21@gmail.com', STANDARD_HASH('1111', 'SHA256'), '사용자21', 'M', '01011111111', '11111', '디지털로', '가산동', '101동 21호', 0, 0, 1, TO_CHAR(TO_DATE('20200104'), 'YYYY/MM/DD'), TO_CHAR(TO_DATE('20200104'), 'YYYY/MM/DD'));
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



