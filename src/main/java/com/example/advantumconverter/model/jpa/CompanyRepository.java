package com.example.advantumconverter.model.jpa;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    public Company getCompaniesByCompanyName(String conpanyName);

    public Company findCompanyByCompanyId(Long companyId);
    @Override
    public List<Company> findAll();
}
