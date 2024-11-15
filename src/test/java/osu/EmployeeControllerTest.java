package osu;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentMatchers;
import osu.controller.EmployeeController;
import osu.dto.EmployeeDTO;
import osu.services.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;


@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employeeDTO = new EmployeeDTO();
        employeeDTO.setPersonalNumber(1L);
        employeeDTO.setFirstName("John");
        employeeDTO.setLastName("Doe");
        employeeDTO.setTitle("Dr.");
        employeeDTO.setContractStart(new Date());
        employeeDTO.setContractEnd(null);
        employeeDTO.setWorkloadPercentage(100.0);
        employeeDTO.setSalaryGrade("A1");
        employeeDTO.setTariffAmount(5000.0);
        employeeDTO.setPerformanceBonus(1000.0);
        employeeDTO.setGrossSalary(6000.0);
    }

    @Test
    void createEmployee_shouldReturnCreatedEmployee() throws Exception {
        when(employeeService.createEmployee(ArgumentMatchers.any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/v1/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.personalNumber", is(employeeDTO.getPersonalNumber().intValue())))
                .andExpect(jsonPath("$.firstName", is(employeeDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeDTO.getLastName())));

        verify(employeeService, times(1)).createEmployee(ArgumentMatchers.any(EmployeeDTO.class));
    }

    @Test
    void getEmployee_shouldReturnEmployee() throws Exception {
        when(employeeService.getEmployee(1L)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/v1/employees/{personalNumber}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personalNumber", is(employeeDTO.getPersonalNumber().intValue())))
                .andExpect(jsonPath("$.firstName", is(employeeDTO.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employeeDTO.getLastName())));

        verify(employeeService, times(1)).getEmployee(1L);
    }

    @Test
    void getAllEmployees_shouldReturnListOfEmployees() throws Exception {
        List<EmployeeDTO> employees = Arrays.asList(employeeDTO);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].personalNumber", is(employeeDTO.getPersonalNumber().intValue())));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void updateEmployee_shouldReturnUpdatedEmployee() throws Exception {
        employeeDTO.setFirstName("Jane");
        when(employeeService.updateEmployee(eq(1L), any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(put("/api/v1/employees/{personalNumber}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is("Jane")));

        verify(employeeService, times(1)).updateEmployee(eq(1L), any(EmployeeDTO.class));
    }

    @Test
    void deleteEmployee_shouldReturnNoContent() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/v1/employees/{personalNumber}", 1L))
                .andExpect(status().isNoContent());

        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
