package xyz.qiquqiu.aiserver.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.FlowerImageVO;
import xyz.qiquqiu.aiserver.entity.po.Gallery;
import xyz.qiquqiu.aiserver.entity.query.PageQuery;
import xyz.qiquqiu.aiserver.mapper.GalleryMapper;
import xyz.qiquqiu.aiserver.service.IGalleryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 花卉图库 服务实现类
 * </p>
 *
 * @author lyh
 * @since 2025-07-07
 */
@Service
@Slf4j
public class GalleryServiceImpl extends ServiceImpl<GalleryMapper, Gallery> implements IGalleryService {

    @Override
    public BaseResult<List<FlowerImageVO>> pageQuery(PageQuery query) {
        Page<Gallery> page = this.lambdaQuery()
                .page(query.toMpPage("star", false));
        List<Gallery> list = page.getRecords();
        if (CollectionUtil.isEmpty(list)) {
            return BaseResult.error("暂无数据");
        }
        // PO集合转VO集合返回
        log.debug("查询完毕：{}", list);
        return BaseResult.success(list.stream().map(FlowerImageVO::of).toList());
    }
}
