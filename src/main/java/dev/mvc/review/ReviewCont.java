package dev.mvc.review;

import java.util.ArrayList;
import java.util.HashMap;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import dev.mvc.bookinfo.BookinfoProc;
import dev.mvc.bookinfo.BookinfoVO;
import dev.mvc.bookinfo.BookinfoVOMenu;
import dev.mvc.users.UsersProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@RequestMapping(value = "/review")
@Controller
public class ReviewCont {
  @Autowired
  @Qualifier("dev.mvc.users.UsersProc") // @Service("dev.mvc.users.UsersProc")
  private UsersProcInter usersProc;

  @Autowired
  @Qualifier("dev.mvc.bookinfo.BookinfoProc") // @Component("dev.mvc.bookinfo.BookinfoProc")
  private BookinfoProc bookinfoProc;

  @Autowired
  @Qualifier("dev.mvc.review.ReviewProc") // @Component("dev.mvc.review.ReviewProc")
  private ReviewProcInter reviewProc;

  public ReviewCont() {
    System.out.println("-> ReviewCont created.");
  }

  /**
   * POST 요청시 새로고침 방지, POST 요청 처리 완료 → redirect → url → GET → forward -> html 데이터
   * 전송
   * 
   * @return
   */
  @GetMapping(value = "/post2get")
  public String post2get(Model model, 
                               @RequestParam(name="url", defaultValue = "") String url) {
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    return url; // forward, /templates/...
  }

  // 등록 폼, review 테이블은 FK로 bookno를 사용함.
  // http://localhost:9091/review/create X
  // http://localhost:9091/review/create?bookno=1 // bookno 변수값을 보내는 목적
  // http://localhost:9091/review/create?bookno=2
  // http://localhost:9091/review/create?bookno=5
  @GetMapping(value = "/create")
  public String create(Model model, 
      @ModelAttribute("reviewVO") ReviewVO reviewVO, 
      @RequestParam(name="bookno", defaultValue="0") int bookno) {
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    BookinfoVO bookinfoVO = this.bookinfoProc.read(bookno); // 카테고리 정보를 출력하기위한 목적
    model.addAttribute("bookinfoVO", bookinfoVO);

    return "/review/create"; // /templates/review/create.html
  }

