package com.society.backend.gl.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.society.backend.gl.entity.Vendor;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findBySocietyIdOrderByVendorNameAsc(Long societyId);
}
