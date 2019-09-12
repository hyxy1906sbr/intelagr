package com.oracle.intelagr.controller;

import com.oracle.intelagr.common.BaseModel;
import com.oracle.intelagr.common.CommonUtil;
import com.oracle.intelagr.common.JsonResult;
import com.oracle.intelagr.common.PageModel;
import com.oracle.intelagr.entity.Role;
import com.oracle.intelagr.entity.User;
import com.oracle.intelagr.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @RequestMapping("/list")
    public String list(Role role,Map m, PageModel pageModel){
        pageModel.setData(role);
        roleService.queryForPage(pageModel);
        m.put("pageModel",pageModel);
        return "/role/roleList";
    }
    @RequestMapping("/add")
    public String add(){
        return "/role/addRole";
    }
    @RequestMapping("/save")
    @ResponseBody
    public JsonResult save(Role role, HttpServletRequest request){
        BaseModel baseModel = CommonUtil.getBaseModel(request);
        role.setCreateDate(baseModel.getCreateDate());
        role.setCreateUserId(baseModel.getCreateUserId());
        role.setUpdateDate(baseModel.getUpdateDate());
        role.setUpdateUserId(baseModel.getUpdateUserId());
        roleService.save(role);
        return new JsonResult(true);
    }
    @RequestMapping("/editRoleInit")
    public String editRoleInit(Map m,int id){
        Role role = roleService.queryById(id);
        m.put("role",role);
        return "/role/editRole";
    }
    @RequestMapping("/update")
    @ResponseBody
    public JsonResult update(Role role){
        roleService.update(role);
        return new JsonResult(true);
    }
}
