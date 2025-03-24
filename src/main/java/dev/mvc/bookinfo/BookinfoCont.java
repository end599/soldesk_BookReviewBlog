package dev.mvc.bookinfo;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.users.UsersProcInter;
import dev.mvc.tool.Tool;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookinfoCont {
  
  @Autowired
  @Qualifier("dev.mvc.bookinfo.BookinfoProc")
  private BookinfoProcInter bookinfoProc;
  
  @Autowired
  @Qualifier("dev.mvc.users.UsersProc")  // @Service("dev.mvc.users.UsersProc")
  private UsersProcInter usersProc;
  
  /** 페이지당 출력할 레코드 갯수, nowPage는 1부터 시작 */
  public int record_per_page = 10;

  /** 블럭당 페이지 수, 하나의 블럭은 10개의 페이지로 구성됨 */
  public int page_per_block = 5;
  
  /** 페이징 목록 주소 */
  private String list_file_name = "/book/list_search";
  
  /**
   * 콘트롤러 생성 확인
   */
  public BookinfoCont() {
    System.out.println("-> BookinfoCont create.");
  }
  
  //----------------------------------------------------------------------------------------------
  // create 시작
  /**
   * 등록 홈 호출
   * @param model
   * @return
   */
  @GetMapping(value = "/create")
  public String create(Model model) {
    BookinfoVO bookinfoVO = new BookinfoVO();
    bookinfoVO.setBookname("도서 이름을 입력하세요.");
    
    model.addAttribute("bookinfoVO", bookinfoVO);
    
    return "book/create";
  }
  
  /**
   * 등록 처리
   * @param model
   * @param bookinfoVO
   * @param bindingResult
   * @return
   */
  @PostMapping(value = "/create")
  public String create(Model model, 
                            @Valid BookinfoVO bookinfoVO, 
                            BindingResult bindingResult) {
    
    if(bindingResult.hasErrors() == true) {
      return "/book/create";
    }
    
    System.out.println(bookinfoVO.getBookpdate());
    
    int cnt = this.bookinfoProc.create(bookinfoVO);
    System.out.println("-> cnt " + cnt);
    
    if(cnt == 1) {
      return "redirect:/book/list_search";    //@GetMapping(value = "/list_all")
    }
    else {
      model.addAttribute("code", "create_fail");
    }
    model.addAttribute("cnt", cnt);
    
    return "/book/msg";
  }
  // create 끝
  //----------------------------------------------------------------------------------------------
  // 정보 호출 시작
  /**
   * 전체 데이터 호출
   * @param model
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(Model model) {
    BookinfoVO bookinfoVO = new BookinfoVO();
    model.addAttribute("bookinfoVO", bookinfoVO);
    
    ArrayList<String> list_genre = this.bookinfoProc.genreset();
    bookinfoVO.setBookname(String.join("/", list_genre));
    
    ArrayList<BookinfoVO> list = this.bookinfoProc.list_all();
    model.addAttribute("list", list);
    
//    ArrayList<BookinfoVO> menu = this.bookinfoProc.list_all_bookgrp_y();
//    model.addAttribute("menu", menu);
    
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);
    
    return "/book/list_search"; // /templates/book/list_search.html
  }
  
  /**
   * 특정 데이터 호출
   * http://localhost:9092/book/read/1
   */
  @GetMapping(value = "/read/{bookinfono}")
  public String read(Model model, @PathVariable("bookinfono") Integer bookinfono, 
                          @RequestParam(name="word", defaultValue = "") String word, 
                          @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    BookinfoVO bookinfoVO = this.bookinfoProc.read(bookinfono);
    model.addAttribute("bookinfoVO", bookinfoVO);
    
    ArrayList<BookinfoVO> list = this.bookinfoProc.list_search_paging(word, now_page, this.record_per_page);
    model.addAttribute("list", list);
    
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);
    model.addAttribute("word", word);
    
    int search_count = this.bookinfoProc.list_search_count(word);
    // -------------------------------------------------------------------------
    // 페이지 번호 목록
    // -------------------------------------------------------------------------
    String list_file_name = "/book/read/" + bookinfono;
    String paging = this.bookinfoProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page, this.page_per_block);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);
    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * this.record_per_page);
    model.addAttribute("no", no);
    // -------------------------------------------------------------------------
    
    return "/book/read";
  }
  // 정보 호출 끝
  //----------------------------------------------------------------------------------------------
  // 수정 시작
  /**
   * 수정 페이지 호출
   * http://localhost:9092/book/update/1
   */
  @GetMapping(value = "/update/{bookinfono}")
  public String update(Model model, HttpSession session, 
                            @PathVariable("bookinfono") Integer bookinfono, 
                            @RequestParam(name="word",defaultValue = "") String word, 
                            @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.usersProc.isUsersAdmin(session)) {
      BookinfoVO bookinfoVO = this.bookinfoProc.read(bookinfono);
      model.addAttribute("bookinfoVO", bookinfoVO);
      
      ArrayList<BookinfoVO> list = this.bookinfoProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      ArrayList<String> list_genre = this.bookinfoProc.genreset();
      model.addAttribute("list_genre", String.join("/", list_genre));
      
      ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
      model.addAttribute("menu", menu);
      model.addAttribute("word", word);
      
      int search_count = this.bookinfoProc.list_search_count(word);
      String list_file_name = "/book/update/" + bookinfono;
      String paging = this.bookinfoProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      return "/book/update";
    } else {
      return "redirect:/users/login_cookie_need";  // redirect
    }

  }
  
  /**
   * 수정 요청 응답
   */
  @PostMapping(value = "/update")
  public String update(Model model, HttpSession session, 
                            @Valid BookinfoVO bookinfoVO, 
                            BindingResult bindingResult, 
                            @RequestParam(name="word", defaultValue = "") String word, 
                            @RequestParam(name="now_page", defaultValue = "1") int now_page, 
                            RedirectAttributes ra) {
    if (this.usersProc.isUsersAdmin(session)) {
      model.addAttribute("word", word);
      
      if(bindingResult.hasErrors() == true) {
        ArrayList<BookinfoVO> list = this.bookinfoProc.list_all();
        model.addAttribute("list", list);
        return "/book/update";
      }
      
      int cnt = this.bookinfoProc.update(bookinfoVO);
      System.out.println("-> cnt " + cnt);
      
      if(cnt == 1) {
        ra.addAttribute("word", word); // redirect로 값 보내는 법
        ra.addAttribute("now_page", now_page);
        return "redirect:/book/update/" + bookinfoVO.getBookno();
      }
      else {
        model.addAttribute("code", "update_fail");
      }
      model.addAttribute("cnt", cnt);
      
      return "/book/msg";
    } else {
      return "redirect:/users/login_cookie_need";  // redirect
    }

  }
  // 수정 끝
  //----------------------------------------------------------------------------------------------
  // 삭제 시작
  /**
   * 삭제 페이지 호출
   * http://localhost:9092/book/delete/1
   */
  @GetMapping(value = "/delete/{bookno}")
  public String delete(Model model, HttpSession session, 
                            @PathVariable("bookno") Integer bookno, 
                            @RequestParam(name="word", defaultValue = "") String word, 
                            @RequestParam(name="now_page", defaultValue = "1") int now_page) {
    if (this.usersProc.isUsersAdmin(session)) {
      
      BookinfoVO bookinfoVO = this.bookinfoProc.read(bookno);
      model.addAttribute("bookinfoVO", bookinfoVO);
      
      ArrayList<BookinfoVO> list = this.bookinfoProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);
      
      
      ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
      model.addAttribute("menu", menu);
      model.addAttribute("word",word);
      
      int search_count = this.bookinfoProc.list_search_count(word);
      String list_file_name="/book/delete/" + bookno;
      String paging = this.bookinfoProc.pagingBox(now_page, word, list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      
      int del_cnt = this.bookinfoProc.delete_count(bookno);
      model.addAttribute("del_cnt", del_cnt);
      
      return "/book/delete";
    } else {
      return "redirect:/users/login_cookie_need";  // redirect
    }

  }
  
  /**
   * 삭제 요청 응답
   */
  @PostMapping(value = "/delete")
  public String delete_process(Model model, 
                                      @RequestParam(name = "bookno", defaultValue = "bookno") Integer bookno, 
                                      @RequestParam(name = "word", defaultValue = "") String word, 
                                      @RequestParam(name="now_page", defaultValue = "1") int now_page, 
                                      RedirectAttributes ra){
    
    BookinfoVO bookinfoVO = this.bookinfoProc.read(bookno);
    model.addAttribute("bookinfoVO", bookinfoVO);
    
    int cnt = this.bookinfoProc.delete(bookno);
    System.out.println("-> cnt " + cnt);
    
    if(cnt == 1) {
      // ----------------------------------------------------------------------------------------------------------
      // 마지막 페이지에서 모든 레코드가 삭제되면 페이지수를 1 감소 시켜야함.
      int search_cnt = this.bookinfoProc.list_search_count(word);
      if(search_cnt % this.record_per_page == 0) {
        now_page = now_page - 1;
        if(now_page < 1) {
          now_page = 1;
        }
      }
      // ----------------------------------------------------------------------------------------------------------
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      return "redirect:/book/list_search";
    }
    else {
      model.addAttribute("code", "delete_fail");
    }
    model.addAttribute("cnt", cnt);
    
    return "/book/msg";
  }
  // 삭제 끝
  //----------------------------------------------------------------------------------------------
  // 우선 순위 시작
  /**
   * 우선 순위 높임
   * @param model
   * @param bookno
   * @return
   */
  @GetMapping(value="/update_seqno_forward/{bookno}")
  public String update_seqno_forward(Model model, 
                                                @PathVariable("bookno") Integer bookno, 
                                                @RequestParam(name="word", defaultValue = "") String word, 
                                                @RequestParam(name="now_page", defaultValue = "1") int now_page, 
                                                RedirectAttributes ra) {
    
    this.bookinfoProc.update_seqno_forward(bookno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    return "redirect:/book/list_search"; // @GetMapping(value="/list_all")
  }
  
  /**
   * 우선 순위 낮춤
   * @param model
   * @param bookno
   * @return
   */
  @GetMapping(value="/update_seqno_backward/{bookno}")
  public String update_seqno_backward(Model model, 
                                                  @PathVariable("bookno") Integer bookno, 
                                                  @RequestParam(name="word", defaultValue = "") String word, 
                                                  @RequestParam(name="now_page", defaultValue = "1") int now_page, 
                                                  RedirectAttributes ra) {
    
    this.bookinfoProc.update_seqno_backward(bookno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    return "redirect:/book/list_search"; // @GetMapping(value="/list_all")
  }
  // 우선 순위 끝
  //----------------------------------------------------------------------------------------------
  // 공개 처리 시작
  /**
   * 도서 공개 설정
   * @param model
   * @param bookno
   * @return
   */
  @GetMapping(value="/update_visible_y/{bookno}")
  public String update_visible_y(Model model, HttpSession session,
                                        @PathVariable("bookno") Integer bookno, 
                                        @RequestParam(name="word", defaultValue = "") String word, 
                                        @RequestParam(name="now_page", defaultValue = "") int now_page,
                                        RedirectAttributes ra) {
    if (this.usersProc.isUsersAdmin(session)) {
      this.bookinfoProc.update_visible_y(bookno);
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      return "redirect:/book/list_search"; // @GetMapping(value="/list_all")
    } else {
      return "redirect:/users/login_cookie_need";  // redirect
    }
  }
  
  /**
   * 도서 비공개 설정
   * @param model
   * @param bookno
   * @return
   */
  @GetMapping(value="/update_visible_n/{bookno}")
  public String update_visible_n(Model model, HttpSession session, 
                                        @PathVariable("bookno") Integer bookno, 
                                        @RequestParam(name="word", defaultValue = "") String word, 
                                        @RequestParam(name="now_page", defaultValue = "") int now_page,
                                        RedirectAttributes ra) {
    if (this.usersProc.isUsersAdmin(session)) {
      this.bookinfoProc.update_visible_n(bookno);
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      return "redirect:/book/list_search"; // @GetMapping(value="/list_all")
    } else {
      return "redirect:/users/login_cookie_need";  // redirect
    }
  }
  //공개 처리 끝
  //----------------------------------------------------------------------------------------------
  // 검색 시작
//  /**
//  * 등록 폼 및 검색 목록
//  * @return model
//  */
//  @GetMapping(value="/list_search")
//  public String list_search(Model model, 
//                                       @RequestParam(name="word", defaultValue="") String word){
//    BookinfoVO bookinfoVO = new BookinfoVO();
//    
//    ArrayList<String> list_genre = this.bookinfoProc.genreset();
//    bookinfoVO.setBookname(String.join("/", list_genre));
//    model.addAttribute("bookinfoVO", bookinfoVO);
//
//    word = Tool.checkNull(word);
//    model.addAttribute("word", word);
//
//    ArrayList<BookinfoVO> list = this.bookinfoProc.list_search(word);
//    model.addAttribute("list", list);
//
//    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
//    model.addAttribute("menu", menu);
//    
//    int search_cnt = this.bookinfoProc.list_search_count(word);
//    model.addAttribute("search_cnt", search_cnt);
//    
//    return "/book/list_search";
//  }
  
  /**
  * 등록 폼 및 검색 목록 + 페이징
  * http://localhost:9092/book/list_search?word=스&now_page=1
  * @return model
  */
  @GetMapping(value="/list_search")
  public String list_search_paging(HttpSession session, 
                                        Model model, 
                                       @RequestParam(name="word", defaultValue="") String word, 
                                       @RequestParam(name="now_page", defaultValue = "1") int now_page){
    if(this.usersProc.isUsersAdmin(session)) {
      
      BookinfoVO bookinfoVO = new BookinfoVO();
      
      ArrayList<String> list_genre = this.bookinfoProc.genreset();
      bookinfoVO.setBookname(String.join("/", list_genre));
      model.addAttribute("bookinfoVO", bookinfoVO);

      word = Tool.checkNull(word);

      ArrayList<BookinfoVO> list = this.bookinfoProc.list_search_paging(word, now_page, this.record_per_page);
      model.addAttribute("list", list);

      ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
      model.addAttribute("menu", menu);
      
      int search_cnt = this.bookinfoProc.list_search_count(word);
      model.addAttribute("search_cnt", search_cnt);
      
      model.addAttribute("word", word);
      
      // -------------------------------------------------------------------------
      // 페이지 번호 목록
      // -------------------------------------------------------------------------
      int search_count = this.bookinfoProc.list_search_count(word);
      String paging = this.bookinfoProc.pagingBox(now_page, word, this.list_file_name, search_count, this.record_per_page, this.page_per_block);
      model.addAttribute("paging", paging);
      model.addAttribute("now_page", now_page);
      
   // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
      int no = search_count - ((now_page - 1) * this.record_per_page);
      model.addAttribute("no", no);
      

      
      return "/book/list_search";
    } else {
      
      return "redirect:/users/login_cookie_need";
      
    }

  }
  // 검색 끝
  //----------------------------------------------------------------------------------------------

  
}