package com.devsuperior.dsmeta.services;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.dto.SaleReportDTO;
import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.projections.SaleSummaryProjection;
import com.devsuperior.dsmeta.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> findReport(String minDate, String maxDate, String sellerName, Pageable pageable) {

		LocalDate maxLocalDate = maxDate.equals("") ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()) : LocalDate.parse(maxDate);
		LocalDate minLocalDate = minDate.equals("") ? maxLocalDate.minusYears(1L) : LocalDate.parse(minDate);

		return repository.searchReport(minLocalDate, maxLocalDate, sellerName, pageable);

	}

	public List<SaleSummaryDTO> findSummary(String minDate, String maxDate) {

		LocalDate maxLocalDate = maxDate.equals("") ? LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault()) : LocalDate.parse(maxDate);
		LocalDate minLocalDate = minDate.equals("") ? maxLocalDate.minusYears(1L) : LocalDate.parse(minDate);

		List<SaleSummaryProjection> result = repository.searchSummary(minLocalDate, maxLocalDate);
		return result.stream().map(x -> new SaleSummaryDTO(x)).collect(Collectors.toList());
	}
}
