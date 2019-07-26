package life.hk1221.community.controller;

import life.hk1221.community.dto.AccessTokenDTO;
import life.hk1221.community.dto.GithubUser;
import life.hk1221.community.mapper.UserMapper;
import life.hk1221.community.model.User;
import life.hk1221.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.Client.id}")
    private String clientId;

    @Value("${github.Client.secret}")
    private String clientSecret;

    @Value("${github.Redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        // System.out.println(user.getName());
        if (githubUser != null && githubUser.getId() != null) {
            //登录成功 写入cookie 和session
            System.out.println("登陆成功 写入cookie 和session");
            System.out.println(githubUser.getId());

            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            user.setBio(user.getBio());
            user.setAvatarUrl(githubUser.getAvatar_url());
            userMapper.insert(user);
            //写入cookie
            response.addCookie(new Cookie("token", token));

            //写入Session
            //request.getSession().setAttribute("user",githubUser);
            return "redirect:/";
        } else {
            //登陆失败，重新登录
            System.out.println("登陆失败，重新登录");
            return "redirect:/";
        }
    }
}
