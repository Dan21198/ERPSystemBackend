package osu.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.contract.model.Contract;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}
