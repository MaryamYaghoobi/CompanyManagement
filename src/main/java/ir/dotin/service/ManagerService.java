package ir.dotin.service;

import ir.dotin.entity.Employee;
import ir.dotin.repository.ManagerDao;

import java.util.List;

public class ManagerService {
    public List<Employee> RegisteredLeaves(Employee manager) {
        ManagerDao managerDao = new ManagerDao();
        return managerDao.RegisteredLeaves(manager);
    }

    public List<Employee> getAllActiveEmployees() {
        ManagerDao managerDao = new ManagerDao();
        return managerDao.getAllActiveEmployees();
    }

    public List<Employee> allManager() {
        ManagerDao managerDao = new ManagerDao();
        return managerDao.allManager();
    }

    public List<Employee> searchEmployee(Employee employee) {
        ManagerDao managerDao = new ManagerDao();
        return managerDao.search(employee);
    }

    public Employee searchId(long id) {
        ManagerDao managerDao = new ManagerDao();
        return managerDao.searchId(id);
    }

    public int searchAllUsername(String username) {
        ManagerDao managerDao = new ManagerDao();
        return managerDao.searchAllUsername(username);
    }

    public Employee searchUsername(String username) {
        ManagerDao managerDao = new ManagerDao();
        return managerDao.searchUsername(username);
    }

    public Employee login(String username, String password) {
        ManagerDao managerDao = new ManagerDao();
        Employee employee = managerDao.login(username, password);
        return employee;
    }

}


