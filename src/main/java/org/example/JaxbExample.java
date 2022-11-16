package org.example;
import java.io.File;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
//import com.howtodoinjava.demo.model.Employee;

public class JaxbExample
{
    public static void main(String[] args)
    {
        String xmlFile = "employee.xml";
        String xsdFile = "employee.xsd";

        jaxbXmlFileToObject(xmlFile, xsdFile);
    }

    private static void jaxbXmlFileToObject(String xmlFile, String xsdFile) {

        JAXBContext jaxbContext;

        try
        {
            //Get JAXBContext
            jaxbContext = JAXBContext.newInstance(Employee.class);

            //Create Unmarshaller
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            //Setup schema validator
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema employeeSchema = sf.newSchema(new File(xsdFile));
            jaxbUnmarshaller.setSchema(employeeSchema);

            System.out.println("Before unmarshalling");

//
            //Unmarshal xml file
            Employee employee = (Employee) jaxbUnmarshaller.unmarshal(new File(xmlFile));

            System.out.println("After unmarshalling");
            System.out.println(employee);
        }
        catch (JAXBException | SAXException e)
        {
            e.printStackTrace();
        }
    }
}