package dev.mvc.Book_Review_Blog;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import dev.mvc.bookinfo.BookinfoProcInter;
import dev.mvc.bookinfo.BookinfoVO;
import dev.mvc.bookinfo.BookinfoVOMenu;

@Controller
public class HomeCont {
  @Autowired
  @Qualifier("dev.mvc.bookinfo.BookinfoProc")
  private BookinfoProcInter bookinfoProc;

  public HomeCont() {
    System.out.println("-> HomeCont created.");
  }
  
  @GetMapping(value = {"/"})
  public String home(Model model) {
    ArrayList<BookinfoVOMenu> menu = this.bookinfoProc.menu();
    model.addAttribute("menu", menu);
    
    return "index";
  }
}

