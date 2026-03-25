package com.realtimecode.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
public class WebSocketController {

    @MessageMapping("/code/{sessionId}")  
    @SendTo("/topic/session/{sessionId}")  
    public CodeUpdate sendUpdate(CodeUpdate update) {
        return new CodeUpdate(HtmlUtils.htmlEscape(update.getCode()));  
    }

    public static class CodeUpdate {
        private String code;
        public CodeUpdate() {}
        public CodeUpdate(String code) { this.code = code; }
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }
}
