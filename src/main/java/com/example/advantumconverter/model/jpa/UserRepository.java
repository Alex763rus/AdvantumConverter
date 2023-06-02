package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.constant.Constant;
import com.example.advantumconverter.enums.UserRole;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    public List<User> findUserByUserRole(UserRole userRole);
    public User findUserByChatId(Long chatId);
}
