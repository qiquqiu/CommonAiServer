package xyz.qiquqiu.aiserver.service;

import xyz.qiquqiu.aiserver.common.dto.SendMessageDTO;
import xyz.qiquqiu.aiserver.common.po.Message;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 聊天消息表 服务类
 * </p>
 *
 * @author lyh
 * @since 2025-06-28
 */
public interface IMessageService extends IService<Message> {

    void saveAndUpdate(SendMessageDTO dto);
}
