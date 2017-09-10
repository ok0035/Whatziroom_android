package com.graduation.project.whatziroom.adapter;

import net.daum.mf.map.api.MapView;

import java.util.List;

/**
 * Created by mapl0 on 2017-07-07.
 */

public class ChatNoticeItem {
    public int type;
    public String text;
    public List<ChatNoticeItem> invisibleChildren;
    public MapView map;

    public ChatNoticeItem() {

    }

    public ChatNoticeItem(int type, String text) {
        this.type = type;
        this.text = text;
    }
}
