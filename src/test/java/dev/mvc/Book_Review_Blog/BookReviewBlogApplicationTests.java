package dev.mvc.Book_Review_Blog;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import dev.mvc.bookinfo.BookinfoDAOInter;
import dev.mvc.bookinfo.BookinfoProcInter;
import dev.mvc.bookinfo.BookinfoVO;
import dev.mvc.bookinfo.BookinfoVOMenu;

@SpringBootTest
class BookReviewBlogApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
	private BookinfoDAOInter bookinfoDAO;
	
	@Autowired
	@Qualifier("dev.mvc.bookinfo.BookinfoProc")
	private BookinfoProcInter bookinfoProc;
	
//  @Test
//  public void list_all() {
//    ArrayList<BookinfoVO> list = this.bookinfoProc.list_all();
//    
//    for(BookinfoVO bookinfoVO:list) {
//      System.out.println(bookinfoVO.toString());
//    }
////  }
//  @Test
//  public void read() {
//    BookinfoVO bookinfoVO = this.bookinfoProc.read(1);
//    System.out.println(bookinfoVO.toString());
//  }
	
	@Test
	public void menu() {
	  ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
	  
	  for(BookinfoVOMenu bookinfoVOMenu:menu) {
	    System.out.println("-> bookname: " + bookinfoVOMenu.getBookclass());
	    
	    ArrayList<BookinfoVO> list_name = bookinfoVOMenu.getList_name();
	    
	    for(BookinfoVO bookinfoVO: list_name) {
	      System.out.println("     " + bookinfoVO.getWriter());
	    }
	  }
	}
}
