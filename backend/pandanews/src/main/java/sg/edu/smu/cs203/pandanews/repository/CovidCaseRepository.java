package sg.edu.smu.cs203.pandanews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.edu.smu.cs203.pandanews.model.CovidStats.CovidCase;

public interface CovidCaseRepository extends JpaRepository<CovidCase, Integer> {
}
