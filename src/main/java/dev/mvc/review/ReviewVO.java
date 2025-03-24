package dev.mvc.review;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

///**********************************/
///* Table Name: 도서 리뷰 */
///**********************************/
//CREATE TABLE BOOK_REVIEW(
//    REVIEWNO                          NUMBER(10)     NOT NULL    PRIMARY KEY,
//    USERNO                            NUMBER(10)     NULL ,
//    BOOKNO                            NUMBER(10)     NULL ,
//    TITLE                             VARCHAR2(200)    NOT NULL,
//    CONTENT                           VARCHAR2(2000)     NOT NULL,
//    RECOM                             NUMBER(7)    DEFAULT 0     NOT NULL,
//    CNT                               NUMBER(7)    DEFAULT 0     NULL ,
//    COMCNT                            NUMBER(7)    DEFAULT 0     NOT NULL,
//    passwd                            VARCHAR2(100)    NOT NULL,
//    word                              VARCHAR2(200)    NULL ,
//    RDATE                             DATE     NOT NULL,
//    FILE1                             VARCHAR2(100)    NULL ,
//    FILE1SAVED                        VARCHAR2(100)    NULL ,
//    THUMB1                            VARCHAR2(100)    NULL ,
//    SIZE1                             NUMBER(10)     NULL ,
//    MAP                               VARCHAR2(1000)     NULL ,
//    YOUTUBE                           VARCHAR2(1000)     NULL ,
//    VISBLE                            CHAR(1)    NOT NULL,
//  FOREIGN KEY (BOOKNO) REFERENCES BOOK_INFO (BOOKNO),
//  FOREIGN KEY (USERNO) REFERENCES USERS (USERNO)
//);

@Getter @Setter @ToString
public class ReviewVO {
    /** 컨텐츠 번호 */
    private int reviewno;
    /** 관리자 권한의 회원 번호 */
    private int userno;
    /** 카테고리 번호 */
    private int bookno;
    /** 제목 */
    private String title = "";
    /** 내용 */
    private String content = "";
    /** 추천수 */
    private int recom;
    /** 조회수 */
    private int cnt = 0;
    /** 댓글수 */
    private int replycnt = 0;
    /** 패스워드 */
    private String passwd = "";
    /** 검색어 */
    private String word = "";
    /** 등록 날짜 */
    private String rdate = "";
    /** 지도 */
    private String map = "";
    /** Youtube */
    private String youtube = "";

    
    // 파일 업로드 관련
    // -----------------------------------------------------------------------------------
    /**
    이미지 파일
    <input type='file' class="form-control" name='file1MF' id='file1MF' 
               value='' placeholder="파일 선택">
    */
    private MultipartFile file1MF = null;
    /** 메인 이미지 크기 단위, 파일 크기 */
    private String size1_label = "";
    /** 메인 이미지 */
    private String file1 = "";
    /** 실제 저장된 메인 이미지 */
    private String file1saved = "";
    /** 메인 이미지 preview */
    private String thumb1 = "";
    /** 메인 이미지 크기 */
    private long size1 = 0;

    
  
}