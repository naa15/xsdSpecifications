package org.example;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "employees")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Employees implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Employee> list = new ArrayList<Employee>();

    public Employees() {
        super();
    }

    //Setters and Getters

    public void setEmployees(List<Employee> employee) {
        for (int i = 0; i < employee.size(); i++) {
            this.list.add(employee.get(i));
        }
    }

    @Override
    public String toString() {
        String res = "";

        for (int i=0; i<list.size(); i++) {
            res += list.get(i).toString();
            res += '/';
        }
        return res;
    }
}
