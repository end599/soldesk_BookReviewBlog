package dev.mvc.review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Spring Boot가 자동 구현
 * @author soldesk
 *
 */
public interface ReviewDAOInter {
  /**
   * 등록, 추상 메소드
   * @param reviewVO
   * @return
   */
  public int create(ReviewVO reviewVO);
  
  //커스텀 함수
  
  /**
   * 이미지 삭제
   * @param cateno
   * @return
   */
  public Integer[] delete_img(int bookno);
  
  // 커스텀 함수
  
  /**
   * 모든 카테고리의 등록된 글목록
   * @return
   */
  public ArrayList<ReviewVO> list_all();
  
  /**
   * 카테고리별 등록된 글 목록
   * @param bookno
   * @return
   */
  public ArrayList<ReviewVO> list_by_bookno(int bookno);
  
  /**
   * 조회
   * @param userno
   * @return
   */
  public ReviewVO read(int userno);
  
  /**
   * map 등록, 수정, 삭제
   * @param map
   * @return 수정된 레코드 갯수
   */
  public int map(HashMap<String, Object> map);

  /**
   * youtube 등록, 수정, 삭제
   * @param youtube
   * @return 수정된 레코드 갯수
   */
  public int youtube(HashMap<String, Object> map);
  
  /**
   * 카테고리별 검색 목록
   * @param map
   * @return
   */
  public ArrayList<ReviewVO> list_by_bookno_search(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색된 레코드 갯수
   * @param map
   * @return
   */
  public int list_by_bookno_search_count(HashMap<String, Object> hashMap);
  
  /**
   * 카테고리별 검색 목록 + 페이징
   * @param reviewVO
   * @return
   */
  public ArrayList<ReviewVO> list_by_bookno_search_paging(HashMap<String, Object> map);
  
  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int password_check(HashMap<String, Object> hashMap);
  
  /**
   * 글 정보 수정
   * @param reviewVO
   * @return 처리된 레코드 갯수
   */
  public int update_text(ReviewVO reviewVO);

  /**
   * 파일 정보 수정
   * @param reviewVO
   * @return 처리된 레코드 갯수
   */
  public int update_file(ReviewVO reviewVO);
 
  /**
   * 삭제
   * @param userno
   * @return 삭제된 레코드 갯수
   */
  public int delete(int userno);
  
  /**
   * FK bookno 값이 같은 레코드 갯수 산출
   * @param bookno
   * @return
   */
  public int count_by_bookno(int bookno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param bookno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_bookno(int bookno);
  
  /**
   * FK userno 값이 같은 레코드 갯수 산출
   * @param userno
   * @return
   */
  public int count_by_userno(int userno);
 
  /**
   * 특정 카테고리에 속한 모든 레코드 삭제
   * @param userno
   * @return 삭제된 레코드 갯수
   */
  public int delete_by_userno(int userno);
  
  /**
   * 글 수 증가
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int userno);
 
  /**
   * 글 수 감소
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int userno);
}