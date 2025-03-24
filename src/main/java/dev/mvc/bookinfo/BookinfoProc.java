package dev.mvc.bookinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import dev.mvc.review.Review;
import dev.mvc.review.ReviewDAOInter;
import dev.mvc.review.ReviewVO;
import dev.mvc.tool.Tool;


@Service("dev.mvc.bookinfo.BookinfoProc")
public class BookinfoProc implements BookinfoProcInter {
  
  @Autowired
  private BookinfoDAOInter bookinfoDAO;
  
  @Autowired
  private ReviewDAOInter reviewDAO;
  
  @Override
  public int create(BookinfoVO bookinfoVO) {
    int cnt = this.bookinfoDAO.create(bookinfoVO);
    return cnt;
  }


  @Override
  public ArrayList<BookinfoVO> list_all() {
    ArrayList<BookinfoVO> list = this.bookinfoDAO.list_all();
    return list;
  }


  @Override
  public BookinfoVO read(Integer bookinfono) {
    BookinfoVO bookinfoVO = this.bookinfoDAO.read(bookinfono);
    return bookinfoVO;
  }


  @Override
  public int update(BookinfoVO bookinfoVO) {
    int cnt = this.bookinfoDAO.update(bookinfoVO);
    return cnt;
  }


  @Override
  public int delete(int bookno) {
    
    Integer[] item = this.reviewDAO.delete_img(bookno);
    for(int i : item) {
      ReviewVO reviewVO_read = this.reviewDAO.read(i);
      
      String file1saved = reviewVO_read.getFile1saved();
      String thumb1 = reviewVO_read.getThumb1();
      
      String uploadDir = Review.getUploadDir();
      Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
      Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    }
    int cnt = this.bookinfoDAO.delete(bookno);
    return cnt;
  }

  
  @Override
  public int update_seqno_forward(int bookno) {
    int cnt = this.bookinfoDAO.update_seqno_forward(bookno);
    return cnt;
  }


  @Override
  public int update_seqno_backward(int bookno) {
    int cnt = this.bookinfoDAO.update_seqno_backward(bookno);
    return cnt;
  }


  @Override
  public int update_visible_y(int bookno) {
    int cnt = this.bookinfoDAO.update_visible_y(bookno);
    return cnt;
  }


  @Override
  public int update_visible_n(int bookno) {
    int cnt = this.bookinfoDAO.update_visible_n(bookno);
    return cnt;
  }


  @Override
  public ArrayList<BookinfoVO> list_all_bookgrp_y() {
    ArrayList<BookinfoVO> list = this.bookinfoDAO.list_all_bookgrp_y();
    return list;
  }


  @Override
  public ArrayList<BookinfoVO> list_all_book_y(String bookname) {
    ArrayList<BookinfoVO> list = this.bookinfoDAO.list_all_book_y(bookname);
    return list;
  }


  @Override
  public ArrayList<BookinfoVOMenu> menu() {
    ArrayList<BookinfoVOMenu> menu = new ArrayList<BookinfoVOMenu>();
    ArrayList<BookinfoVO> booknames = this.bookinfoDAO.list_all_bookgrp_y();
    
    for(BookinfoVO bookinfoVO:booknames) {
      BookinfoVOMenu bookinfoVOMenu = new BookinfoVOMenu();
      
      bookinfoVOMenu.setBookclass(bookinfoVO.getBookkey());
      
      ArrayList<BookinfoVO> list_name = this.bookinfoDAO.list_all_book_y(bookinfoVO.getBookkey());

      bookinfoVOMenu.setList_name(list_name);
      menu.add(bookinfoVOMenu);
    }
    return menu;
  }

  
  /**
  * 장르 목록
  *@return
  */
  public ArrayList<String> genreset(){
    ArrayList<String> list = this.bookinfoDAO.genreset();
    return list;
  }
  
  
  /**
  * 검색 목록 
  * @return
  */
  public ArrayList<BookinfoVO> list_search(String word){
    ArrayList<BookinfoVO> list = this.bookinfoDAO.list_search(word);
    return list;
  }


  @Override
  public Integer list_search_count(String word) {
    int cnt = this.bookinfoDAO.list_search_count(word);
    return cnt;
  }


  @Override
  public ArrayList<BookinfoVO> list_search_paging(String word, int now_page, int record_per_page) {
    /*
    페이지당 10개의 레코드 출력
    1 page : WHERE >= 1 AND r <= 10
    2 page : WHERE >= 11 AND r <= 20
    3 page : WHERE >= 21 AND r <= 30
    
    now_page 1
    start_num = (now_page - 1) * record_per_page
    end_num = now_page + 10
    */
    int start_num = ((now_page - 1) * record_per_page) + 1;
    int end_num = (start_num + record_per_page) - 1;
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("word", word);
    map.put("start_num", start_num);
    map.put("end_num", end_num);
    
    ArrayList<BookinfoVO> list = this.bookinfoDAO.list_search_paging(map);
    
    System.out.println("-> " + list.size());
    
    return list;
  }


