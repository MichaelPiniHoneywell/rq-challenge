package com.example.rqchallenge.employees;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class DummyService {
    private static final String BASE_URL = "https://dummy.restapiexample.com/";
    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<EmployeeListResponse> getEmployees() {
        String uri = BASE_URL + "api/v1/employees";
        return restTemplate.getForEntity(uri, EmployeeListResponse.class);
    }

    public ResponseEntity<EmployeeResponse> getEmployeeById(String id) {
        String uri = BASE_URL + "api/v1/employee/" + id;
        return restTemplate.getForEntity(uri, EmployeeResponse.class);
    }

    public ResponseEntity<EmployeeResponse> createEmployee(Employee employee) {
        String uri = BASE_URL + "api/v1/create";
        // call POST API
        return restTemplate.postForEntity(uri, employee, EmployeeResponse.class);
    }

    public void deleteEmployee(String id) {
        String uri = BASE_URL + "api/v1/delete/" + id;
        restTemplate.delete(uri);
    }
}