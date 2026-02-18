package br.com.joga_together.repository;

import br.com.joga_together.model.Scheduling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SchedulingRepository extends JpaRepository<Scheduling, UUID> {
}
