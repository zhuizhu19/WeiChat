package org.liyou.qixiaobo.entities.weichat.response;

import org.liyou.qixiaobo.utils.MessageUtil;

/**
 * Created by Administrator on 14-3-1.
 */
public class MusicResponseMessage extends BaseResponseMessage {
    // 音乐
    private Music music;

    public MusicResponseMessage () {
        this.setMsgType (MessageUtil.RESP_MESSAGE_TYPE_MUSIC);
    }

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
