package fortran.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {
    public static <T> List<T> prepend(T t, List<T> list){
        List<T> l=new ArrayList<>();
        l.add(t);
        l.addAll(list);
        return l;
    }

    public static <T>List<T> append(List<T> list, T t){
        List<T> l=new ArrayList<>(list);
        l.add(t);
        return l;
    }

    public static String readFile(String filename){
        StringBuilder s= new StringBuilder();
        Scanner scanner;
        try {
            scanner = new Scanner(new FileReader(filename));
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
        while (scanner.hasNextLine()) s.append(scanner.nextLine()).append("\n");
        scanner.close();
        return s.toString();
    }
}
