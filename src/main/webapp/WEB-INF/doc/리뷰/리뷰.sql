/**********************************/
/* Table Name: 도서 리뷰 */
/**********************************/
drop table BOOK_REVIEW;
CREATE TABLE BOOK_REVIEW(
		REVIEWNO                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		USERNO                        		NUMBER(10)		 NULL ,
		BOOKNO                        		NUMBER(10)		 NULL ,
		TITLE                         		VARCHAR2(200)		 NOT NULL,
		CONTENT                       		VARCHAR2(2000)		 NOT NULL,
		RECOM                         		NUMBER(7)		 DEFAULT 0		 NOT NULL,
		CNT                           		NUMBER(7)		 DEFAULT 0		 NULL ,
		COMCNT                        		NUMBER(7)		 DEFAULT 0		 NOT NULL,
		passwd                        		VARCHAR2(100)		 NOT NULL,
		word                          		VARCHAR2(200)		 NULL ,
		RDATE                         		DATE		 NOT NULL,
		FILE1                         		VARCHAR2(100)		 NULL ,
		FILE1SAVED                    		VARCHAR2(100)		 NULL ,
		THUMB1                        		VARCHAR2(100)		 NULL ,
		SIZE1                         		NUMBER(10)		 NULL ,
		MAP                           		VARCHAR2(1000)		 NULL ,
		YOUTUBE                       		VARCHAR2(1000)		 NULL ,
		VISBLE                        		CHAR(1)		 NOT NULL,
  FOREIGN KEY (BOOKNO) REFERENCES BOOK_INFO (BOOKNO),
  FOREIGN KEY (USERNO) REFERENCES USERS (USERNO)
);

COMMENT ON TABLE BOOK_REVIEW is '도서 리뷰';
COMMENT ON COLUMN BOOK_REVIEW.REVIEWNO is '리뷰 번호';
COMMENT ON COLUMN BOOK_REVIEW.USERNO is '회원번호';
COMMENT ON COLUMN BOOK_REVIEW.BOOKNO is '도서 번호';
COMMENT ON COLUMN BOOK_REVIEW.TITLE is '제목';
COMMENT ON COLUMN BOOK_REVIEW.CONTENT is '내용';
COMMENT ON COLUMN BOOK_REVIEW.RECOM is '추천수';
COMMENT ON COLUMN BOOK_REVIEW.CNT is '조회수';
COMMENT ON COLUMN BOOK_REVIEW.COMCNT is '댓글수';
COMMENT ON COLUMN BOOK_REVIEW.passwd is '패스워드';
COMMENT ON COLUMN BOOK_REVIEW.word is '검색어';
COMMENT ON COLUMN BOOK_REVIEW.RDATE is '등록일';
COMMENT ON COLUMN BOOK_REVIEW.FILE1 is '메인 이미지';
COMMENT ON COLUMN BOOK_REVIEW.FILE1SAVED is '저장된 메인 이미지';
COMMENT ON COLUMN BOOK_REVIEW.THUMB1 is '메인 이미지 미리보기';
COMMENT ON COLUMN BOOK_REVIEW.SIZE1 is '메인 이미지 크기';
COMMENT ON COLUMN BOOK_REVIEW.MAP is '지도';
COMMENT ON COLUMN BOOK_REVIEW.YOUTUBE is 'Youtube영상';
COMMENT ON COLUMN BOOK_REVIEW.VISBLE is '공개';

DROP SEQUENCE review_seq;

CREATE SEQUENCE review_seq
  START WITH 1                -- 시작 번호
  INCREMENT BY 1            -- 증가값
  MAXVALUE 9999999999  -- 최대값: 9999999999 --> NUMBER(10) 대응
  CACHE 2                        -- 2번은 메모리에서만 계산
  NOCYCLE;    



-- insert
INSERT INTO BOOK_REVIEW (REVIEWNO, TITLE, CONTENT, RECOM, COMCNT, passwd, RDATE, VISBLE) 
VALUES (review_seq.nextval,'리뷰 제목1','리뷰 내용2',10,0,'비밀번호1',SYSDATE,'Y');

INSERT INTO BOOK_REVIEW (REVIEWNO, TITLE, CONTENT, RECOM, COMCNT, passwd, RDATE, VISBLE) 
VALUES (review_seq.nextval,'리뷰 제목2','리뷰 내용2',10,0,'비밀번호2',SYSDATE,'Y');

-- delete
DELETE FROM BOOK_REVIEW 
WHERE REVIEWNO = 1;

-- update
UPDATE BOOK_REVIEW 
SET RECOM = RECOM + 1, COMCNT = 0, VISBLE = 'N'
WHERE REVIEWNO = 1;


-- select
SELECT REVIEWNO, TITLE, CONTENT, RECOM, COMCNT, RDATE, VISBLE 
FROM BOOK_REVIEW
WHERE BOOKNO = 1;

-- select 목록
SELECT reviewno, userno, bookno, title, content, recom, cnt, comcnt, passwd, word, rdate, file1, file1saved, thumb1, size1
FROM book_review
ORDER BY reviewno DESC;

-- select 특정 항목의 목록
SELECT reviewno, userno, bookno, title, content, recom, cnt, comcnt, passwd, word, rdate, file1, file1saved, thumb1, size1
FROM book_review
WHERE bookno = 1
ORDER BY reviewno DESC;