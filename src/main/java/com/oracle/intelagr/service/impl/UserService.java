package com.oracle.intelagr.service.impl;

import com.oracle.intelagr.common.MD5Util;
import com.oracle.intelagr.common.PageModel;
import com.oracle.intelagr.entity.Function;
import com.oracle.intelagr.entity.Role;
import com.oracle.intelagr.entity.User;
import com.oracle.intelagr.entity.UserRole;
import com.oracle.intelagr.mapper.UserMapper;
import com.oracle.intelagr.mapper.UserRoleMapper;
import com.oracle.intelagr.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class UserService implements IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public List<User> login(User user) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("userID",user.getUserID());
        map.put("password", MD5Util.getMD5Code(user.getPassword()));
        List<User> list = userMapper.select(map);
        return list;
    }

    @Override
    public List<Map> getFunction(String userID) {
        User user = userMapper.selectById(userID);
        List<Map> list = new ArrayList<Map>();
        Map modelMap = new HashMap();
        for(Role role:user.getRoles()){
            for (Function function:role.getFunctions()){
                if(!modelMap.containsKey(function.getModuleCode())){
                    Map map  = new HashMap();
                    list.add(map);
                    map.put("parent",function.getModuleName());
                    List child = new ArrayList();
                    child.add(function);
                    map.put("child",child);
                    modelMap.put(function.getModuleCode(),map);
                }else {
                    Map map = (Map)modelMap.get(function.getModuleCode());
                    List<Function> child = (List<Function>)map.get("child");
                    child.add(function);
                }
            }
        }
        return list;
    }

    @Override
    public void queryForPage(PageModel pageModel) {
        User user = (User)pageModel.getData();
        Map<String,Object> map =new HashMap<String, Object>();
        map.put("userID",user.getUserID());
        map.put("userName",user.getUserName());
        map.put("userType",user.getUserType());
        map.put("index",(pageModel.getPage()-1)*pageModel.getPageSize());
        map.put("pageSize",pageModel.getPageSize());
        List<User> list = userMapper.select(map);
        pageModel.setResult(list);
        pageModel.setTotalCount(userMapper.count(map));
    }

    @Override
    @Transactional
    public void save(User user) {
        userMapper.insert(user);
        for(Role role:user.getRoles()){
            UserRole userRole = new UserRole();
            userRole.setRoleCode(role.getRoleCode());
            userRole.setUserID(user.getUserID());
            userRole.setCreateDate(user.getCreateDate());
            userRole.setCreateUserId(user.getUserID());
            userRole.setUpdateUserId(user.getUserID());
            userRole.setUpdateDate(user.getUpdateDate());
            userRoleMapper.insert(userRole);
        }
    }

    @Override
    public User selectById(String userID) {
        return userMapper.selectById(userID);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    @Transactional
    public void delete(String userID) {
        User user = userMapper.selectById(userID);
        user.setDeleteFlag("Y");
        userMapper.update(user);
        UserRole userRole = new UserRole();
        userRole.setUserID(userID);
        userRole.setDeleteFlag("Y");
        userRoleMapper.update(userRole);;
    }

    @Override
    public void resetPwd(String userID, String password) {

    }

    @Override
    public void startUse(String userID) {

    }

    @Override
    public void endUse(String userID) {

    }
}