  // ------------------------------------------------------------------------------------------------
  // 페이징 시작
  /** 
   * SPAN태그를 이용한 박스 모델의 지원, 1 페이지부터 시작 
   * 현재 페이지: 11 / 22   [이전] 11 12 13 14 15 16 17 18 19 20 [다음] 
   *
   * @param now_page  현재 페이지
   * @param word 검색어
   * @param list_file_name 목록 파일명
   * @param search_count 검색 레코드수   
   * @param record_per_page 페이지당 레코드 수
   * @param page_per_block 블럭당 페이지 수
   * @return 페이징 생성 문자열
   */ 
  @Override
  public String pagingBox(int now_page, String word, String list_file_name, int search_count, 
                                      int record_per_page, int page_per_block){    
    // 전체 페이지 수: (double)1/10 -> 0.1 -> 1 페이지, (double)12/10 -> 1.2 페이지 -> 2 페이지
    int total_page = (int)(Math.ceil((double)search_count / record_per_page));
    // 전체 그룹  수: (double)1/10 -> 0.1 -> 1 그룹, (double)12/10 -> 1.2 그룹-> 2 그룹
    int total_grp = (int)(Math.ceil((double)total_page / page_per_block)); 
    // 현재 그룹 번호: (double)13/10 -> 1.3 -> 2 그룹
    int now_grp = (int)(Math.ceil((double)now_page / page_per_block));  
    
    // 1 group: 1, 2, 3 ... 9, 10
    // 2 group: 11, 12 ... 19, 20
    // 3 group: 21, 22 ... 29, 30
    int start_page = ((now_grp - 1) * page_per_block) + 1; // 특정 그룹의 시작 페이지  
    int end_page = (now_grp * page_per_block);               // 특정 그룹의 마지막 페이지   
     
    StringBuffer str = new StringBuffer(); // String class 보다 문자열 추가등의 편집시 속도가 빠름 
    
    // style이 java 파일에 명시되는 경우는 로직에 따라 css가 영향을 많이 받는 경우에 사용하는 방법
    str.append("<style type='text/css'>"); 
    str.append("  #paging {text-align: center; margin-top: 5px; font-size: 1em;}"); 
    str.append("  #paging A:link {text-decoration:none; color:black; font-size: 1em;}"); 
    str.append("  #paging A:hover{text-decoration:none; background-color: #FFFFFF; color:black; font-size: 1em;}"); 
    str.append("  #paging A:visited {text-decoration:none;color:black; font-size: 1em;}"); 
    str.append("  .span_box_1{"); 
    str.append("    text-align: center;");    
    str.append("    font-size: 1em;"); 
    str.append("    border: 1px;"); 
    str.append("    border-style: solid;"); 
    str.append("    border-color: #cccccc;"); 
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("  }"); 
    str.append("  .span_box_2{"); 
    str.append("    text-align: center;");    
    str.append("    background-color: #668db4;"); 
    str.append("    color: #FFFFFF;"); 
    str.append("    font-size: 1em;"); 
    str.append("    border: 1px;"); 
    str.append("    border-style: solid;"); 
    str.append("    border-color: #cccccc;"); 
    str.append("    padding:1px 6px 1px 6px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("    margin:1px 2px 1px 2px; /*위, 오른쪽, 아래, 왼쪽*/"); 
    str.append("  }"); 
    str.append("</style>"); 
    str.append("<div id='paging'>"); 
//    str.append("현재 페이지: " + nowPage + " / " + totalPage + "  "); 
 
    // 이전 10개 페이지로 이동
    // now_grp: 1 (1 ~ 10 page)
    // now_grp: 2 (11 ~ 20 page)
    // now_grp: 3 (21 ~ 30 page) 
    // 현재 2그룹일 경우: (2 - 1) * 10 = 1그룹의 마지막 페이지 10
    // 현재 3그룹일 경우: (3 - 1) * 10 = 2그룹의 마지막 페이지 20
    int _now_page = (now_grp - 1) * page_per_block;  
    if (now_grp >= 2){ // 현재 그룹번호가 2이상이면 페이지수가 11페이지 이상임으로 이전 그룹으로 갈수 있는 링크 생성 
      str.append("<span class='span_box_1'><A href='"+list_file_name+"?&word="+word+"&now_page="+_now_page+"'>이전</A></span>"); 
    } 
 
    // 중앙의 페이지 목록
    for(int i=start_page; i<=end_page; i++){ 
      if (i > total_page){ // 마지막 페이지를 넘어갔다면 페이 출력 종료
        break; 
      } 
  
      if (now_page == i){ // 목록에 출력하는 페이지가 현재페이지와 같다면 CSS 강조(차별을 둠)
        str.append("<span class='span_box_2'>"+i+"</span>"); // 현재 페이지, 강조 
      }else{
        // 현재 페이지가 아닌 페이지는 이동이 가능하도록 링크를 설정
        str.append("<span class='span_box_1'><A href='"+list_file_name+"?word="+word+"&now_page="+i+"'>"+i+"</A></span>");   
      } 
    } 
 
    // 10개 다음 페이지로 이동
    // nowGrp: 1 (1 ~ 10 page),  nowGrp: 2 (11 ~ 20 page),  nowGrp: 3 (21 ~ 30 page) 
    // 현재 페이지 5일경우 -> 현재 1그룹: (1 * 10) + 1 = 2그룹의 시작페이지 11
    // 현재 페이지 15일경우 -> 현재 2그룹: (2 * 10) + 1 = 3그룹의 시작페이지 21
    // 현재 페이지 25일경우 -> 현재 3그룹: (3 * 10) + 1 = 4그룹의 시작페이지 31
    _now_page = (now_grp * page_per_block)+1; //  최대 페이지수 + 1 
    if (now_grp < total_grp){ 
      str.append("<span class='span_box_1'><A href='"+list_file_name+"?&word="+word+"&now_page="+_now_page+"'>다음</A></span>"); 
    } 
    str.append("</div>"); 
     
    return str.toString(); 
  }
  // 페이징 끝
  // ------------------------------------------------------------------------------------------------
  
  /**
   * 구현중
   * 삭제할 항목을 참조하는 행의 수
   * @param bookno
   * @return
   */
  public int delete_count(int bookno) {
    int cnt = this.bookinfoDAO.delete_count(bookno);
    return cnt;
  }

}
