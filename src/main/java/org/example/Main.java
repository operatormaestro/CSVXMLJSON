package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        String jsonFileName = "data.json";
        List<Employee> list = parseCSV(columnMapping, fileName);
        String json = listToJson(list);
        writeString(json, jsonFileName);
        String json2 = listToJson(parseXML("data.xml"));
        writeString(json2, "data2.json");
        String jsonString = readString(jsonFileName);
        System.out.println(jsonToList(jsonString));
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        File file = new File(fileName);
        List<Employee> list;
        try (FileReader fileReader = new FileReader(file)) {
            CSVReader csvReader = new CSVReader(fileReader);
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public static String listToJson(List<Employee> list) {
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String filename) {
        try (FileWriter fileWriter = new FileWriter(filename)) {
            fileWriter.write(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName));
        Node root = doc.getDocumentElement();
        return convertLists(readXML(root));
    }

    public static List<String> readXML(Node node) {
        NodeList nodeList = node.getChildNodes();
        List<String> employee = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;
                String map = element.getTextContent();
                if (node_.getNodeName().equals("employee")) {
                    employee.add(map);
                    readXML(node_);
                }
            }
        }
        return employee;
    }

    public static List<Employee> convertLists(List<String> list) {
        List<Employee> employeeList = new ArrayList<>();
        for (String s : list) {
            String[] arr = s.split("\n");
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].trim();
            }
            employeeList.add(new Employee(Long.parseLong(arr[1]), arr[2], arr[3], arr[4], Integer.parseInt(arr[5])));
        }
        return employeeList;
    }

    public static String readString(String filename) {
        StringBuilder result = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line.trim());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result.toString();
    }

    public static List<Employee> jsonToList(String json) {
        List<Employee> list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONArray jsonArray;
        try {
            jsonArray = (JSONArray) parser.parse(json);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            Employee employee = gson.fromJson(jsonObject.toJSONString(), Employee.class);
            list.add(employee);
        }
        return list;
    }


}