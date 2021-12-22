package apppack;

import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class App {
    public static void main(String[] args) {
        // поскольку большинство методов java.util.Date помечены аннотацией @Deprecated («устаревшее»)
        // появился отдельный пакет java.time с различными классами и богатым функционалом.
        LocalDate localDateNow = LocalDate.now(); // получаем текущую дату
        int year = localDateNow.getYear();
        int month = localDateNow.getMonthValue();
        int dayOfMonth = localDateNow.getDayOfMonth();
        DayOfWeek dayOfWeek = localDateNow.getDayOfWeek();
        System.out.println(localDateNow);
        System.out.println(dayOfWeek);
        System.out.printf("%d.%d.%d \n", dayOfMonth, month, year);

        LocalDate date = LocalDate.of(1979, 9, 26);
        Period period = Period.ofYears(40);
        System.out.println(date.plus(period)); // 2005-4-22

        LocalTime time = LocalTime.of(10, 30);
        Duration duration = Duration.ofMinutes(15);
        System.out.println(time.plus(duration)); // 16:45

        // метод принимает 2 объекта и возвращает true, если переданное время + промежуток больше текущего времени
        Boolean methodRet = isGreaterTime(LocalTime.of(11, 45, 15), Duration.ofMinutes(10));
        System.out.println(methodRet);

        // Lambda Syntax
        //doubleFromString(5, val -> Double.valueOf(val));
        double x = doubleFromString(5, val -> (double) val);
        System.out.println(x);

        // ???
        task(() -> 65.7);

        // 4 типа method reference
        Consumer<List<Integer>> methodRef1 = Collections::sort; // method reference
        Consumer<List<Integer>> lambda1 = l -> Collections.sort(l); // lambda-expression

        String str = "abc";
        Predicate<String> methodRef2 = str::startsWith; // method reference
        Predicate<String> lambda2 = s -> s.startsWith(s); // lambda-expression

        Function<Object, String> f1 = String::valueOf;
        Function<Object, String> f2 = (obj) -> (obj == null) ? "null" : obj.toString();

        Supplier<String> methodRef4 = String::new;
        Supplier<String> lambda4 = () -> new String();

        //() -> Math.random() * 100;
        //() -> 123;

        Arithmetic ar = new Arithmetic();
        //ar.arithSchema1();
        System.out.println(ar.arithSchema1());
        //ar.equatSchema1();
        System.out.println(ar.equatSchema1());

        
    }

    private static boolean isGreaterTime(LocalTime time, Duration duration) {
        return time.plus(duration).isAfter(LocalTime.now());
    }

    public static double doubleFromString(int value, Worker worker) {
        return worker.rework(value);
    }

    // ????
    private static Supplier<Double> task(Supplier<Double> param) {
        return param;
    }
}
