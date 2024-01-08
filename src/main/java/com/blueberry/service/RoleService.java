package com.blueberry.service;

import com.blueberry.model.acc.Role;

public interface RoleService extends GenericService<Role>{
    Role findByName(String roleName);
}
