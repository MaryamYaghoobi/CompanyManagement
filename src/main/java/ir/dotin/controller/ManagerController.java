package ir.dotin.controller;

import ir.dotin.entity.CategoryElement;
import ir.dotin.entity.Employee;
import ir.dotin.service.*;
import ir.dotin.share.Validation;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/ManagerController")
public class ManagerController extends HttpServlet {
    ManagerService managerService = new ManagerService();
    LeavesService leavesService = new LeavesService();
    EmailService emailService = new EmailService();
    EmployeeService employeeService = new EmployeeService();
    SearchCategoryElement searchCategoryElement = new SearchCategoryElement();

    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = null;
        action = request.getParameter("action");
        switch (action) {
            case "addUser":
                addUser(request, response);
                break;
            case "getAllActiveEmployees":
                getAllActiveEmployees(request, response);
                break;
            case "delete":
                delete(request, response);
                break;
            case "search":
                search(request, response);
                break;
            case "editAndAppointmentOfManager":
                editAndAppointmentOfManager(request, response);
                break;
            case "updateProfile":
                updateProfile(request, response);
            case "insertEmployee":
                insertEmployee(request, response);
                break;
            case "RegisteredLeaves":
                RegisteredLeaves(request, response);
                break;
            case "LeaveConfirmation":
                LeaveConfirmation(request, response);
                break;
            case "rejectLeave":
                rejectLeave(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
        }
    }
//------------------------------------------------------------


    public void rejectLeave(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        long leaveId = Long.parseLong(request.getParameter("leaveId"));
        leavesService.rejectionLeave(leaveId);
        System.out.println("The leave request was rejected");
        rejectLeave(request, response);
    }

    public void LeaveConfirmation(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long leaveId = Long.parseLong(request.getParameter("leaveId"));
        leavesService.LeaveConfirmation(leaveId);
        System.out.println("The leave request was approved");
        RegisteredLeaves(request, response);
    }

    public void RegisteredLeaves(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        Employee manager = managerService.
                searchUsername((String) request.getSession().getAttribute("username"));
        List<Employee> RegisteredLeaves = managerService.RegisteredLeaves((manager));
        request.setAttribute("RegisteredLeaves", RegisteredLeaves);
        RequestDispatcher rs = request.getRequestDispatcher("leavesManagement.jsp");
        rs.forward(request, response);
    }


