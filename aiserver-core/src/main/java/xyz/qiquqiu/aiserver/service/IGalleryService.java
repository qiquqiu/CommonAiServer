package xyz.qiquqiu.aiserver.service;

import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.vo.FlowerImageVO;
import xyz.qiquqiu.aiserver.common.po.Gallery;
import com.baomidou.mybatisplus.extension.service.IService;
import xyz.qiquqiu.aiserver.common.query.PageQuery;

import java.util.List;

/**
 * <p>
 * 花卉图库 服务类
 * </p>
 *
 * @author lyh
 * @since 2025-07-07
 */
public interface IGalleryService extends IService<Gallery> {

    BaseResult<List<FlowerImageVO>> pageQuery(PageQuery query);
}
