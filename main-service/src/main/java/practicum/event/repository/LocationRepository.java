package practicum.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practicum.event.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
