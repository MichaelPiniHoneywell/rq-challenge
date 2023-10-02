package com.example.rqchallenge.employees;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.EXPECTATION_FAILED;
import static org.springframework.http.HttpStatus.OK;

public class EmployeeControllerImpl implements IEmployeeController {
    private static final String BASE_URL = "https://dummy.restapiexample.com/";

    /**
     * Retrieves a list of all employees from the API.
     *
     * @return the list of employees.
     * @throws ResponseStatusException if there is an issue with the API request or response.
     */
    private List<Employee> getAllEmployeesResponse() throws ResponseStatusException {
        String uri = BASE_URL + "api/v1/employees";
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<EmployeeListResponse> responseEntity = restTemplate.getForEntity(uri, EmployeeListResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.hasBody()) {
            return responseEntity.getBody().getData();
        } else {
            throw new ResponseStatusException(responseEntity.getStatusCode());
        }

    }

    /**
     * Retrieves a list of all employees and returns it as a ResponseEntity.
     *
     * @return A ResponseEntity containing the list of employees.
     */
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {

        try {
            List<Employee> employees = getAllEmployeesResponse();
            return ResponseEntity.ok(employees);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    /**
     * Retrieves all employees matching name provided.
     *
     * @param searchString The name to search for.
     * @return A ResponseEntity containing the list of employees matching the search criteria.
     */
    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        try {
            List<Employee> employees = getAllEmployeesResponse();
            List<Employee> result = employees.stream().filter(e -> e.getEmployee_name().equals(searchString)).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    /**
     * Retrieves an employee by ID.
     *
     * @param id The ID of the employee to retrieve.
     * @return A ResponseEntity containing the employee with the specified ID.
     */
    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        String uri = BASE_URL + "api/v1/employee/" + id;
        // Create a RestTemplate with JSON message converter
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(uri, EmployeeResponse.class);
        if (response.getStatusCode() == OK && response.hasBody()) {
            Employee employee = response.getBody().getData();
            return ResponseEntity.ok(employee);
        }
        return new ResponseEntity<>(response.getStatusCode());
    }

    /**
     * Retrieves the highest salary among all employees.
     *
     * @return A ResponseEntity containing the highest salary.
     */
    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            List<Employee> employees = getAllEmployeesResponse();
            int highestSalary = 0; // Assuming salaries are non-negative

            for (Employee employee : employees) {
                int salary = employee.getEmployee_salary();
                if (salary > highestSalary) {
                    highestSalary = salary;
                }
            }

            return ResponseEntity.ok(highestSalary);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    /**
     * Retrieves the names of the top ten highest-earning employees.
     *
     * @return A ResponseEntity containing a list of the names of the top ten highest-earning employees.
     */
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<Employee> employees = getAllEmployeesResponse();
            employees.sort(new SortEmployeeBySalary()); //sort the array using comparator

            List<Employee> topTen = employees.subList(0, 10);
            List<String> result = topTen.stream().map(Employee::getEmployee_name).collect(Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getStatus());
        }
    }

    /**
     * Creates an employee with the given name, salary, and age, and returns it as a ResponseEntity.
     *
     * @param employeeInput   map containing details of employee
     * @return A ResponseEntity containing the created employee.
     */
    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        Employee employee = new Employee();
        employee.setEmployee_name((String) employeeInput.get("name"));
        employee.setEmployee_salary((Integer) employeeInput.get("salary"));
        employee.setEmployee_age((Integer) employeeInput.get("age"));

        return createEmployee(employee);
    }

    /**
     * Creates an employee using the provided Employee object.
     *
     * @param employee The Employee object to create.
     * @return A ResponseEntity containing the created employee.
     */
    public ResponseEntity<Employee> createEmployee(Employee employee) {
        String uri = BASE_URL + "api/v1/create";

        //call POST api
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EmployeeResponse> response = restTemplate.postForEntity(uri, employee, EmployeeResponse.class);

        if (response.getStatusCode() == OK && response.hasBody()) {
            Employee createdEmployee = response.getBody().getData();
            return ResponseEntity.ok(createdEmployee);
        }
        return new ResponseEntity<>(response.getStatusCode());


    }

    /**
     * Deletes an employee with the specified ID.
     *
     * @param id The ID of the employee to delete.
     * @return A ResponseEntity containing a status message indicating the result of the deletion.
     */
    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        String uri = BASE_URL + "api/v1/delete/" + id;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(uri);
        if (getEmployeeById(id).getStatusCode() == HttpStatus.NOT_FOUND) {//todo Check status code
            return ResponseEntity.ok("Employee removed Successfully");
        }
        return new ResponseEntity<>("Application failed to remove Employee", EXPECTATION_FAILED);
    }


    /**
     * Helper class extending Comparator interface for sorting Employees by Salary in descending order.
     */
    static class SortEmployeeBySalary implements Comparator<Employee> {
        // descending order
        public int compare(Employee a, Employee b) {
            return b.getEmployee_salary() - a.getEmployee_salary();
        }
    }

}
