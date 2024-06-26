package com.hacken.csv_api.repositories;

import com.hacken.csv_api.entities.Csv;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsvRepository extends JpaRepository<Csv, Long> {
}
