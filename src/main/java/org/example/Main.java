package org.example;

import org.apache.xmlbeans.*;

import javax.xml.validation.Schema;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class Main {

    private static ArrayList<PaymentRequestPayload> csvInfo = new ArrayList<PaymentRequestPayload>();
    public static void main(String[] args) throws XmlException, IOException {
        String filename = "Payment Request Payload.csv";
        readCSVfile(filename);

//        String xsdFile = "employee.xsd";

        String xsdFile = "pain.001.001.11.xsd";
        xmlBeansTest(xsdFile);
    }

    private static void readCSVfile(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            myReader.useDelimiter(",");
            while (myReader.hasNext()) {
                PaymentRequestPayload p = new PaymentRequestPayload();
                String lvl = myReader.next();
                p.setLvl(Integer.valueOf(lvl));
                p.setName(myReader.next());
                p.setTag(myReader.next());
                p.setOccur(myReader.next());
                String type = myReader.next();
                if(p.getTag().equals("")) {
                    p.setValue(type);
                } else {
                    while(true) {
                        String s = myReader.next();
                        if(s.length() != 0 && ! s.startsWith("-")) {
                            type = type + ',' + s;
                        } else {
                            p.setType(type);
                            p.setValidationRule(s);
                            break;
                        }
                    }
                }
                p.setPath(myReader.next());
                String definition = myReader.next();
                while(myReader.hasNext()) {
                    String s = myReader.next();
//                    if(! s.equals("") && !s.equals("\r\n") && !s.equals("\n") ) {
                    if(!s.endsWith("\n")) {
                        definition = definition + s;
                    } else{
                        break;
                    }
                }
                p.setDefinition(definition);

//                if(! p.isDate() && ! p.isChoice() && ! p.getType().equals("")
//                    && ! p.isBoolean() && ! p.isDecimal() && ! p.isTextWithLength()){
//                    System.out.println("start" + p.getType() + "end");
//                }
//                System.out.println(p.toString());

                csvInfo.add(p);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    private static void xmlBeansTest(String xsdFile) throws XmlException, IOException {
        SchemaTypeSystem sts = XmlBeans.compileXsd(
                new XmlObject[] { XmlObject.Factory.parse(new File(xsdFile)) }, XmlBeans.getBuiltinTypeSystem(), null);

        List allSeenTypes = new ArrayList();
        allSeenTypes.addAll(Arrays.asList(sts.documentTypes()));
        allSeenTypes.addAll(Arrays.asList(sts.attributeTypes()));
        allSeenTypes.addAll(Arrays.asList(sts.globalTypes()));
        for (int i = 0; i < allSeenTypes.size(); i++)
        {
            SchemaType sType = (SchemaType)allSeenTypes.get(i);
            System.out.println("Visiting " + sType.toString());

            if(sType.getEnumerationValues() != null) {
                for (int j = 0; j < sType.getEnumerationValues().length; j++) {
                    String code = sType.getEnumerationValues()[j].getStringValue();
//                    System.out.println(code);

                    PaymentRequestPayload p = new PaymentRequestPayload();
                    p.setType(code);

                    if (! listContains(p)) {
//                        System.out.println("The arraylist contains me");
                        System.out.println(code + " is not in the arraylist ");

                    }
                }
            }

            for (int j = 0; j < sType.getProperties().length; j++) {
                String name = sType.getProperties()[j].getName().toString();
//                System.out.println("Property name: " + j + " " + name);
//                System.out.println("Property type: " + sType.getProperties()[j].getType().getName());

                PaymentRequestPayload p = new PaymentRequestPayload();
                p.setTag(formatName(name));

                if (! listContains(p)) {
                    System.out.println("Property name: " + j + " " + name + " is not in the arraylist ");
//                    System.out.println("Property type: " + sType.getProperties()[j].getType().getName());

//                    System.out.println("The arraylist contains me");
                }

            }

            allSeenTypes.addAll(Arrays.asList(sType.getAnonymousTypes()));
        }

//        System.out.println(sts.getName());

//        SchemaType [] globalTypes = sts.globalTypes();
//
//        for (int i = 0; i < globalTypes.length; i++) {
//            SchemaType arr = globalTypes[i];
//            System.out.println(arr.getName());
////            System.out.println(arr.getProperties());
//            for (int j = 0; j < arr.getProperties().length; j++) {
//                System.out.println(arr.getProperties()[j].getName());
//                System.out.println(arr.getProperties()[j].getType());
//            }
//        }
//        System.out.println(sts.globalElements());
//        for (int i = 0; i < sts.globalTypes().length; i++) {
//            System.out.println("In for cycle");
//            SchemaType globalType = sts.globalTypes();
//            SchemaGlobalElement globalElement = sts.globalElements()[i];
//            SchemaType type = globalElement.getType();
//            System.out.println(globalElement.getName() + " = " + type.getName());
//
//            for (int j = 0; j < type.getProperties().length; j++) {
//                System.out.println("in another for cycle");
//                SchemaProperty property = type.getProperties()[j];
//                System.out.println("\t" + property.getName() +
//                        " [" + property.getMinOccurs() + ", " + property.getMaxOccurs() + "]" +
//                        " = " + property.getType().getName());
//            }
//        }
    }

    private static String formatName(String name) {
        for (int i = 0; i < name.length(); i++) {
            if(name.charAt(i) == '}') {
                return "<" + name.substring(i+1) + ">";
            }
        }
        return name;
    }

    private static boolean listContains(PaymentRequestPayload p) {
        for (int i = 0; i < csvInfo.size(); i++) {
            String tag = csvInfo.get(i).getTag();
            if(! tag.equals("")) {
                if (tag.equals(p.getTag())) return true;
            } else {
                if (csvInfo.get(i).getType().equals(p.getTag())) return true;
            }
        }
        return false;
    }


}