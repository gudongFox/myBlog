package myblog.demo.web.admin;

import myblog.demo.po.User;
import myblog.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")  //后台控制
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    //跳转到登录页面
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    //后台处理登录信息
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user = userService.checkUser(username, password);
        if (user != null){
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        }else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";
        }
    }


    @GetMapping("/logout")
    //注销当前用户
    public String logout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
