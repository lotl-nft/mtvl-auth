package land.metadefi.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.sql.Timestamp;

@Getter
@Setter
@MongoEntity(collection = "users")
public class UserEntity {
    ObjectId id;
    String gameId;
    String username;
    String password;
    String telegramId;
    String twitter;
    String discord;
    String email;
    String address;
    Integer status;
    Timestamp createdAt;
    Timestamp updatedAt;
}
