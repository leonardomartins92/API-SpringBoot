package com.voluptaria.vlpt.repository;

import com.voluptaria.vlpt.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