  /**
   * 등록 처리 http://localhost:9091/review/create
   * 
   * @return
   */
  @PostMapping(value = "/create")
  public String create(HttpServletRequest request, 
      HttpSession session, 
      Model model, 
      @ModelAttribute("reviewVO") ReviewVO reviewVO,
      RedirectAttributes ra) {

    if (usersProc.isUsersAdmin(session)) { // 관리자로 로그인한경우
      // ------------------------------------------------------------------------------
      // 파일 전송 코드 시작
      // ------------------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image
      String file1saved = ""; // 저장된 파일명, image
      String thumb1 = ""; // preview image

      String upDir = Review.getUploadDir(); // 파일을 업로드할 폴더 준비
      // upDir = upDir + "/" + 한글을 제외한 카테고리 이름
      System.out.println("-> upDir: " + upDir);

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = reviewVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명 산출, 01.jpg
      System.out.println("-> 원본 파일명 산출 file1: " + file1);

      long size1 = mf.getSize(); // 파일 크기
      if (size1 > 0) { // 파일 크기 체크, 파일을 올리는 경우
        if (Tool.checkUploadFile(file1) == true) { // 업로드 가능한 파일인지 검사
          // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg, spring_2.jpg...
          file1saved = Upload.saveFileSpring(mf, upDir);

          if (Tool.isImage(file1saved)) { // 이미지인지 검사
            // thumb 이미지 생성후 파일명 리턴됨, width: 200, height: 150
            thumb1 = Tool.preview(upDir, file1saved, 200, 150);
          }

          reviewVO.setFile1(file1); // 순수 원본 파일명
          reviewVO.setFile1saved(file1saved); // 저장된 파일명(파일명 중복 처리)
          reviewVO.setThumb1(thumb1); // 원본이미지 축소판
          reviewVO.setSize1(size1); // 파일 크기

        } else { // 전송 못하는 파일 형식
          ra.addFlashAttribute("code", "check_upload_file_fail"); // 업로드 할 수 없는 파일
          ra.addFlashAttribute("cnt", 0); // 업로드 실패
          ra.addFlashAttribute("url", "/review/msg"); // msg.html, redirect parameter 적용
          return "redirect:/review/msg"; // Post -> Get - param...
        }
      } else { // 글만 등록하는 경우
        System.out.println("-> 글만 등록");
      }

      // ------------------------------------------------------------------------------
      // 파일 전송 코드 종료
      // ------------------------------------------------------------------------------

      // Call By Reference: 메모리 공유, Hashcode 전달
      int userno = (int) session.getAttribute("userno"); // userno FK
      reviewVO.setUserno(userno);
      int cnt = this.reviewProc.create(reviewVO);

      // ------------------------------------------------------------------------------
      // PK의 return
      // ------------------------------------------------------------------------------
      // System.out.println("--> userno: " + reviewVO.getuserno());
      // mav.addObject("userno", reviewVO.getuserno()); // redirect
      // parameter 적용
      // ------------------------------------------------------------------------------

      if (cnt == 1) {
        // type 1, 재업로드 발생
        // return "<h1>파일 업로드 성공</h1>"; // 연속 파일 업로드 발생

        // type 2, 재업로드 발생
        // model.addAttribute("cnt", cnt);
        // model.addAttribute("code", "create_success");
        // return "review/msg";

        // type 3 권장
        // return "redirect:/review/list_all"; // /templates/review/list_all.html

        // System.out.println("-> reviewVO.getbookno(): " + reviewVO.getbookno());
        // ra.addFlashAttribute("bookno", reviewVO.getbookno()); // controller ->
        // controller: X

        ra.addAttribute("bookno", reviewVO.getBookno()); // controller -> controller: O
        return "redirect:/review/list_by_bookno";

        // return "redirect:/review/list_by_bookno?bookno=" + reviewVO.getbookno();
        // // /templates/review/list_by_bookno.html
      } else {
        ra.addFlashAttribute("code", "create_fail"); // DBMS 등록 실패
        ra.addFlashAttribute("cnt", 0); // 업로드 실패
        ra.addFlashAttribute("url", "/review/msg"); // msg.html, redirect parameter 적용
        return "redirect:/review/msg"; // Post -> Get - param...
      }
    } else { // 로그인 실패 한 경우
      return "redirect:/users/login_cookie_need"; // /users/login_cookie_need.html
    }
  }

