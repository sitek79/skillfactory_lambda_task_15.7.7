package apppack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) {
        //
        LocalDate localDateNow = LocalDate.now(); // получаем текущую дату
        String now = "Сегодня: ";
        int year = localDateNow.getYear();
        int month = localDateNow.getMonthValue();
        int dayOfMonth = localDateNow.getDayOfMonth();
        DayOfWeek dayOfWeek = localDateNow.getDayOfWeek();
        System.out.printf("%s %s %d.%d.%d \n", now, dayOfWeek, dayOfMonth, month, year);
        System.out.println("А Куликовская битва была в: ");
        LocalDate date = LocalDate.of(1380, 9, 8);
        //Period period = Period.ofYears(40);
        //System.out.println(date.plus(period)); // 2005-4-22
        System.out.println(date); // конкретная дата

        LocalTime time = LocalTime.of(10, 30);
        Duration duration = Duration.ofMinutes(15);
        System.out.println(time.plus(duration)); // 16:45
        //

        // печатаем какое-то системное св-во
        Properties p = new Properties(System.getProperties());
        String sysprop = "os.arch";
        System.out.print("Системное свойство " + sysprop + ": ");
        System.out.println(p.getProperty(sysprop));

        try {
            Stream<String> lines = Files.lines(Paths.get("src/read.txt"));
            Stream<Path> list = Files.list(Paths.get("./"));
            Stream<Path> walk = Files.walk(Paths.get("./"), 3);
            lines.forEach(System.out::print);
            list.forEach(System.out::print);
            System.out.println("\n...");
            System.out.println("Обойдем текущую директорию ограничиваясь тремя уровнями:");
            walk.forEach(System.out::print);

        } catch (NoSuchFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        IntStream intStream = IntStream.of(1, 2, 3, 4);
        DoubleStream doubleStream = DoubleStream.of(1.2, 3.4);
        IntStream range = IntStream.range(10, 100); // 10 .. 99
        IntStream intStream1 = IntStream.rangeClosed(10, 100); // 10 .. 100
        //
        System.out.println("\nТак мы генерируем один раз уникальный ID");
        CreateID cri = new CreateID();
        cri.createID();
        //
        System.out.println("Так мы генерируем целую пачку UID'ов");
        Stream<Event> generateUID = Stream.generate(() -> new Event(UUID.randomUUID(), LocalDateTime.now(), ""));
        // так поток будет распечатываться бесконечно долго
        //generate.forEach(System.out::print);
        // и его вывод нужно ограничить. Распечатаем только первые 10 элементов
        System.out.println(generateUID.limit(10).collect(Collectors.toList()));
        // попробуем поток обработать еще раз и посмотрим что получится
        try {
            Stream stringStream = generateUID.map((value) -> {return value.toString();});
            System.out.println(stringStream.limit(10).collect(Collectors.toList()));
        } catch (IllegalStateException e) {
            System.err.println("Как раз та ситуация когда: 'Поток уже был обработан или закрыт'");
            e.printStackTrace();
            System.err.println("Продолжаем выполнение программы дальше...");
        }
        // сгенерируем UID еще раз, обработаем и сохраним вывод в файл
        Stream<Event> generateUID2 = Stream.generate(() -> new Event(UUID.randomUUID(), LocalDateTime.now(), ""));
       /*generateUID2
               .map(Object::toString)
               // или так
               .map((value) -> { return value.toString(); })
               .map((value) -> { return value.toUpperCase(); })
               .map((value) -> { return value.substring(0,3); });*/
        //System.out.println(generateUID2.map(Object::toString));

        // сохраним поток в файл используя java.nio.file.Path
        System.out.println("Другой поток генерации...");
        Path pathSaveUID2 = Paths.get("src/UID.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(pathSaveUID2, StandardCharsets.UTF_8)) {
            //writer.write(String.valueOf(generateUID2.map((value) -> { return value.toString(); })));
            //generateUID2.map((value) -> { return writer.write(); });
            //writer.write(String.valueOf(generateUID2.map(event -> event.toString())));
            // уже лучше но...
            //writer.write(String.valueOf(generateUID2.map((gen) -> gen + " ").limit(5).collect(Collectors.toList()).toString()));
            // прересчитали элементы и записали в файл
            //writer.write(String.valueOf(generateUID2.map((gen) -> gen + "").limit(5).collect(Collectors.toList()).stream().count()));
            // вывели в консоль
            //generateUID2.map((gen) -> gen + " ").limit(5).forEach(s->System.out.println(s));
            //
            //writer.write(String.valueOf(generateUID2.map(o -> o.toString()).limit(5).collect(Collectors.toList()).toString()));
            //writer.write(String.valueOf(generateUID2.map(Object::toString).limit(5).collect(Collectors.toList()).toString()));
            //
            Files.write(Paths.get("src/UID.txt"), (Iterable<String>)IntStream.range(0, 5000).mapToObj(String::valueOf)::iterator);

        } catch (IOException e) {
            System.err.println("Во время записи возникла ошибка...");
            e.printStackTrace();
        }
        // отдельным потоком запишем в файл
        // выделим запись в отдельный поток
        WriteFile wr = new WriteFile();
        //generate.forEach(wr.trueWriter());
        wr.trueWriter("Hello");

        //

        //
        System.out.println("Main thread started...");
        ReadFile rf = new ReadFile();

        // выделим чтение в отдельный поток
        Runnable r = () -> {
            System.out.printf("%s started... \n", Thread.currentThread().getName());
            try {
                Thread.sleep(100);
                rf.trueReader();
            } catch (Exception e) {
                System.out.println("Thread has been interrupted");
                e.printStackTrace();
            }
            System.out.printf("%s finished... \n", Thread.currentThread().getName());
     
        Thread fileReader = new Thread(r, "Читатель");
        fileReader.start();
        System.out.println("Main thread finished...");


        LocalDate date2 = LocalDate.of(1979, 9, 26);
        Period period = Period.ofYears(40);
        System.out.println(date2.plus(period)); // 2005-4-22

        LocalTime time2 = LocalTime.of(10, 30);
        Duration duration2 = Duration.ofMinutes(15);
        System.out.println(time2.plus(duration2)); // 16:45

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
