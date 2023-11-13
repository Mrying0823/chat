import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.mrying.chat.utils.JwtUtils;

import java.util.Objects;

/**
 * @author 邓和颖
 * @since 2023/11/9 8:40
 */
@Log
public class JWT {
    @Test
    public void JWTTest() {
        JwtUtils jwtUtils = new JwtUtils("2bc1a2207c684d7fb29df11f8abdc815", 604800L);

        String userId = jwtUtils.getUserIdFromToken("eyJhbGciOiJIUzI1NiJ9.eyJwaG9uZSI6IjE4OTc4OTU1ODA1IiwiaXNzIjoiQ2hhdCIsImlhdCI6MTY5OTQ0NzgyMSwiZXhwIjoxNzAwMDUyNjIxfQ.73b77soQVGWG-cnKfp7m099by_I5gQcgu6gmRsihxFM");

        log.info(userId);

        // AssertionError 说明断言不成立
        assert userId != null;
    }
}
