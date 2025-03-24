package dev.mvc.bookinfo;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//CREATE TABLE BOOK_INFO(
//BOOKNO                            NUMBER(10)     NOT NULL    PRIMARY KEY,
//BOOKNAME                          VARCHAR2(100)    NOT NULL,
//WRITER                            VARCHAR2(50)     DEFAULT '미상'    NOT NULL,
//BOOKPDATE                         DATE     NOT NULL,
//BOOKPRICE                         NUMBER(10)     DEFAULT 0     NOT NULL
//);
@Setter @Getter @ToString
public class BookinfoVO {

/** 도서 번호 */
private Integer bookno=0;

/** 도서 분류 */
private int bookclass=0;
private String bookkey="";

/** 도서 제목 */
@NotEmpty(message="도서 제목은 필수 입력 항목입니다.")
@Size(max=50, message="도서 제목은 최대 50글자 입니다.")
private String bookname="";

/** 작가 */
@NotEmpty(message="작가는 필수 입력 항목입니다.")
private String writer = "";

/** 출판일 */
@NotNull(message = "출판일은 필수 입력 항목입니다.") // @NotEmpty는 String에 사용되므로 @NotNull로 변경
@DateTimeFormat(pattern = "yyyy-MM-dd")
//private String bookpdate="";
private LocalDate bookpdate;

/** 가격 */
@NotNull(message="가격은 필수 입력 항목입니다.")
@Min(value = 0, message = "가격은 0이상의 값이어야 합니다.")
private Integer bookprice;

/** 출력 순서 */
private Integer seqno;

/** 출력 모드 */
//@NotEmpty(message="출력 모드는 필수 항목입니다.")
//@Pattern(regexp="^[YN]$", message="Y 또는 N만 입력 가능합니다.")
private String visible;
}
