package com.example.advantumconverter.model.jpa;

import org.springframework.data.repository.CrudRepository;

public interface CompanyRepository extends CrudRepository<Company, Long> {

    public Company getCompaniesByCompanyName(String conpanyName);
}
