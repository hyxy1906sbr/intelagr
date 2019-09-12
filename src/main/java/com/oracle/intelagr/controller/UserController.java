package com.oracle.intelagr.controller;

import com.oracle.intelagr.common.*;
import com.oracle.intelagr.entity.Function;
import com.oracle.intelagr.entity.Role;
import com.oracle.intelagr.entity.User;
import com.oracle.intelagr.service.IRoleService;
import com.oracle.intelagr.service.IUserService;
import com.oracle.intelagr.service.impl.UserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IRoleService roleService;
    @RequestMapping("/login")
    @ResponseBody
    public JsonResult login(@RequestBody User user, HttpServletRequest request){
        List<User> list = userService.login(user);
        if(list.size()>0){
            if("02".equals(list.get(0).getLoginStatus())){
                return new JsonResult(false,"该账户已被封号");
            }
            HttpSession session = request.getSession();
            session.setAttribute("user",list.get(0));
            return new JsonResult(true);
        }else {
            return new JsonResult(false,"用户名或密码不正确");
        }
    }
    @RequestMapping("/main")
    public String main(Map m,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("user");
        List<Map> list = userService.getFunction(user.getUserID());
        m.put("menuList",list);
        return "/main";
    }
    @RequestMapping("/list")
    public String list(Map m, User user, @RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "5")int pageSize){
        PageModel pageModel = new PageModel();
        pageModel.setPage(page);
        pageModel.setData(user);
        pageModel.setPageSize(pageSize);
        userService.queryForPage(pageModel);
        m.put("pageModel",pageModel);
        return "user/userList";
    }
    @RequestMapping("/delete")
    @ResponseBody
    public JsonResult delete(@RequestBody List<String> ulist){
        for(String s:ulist){
            userService.delete(s);
        }
        return new JsonResult(true);
    }
    @RequestMapping("/add")
    public String add(Map m){
        List<Role> list = roleService.selectAll();
        JSONArray jsonArray = new JSONArray();
        for(Role role:list){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("roleCode",role.getRoleCode());
            jsonObject.put("roleName",role.getRoleName());
            jsonArray.add(jsonObject);
        }
        m.put("roleList",jsonArray.toString());
        return "user/addUser";
    }

    @RequestMapping("/save")
    @ResponseBody
    public JsonResult save(User u,String role,HttpServletRequest request){
        String[] str = role.split(",");
        List<Role> rlist = new ArrayList<Role>();
        for(String s:str){
            Role r= new Role();
            r.setRoleCode(s);
            rlist.add(r);
        }
        u.setRoles(rlist);
        BaseModel baseModel = CommonUtil.getBaseModel(request);
        u.setCreateDate(baseModel.getCreateDate());
        u.setCreateUserId(baseModel.getCreateUserId());
        u.setUpdateDate(baseModel.getUpdateDate());
        u.setUpdateUserId(baseModel.getUpdateUserId());
        userService.save(u);
        return new JsonResult(true);
    }

    @RequestMapping("/edit")
    public String edit(String userID,Map m){
        User user = userService.selectById(userID);
        m.put("user",user);
        return "user/basicInfoEdit";
    }
    @RequestMapping("/update")
    @ResponseBody
    public JsonResult update(@RequestBody User u){
        userService.update(u);
        return new JsonResult(true);
    }
    @RequestMapping("/detail")
    public String detail(String id,Map m){
        User user = userService.selectById(id);
        m.put("user",user);
        return "user/toDoView";
    }
    @RequestMapping("resetPwdInit")
    public String resetPwdInit(String userID,Map m){
        User user = userService.selectById(userID);
        m.put("user",user);
        return "/user/editPass";
    }
    @RequestMapping("/resetPwd")
    @ResponseBody
    public JsonResult resetPwd(User u){
        u.setPassword(MD5Util.getMD5Code(u.getPassword()));
        userService.update(u);
        return new JsonResult(true);
    }
    @RequestMapping("/startUse")
    @ResponseBody
    public JsonResult startUse(String userID){
        User user = userService.selectById(userID);
        user.setLoginStatus("01");
        userService.update(user);
        return new JsonResult(true);
    }
    @RequestMapping("/endUse")
    @ResponseBody
    public JsonResult endUse(String userID){
        User user = userService.selectById(userID);
        user.setLoginStatus("02");
        userService.update(user);
        return new JsonResult(true);
    }
}
