package com.charlie.pojo.entity;

import lombok.Data;

@Data
public class LarkMessageDO {
    private String msg_type = "text"; // 默认文本消息
    private Content content;

    public LarkMessageDO(String text) {
        this.content = new Content(text);
    }

    @Data
    public static class Content {
        private String text;

        public Content(String text) {
            this.text = text;
        }

        // getter/setter
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
    }
}