  /**
   * 전체 목록, 관리자만 사용 가능 http://localhost:9091/review/list_all
   * 
   * @return
   */
  @GetMapping(value = "/list_all")
  public String list_all(HttpSession session, Model model) {
    // System.out.println("-> list_all");
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    if (this.usersProc.isUsersAdmin(session)) { // 관리자만 조회 가능
      ArrayList<ReviewVO> list = this.reviewProc.list_all(); // 모든 목록

      // Thymeleaf는 CSRF(크로스사이트) 스크립팅 해킹 방지 자동 지원
      // for문을 사용하여 객체를 추출, Call By Reference 기반의 원본 객체 값 변경
//      for (ReviewVO reviewVO : list) {
//        String title = reviewVO.getTitle();
//        String content = reviewVO.getContent();
//        
//        title = Tool.convertChar(title);  // 특수 문자 처리
//        content = Tool.convertChar(content); 
//        
//        reviewVO.setTitle(title);
//        reviewVO.setContent(content);  
//
//      }

      model.addAttribute("list", list);
      return "/review/list_all";

    } else {
      return "redirect:/users/login_cookie_need";

    }

  }

//  /**
//   * 유형 1
//   * 카테고리별 목록
//   * http://localhost:9091/review/list_by_bookno?bookno=5
//   * http://localhost:9091/review/list_by_bookno?bookno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_bookno")
//  public String list_by_bookno(HttpSession session, Model model, 
//                                     @RequestParam(name="bookno", defaultValue = "") int bookno) {
//    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
//    model.addAttribute("menu", menu);
//    
//     BookinfoVO bookinfoVO = this.bookinfoProc.read(bookno);
//     model.addAttribute("bookinfoVO", bookinfoVO);
//    
//    ArrayList<ReviewVO> list = this.reviewProc.list_by_bookno(bookno);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//
//    return "/review/list_by_bookno";
//  }

//  /**
//   * 유형 2
//   * 카테고리별 목록 + 검색
//   * http://localhost:9091/review/list_by_bookno?bookno=5
//   * http://localhost:9091/review/list_by_bookno?bookno=6 
//   * @return
//   */
//  @GetMapping(value="/list_by_bookno")
//  public String list_by_bookno_search(HttpSession session, Model model, 
//                                                    int bookno, @RequestParam(name="word", defaultValue = "") String word) {
//    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
//    model.addAttribute("menu", menu);
//    
//     BookinfoVO bookinfoVO = this.bookinfoProc.read(bookno);
//     model.addAttribute("bookinfoVO", bookinfoVO);
//    
//     word = Tool.checkNull(word).trim();
//     
//     HashMap<String, Object> map = new HashMap<>();
//     map.put("bookno", bookno);
//     map.put("word", word);
//     
//    ArrayList<ReviewVO> list = this.reviewProc.list_by_bookno_search(map);
//    model.addAttribute("list", list);
//    
//    // System.out.println("-> size: " + list.size());
//    model.addAttribute("word", word);
//    
//    int search_count = this.reviewProc.list_by_bookno_search_count(map);
//    model.addAttribute("search_count", search_count);
//    
//    return "review/list_by_bookno_search"; // /templates/review/list_by_bookno_search.html
//  }

  /**
   * 유형 3
   * 카테고리별 목록 + 검색 + 페이징 http://localhost:9091/review/list_by_bookno?bookno=5
   * http://localhost:9091/review/list_by_bookno?bookno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_bookno")
  public String list_by_bookno_search_paging(HttpSession session, Model model, 
                                                        @RequestParam(name = "bookno", defaultValue = "") int bookno,
                                                        @RequestParam(name = "word", defaultValue = "") String word,
                                                        @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> bookno: " + bookno);

    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    BookinfoVO bookinfoVO = this.bookinfoProc.read(bookno);
    model.addAttribute("bookinfoVO", bookinfoVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("bookno", bookno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<ReviewVO> list = this.reviewProc.list_by_bookno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.reviewProc.list_by_bookno_search_count(map);
    String paging = this.reviewProc.pagingBox(bookno, now_page, word, "/review/list_by_bookno", search_count,
        Review.RECORD_PER_PAGE, Review.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Review.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    return "/review/list_by_bookno_search_paging"; // /templates/review/list_by_bookno_search_paging.html
  }

  /**
   * 카테고리별 목록 + 검색 + 페이징 + Grid
   * http://localhost:9091/review/list_by_bookno?bookno=5
   * http://localhost:9091/review/list_by_bookno?bookno=6
   * 
   * @return
   */
  @GetMapping(value = "/list_by_bookno_grid")
  public String list_by_bookno_search_paging_grid(HttpSession session, Model model, 
      @RequestParam(name = "bookno", defaultValue = "") int bookno,
      @RequestParam(name = "word", defaultValue = "") String word,
      @RequestParam(name = "now_page", defaultValue = "1") int now_page) {

    // System.out.println("-> bookno: " + bookno);

    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    BookinfoVO bookinfoVO = this.bookinfoProc.read(bookno);
    model.addAttribute("bookinfoVO", bookinfoVO);

    word = Tool.checkNull(word).trim();

    HashMap<String, Object> map = new HashMap<>();
    map.put("bookno", bookno);
    map.put("word", word);
    map.put("now_page", now_page);

    ArrayList<ReviewVO> list = this.reviewProc.list_by_bookno_search_paging(map);
    model.addAttribute("list", list);

    // System.out.println("-> size: " + list.size());
    model.addAttribute("word", word);

    int search_count = this.reviewProc.list_by_bookno_search_count(map);
    String paging = this.reviewProc.pagingBox(bookno, now_page, word, "/review/list_by_bookno_grid", search_count,
        Review.RECORD_PER_PAGE, Review.PAGE_PER_BLOCK);
    model.addAttribute("paging", paging);
    model.addAttribute("now_page", now_page);

    model.addAttribute("search_count", search_count);

    // 일련 변호 생성: 레코드 갯수 - ((현재 페이지수 -1) * 페이지당 레코드 수)
    int no = search_count - ((now_page - 1) * Review.RECORD_PER_PAGE);
    model.addAttribute("no", no);

    // /templates/review/list_by_bookno_search_paging_grid.html
    return "review/list_by_bookno_search_paging_grid";
  }

