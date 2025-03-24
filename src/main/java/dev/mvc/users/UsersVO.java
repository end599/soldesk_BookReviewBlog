package dev.mvc.users;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE USERS(
//    USERNO                            NUMBER(10)     NOT NULL    PRIMARY KEY,     -- 유저 번호
//    UNAME                             VARCHAR2(20)     NOT NULL,    -- 유저 이름
//    USERID                              VARCHAR2(20)     NOT NULL,    -- ID
//    UPASSWD                           VARCHAR2(20)     NOT NULL,    --PASSWORD
//    TEL                               VARCHAR2(20)     NULL ,    -- 전화 번호
//    ZIPCODE                           VARCHAR2(5)    NULL ,    -- 도로 주소
//    ADDRESS1                          VARCHAR2(80)     NULL ,    -- 주소1
//    ADDRESS2                          VARCHAR2(50)     NULL ,    -- 주소2
//    UEMAIL                            VARCHAR2(50)     NULL ,    -- 메일
//    JOIN_DATE                         DATE     NULL ,    -- 가입일
//    ACCESS_DATE                       DATE     NULL ,    -- 접속일
//    DELETE_DATE                       DATE     NULL ,    -- 탈퇴일
//    GRADE                             NUMBER(2)    NOT NULL    -- 등급
//);
@Getter @Setter @ToString
public class UsersVO {
  /** 회원 번호 */
  private int userno;
  /** 아이디(이메일) */
  private String userid = "";
  /** 패스워드 */
  private String upasswd = "";
  /** 회원 성명 */
  private String uname = "";
  /** 전화 번호 */
  private String tel = "";
  /** 우편 번호 */
  private String zipcode = "";
  /** 주소 1 */
  private String address1 = "";
  /** 주소 2 */
  private String address2 = "";
  /** 가입일 */
  private String join_date = "";
  /** 등급 */
  private int grade = 0;

  /** 등록된 패스워드 */
  private String old_passwd = "";
  /** id 저장 여부 */
  private String id_save = "";
  /** passwd 저장 여부 */
  private String passwd_save = "";
  /** 이동할 주소 저장 */
  private String url_address = "";
}
