package com.example.advantumconverter.model.jpa;

import com.example.advantumconverter.enums.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findUserByUserRole(UserRole userRole);
    User findUserByChatId(Long chatId);

    List<User> findUserByCompanyAndAndUserRole(Company company, UserRole userRole);
    List<User> findUserByCompany(Company company);
    @Override
    List<User> findAll();
}
