package com.example.readingcomicwebsite.repository;

import com.example.readingcomicwebsite.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleReposiroty extends JpaRepository<Role, Integer> {
}
