package org.liyou.qixiaobo.entities.response;

/**
 * Created by Administrator on 14-3-1.
 */
public class MusicMessage extends BaseMessage  {
    // 音乐
    private Music music;

    public Music getMusic() {
        return music;
    }

    public void setMusic(Music music) {
        this.music = music;
    }
}
