package xyz.qiquqiu.aiserver.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.ai.chat.messages.MessageType;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessage {
    private MessageType type;
    private String content;

    @Override
    public String toString() {
        if (this.type == MessageType.USER) {
            return "[对话](" + "用户说：" + content + ")";
        } else if (this.type == MessageType.ASSISTANT) {
            return "[对话](" + "AI回复：" + content + ")";
        }
        return "(" + "type=" + type + ", " + "content=" + content + ")";
    }
}
