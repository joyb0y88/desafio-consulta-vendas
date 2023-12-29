package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT new com.devsuperior.dsmeta.dto.SaleReportDTO(obj) " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :minLocalDate AND :maxLocalDate " +
            "AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :sellerName, '%'))")
    Page<SaleReportDTO> searchReport(LocalDate minLocalDate, LocalDate maxLocalDate, String sellerName, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT tb_seller.name AS sellerName, SUM(tb_sales.amount) AS total " +
            "FROM tb_seller " +
            "INNER JOIN tb_sales ON tb_sales.seller_id = tb_seller.id " +
            "WHERE tb_sales.date BETWEEN :minLocalDate AND :maxLocalDate " +
            "GROUP BY tb_seller.name")
    List<SaleSummaryProjection> searchSummary(LocalDate minLocalDate, LocalDate maxLocalDate);
}
