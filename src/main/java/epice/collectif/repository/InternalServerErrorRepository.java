package epice.collectif.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

/**
 * Created by anthonyrey on 23/11/2016.
 */
@Repository
public class InternalServerErrorRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private static String serverSessionToken = UUID.randomUUID().toString();

    public void increment(){
        redisTemplate.opsForValue().increment(serverSessionToken, 1);
    }

    public int getSessionCount(){

        String count = redisTemplate.opsForValue().get(serverSessionToken);
        if(count == null)
            return 0;
        else
            return Integer.valueOf(count);


    }

}
