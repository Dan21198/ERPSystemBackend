package osu.performanceBonus.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import osu.employee.model.Employee;
import osu.performanceBonus.model.PerformanceBonus;
import osu.performanceBonus.model.PerformanceBonusDTO;
import osu.performanceBonus.mapper.PerformanceBonusMapper;
import osu.employee.repository.EmployeeRepository;
import osu.performanceBonus.repository.PerformanceBonusRepository;
import osu.exception.RecordNotFoundException;

@Service
public class PerformanceBonusServiceImpl implements PerformanceBonusService {

    private final EmployeeRepository employeeRepository;
    private final PerformanceBonusRepository performanceBonusRepository;
    private final PerformanceBonusMapper performanceBonusMapper;

    @Autowired
    public PerformanceBonusServiceImpl(EmployeeRepository employeeRepository,
                                       PerformanceBonusRepository performanceBonusRepository,
                                       PerformanceBonusMapper performanceBonusMapper) {
        this.employeeRepository = employeeRepository;
        this.performanceBonusRepository = performanceBonusRepository;
        this.performanceBonusMapper = performanceBonusMapper;
    }

    @Override
    @Transactional
    public PerformanceBonusDTO addBonus(Long employeeId, PerformanceBonusDTO bonusDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found"));

        PerformanceBonus bonus = performanceBonusMapper.toEntity(bonusDTO);
        bonus.setEmployee(employee);
        PerformanceBonus savedBonus = performanceBonusRepository.save(bonus);

        employee.calculateGrossSalary();
        employeeRepository.save(employee);

        return performanceBonusMapper.toDto(savedBonus);
    }

    @Override
    @Transactional
    public void removeBonus(Long employeeId, Long bonusId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found with id: " + employeeId));

        PerformanceBonus bonus = performanceBonusRepository.findByIdAndEmployeeId(bonusId, employeeId)
                .orElseThrow(() -> new RecordNotFoundException(
                        String.format("Bonus with id %s not found for employee %s", bonusId, employeeId)));

        employee.getPerformanceBonuses().remove(bonus);

        performanceBonusRepository.delete(bonus);

        employee.calculateGrossSalary();
        employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public PerformanceBonusDTO updateBonus(Long employeeId, Long bonusId, PerformanceBonusDTO bonusDTO) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RecordNotFoundException("Employee not found"));

        PerformanceBonus existingBonus = performanceBonusRepository.findById(bonusId)
                .orElseThrow(() -> new RecordNotFoundException("Bonus not found"));

        if (!existingBonus.getEmployee().getId().equals(employeeId)) {
            throw new IllegalArgumentException("Bonus does not belong to the specified employee");
        }

        performanceBonusMapper.updateBonusFromDto(bonusDTO, existingBonus);
        PerformanceBonus updatedBonus = performanceBonusRepository.save(existingBonus);

        employee.calculateGrossSalary();
        employeeRepository.save(employee);

        return performanceBonusMapper.toDto(updatedBonus);
    }

}