    public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Employee employee = new Employee();
        String userName = request.getParameter("username");
        Validation usernameValidation = new Validation();
        if (usernameValidation.checkUsername(userName)) {
            request.getSession().setAttribute("invalidationUsername",
                    request.getParameter("username"));
        } else {
            employee.setUsername(userName);
        insertEmployee(request, response);

       /* long employeeId = employee.getId();
        long lastVersion = employee.getC_version();
        employeeService.updateVersion(employeeId, lastVersion);*/
        String firstName = request.getParameter("firstName");
        employee.setFirstName(firstName);
        String lastName = request.getParameter("lastName");
        employee.setLastName(lastName);
        String email = request.getParameter("email");
        employee.setEmail(email);
        String fatherName = request.getParameter("fatherName");
        employee.setFatherName(fatherName);
        String dateOfBirth = request.getParameter("dateOfBirth");
        employee.setDateOfBirth(dateOfBirth);


            String password = request.getParameter("password");
            employee.setPassword(password);
            CategoryElement role = searchCategoryElement.findCategoryElement
                    (request.getParameter("role"));
            employee.setRole(role);
            String Status = request.getParameter("employeeStatus");
            CategoryElement employeeStatus = searchCategoryElement.findCategoryElement(Status);
            employee.setEmployeeStatus(employeeStatus);
            String g = request.getParameter("gender");
            CategoryElement gender = searchCategoryElement.findCategoryElement(g);
            employee.setGender(gender);
            String[] managerDetail = request.getParameter("getManagerDetail").split("  ");
            employee.setManager(managerService.getManagerDetail(managerDetail[0], managerDetail[1]));
            managerService.addUser(employee);
            getAllActiveEmployees(request, response);
       /* String strLastVersion = String.valueOf(lastVersion);
        String strGetVersion = String.valueOf(employee.getC_version());
        if (!strGetVersion.equals(strLastVersion)) {
            System.out.println("Synchronization has occurred");
        }*/
        }
    }

    public void insertEmployee(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Employee employee = new Employee();
        if (request.getSession().getAttribute("invalidationUsername") != null) {
            request.getSession().removeAttribute("invalidationUsername");
        }
        List<String> allManager = allManager();
        request.setAttribute("managerList", allManager);
       /* long employeeId = 1;
        long lastVersion = employee.getC_version();
        employeeService.updateVersion(employeeId, lastVersion);*/
        RequestDispatcher rs = request.getRequestDispatcher("insertEmployee.jsp");
        rs.forward(request, response);
      /*  String strLastVersion = String.valueOf(lastVersion);
        String strGetVersion = String.valueOf(employee.getC_version());
        if (!strGetVersion.equals(strLastVersion)) {
            System.out.println("Synchronization has occurred");
        }*/
    }

    public List<String> allManager() {
        List<String> managerInformation = new ArrayList<>();
        List<Employee> allManager = managerService.allManager();
        for (Employee manager : allManager) {
            String managerName = manager.getFirstName() + "  " + manager.getLastName();
            managerInformation.add(managerName);
        }
        return managerInformation;
    }


    public void getAllActiveEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("AllActiveEmployees", managerService.getAllActiveEmployees());
        RequestDispatcher rs = request.getRequestDispatcher("employeeManagement.jsp");
        rs.forward(request, response);
    }

    //inactive employee
    public void delete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long employeeId = Long.parseLong(request.getParameter("employeeId"));
        managerService.delete(employeeId);
        getAllActiveEmployees(request, response);
    }

    public void editAndAppointmentOfManager(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> allManager = allManager();
        request.setAttribute("managerList", allManager);
        String id = request.getParameter("employeeId");
        long employeeId = Long.parseLong(id);
        Employee employee = managerService.searchId(employeeId);
        long empId = 1;
        long lastVersion = employee.getC_version();
        employeeService.updateVersion(empId, lastVersion);
        Employee manager = employee.getManager();
        request.setAttribute("Manager : ", manager.getFirstName() + "  " + manager.getLastName());
        request.setAttribute("Employee : ", employee);
        RequestDispatcher rs = request.getRequestDispatcher("editAndAppointmentOfManager.jsp");
        rs.forward(request, response);
        String strLastVersion = String.valueOf(lastVersion);
        String strGetVersion = String.valueOf(employee.getC_version());
        if (!strGetVersion.equals(strLastVersion)) {
            System.out.println("Synchronization has occurred");
        }
    }

    public void updateProfile(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long employeeId = Long.parseLong(request.getParameter("id"));
        Employee employee = employeeService.getUserDetails(employeeId);
      /*  long empId = 1;
        long lastVersion = employee.getC_version();
        employeeService.updateVersion(empId, lastVersion);*/
        String firstName = request.getParameter("firstName");
        employee.setFirstName(firstName);
        String lastName = request.getParameter("lastName");
        employee.setLastName(lastName);
        String email = request.getParameter("email");
       /* Pattern pattern = Pattern.compile("\\b[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b");
        String matcher = String.valueOf(pattern.matcher(email));*/
        employee.setEmail(email);
        String Status = request.getParameter("employeeStatus");
        CategoryElement employeeStatus = searchCategoryElement.findCategoryElement(Status);
        employee.setEmployeeStatus(employeeStatus);
        String[] managerDetail = request.getParameter("getManagerDetail").split("  ");
        employee.setManager(managerService.getManagerDetail(managerDetail[0], managerDetail[1]));
        managerService.updateUserDetails(employee);
        getAllActiveEmployees(request, response);
       /* String strLastVersion = String.valueOf(lastVersion);
        String strGetVersion = String.valueOf(employee.getC_version());
        if (!strGetVersion.equals(strLastVersion)) {
            System.out.println("Synchronization has occurred");
        }*/

    }

    public void search(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Employee employee = new Employee();
        String firstName = request.getParameter("firstName");
        employee.setFirstName(firstName);
        String lastName = request.getParameter("lastName");
        employee.setLastName(lastName);
        String username = request.getParameter("username");
        employee.setUsername(username);
        List<Employee> employees = managerService.searchEmployee(employee);
        request.setAttribute("employeeList", employees);
        request.setAttribute("firstName", request.getParameter("firstName"));
        request.setAttribute("lastName", request.getParameter("lastName"));
        request.setAttribute("username", request.getParameter("username"));
        RequestDispatcher rs = request.getRequestDispatcher("employeeManagement.jsp");
        rs.forward(request, response);

    }

    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().removeAttribute("username");
        request.getSession().invalidate();
        response.sendRedirect("login.jsp");
    }

}