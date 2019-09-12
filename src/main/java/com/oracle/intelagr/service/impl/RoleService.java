package com.oracle.intelagr.service.impl;

import com.oracle.intelagr.common.PageModel;
import com.oracle.intelagr.entity.Role;
import com.oracle.intelagr.entity.User;
import com.oracle.intelagr.mapper.RoleMapper;
import com.oracle.intelagr.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleService implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<Role> selectAll() {
        return roleMapper.selectAll();
    }

    @Override
    public void queryForPage(PageModel pageModel) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("index",(pageModel.getPage()-1)*pageModel.getPageSize());
        map.put("pageSize",pageModel.getPageSize());
        Role role = (Role) pageModel.getData();
        map.put("roleCode",role.getRoleCode());
        map.put("roleName",role.getRoleName());
        pageModel.setTotalCount(roleMapper.count(map));
        pageModel.setResult(roleMapper.select(map));
    }

    @Override
    public Role queryById(int id) {
        return roleMapper.selectById(id);
    }

    @Override
    public void delete(int[] ids) {

    }

    @Override
    public void update(Role role) {
        roleMapper.update(role);
    }

    @Override
    public void save(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void saveRoleAuth(String roleCode, String[] funIds, User user) {

    }
}
