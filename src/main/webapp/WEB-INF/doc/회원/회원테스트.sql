/**********************************/
/* Table Name: 도서 정보 */
/**********************************/
CREATE TABLE BOOK_INFO(
		BOOKNO                        		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		BOOKNAME                      		VARCHAR2(100)		 NOT NULL,
		WRITER                        		VARCHAR2(50)		 DEFAULT '미상'		 NOT NULL,
		BOOKPDATE                     		DATE		 NOT NULL,
		BOOKPRICE                     		NUMBER(10)		 DEFAULT 0		 NOT NULL
);

COMMENT ON TABLE BOOK_INFO is '도서 정보';
COMMENT ON COLUMN BOOK_INFO.BOOKNO is '도서 번호';
COMMENT ON COLUMN BOOK_INFO.BOOKNAME is '도서 이름';
COMMENT ON COLUMN BOOK_INFO.WRITER is '작가';
COMMENT ON COLUMN BOOK_INFO.BOOKPDATE is '출판일';
COMMENT ON COLUMN BOOK_INFO.BOOKPRICE is '도서 가격';


/**********************************/
/* Table Name: 도서 키워드 목록 */
/**********************************/
CREATE TABLE BOOK_KEYWORD_LIST(
		KEYNO                         		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		KEYNAME                       		VARCHAR2(50)		 NOT NULL
);

COMMENT ON TABLE BOOK_KEYWORD_LIST is '도서 키워드 목록';
COMMENT ON COLUMN BOOK_KEYWORD_LIST.KEYNO is '키워드 번호';
COMMENT ON COLUMN BOOK_KEYWORD_LIST.KEYNAME is '키워드 명';


/**********************************/
/* Table Name: 회원 */
/**********************************/
DROP TABLE USERS;
CREATE TABLE USERS(
		USERNO                        		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		UNAME                         		VARCHAR2(20)		 NOT NULL,
		USERID                           		VARCHAR2(20)		 NOT NULL,
		UPASSWD                       		VARCHAR2(50)		 NOT NULL,
		TEL                           		VARCHAR2(20)		 NULL ,
		ZIPCODE                       		VARCHAR2(5)		 NULL ,
		ADDRESS1                      		VARCHAR2(80)		 NULL ,
		ADDRESS2                      		VARCHAR2(50)		 NULL ,
		UEMAIL                        		VARCHAR2(50)		 NULL ,
		JOIN_DATE                     		DATE		 NULL ,
		ACCESS_DATE                   		DATE		 NULL ,
		DELETE_DATE                   		DATE		 NULL ,
		GRADE                         		NUMBER(2)		 NOT NULL
);

COMMENT ON TABLE USERS is '회원';
COMMENT ON COLUMN USERS.USERNO is '회원번호';
COMMENT ON COLUMN USERS.UNAME is '회원 이름';
COMMENT ON COLUMN USERS.USERID is '회원 ID';
COMMENT ON COLUMN USERS.UPASSWD is '회원 비밀번호';
COMMENT ON COLUMN USERS.TEL is '회원 연락처';
COMMENT ON COLUMN USERS.ZIPCODE is '우편 번호';
COMMENT ON COLUMN USERS.ADDRESS1 is '주소1';
COMMENT ON COLUMN USERS.ADDRESS2 is '주소2';
COMMENT ON COLUMN USERS.UEMAIL is '회원 이메일';
COMMENT ON COLUMN USERS.JOIN_DATE is '가입일';
COMMENT ON COLUMN USERS.ACCESS_DATE  is '접속일';
COMMENT ON COLUMN USERS.DELETE_DATE is '탈퇴일';
COMMENT ON COLUMN USERS.GRADE is '등급';