  /**
   * 조회 http://localhost:9091/review/read?reviewno=17
   * 
   * @return
   */
  @GetMapping(value = "/read")
  public String read(Model model, 
                         @RequestParam(name="reviewno", defaultValue = "0") int reviewno, 
                         @RequestParam(name="word", defaultValue = "") String word, 
                         @RequestParam(name="now_page", defaultValue = "1") int now_page) { // int bookno =
                                                                               // (int)request.getParameter("bookno");
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);
    System.out.println("리뷰no" + reviewno);
    ReviewVO reviewVO = this.reviewProc.read(reviewno);

//    String title = reviewVO.getTitle();
//    String content = reviewVO.getContent();
//    
//    title = Tool.convertChar(title);  // 특수 문자 처리
//    content = Tool.convertChar(content); 
//    
//    reviewVO.setTitle(title);
//    reviewVO.setContent(content);  

    long size1 = reviewVO.getSize1();
    String size1_label = Tool.unit(size1);
    reviewVO.setSize1_label(size1_label);

    model.addAttribute("reviewVO", reviewVO);

    BookinfoVO bookinfoVO = this.bookinfoProc.read(reviewVO.getBookno());
    model.addAttribute("bookinfoVO", bookinfoVO);

    // 조회에서 화면 하단에 출력
    // ArrayList<ReplyVO> reply_list = this.replyProc.list_review(userno);
    // mav.addObject("reply_list", reply_list);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    return "/review/read";
  }

  /**
   * 맵 등록/수정/삭제 폼 http://localhost:9091/review/map?reviewno=1
   * 
   * @return
   */
  @GetMapping(value = "/map")
  public String map(Model model, 
                         @RequestParam(name="reviewno", defaultValue = "") int reviewno) {
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    ReviewVO reviewVO = this.reviewProc.read(reviewno); // map 정보 읽어 오기
    model.addAttribute("reviewVO", reviewVO); // request.setAttribute("reviewVO", reviewVO);

    BookinfoVO bookinfoVO = this.bookinfoProc.read(reviewVO.getBookno()); // 그룹 정보 읽기
    model.addAttribute("bookinfoVO", bookinfoVO);

    return "/review/map";
  }

  /**
   * MAP 등록/수정/삭제 처리 http://localhost:9091/review/map
   * 
   * @param reviewVO
   * @return
   */
  @PostMapping(value = "/map")
  public String map_update(Model model, 
                                   @RequestParam(name="reviewno", defaultValue = "") int reviewno, 
                                   @RequestParam(name="map", defaultValue = "") String map) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("reviewno", reviewno);
    hashMap.put("map", map);

    this.reviewProc.map(hashMap);

    return "redirect:/review/read?reviewno=" + reviewno;
  }

  
  /**
   * Youtube 등록/수정/삭제 폼 http://localhost:9091/review/youtube?userno=1
   * 
   * @return
   */
  @GetMapping(value = "/youtube")
  public String youtube(Model model, 
                              @RequestParam(name = "reviewno", defaultValue = "") int reviewno, 
                              @RequestParam(name = "word", defaultValue = "") String word, 
                              @RequestParam(name="now_page", defaultValue = "") int now_page) {
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    ReviewVO reviewVO = this.reviewProc.read(reviewno); // map 정보 읽어 오기
    model.addAttribute("reviewVO", reviewVO); // request.setAttribute("reviewVO", reviewVO);

    BookinfoVO bookinfoVO = this.bookinfoProc.read(reviewVO.getBookno()); // 그룹 정보 읽기
    model.addAttribute("bookinfoVO", bookinfoVO);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    return "/review/youtube";  // forward
  }

  /**
   * Youtube 등록/수정/삭제 처리 http://localhost:9091/review/youtube
   * 
   * @param reviewVO
   * @return
   */
  @PostMapping(value = "/youtube")
  public String youtube_update(Model model, 
                                             RedirectAttributes ra,
                                             @RequestParam(name = "reviewno", defaultValue = "") int reviewno, 
                                             @RequestParam(name = "youtube", defaultValue = "") String youtube, 
                                             @RequestParam(name = "word", defaultValue = "") String word, 
                                             @RequestParam(name = "now_page", defaultValue = "") int now_page) {

    if (youtube.trim().length() > 0) { // 삭제 중인지 확인, 삭제가 아니면 youtube 크기 변경
      youtube = Tool.youtubeResize(youtube, 640); // youtube 영상의 크기를 width 기준 640 px로 변경
    }

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("reviewno", reviewno);
    hashMap.put("youtube", youtube);

    this.reviewProc.youtube(hashMap);
    
    ra.addAttribute("reviewno", reviewno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);

    // return "redirect:/review/read?reviewno=" + reviewno;
    return "redirect:/review/read";
  }

  /**
   * 수정 폼 http:// localhost:9091/review/update_text?userno=1
   *
   */
  @GetMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, RedirectAttributes ra,  
                                  @RequestParam(name="reviewno", defaultValue="") int reviewno, 
                                  @RequestParam(name="word", defaultValue="") String word,
                                  @RequestParam(name="now_page", defaultValue="") int now_page) {
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);

    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);

    if (this.usersProc.isUsersAdmin(session)) { // 관리자로 로그인한경우
      ReviewVO reviewVO = this.reviewProc.read(reviewno);
      model.addAttribute("reviewVO", reviewVO);

      BookinfoVO bookinfoVO = this.bookinfoProc.read(reviewVO.getBookno());
      model.addAttribute("bookinfoVO", bookinfoVO);

      return "/review/update_text"; // /templates/review/update_text.html
      // String content = "장소:\n인원:\n준비물:\n비용:\n기타:\n";
      // model.addAttribute("content", content);

    } else {
//      ra.addAttribute("url", "/users/login_cookie_need"); // /templates/users/login_cookie_need.html
//      return "redirect:/review/msg"; // @GetMapping(value = "/read")
      return "/review/login_cookie_need";
    }

  }

  /**
   * 수정 처리 http://localhost:9091/review/update_text?userno=1
   * 
   * @return
   */
  @PostMapping(value = "/update_text")
  public String update_text(HttpSession session, Model model, RedirectAttributes ra,
                                  @ModelAttribute("reviewVO") ReviewVO reviewVO, 
                                  @RequestParam(name="search_word", defaultValue = "") String search_word, // reviewVO.word와 구분 필요
                                  @RequestParam(name="now_page", defaultValue = "") int now_page) {
    ra.addAttribute("word", search_word);
    ra.addAttribute("now_page", now_page);

    if (this.usersProc.isUsersAdmin(session)) { // 관리자 로그인 확인
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("reviewno", reviewVO.getReviewno());
      map.put("passwd", reviewVO.getPasswd());

      if (this.reviewProc.password_check(map) == 1) { // 패스워드 일치
        this.reviewProc.update_text(reviewVO); // 글수정

        // mav 객체 이용
        ra.addAttribute("reviewno", reviewVO.getReviewno());
        ra.addAttribute("bookno", reviewVO.getBookno());
        return "redirect:/review/read"; // @GetMapping(value = "/read")

      } else { // 패스워드 불일치
        ra.addFlashAttribute("code", "passwd_fail"); // redirect -> forward -> html
        ra.addFlashAttribute("cnt", 0);
        ra.addAttribute("url", "/review/msg"); // msg.html, redirect parameter 적용

        return "redirect:/review/post2get"; // @GetMapping(value = "/msg")
      }
    } else { // 정상적인 로그인이 아닌 경우 로그인 유도
      ra.addAttribute("url", "/users/login_cookie_need"); // /templates/users/login_cookie_need.html
      return "redirect:/review/post2get"; // @GetMapping(value = "/msg")
    }

  }

  /**
   * 파일 수정 폼 http://localhost:9091/review/update_file?userno=1
   * 
   * @return
   */
  @GetMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, 
                                 @RequestParam(name="reviewno", defaultValue="") int reviewno,
                                 @RequestParam(name="word", defaultValue="") String word, 
                                 @RequestParam(name="now_page", defaultValue="") int now_page) {
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);
    
    model.addAttribute("word", word);
    model.addAttribute("now_page", now_page);
    
    ReviewVO reviewVO = this.reviewProc.read(reviewno);
    model.addAttribute("reviewVO", reviewVO);

    BookinfoVO bookinfoVO = this.bookinfoProc.read(reviewVO.getBookno());
    model.addAttribute("bookinfoVO", bookinfoVO);

    return "/review/update_file";

  }

  /**
   * 파일 수정 처리 http://localhost:9091/review/update_file
   * 
   * @return
   */
  @PostMapping(value = "/update_file")
  public String update_file(HttpSession session, Model model, RedirectAttributes ra,
                                 @ModelAttribute("reviewVO") ReviewVO reviewVO, 
                                 @RequestParam(name = "word", defaultValue = "") String word, 
                                 @RequestParam(name = "now_page", defaultValue = "") int now_page) {

    if (this.usersProc.isUsersAdmin(session)) {
      // 삭제할 파일 정보를 읽어옴, 기존에 등록된 레코드 저장용
      ReviewVO reviewVO_old = reviewProc.read(reviewVO.getReviewno());

      // -------------------------------------------------------------------
      // 파일 삭제 시작
      // -------------------------------------------------------------------
      String file1saved = reviewVO_old.getFile1saved(); // 실제 저장된 파일명
      String thumb1 = reviewVO_old.getThumb1(); // 실제 저장된 preview 이미지 파일명
      long size1 = 0;

      String upDir = Review.getUploadDir(); // C:/kd/deploy/resort_v4sbm3c/review/storage/

      Tool.deleteFile(upDir, file1saved); // 실제 저장된 파일삭제
      Tool.deleteFile(upDir, thumb1); // preview 이미지 삭제
      // -------------------------------------------------------------------
      // 파일 삭제 종료
      // -------------------------------------------------------------------

      // -------------------------------------------------------------------
      // 파일 전송 시작
      // -------------------------------------------------------------------
      String file1 = ""; // 원본 파일명 image

      // 전송 파일이 없어도 file1MF 객체가 생성됨.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="파일 선택">
      MultipartFile mf = reviewVO.getFile1MF();

      file1 = mf.getOriginalFilename(); // 원본 파일명
      size1 = mf.getSize(); // 파일 크기

      if (size1 > 0) { // 폼에서 새롭게 올리는 파일이 있는지 파일 크기로 체크 ★
        // 파일 저장 후 업로드된 파일명이 리턴됨, spring.jsp, spring_1.jpg...
        file1saved = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1saved)) { // 이미지인지 검사
          // thumb 이미지 생성후 파일명 리턴됨, width: 250, height: 200
          thumb1 = Tool.preview(upDir, file1saved, 250, 200);
        }

      } else { // 파일이 삭제만 되고 새로 올리지 않는 경우
        file1 = "";
        file1saved = "";
        thumb1 = "";
        size1 = 0;
      }

      reviewVO.setFile1(file1);
      reviewVO.setFile1saved(file1saved);
      reviewVO.setThumb1(thumb1);
      reviewVO.setSize1(size1);
      // -------------------------------------------------------------------
      // 파일 전송 코드 종료
      // -------------------------------------------------------------------

      this.reviewProc.update_file(reviewVO); // Oracle 처리
      ra.addAttribute ("reviewno", reviewVO.getReviewno());
      ra.addAttribute("bookno", reviewVO.getBookno());
      ra.addAttribute("word", word);
      ra.addAttribute("now_page", now_page);
      
      return "redirect:/review/read";
    } else {
      ra.addAttribute("url", "/users/login_cookie_need"); 
      return "redirect:/review/msg"; // GET
    }
  }

  /**
   * 파일 삭제 폼
   * http://localhost:9091/review/delete?reviewno=1
   * 
   * @return
   */
  @GetMapping(value = "/delete")
  public String delete(HttpSession session, Model model, RedirectAttributes ra,
                               @RequestParam(name="bookno", defaultValue="") int bookno, 
                               @RequestParam(name="reviewno", defaultValue="") int reviewno, 
                               @RequestParam(name="word", defaultValue="") String word, 
                               @RequestParam(name="now_page", defaultValue="") int now_page) {
    if (this.usersProc.isUsersAdmin(session)) { // 관리자로 로그인한경우
      model.addAttribute("bookno", bookno);
      model.addAttribute("word", word);
      model.addAttribute("now_page", now_page);
      
      ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
      model.addAttribute("menu", menu);
      
      ReviewVO reviewVO = this.reviewProc.read(reviewno);
      model.addAttribute("reviewVO", reviewVO);
      
      BookinfoVO bookinfoVO = this.bookinfoProc.read(reviewVO.getBookno());
      model.addAttribute("bookinfoVO", bookinfoVO);
      
      return "/review/delete"; // forward
      
    } else {
      ra.addAttribute("url", "/admin/login_cookie_need");
      return "redirect:/review/msg"; 
    }

  }
  
  /**
   * 삭제 처리 http://localhost:9091/review/delete
   * 
   * @return
   */
  @PostMapping(value = "/delete")
  public String delete(RedirectAttributes ra,
                           @RequestParam(name="reviewno", defaultValue="") int reviewno, 
                           @RequestParam(name="bookno", defaultValue="") int bookno, 
                           @RequestParam(name="word", defaultValue="") String word, 
                           @RequestParam(name="now_page", defaultValue="") int now_page) {
    // -------------------------------------------------------------------
    // 파일 삭제 시작
    // -------------------------------------------------------------------
    // 삭제할 파일 정보를 읽어옴.
    ReviewVO reviewVO_read = reviewProc.read(reviewno);
        
    String file1saved = reviewVO_read.getFile1saved();
    String thumb1 = reviewVO_read.getThumb1();
    
    String uploadDir = Review.getUploadDir();
    Tool.deleteFile(uploadDir, file1saved);  // 실제 저장된 파일삭제
    Tool.deleteFile(uploadDir, thumb1);     // preview 이미지 삭제
    // -------------------------------------------------------------------
    // 파일 삭제 종료
    // -------------------------------------------------------------------
        
    this.reviewProc.delete(reviewno); // DBMS 삭제
        
    // -------------------------------------------------------------------------------------
    // 마지막 페이지의 마지막 레코드 삭제시의 페이지 번호 -1 처리
    // -------------------------------------------------------------------------------------    
    // 마지막 페이지의 마지막 10번째 레코드를 삭제후
    // 하나의 페이지가 3개의 레코드로 구성되는 경우 현재 9개의 레코드가 남아 있으면
    // 페이지수를 4 -> 3으로 감소 시켜야함, 마지막 페이지의 마지막 레코드 삭제시 나머지는 0 발생
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("bookno", bookno);
    map.put("word", word);
    
    if (this.reviewProc.list_by_bookno_search_count(map) % Review.RECORD_PER_PAGE == 0) {
      now_page = now_page - 1; // 삭제시 DBMS는 바로 적용되나 크롬은 새로고침등의 필요로 단계가 작동 해야함.
      if (now_page < 1) {
        now_page = 1; // 시작 페이지
      }
    }
    // -------------------------------------------------------------------------------------

    ra.addAttribute("bookno", bookno);
    ra.addAttribute("word", word);
    ra.addAttribute("now_page", now_page);
    
    return "redirect:/review/list_by_bookno";    
    
  }   
   
  
}
