import org.junit.Test;
import org.liyou.qixiaobo.utils.IWeiChat;
import org.liyou.qixiaobo.utils.ImageUtils;

/**
 * Created by Administrator on 14-3-9.
 */
public class TestPic {
    @Test
    public void testPic(){
        ImageUtils.pressText ("李尤","C:\\Users\\Administrator\\Desktop\\clean.jpg",IWeiChat.FONT_NAME,0, IWeiChat.FONT_COLOR,IWeiChat.FONT_SIZE
                , IWeiChat.NAME_START_PX,IWeiChat.START_PX_Y,1f
        );
    }
}

