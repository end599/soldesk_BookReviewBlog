package dev.mvc.bookinfo;

import java.util.ArrayList;
import java.util.Map;

public interface BookinfoDAOInter {
  /**
  * <pre>
  * /src/main/resources/mybatis/bookinfo.xml에 존재하는 코드 가져오기
  * MyBATIS : insert id = "create" parameterType = "dev.mvc.bookinfo.BookinfoVO"
  * insert: int를 리턴, 등록한 레코드 갯수를 리턴
  * id = "create" : 메소드 명으로 사용
  * parameterType = "dev.mvc.bookinfo.BookinfoVO" : 메소드의 파라미터
  * Spring Boot 자동 구현
  * </pre>
  * @param bookinfoVO
  * @return
  */
  public int create(BookinfoVO bookinfoVO);
  
  
  /**
   * 전체 데이터 호출, 전체 목록
   * SQL -> BookinfoVO 객체 레코드 수 만큼 생성 -> ArrayList<BookinfoVO> 객체 생성되어 BookinfoDAOInter로 리턴
   * select id="list_all" resultType="dev.mvc.book.BookinfoVO"
   * @return
   */
  public ArrayList<BookinfoVO> list_all();
  
  /**
   * 특정 데이터 호출
   * @param bookinfono
   * @return
   */
  public BookinfoVO read(Integer bookinfono);
  
  /**
   * 특정 데이터 수정
   * @param bookinfoVO
   * @return
   */
  public int update(BookinfoVO bookinfoVO);
  
  /**
   * 구현중
   * 삭제할 항목을 참조하는 행의 수
   * @param bookno
   * @return
   */
  public int delete_count(int bookno);
  
  /**
   * 특정 데이터 삭제
   * @param bookno 삭제할 레코드PK
   * @return 삭제된 레코드 갯수
   */
  public int delete(int bookno);
  
  /**
   * 우선 순위 높임
   * @param bookno
   * @return
   */
  public int update_seqno_forward(int bookno);
  
  /**
   * 우선 순위 낮춤
   * @param bookno
   * @return
   */
  public int update_seqno_backward(int bookno);
  
  /**
   * 도서 공개 설정
   * @param bookno
   * @return
   */
  public int update_visible_y(int bookno);
  
  /**
   * 도서 비공개 설정
   * @param bookno
   * @return
   */
  public int update_visible_n(int bookno);
  
  /**
   * 숨긴 '카테고리 그룹'을 제외하고 접속자에게 공개할 '카테고리 그룹' 출력
   * @return
   */
  public ArrayList<BookinfoVO> list_all_bookgrp_y();
  
  /**
   * 숨긴 '카테고리 그룹'을 제외하고 접속자에게 공개할 '카테고리' 출력
   * @param bookname
   * @return
   */
  public ArrayList<BookinfoVO> list_all_book_y(String bookname);
  
  /**
   * 장르 목록
   * @return
   */
  public ArrayList<String> genreset();
  
  /**
   * 검색 목록
   * @return
   */
  public ArrayList<BookinfoVO> list_search(String word);
  
  /**
   * 검색 갯수
   * @param word
   * @return
   */
  public Integer list_search_count(String word);
  
  /**
   * 검색 + 페이징 목록
   * @param map
   * @return
   */
  public ArrayList<BookinfoVO> list_search_paging(Map<String, Object> map);
}
