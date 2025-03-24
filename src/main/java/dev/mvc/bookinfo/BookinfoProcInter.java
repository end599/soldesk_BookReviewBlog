package dev.mvc.bookinfo;

import java.util.ArrayList;

public interface BookinfoProcInter {
  /**
  * <pre>
  * 등록
  * </pre>
  * @param bookinfoVO
  * @return
  */
  public int create(BookinfoVO bookinfoVO);
  
  /**
   * 전체 데이터 호출
   * @return
   */
  public ArrayList<BookinfoVO> list_all();
  
  /**
   * 특정 데이터 호출
   * @return
   */
  public BookinfoVO read(Integer bookinfono);
  
  /**
   * 특정 데이터 수정
   * @param BookinfoVO 수정된 값
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
  
  
  public ArrayList<BookinfoVOMenu> menu();
  
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
   * 
   * 검색 + 페이징 목록
   * @param word 검색어
   * @param now_page 현재 페이지, 시작 페이지 번호 1
   * @param record_per_page 페이지당 출력할 레코드 수
   * @return 
   */
  public ArrayList<BookinfoVO> list_search_paging(String word, int now_page, int record_per_page);

  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  public String pagingBox(int now_page, String word, String list_file, int search_count, int record_per_page, int page_per_block);
  
  
}
