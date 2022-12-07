//package org.example;
//
//import javax.xml.bind.annotation.*;
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//
//@XmlRootElement(name = "employees")
//@XmlSeeAlso({Employee.class})
//@XmlAccessorType(XmlAccessType.PROPERTY)
//public class Employees implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    private ArrayList<Employee> list;
//
//    public Employees() {
//        super();
//        list = new ArrayList<Employee>();
//    }
//
//    //Setters and Getters
//
//    public void setEmployees(List<Employee> employee) {
//        for (int i = 0; i < employee.size(); i++) {
//            this.list.add(employee.get(i));
//        }
//    }
//
//    @XmlElement(name = "employee")
//    public List<Employee> getEmployees() {
//        return list;
//    }
//
//    @Override
//    public String toString() {
//        String res = "";
//
//        for (int i=0; i<list.size(); i++) {
//            res += list.get(i).toString();
//            res += '/';
//        }
//        return res;
//    }
//}
