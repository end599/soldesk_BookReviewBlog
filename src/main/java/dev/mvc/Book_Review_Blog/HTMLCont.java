package dev.mvc.Book_Review_Blog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/tag")
public class HTMLCont {
  public HTMLCont() {
    System.out.println("-> HTMLCont create.");
  }
  
  @GetMapping("/init")
  public String showForm(Model model) {
    // radio
    String radio_value = "N";
    model .addAttribute("radio_value", radio_value);
    
    // checkbox
    boolean isCode1 = true;
    boolean isCode2 = false;
    boolean isCode3 = true;
    
    model.addAttribute("isCode1", isCode1);
    model.addAttribute("isCode2", isCode2);
    model.addAttribute("isCode3", isCode3);
    
    return "/tag/init"; // /templates/tag/init.html
  }
  
  @PostMapping("/init")
  @ResponseBody
  public String processForm(Model model, 
      @RequestParam(name = "radio_value", defaultValue = "N") String radio_value,
      @RequestParam(name = "code1", defaultValue = "") String code1,
      @RequestParam(name = "code2", defaultValue = "") String code2,
      @RequestParam(name = "code3", defaultValue = "") String code3) {
    
    StringBuffer sb = new StringBuffer();
    sb.append("<h2>radio_value: " + radio_value + "</h2>");
    sb.append("<h2>code1: " + code1 + "</h2>");
    sb.append("<h2>code2: " + code2 + "</h2>");
    sb.append("<h2>code3: " + code3 + "</h2>");


    return sb.toString();
  }
}
