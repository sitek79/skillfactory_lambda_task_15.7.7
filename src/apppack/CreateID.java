package apppack;

import java.util.UUID;

public class CreateID {
    UUID createID() {
        UUID uniqueKey = UUID.randomUUID();
        System.out.println("Уникальный ключ: " + uniqueKey);
        return uniqueKey;
    }
}
