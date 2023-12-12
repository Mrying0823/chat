import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.mrying.chat.utils.DateConvertUtils;

/**
 * @author 邓和颖
 * @since 2023/12/12 20:13
 */
@Log
public class TimestampConvert {
    @Test
    public void timestampConvert() {
        log.info(DateConvertUtils.getTimeStampString(1702198023000L));
    }
}
