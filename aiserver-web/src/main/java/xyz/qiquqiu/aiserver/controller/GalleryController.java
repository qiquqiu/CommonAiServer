package xyz.qiquqiu.aiserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.qiquqiu.aiserver.common.BaseResult;
import xyz.qiquqiu.aiserver.common.vo.FlowerImageVO;
import xyz.qiquqiu.aiserver.common.query.PageQuery;
import xyz.qiquqiu.aiserver.service.IGalleryService;

import java.util.List;

/**
 * <p>
 * 花卉图库 前端控制器
 * </p>
 *
 * @author lyh
 * @since 2025-07-07
 */
@Slf4j
@RestController
@RequestMapping("/gallery")
@RequiredArgsConstructor
public class GalleryController {

    private final IGalleryService galleryService;

    @GetMapping("/page")
    public BaseResult<List<FlowerImageVO>> pageQuery(PageQuery query) {
        log.debug("分页查询图库：【{}】", query);
        return galleryService.pageQuery(query);
    }

}
