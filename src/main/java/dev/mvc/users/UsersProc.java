package dev.mvc.users;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import dev.mvc.tool.Security;
 
@Component("dev.mvc.users.UsersProc")
public class UsersProc implements UsersProcInter {
  @Autowired
  private UsersDAOInter usersDAO;
  
  @Autowired
  Security security;
  
  public UsersProc(){
    // System.out.println("-> UsersProc created.");
  }

  @Override
  public int checkID(String id) {
    int cnt = this.usersDAO.checkID(id);
    return cnt;
  }

  @Override
  public int create(UsersVO usersVO) {
    String passwd = usersVO.getUpasswd();
    // Security security = new Security();
    String passwd_encoded = this.security.aesEncode(passwd);
    usersVO.setUpasswd(passwd_encoded);
    
    // usersVO.setPasswd(new Security().aesEncode(usersVO.getPasswd())); // 단축형
    
    int cnt = this.usersDAO.create(usersVO);
    return cnt;
  }
 
  @Override
  public ArrayList<UsersVO> list() {
    ArrayList<UsersVO> list = this.usersDAO.list();
    return list;
  }
  
  @Override
  public UsersVO read(int userno) {
    UsersVO usersVO = this.usersDAO.read(userno);
    return usersVO;
  }

  @Override
  public UsersVO readById(String id) {
    UsersVO usersVO = this.usersDAO.readById(id);
    return usersVO;
  }

  /**
   * 회원인지 검사
   */
  @Override
  public boolean isUsers(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin") || grade.equals("users")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    
    return sw;
  }
  
  /**
   * 관리자, 회원인지 검사
   */  
  @Override
  public boolean isUsersAdmin(HttpSession session){
    boolean sw = false; // 로그인하지 않은 것으로 초기화
    String grade = (String)session.getAttribute("grade");
    
    if (grade != null) {
      if (grade.equals("admin")) {
        sw = true;  // 로그인 한 경우
      }      
    }
    
    return sw;
  }
  
  @Override
  public int update(UsersVO usersVO) {
    int cnt = this.usersDAO.update(usersVO);
    return cnt;
  }
  
  @Override
  public int delete(int userno) {
    int cnt = this.usersDAO.delete(userno);
    return cnt;
  }
  
  @Override
  public int passwd_check(HashMap<String, Object> map) {
    String passwd = (String)map.get("current_passwd");

    passwd = this.security.aesEncode(passwd);
    map.put("current_passwd", passwd);

    
    int cnt = this.usersDAO.passwd_check(map);
    return cnt;
  }

  @Override
  public int passwd_update(HashMap<String, Object> map) {
    String passwd = (String)map.get("current_passwd");
    System.out.println(passwd);
    passwd = this.security.aesEncode(passwd);
    map.put("current_passwd", passwd);
    
    int cnt = this.usersDAO.passwd_update(map);
    return cnt;
  }
  
  @Override
  public int login(HashMap<String, Object> map) {
    String upasswd = (String)map.get("upasswd");
    upasswd = this.security.aesEncode(upasswd);
    map.put("upasswd", upasswd);
    
    int cnt = this.usersDAO.login(map);
    return cnt;
  }

}