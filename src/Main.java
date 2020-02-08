package sorting;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;

public class Main<T> {
    private String sortType;
    private String dataType;
    private Boolean isWrite;
    private Boolean isRead;
    private String outputFile;
    private String inputFile;
    private Map<T, Integer> dataMap;
    private List<T> sortedList;

    private final List<String> PARAMS = new ArrayList<>(Arrays.asList(
            "-sortingType",
            "-dataType",
            "-inputFile",
            "-outputFile"
    ));

    public Main(String[] args) {
        this.defineParams(args);
    }

    public static void main(final String[] args) throws IOException {
//        String[] args = {"-dataType", "long"};
        Main sortData = new Main<>(args);
        sortData.readData();
        sortData.printData();
    }


    private void readData() throws FileNotFoundException {
        Scanner scanner = defineScanner(isRead);
        sortedList = sortedDataEntries(scanner);
        dataMap = dataEntryToCount(sortedList);
    }

    private Scanner defineScanner(Boolean isRead) throws FileNotFoundException {

        if (isRead == false) {
            return new Scanner(System.in);
        } else {
            try {
                return new Scanner(new File(this.inputFile));
            } catch (FileNotFoundException e) {
                System.out.println("File not found!");
            }
        }
        return null;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private static <K> Map<K, Integer> dataEntryToCount(List<K> sortedData) {
        Map<K, Integer> dataMap = new TreeMap<>();
        for (K data : sortedData) {
            if (dataMap.containsKey(data)) {
                dataMap.put(data, dataMap.get(data) + 1);
            } else {
                dataMap.put(data, 1);
            }
        }
        return dataMap;
    }

    private <T> List<T> sortedDataEntries(Scanner scanner) {

        if (dataType.equals("long")) {
            List<Long> list = new ArrayList<>();
            while (scanner.hasNextLong()) {
                try {
                    list.add(scanner.nextLong());
                } catch (InputMismatchException e) {
                    System.out.println("\"" + scanner.next() + "\"" + "isn't a" + dataType + ". It's skipped.");
                }
            }
            Collections.sort(list);
            return ((ArrayList<T>) list);
        } else if (dataType.equals("word")) {
            List<String> list = new ArrayList<>();
            while (scanner.hasNext()) {
                try {
                    list.add(scanner.next());
                } catch (InputMismatchException e) {
                    System.out.println("\"" + scanner.next() + "\"" + "isn't a" + dataType + ". It's skipped.");
                }
            }
            Collections.sort(list);
            return ((ArrayList<T>) list);
        } else if (dataType.equals("line")) {
            List<String> list = new ArrayList<>();
            while (scanner.hasNextLine()) {
                try {
                    list.add(scanner.nextLine());
                } catch (InputMismatchException e) {
                    System.out.println("\"" + scanner.next() + "\"" + "isn't a" + dataType + ". It's skipped.");
                }
            }
            Collections.sort(list);
            return ((ArrayList<T>) list);
        }
        return null;
    }

    private String defineParams(String[] strings) {
        List<String> list = Arrays.asList(strings);

        Function<String, String> parser = type -> {
            try {
                if (list.contains(type)) {
                    return list.get(list.indexOf(type) + 1);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                if (type.equals(PARAMS.get(0)) || type.equals(PARAMS.get(1))) {
                    System.out.println("No " + type + " type defined!");
                    System.exit(1);
                }
            }

            return "";
        };

        this.sortType = parser.apply(PARAMS.get(0));
        this.dataType = parser.apply(PARAMS.get(1));
        this.inputFile = parser.apply(PARAMS.get(2));
        this.outputFile = parser.apply(PARAMS.get(3));
        if (sortType.isEmpty())
            sortType = "natural";
        this.isRead = this.inputFile.isEmpty() ? false : true;
        this.isWrite = this.outputFile.isEmpty() ? false : true;


        for (String param : strings) {
            if (param.startsWith("-") && !PARAMS.contains(param))
                System.out.println("\"" + param + "\"" + " isn't a valid parameter. It's skipped.");
        }
        return "";
    }

    private void printList() {
        System.out.println("Total numbers: " + sortedList.size());
        System.out.print("Sorted data: ");
        for (T t : sortedList) {
            System.out.print(t + " ");
        }
    }

    private void printList(PrintWriter writer) {
        writer.write("Total numbers: " + sortedList.size());
        writer.write("Sorted data: ");
        for (T t : sortedList) {
            writer.write(t + " ");
        }
    }

    public void printOutput(PrintWriter writer) {
        Integer totalCount = dataMap.values().stream().reduce(0, Integer::sum);

        if (dataType.equals("long")) {
            writer.write("Total numbers: " + totalCount);
        } else if (dataType.equals("line")) {
            writer.write("Total lines: " + totalCount);
        } else {
            writer.write("Total words: " + totalCount);
        }

        for (Map.Entry<T, Integer> entry : dataMap.entrySet()) {
            writer.write(
                    entry.getKey() + ": " + entry.getValue() + " time(s) " + (entry.getValue() * 100
                            / totalCount) + "%");
        }
    }

    public void printOutput() {
        Integer totalCount = dataMap.values().stream().reduce(0, Integer::sum);

        if (dataType.equals("long")) {
            System.out.println("Total numbers: " + totalCount);
        } else if (dataType.equals("line")) {
            System.out.println("Total lines: " + totalCount);
        } else {
            System.out.println("Total words: " + totalCount);
        }

        for (Map.Entry<T, Integer> entry : dataMap.entrySet()) {
            System.out.println(
                    entry.getKey() + ": " + entry.getValue() + " time(s) " + (entry.getValue() * 100
                            / totalCount) + "%");
        }
    }

    private void printData() throws IOException {
        if (!isWrite) {
            if (sortType.equals("natural"))
                printList();
            else
                printOutput();
        } else {
            try (PrintWriter writer = new PrintWriter(new File(outputFile))) {
                if (sortType.equals("natural"))
                    printList(writer);
                else
                    printOutput(writer);
            }

        }

    }
}

