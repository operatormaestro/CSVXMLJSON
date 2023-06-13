package org.example;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTests {
    static List<Employee> employeeList = new ArrayList<>();
    static Employee employee1 = new Employee(1, "Eric", "Jones", "USA", 46);
    static Employee employee2 = new Employee(2, "Николай", "Сидоров", "Россия", 22);
    static Employee employee3 = new Employee(3, "Catarina", "Bruno", "Italy", 35);
    public final String JSON = "[{\"id\":1,\"firstName\":\"Eric\",\"lastName\":\"Jones\",\"country\":\"USA\",\"age\":46}," +
            "{\"id\":2,\"firstName\":\"Николай\",\"lastName\":\"Сидоров\",\"country\":\"Россия\",\"age\":22}," +
            "{\"id\":3,\"firstName\":\"Catarina\",\"lastName\":\"Bruno\",\"country\":\"Italy\",\"age\":35}]";

    static {

        employeeList.add(employee1);
        employeeList.add(employee2);
        employeeList.add(employee3);
    }

    @Test
    public void ConvertListsTest() {

        //arrange
        List<String> stringArrayList = new ArrayList<>();
        String employeeString1 = """
                \s
                 1
                 Eric
                 Jones
                 USA
                 46
                """;
        String employeeString2 = """
                \s
                 2
                 Николай
                 Сидоров
                 Россия
                 22
                """;
        String employeeString3 = """
                \s
                 3
                 Catarina
                 Bruno
                 Italy
                 35
                """;
        stringArrayList.add(employeeString1);
        stringArrayList.add(employeeString2);
        stringArrayList.add(employeeString3);

        //act
        List<Employee> result = Main.convertLists(stringArrayList);

        //assert
        Assertions.assertArrayEquals(employeeList.toArray(), result.toArray());

    }

    @Test
    public void ListToJsonTest() {

        //arrange

        //act
        String result = Main.listToJson(employeeList);

        //assert
        Assertions.assertEquals(JSON, result);

    }

    @Test
    public void JsonToListTest() {

        //arrange

        //act
        List<Employee> result = Main.jsonToList(JSON);

        //assert
        Assertions.assertArrayEquals(employeeList.toArray(), result.toArray());
    }

    @Test
    public void ConvertListsTestHamcrest() {

        //arrange
        List<String> stringArrayList = new ArrayList<>();
        String employeeString1 = """
                \s
                 1
                 Eric
                 Jones
                 USA
                 46
                """;
        String employeeString2 = """
                \s
                 2
                 Николай
                 Сидоров
                 Россия
                 22
                """;
        String employeeString3 = """
                \s
                 3
                 Catarina
                 Bruno
                 Italy
                 35
                """;
        stringArrayList.add(employeeString1);
        stringArrayList.add(employeeString2);
        stringArrayList.add(employeeString3);


        //act
        List<Employee> result = Main.convertLists(stringArrayList);

        //assert
        assertThat(result, contains(employee1, employee2, employee3));

    }

    @Test
    public void ListToJsonTestHamcrest() {

        //arrange

        //act
        String result = Main.listToJson(employeeList);

        //assert
        List<String> matchers = Arrays.asList("1", "Eric", "Jones", "USA", "46", "2", "Николай", "Сидоров", "Россия", "22", "3", "Catarina", "Bruno", "Italy", "35");
        assertThat(result, Matchers.stringContainsInOrder(matchers));
    }

    @Test
    public void JsonToListTestHamcrest() {

        //arrange

        //act
        List<Employee> result = Main.jsonToList(JSON);

        //assert
        assertThat(result, containsInAnyOrder(employee3, employee1, employee2));
    }

    @Test
    public void employeeTest() {

        //arrange
        Employee employee = new Employee(0, "Иван", "Иванов", "РФ", 55);

        //assert
        assertThat(employee, hasToString("ID: 0. First name: Иван. Last name: Иванов. Country: РФ. Age: 55"));
    }

    @Test
    public void readStringTest() {
        String filename = "/home/mikhail/IdeaProjects/CSVtoJSON/src/test/resources/test.txt";
        String result = Main.readString(filename);
        assertThat(result,equalToIgnoringCase("проверочный файл"));

    }
}