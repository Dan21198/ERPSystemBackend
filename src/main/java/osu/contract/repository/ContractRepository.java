package osu.contract.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import osu.contract.model.Contract;
import osu.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    Optional<Contract> findByIdAndCreatedBy(Long id, User authenticatedUser);

    List<Contract> findByCreatedBy(User createdBy);
}
