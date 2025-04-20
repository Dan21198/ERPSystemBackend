package osu.employee.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import osu.employee.model.Employee;
import osu.employee.repository.EmployeeRepository;

import java.util.List;

@Component
public class EmployeeContractExpirationScheduler {

    private final EmployeeRepository employeeRepository;

    public EmployeeContractExpirationScheduler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


    @Scheduled(cron = "0 0 0 * * ?")// Run every day at midnight
    @Transactional
    public void checkContractExpirations() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            boolean previousStatus = employee.isContractAboutToExpire();
            employee.updateContractExpirationStatus();

            if (employee.isContractAboutToExpire() != previousStatus) {
                employeeRepository.save(employee);
            }
        }
    }
}