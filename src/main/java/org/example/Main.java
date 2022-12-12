package org.example;

import org.apache.xmlbeans.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
public class Main {

    private static ArrayList<PaymentRequestPayload> csvInfo = new ArrayList<PaymentRequestPayload>();
    private static ArrayList<String> results = new ArrayList<String>();
    public static void main(String[] args) throws XmlException, IOException {
        String filename = "new-Payment Request Payload.csv";
        readCSVfile(filename);

        String xsdFile = "pain.001.001.09.xsd";
        readXSDFile(xsdFile);

        String outputFile = "output.txt";
        writeInFile(outputFile);
    }

    private static void writeInFile(String outputFile) {
        try {
            File myObj = new File(outputFile);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
            try {
                FileWriter myWriter = new FileWriter(outputFile);
                for (int i = 0; i < results.size(); i++) {
                    myWriter.write(results.get(i));
                    myWriter.write('\n');
                }
                myWriter.close();
                System.out.println("Successfully wrote to the output file.");
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
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
                    if(!s.endsWith("\n")) {
                        definition = definition + s;
                    } else{
                        break;
                    }
                }
                p.setDefinition(definition);

                csvInfo.add(p);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File name that was provided doesn't exist.");
            e.printStackTrace();
        }

    }

    private static void readXSDFile(String xsdFile) throws XmlException, IOException {
        SchemaTypeSystem sts = XmlBeans.compileXsd(
                new XmlObject[] { XmlObject.Factory.parse(new File(xsdFile)) }, XmlBeans.getBuiltinTypeSystem(), null);

        List allSeenTypes = new ArrayList();
        allSeenTypes.addAll(Arrays.asList(sts.documentTypes()));
        allSeenTypes.addAll(Arrays.asList(sts.attributeTypes()));
        allSeenTypes.addAll(Arrays.asList(sts.globalTypes()));

        for (int i = 0; i < allSeenTypes.size(); i++)
        {
            SchemaType sType = (SchemaType)allSeenTypes.get(i);

            if(sType.getEnumerationValues() != null) {
                for (int j = 0; j < sType.getEnumerationValues().length; j++) {
                    String value = sType.getEnumerationValues()[j].getStringValue();

                    PaymentRequestPayload p = new PaymentRequestPayload();
                    p.setValue(value);

                    if (! listContains(p)) {
                        String result = "Value: " + value + " of type: " + formatName(sType.getName().toString()) + " is not in the CSV file ";
                        results.add(result);
                    }
                }
            }

            for (int j = 0; j < sType.getProperties().length; j++) {
                String name = sType.getProperties()[j].getName().toString();

                PaymentRequestPayload p = new PaymentRequestPayload();
                p.setTag(formatName(name));

                if (! listContains(p)) {
                    String result = "Property " + j + " name: " + name + " is not in the arraylist ";
                    results.add(result);
                }
            }

            allSeenTypes.addAll(Arrays.asList(sType.getAnonymousTypes()));
        }
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
            PaymentRequestPayload q = csvInfo.get(i);
            String tag = q.getTag();

            if(! tag.equals("")) {
                if (tag.equals(p.getTag())) {
                    return true;
                }
            } else {
                if (q.getValue().equals(p.getValue())) return true;
            }
        }

        return false;
    }

}