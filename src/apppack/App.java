package apppack;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        System.out.println("Процесс Main стартовал...");

        LocalDate localDateNow = LocalDate.now(); // получаем текущую дату
        String now = "Сегодня: ";
        int year = localDateNow.getYear();
        int month = localDateNow.getMonthValue();
        int dayOfMonth = localDateNow.getDayOfMonth();
        DayOfWeek dayOfWeek = localDateNow.getDayOfWeek();
        System.out.printf("%s %s %d.%d.%d \n", now, dayOfWeek, dayOfMonth, month, year);
        System.out.println("А Куликовская битва была в: ");
        LocalDate date = LocalDate.of(1380, 9, 8);
        System.out.println(date); // конкретная дата

        LocalTime time = LocalTime.of(10, 30);
        Duration duration = Duration.ofMinutes(15);
        System.out.println(time.plus(duration)); // 16:45

        try {
            Stream<String> lines = Files.lines(Paths.get("src/some.txt"));
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

        // сохраним поток в файл используя java.nio.file.Path
        System.out.println("Другой поток генерации...");
        Path pathSaveUID2 = Paths.get("src/UID.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(pathSaveUID2, StandardCharsets.UTF_8)) {
            //writer.write(String.valueOf(generateUID2.map((value) -> { return value.toString(); })));
            //generateUID2.map((value) -> { return writer.write(); });
            //writer.write(String.valueOf(generateUID2.map(event -> event.toString())));
            // уже лучше но...
            //writer.write(String.valueOf(generateUID2.map((gen) -> gen + " ").limit(5).collect(Collectors.toList()).toString()));
            // пересчитаем элементы и записали в файл
            //writer.write(String.valueOf(generateUID2.map((gen) -> gen + "").limit(5).collect(Collectors.toList()).stream().count()));
            // вывод в консоль
            //generateUID2.map((gen) -> gen + " ").limit(5).forEach(s->System.out.println(s));
            //writer.write(String.valueOf(generateUID2.map(o -> o.toString()).limit(5).collect(Collectors.toList()).toString()));
            writer.write(String.valueOf(generateUID2.map(Object::toString).limit(5).collect(Collectors.toList()).toString()));

        } catch (IOException e) {
            System.err.println("Во время записи возникла ошибка...");
            e.printStackTrace();
        }

        // Третий поток генерации
        try {
            Files.write(Paths.get("src/Digits.txt"), (Iterable<String>)IntStream.range(0, 5000).mapToObj(String::valueOf)::iterator);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Четвертый поток
        // Прочитаем сайт https://ya.ru и отдельным потоком сохраним в файл
        Path filePath = Paths.get("src/write.txt");
        try {
            URL url = new URL("https://ya.ru");
            BufferedWriter writer3 = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(url.openStream()));
            //reader.lines().map((gen) -> gen + " ").limit(5).forEach(s->System.out.println(s));
            // эту строку запишем в операторе try-catch
            //reader.lines().map((gen) -> gen + " ").limit(5).forEach(s-> writer3.write(s));
            reader.lines().map((gen) -> gen + " ").limit(5).forEach(s-> {
                try {
                    writer3.write(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

        ReadFile rf = new ReadFile();
        // выделим чтение в отдельный поток
        Runnable r = () -> {
            System.out.printf("%s запустился... \n", Thread.currentThread().getName());
            try {
                Thread.sleep(100);
                rf.trueReader();
            } catch (Exception e) {
                System.out.println("Поток был прерван");
                e.printStackTrace();
            }
            System.out.printf("%s завершился... \n", Thread.currentThread().getName());
        };
        Thread fileReader = new Thread(r, "Читатель");
        fileReader.start();

        LocalDate date2 = LocalDate.of(1979, 9, 26);
        Period period = Period.ofYears(40);
        System.out.println(date2.plus(period));

        LocalTime time2 = LocalTime.of(10, 30);
        Duration duration2 = Duration.ofMinutes(15);
        System.out.println(time2.plus(duration2));

        // 4 типа method reference
        Consumer<List<Integer>> methodRef1 = Collections::sort; // method reference
        Consumer<List<Integer>> lambda1 = l -> Collections.sort(l); // lambda-expression

        String str = "abc";
        Predicate<String> methodRef2 = str::startsWith; // method reference
        Predicate<String> lambda2 = s -> s.startsWith(s); // lambda-expression
        System.out.println(methodRef2);
        System.out.println(lambda2);

        Function<Object, String> f1 = String::valueOf;
        Function<Object, String> f2 = (obj) -> (obj == null) ? "null" : obj.toString();

        Supplier<String> methodRef4 = String::new;
        Supplier<String> lambda4 = () -> new String();


        System.out.println("Процесс Main завершился...");
    }
}