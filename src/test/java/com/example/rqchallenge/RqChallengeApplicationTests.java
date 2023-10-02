package com.example.rqchallenge;

import com.example.rqchallenge.employees.Employee;
import com.example.rqchallenge.employees.EmployeeControllerImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

//todo consider mocking responses from the server to avoid too many requests status (to make this more test friendly create a class that handles all outgoing requests that can be mocked)
@SpringBootTest
class RqChallengeApplicationTests {
    private final EmployeeControllerImpl employeeController = new EmployeeControllerImpl();


    @Test
    void contextLoads() {
        //tests if the context loads, can remain empty.
    }

    @Test
    void testGetAllEmployees() {
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Employee> employees = response.getBody();

        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        assertEquals(24, employees.size());
        assertTrue(employees.stream().anyMatch(employee -> employee.getEmployee_name().equals("Tiger Nixon")));
    }

    @Test
    void testGetEmployeesByNameSearch() {
        String searchString = "Tiger Nixon";

        Employee tigerNixon = new Employee();
        tigerNixon.setId(1);
        tigerNixon.setEmployee_name("Tiger Nixon");
        tigerNixon.setEmployee_salary(320800);
        tigerNixon.setEmployee_age(61);
        tigerNixon.setProfile_image(null);

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch(searchString);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.singletonList(tigerNixon), response.getBody());
    }

    @Test
    void testGetEmployeeById() {
        String employeeId = "1";

        Employee employee = new Employee();
        employee.setId(1);
        employee.setEmployee_name("Tiger Nixon");
        employee.setEmployee_salary(320800);
        employee.setEmployee_age(61);
        employee.setProfile_image(null);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee,response.getBody());
    }

    @Test
    void testGetHighestSalaryOfEmployees() {
        int highestSalary = 725000; //highest salary is 725000 (emp ID 17)

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(highestSalary, response.getBody());
    }

    @Test
    void testGetTopTenHighestEarningEmployeeNames() {
        List<String> topTenNames = new ArrayList<>();
        topTenNames.add("Paul Byrd");
        topTenNames.add("Yuri Berry");
        topTenNames.add("Charde Marshall");
        topTenNames.add("Cedric Kelly");
        topTenNames.add("Tatyana Fitzpatrick");
        topTenNames.add("Brielle Williamson");
        topTenNames.add("Jenette Caldwell");
        topTenNames.add("Quinn Flynn");
        topTenNames.add("Rhona Davidson");
        topTenNames.add("Tiger Nixon");


        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<String> topTenNamesResponse = response.getBody();
        assertNotNull(topTenNamesResponse);

        //check to make sure all the names are in the list
        for (String name : topTenNames) {
            assertTrue(topTenNamesResponse.contains(name));
        }
    }

    @Test
    void testDeleteEmployeeById() {//todo test
        String employeeId = "1";

        ResponseEntity<String> response = employeeController.deleteEmployeeById(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee deleted successfully", response.getBody());
    }

    @Test
    void testCreateEmployee() {
        Map<String, Object> employeeInput  = new HashMap<>() {{
            put("name", "newName");
            put("salary", 100000);
            put("age",31);
        }};

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        Employee createdEmployee = response.getBody();
        assertNotNull(createdEmployee);
        assertEquals("newName", createdEmployee.getEmployee_name());
        assertEquals(100000, createdEmployee.getEmployee_salary());
        assertEquals(31, createdEmployee.getEmployee_age());
    }
}
