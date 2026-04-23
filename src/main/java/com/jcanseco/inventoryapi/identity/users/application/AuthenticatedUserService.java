package com.jcanseco.inventoryapi.identity.users.application;

import com.jcanseco.inventoryapi.identity.users.domain.User;





public interface AuthenticatedUserService {
    User getCurrentUser();
}






