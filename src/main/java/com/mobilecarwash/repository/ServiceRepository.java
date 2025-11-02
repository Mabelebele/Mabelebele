package com.mobilecarwash.repository;

import com.mobilecarwash.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByActiveTrue();
    
    List<Service> findByCategory(Service.ServiceCategory category);
    
    List<Service> findByActiveTrueAndCategory(Service.ServiceCategory category);
}
