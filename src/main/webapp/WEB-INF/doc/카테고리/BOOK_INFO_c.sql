/**********************************/
/* Table Name: 도서 정보 */
/**********************************/
DROP TABLE BOOK_INFO;
CREATE TABLE BOOK_INFO(
		BOOKNO                        		NUMBER(10)		NOT NULL		 PRIMARY KEY,
		BOOKNAME                      		VARCHAR2(100)	NOT NULL,
		WRITER                        		VARCHAR2(50)    NOT NULL,
		BOOKPDATE                     		DATE		    NOT NULL,
		BOOKPRICE                     		NUMBER(10)      NOT NULL
);

COMMENT ON TABLE BOOK_INFO is '도서 정보';
COMMENT ON COLUMN BOOK_INFO.BOOKNO is '도서 번호';
COMMENT ON COLUMN BOOK_INFO.BOOKNAME is '도서 이름';
COMMENT ON COLUMN BOOK_INFO.WRITER is '작가';
COMMENT ON COLUMN BOOK_INFO.BOOKPDATE is '출판일';
COMMENT ON COLUMN BOOK_INFO.BOOKPRICE is '도서 가격';

DROP SEQUENCE bookinfo_seq;
CREATE SEQUENCE bookinfo_seq
  START WITH 1      -- 시작 번호
  INCREMENT BY 1    -- 증가값
  MAXVALUE 9999999  -- 최대값: 1~9999999 --> NUMBER(7) 대응
  CACHE 2           -- 2번은 메모리에서만 계산, System 테이블 update 횟수 감소 -> 속도 증가
  NOCYCLE; 



INSERT INTO BOOK_INFO(BOOKNO, BOOKNAME, WRITER, BOOKPDATE, BOOKPRICE) VALUES(bookinfo_seq.nextval, '눈 먼자들의 도시', '주제 사라마구', TO_DATE('2002-11-20', 'YYYY-MM-DD'), 11000);
INSERT INTO BOOK_INFO(BOOKNO, BOOKNAME, WRITER, BOOKPDATE, BOOKPRICE) VALUES(bookinfo_seq.nextval, '해가 지는 곳으로', '최진영', TO_DATE('2017-06-30', 'YYYY-MM-DD'), 12600);
INSERT INTO BOOK_INFO(BOOKNO, BOOKNAME, WRITER, BOOKPDATE, BOOKPRICE) VALUES(bookinfo_seq.nextval, '로드(THE ROAD)', '코맥 매카시', TO_DATE('2008-06-10', 'YYYY-MM-DD'), 11700);
INSERT INTO BOOK_INFO(BOOKNO, BOOKNAME, WRITER, BOOKPDATE, BOOKPRICE) VALUES(bookinfo_seq.nextval, '세계 대전 Z', '맥스 브룩스', TO_DATE('2006-09-12', 'YYYY-MM-DD'), 13800);
INSERT INTO BOOK_INFO(BOOKNO, BOOKNAME, WRITER, BOOKPDATE, BOOKPRICE) VALUES(bookinfo_seq.nextval, '테스트', '테스트', sysdate, 99999);

UPDATE book_info SET WRITER='임시' where bookno=5;

DELETE from book_info WHERE bookno=5;

SELECT BOOKNO, BOOKNAME, WRITER, TO_CHAR(BOOKPDATE, 'YYYY-MM-DD') AS BOOKPDATE, BOOKPRICE
FROM BOOK_INFO;

commit;