package com.endorodrigo.ZonaFitSwing.repositorio;

import com.endorodrigo.ZonaFitSwing.modelo.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepositorio extends JpaRepository<Cliente,Integer> {
}
