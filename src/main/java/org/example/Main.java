package org.example;

import org.apache.xmlbeans.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {

    private static ArrayList<PaymentRequestPayload> csvInfo = new ArrayList<PaymentRequestPayload>();

    private static ArrayList<String> results = new ArrayList<String>();
    private static ArrayList<PaymentRequestPayload> treeFromCSV = new ArrayList<PaymentRequestPayload>();
    private static ArrayList<PaymentRequestPayload> treeFromXSD = new ArrayList<PaymentRequestPayload>();
    public static void main(String[] args) throws XmlException, IOException {
//        String filename = "new-Payment Request Payload.csv";
//        readCSVfile(filename);
//
//        buildTree();
//
//        String xsdFile = "pain.001.001.09.xsd";
//        processXSDFile(xsdFile);

//        printTree(treeFromCSV, "");
//        readXSDFile(xsdFile);

//        String outputFile = "output.txt";
//        writeInFile(outputFile);
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

//    private static void readXSDFile(String xsdFile) throws XmlException, IOException {
//        SchemaTypeSystem sts = XmlBeans.compileXsd(
//                new XmlObject[] { XmlObject.Factory.parse(new File(xsdFile)) }, XmlBeans.getBuiltinTypeSystem(), null);
//
//        List allSeenTypes = new ArrayList();
//        allSeenTypes.addAll(Arrays.asList(sts.documentTypes()));
//        allSeenTypes.addAll(Arrays.asList(sts.attributeTypes()));
//        allSeenTypes.addAll(Arrays.asList(sts.globalTypes()));
//
//        for (int i = 0; i < allSeenTypes.size(); i++)
//        {
//            SchemaType sType = (SchemaType) allSeenTypes.get(i);
//
//            if(sType.getEnumerationValues() != null) {
//                for (int j = 0; j < sType.getEnumerationValues().length; j++) {
//                    String value = sType.getEnumerationValues()[j].getStringValue();
//
//                    PaymentRequestPayload p = new PaymentRequestPayload();
//                    p.setValue(value);
//
//                    if (! listContains(p)) {
//                        String result = "Enum value " + value + " of type " + formatName(sType.getName().toString()) + " is not in the CSV file ";
//                        results.add(result);
//                    }
//                }
//            }
//
//            if(sType.getProperties() != null) {
//                for (int j = 0; j < sType.getProperties().length; j++) {
//                    String name = formatName(sType.getProperties()[j].getName().toString());
//
//                    PaymentRequestPayload p = new PaymentRequestPayload();
//                    p.setTag(name);
//
//                    if (!listContains(p)) {
//                        String result = "";
//                        if (sType.getProperties()[j].getContainerType().getName() != null) {
//                            result = "Tag " + name + " of complexType " + formatName(sType.getProperties()[j].getContainerType().getName().toString()) + " is not in the CSV file ";
//                        } else {
//                            result = "Tag " + name + " is not in the CSV file ";
//                        }
//                        results.add(result);
//                    }
//                }
//            }
//
//            allSeenTypes.addAll(Arrays.asList(sType.getAnonymousTypes()));
//        }
//    }

    private static String formatName(String name) {
        return "<" + name + ">";
    }

    private static boolean listContains(PaymentRequestPayload p) {
        for (int i = 0; i < csvInfo.size(); i++) {
            PaymentRequestPayload q = csvInfo.get(i);
            String tag = q.getTag();

            if(! tag.equals("") && ! p.getTag().isEmpty() && p.getTag() != null) {
                if (tag.equals(p.getTag())) return true;
            } else if (tag.equals("") && ! p.getValue().isEmpty()){
                if (q.getValue().equals(p.getValue())) return true;
            }
        }
        return false;
    }

    public static void buildTree () {
//        int depth = getDepth();
        int currLevel = -1;
        PaymentRequestPayload latest1Deep = null;
        PaymentRequestPayload latest2Deep = null;
        PaymentRequestPayload latest3Deep = null;
        PaymentRequestPayload latest4Deep = null;
        PaymentRequestPayload latest5Deep = null;
        PaymentRequestPayload latest6Deep = null;
        PaymentRequestPayload latest7Deep = null;
        PaymentRequestPayload latest8Deep = null;
        PaymentRequestPayload latest9Deep = null;
        PaymentRequestPayload latest10Deep = null;
        PaymentRequestPayload latest11Deep = null;
        PaymentRequestPayload latest12Deep = null;
        PaymentRequestPayload latest13Deep = null;
        PaymentRequestPayload latest14Deep = null;
        PaymentRequestPayload latest15Deep = null;

        for (int i = 0; i < csvInfo.size(); i++) {
            PaymentRequestPayload current = csvInfo.get(i);
            currLevel = current.getLvl();

            if (currLevel == 0) {
                treeFromCSV.add(current);
            } else if (currLevel == 1) {
                treeFromCSV.get(treeFromCSV.size() - 1).addChild(current);
                current.setParent(treeFromCSV.get(treeFromCSV.size() - 1));
                latest1Deep = current;
             } else if (currLevel == 2) {
                latest1Deep.addChild(current);
                current.setParent(latest1Deep);
                latest2Deep = current;
            } else if (currLevel == 3) {
                latest2Deep.addChild(current);
                current.setParent(latest2Deep);
                latest3Deep = current;
            } else if (currLevel == 4) {
                latest3Deep.addChild(current);
                current.setParent(latest3Deep);
                latest4Deep = current;
            } else if (currLevel == 5) {
                latest4Deep.addChild(current);
                current.setParent(latest4Deep);
                latest5Deep = current;
            } else if (currLevel == 6) {
                latest5Deep.addChild(current);
                current.setParent(latest5Deep);
                latest6Deep = current;
            } else if (currLevel == 7) {
                latest6Deep.addChild(current);
                current.setParent(latest6Deep);
                latest7Deep = current;
            } else if (currLevel == 8) {
                latest7Deep.addChild(current);
                current.setParent(latest7Deep);
                latest8Deep = current;
            } else if (currLevel == 9) {
                latest8Deep.addChild(current);
                current.setParent(latest8Deep);
                latest9Deep = current;
            } else if (currLevel == 10) {
                latest9Deep.addChild(current);
                current.setParent(latest9Deep);
                latest10Deep = current;
            } else if (currLevel == 11) {
                latest10Deep.addChild(current);
                current.setParent(latest10Deep);
                latest11Deep = current;
            } else if (currLevel == 12) {
                latest11Deep.addChild(current);
                current.setParent(latest11Deep);
                latest12Deep = current;
            } else if (currLevel == 13) {
                latest12Deep.addChild(current);
                current.setParent(latest12Deep);
                latest13Deep = current;
            } else if (currLevel == 14) {
                latest13Deep.addChild(current);
                current.setParent(latest13Deep);
                latest14Deep = current;
            } else if (currLevel == 15) {
                latest14Deep.addChild(current);
                current.setParent(latest14Deep);
            }
        }
    }



    public static void printTree(ArrayList<PaymentRequestPayload> arr, String tab) {
        for (int i = 0; i < arr.size(); i++) {
            if (! arr.get(i).getTag().equals("") && ! arr.get(i).getTag().isEmpty() && arr.get(i).getTag() != null) {
                System.out.println(tab + arr.get(i).getTag());
            } else {
                System.out.println(tab + arr.get(i).getValue());
            }
            printTree(arr.get(i).getChildren(), tab + "\t");
        }
    }
    public static int getDepth () {
        int max = -1;
        int curLevel = -1;
        for (int i = 0; i < csvInfo.size(); i++) {
            curLevel = csvInfo.get(i).getLvl();
            if (curLevel > max) {
                max = curLevel;
            }
        }
        return max;
    }


    public static void processXSDFile(String filename) throws XmlException, IOException
    {
        SchemaTypeSystem sts = XmlBeans.compileXsd(
                new XmlObject[] { XmlObject.Factory.parse(new File(filename)) }, XmlBeans.getBuiltinTypeSystem(),
                null);

        for (int i = 0; i < sts.globalElements().length; i++)
        {
            SchemaGlobalElement globalElement = sts.globalElements()[i];
            SchemaType type = globalElement.getType();

            PaymentRequestPayload p = new PaymentRequestPayload();
            p.setTag('<' + globalElement.getName().getLocalPart() + '>');
            p.setParent(null);
            p.setLvl(-1);

            treeFromXSD.add(p);

            processType(type, 0, p);
        }
    }

    private static void processType(SchemaType type, int level, PaymentRequestPayload parent)
    {
        if (type.getEnumerationValues() != null)
        {
            for (int i = 0; i < type.getEnumerationValues().length; i++){
                PaymentRequestPayload p = new PaymentRequestPayload();
                p.setValue(type.getEnumerationValues()[i].getStringValue());
                p.setParent(parent);
                p.setLvl(level);
                parent.addChild(p);
            }
        }

        for (int i = 0; i < type.getProperties().length; i++)
        {
            SchemaProperty property = type.getProperties()[i];

            PaymentRequestPayload p = new PaymentRequestPayload();
            p.setTag('<' + property.getName().getLocalPart() + '>');
            p.setParent(parent);
            p.setLvl(level);
            p.setOccur("[" + property.getMinOccurs() + ", " + property.getMaxOccurs() + "]" );

            parent.addChild(p);
            processType(property.getType(), level + 1, p);
        }
    }

    private static void compareChildren (PaymentRequestPayload p, PaymentRequestPayload q) {
        ArrayList<PaymentRequestPayload> childrenP = p.getChildren();
        ArrayList<PaymentRequestPayload> childrenQ = q.getChildren();

        //TO DO
    }
    private static void compareTrees(PaymentRequestPayload p, PaymentRequestPayload q) {
        // TO DO
        if (p.getTag().equals(q.getTag())) {
            compareChildren(p, q);
        } else {
            System.out.println("Element" + q.getTag() + " is not in the CSV file ");

            compareTrees(p, q.getChildren().get(0));
        }
    }

}