/**********************************/
/* Table Name: 도서 리뷰 */
/**********************************/
CREATE TABLE BOOK_REVIEW(
		REVIEWNO                      		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		USERNO                        		NUMBER(10)		 NULL ,
		BOOKNO                        		NUMBER(10)		 NULL ,
		TITLE                         		VARCHAR2(200)		 NOT NULL,
		CONTENT                       		VARCHAR2(2000)		 NOT NULL,
		RECOM                         		NUMBER(7)		 DEFAULT 0		 NOT NULL,
		CNT                           		NUMBER(7)		 DEFAULT 0		 NULL ,
		COMCNT                        		NUMBER(7)		 DEFAULT 0		 NOT NULL,
		RDATE                         		DATE		 NOT NULL,
		FILE1                         		VARCHAR2(100)		 NULL ,
		FILE1SAVED                    		VARCHAR2(100)		 NULL ,
		THUMB1                        		VARCHAR2(100)		 NULL ,
		SIZE1                         		NUMBER(10)		 NULL ,
		MAP                           		VARCHAR2(1000)		 NULL ,
		YOUTUBE                       		VARCHAR2(1000)		 NULL ,
		MP4                           		VARCHAR2(100)		 NULL ,
		VISBLE                        		CHAR(1)		 NOT NULL,
  FOREIGN KEY (BOOKNO) REFERENCES BOOK_INFO (BOOKNO),
  FOREIGN KEY (USERNO) REFERENCES USER (USERNO)
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
COMMENT ON COLUMN BOOK_REVIEW.RDATE is '등록일';
COMMENT ON COLUMN BOOK_REVIEW.FILE1 is '메인 이미지';
COMMENT ON COLUMN BOOK_REVIEW.FILE1SAVED is '저장된 메인 이미지';
COMMENT ON COLUMN BOOK_REVIEW.THUMB1 is '메인 이미지 미리보기';
COMMENT ON COLUMN BOOK_REVIEW.SIZE1 is '메인 이미지 크기';
COMMENT ON COLUMN BOOK_REVIEW.MAP is '지도';
COMMENT ON COLUMN BOOK_REVIEW.YOUTUBE is 'Youtube영상';
COMMENT ON COLUMN BOOK_REVIEW.MP4 is '영상';
COMMENT ON COLUMN BOOK_REVIEW.VISBLE is '공개';


/**********************************/
/* Table Name: 도서 댓글 */
/**********************************/
CREATE TABLE BOOK_COMMENT(
		COMMENTNO                     		NUMBER(10)		 NOT NULL		 PRIMARY KEY,
		REVIEWNO                      		NUMBER(10)		 NULL ,
		USERNO                        		NUMBER(10)		 NULL ,
		CCONTENTS                     		VARCHAR2(200)		 NOT NULL,
  FOREIGN KEY (REVIEWNO) REFERENCES BOOK_REVIEW (REVIEWNO),
  FOREIGN KEY (USERNO) REFERENCES USER (USERNO)
);

COMMENT ON TABLE BOOK_COMMENT is '도서 댓글';
COMMENT ON COLUMN BOOK_COMMENT.COMMENTNO is '댓글 번호';
COMMENT ON COLUMN BOOK_COMMENT.REVIEWNO is '리뷰 번호';
COMMENT ON COLUMN BOOK_COMMENT.USERNO is '회원번호';
COMMENT ON COLUMN BOOK_COMMENT.CCONTENTS is '댓글 내용';


/**********************************/
/* Table Name: 도서 키워드 정보 */
/**********************************/
CREATE TABLE BOOK_KEYWORD_INFO(
		BOOKNO                        		NUMBER(10)		 NULL ,
		KEYNO                         		NUMBER(10)		 NULL ,
  FOREIGN KEY (BOOKNO) REFERENCES BOOK_INFO (BOOKNO),
  FOREIGN KEY (KEYNO) REFERENCES BOOK_KEYWORD_LIST (KEYNO)
);

COMMENT ON TABLE BOOK_KEYWORD_INFO is '도서 키워드 정보';
COMMENT ON COLUMN BOOK_KEYWORD_INFO.BOOKNO is '도서 번호';
COMMENT ON COLUMN BOOK_KEYWORD_INFO.KEYNO is '키워드 번호';


