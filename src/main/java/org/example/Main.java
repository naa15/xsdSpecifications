package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        String filename = "Payment Request Payload.csv";
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
                        if(s.length() != 0 && !s.startsWith("-")) {
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
//                    && ! p.isBoolean() && !p.isDecimal()){
//                    System.out.println("start" + p.getType() + "end");
//                }

//                if(p.isTextWithLength()) {
//                    System.out.println(p.getRegex());
//                }
//                if(p.isTextWithLength()) {
//                    System.out.println("Tag: " + p.getTag() + "\nType is: " + p.getType()+ "\nMax Length is: " + p.getMaxLength() + "\nMin Length is: " +p.getMinLength()
//                        + "\nRegex:" + p.getRegex());
//                }
//                if (p.isTextWithLength()) {
//                    System.out.println(p.getTag() + "  "  + p.getLength());
//                }
                //                System.out.println(p.toString());

            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

//    private static void xmlBeansTest(String xsdFile) throws XmlException, IOException {
//        SchemaTypeSystem sts = XmlBeans.compileXsd(
//                new XmlObject[] { XmlObject.Factory.parse(new File(xsdFile)) }, XmlBeans.getBuiltinTypeSystem(), null);
//
//        for (int i = 0; i < sts.globalElements().length; i++) {
//            SchemaGlobalElement globalElement = sts.globalElements()[i];
//            SchemaType type = globalElement.getType();
//            System.out.println(globalElement.getName() + " = " + type.getName());
//
//            for (int j = 0; j < type.getProperties().length; j++) {
//                SchemaProperty property = type.getProperties()[j];
//                System.out.println("\t" + property.getName() +
//                        " [" + property.getMinOccurs() + ", " + property.getMaxOccurs() + "]" +
//                        " = " + property.getType().getName());
//            }
//        }
//    }
}