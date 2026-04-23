package com.jcanseco.inventoryapi.identity.users.application;

import com.jcanseco.inventoryapi.identity.users.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;






@Service
public class AuthenticatedUserServiceImpl implements AuthenticatedUserService {
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  (User) authentication.getPrincipal();
    }
}






