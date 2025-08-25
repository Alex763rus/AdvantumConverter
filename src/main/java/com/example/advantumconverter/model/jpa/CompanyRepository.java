package com.example.advantumconverter.model.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    Company getCompaniesByCompanyName(String conpanyName);

    Company findCompanyByCompanyId(Long companyId);
    @Override
    List<Company> findAll();
}
