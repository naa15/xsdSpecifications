package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        System.out.println("Hello world!");
        String filename = "Payment Request Payload.csv";
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            myReader.useDelimiter(",");
            while (myReader.hasNext()) {
//                String data = myReader.next();
                PaymentRequestPayload p = new PaymentRequestPayload();
                String lvl = myReader.next();
                p.setLvl(Integer.valueOf(lvl));
                p.setName(myReader.next());
                p.setTag(myReader.next());
                p.setOccur(myReader.next());
                String type = myReader.next();
                if(p.getTag().equals("")) {
                    p.setCode(type);
                } else {
                    while(true) {
                        String s = myReader.next();
                        if(s.length() != 0 && !s.startsWith("-")) {
                            type = type + s;
                        } else {
                            p.setType(type);
                            p.setValidationRule(s);
                            break;
                        }
                    }
                }
                p.setPath(myReader.next());
                String definition = myReader.next();
                while(true) {
                    String s = myReader.next();
                    if(s.length() != 0) {
                        definition = definition + s;
                    } else {
                        p.setDefinition();
                        break;
                    }
                }
                p.setFieldDefinition(myReader.next());

                System.out.println(p.toString());
                myReader.next();
//                System.out.println(data);
//                System.out.println("new one:");
//                allData.add(data);
            }
//            convert();

            // /Request/RequestPayload/CstmrCdtTrfInitn/PmtInf/CdtTrfTxInf/RmtInf/Strd/AddtlRmtInf
            //
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