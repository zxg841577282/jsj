package other;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * mybaits-plus wrapper对象的封装
 * 简化后台管理时的一些入参过滤查询
 * 切记 只能用作单表过滤查询
 *
 * @see AbsWrapper#page 默认当前分页 = 第0页
 * @see AbsWrapper#pageSize 默认每页条数 = 20条
 * @see AbsWrapper#getIPage() 根据setWrap的条件 获取符合的分页数据
 */

public abstract class AbsWrapper<T> implements WrapperOperation<T>{

    @ApiModelProperty("当前页数")
    protected int page = 0;
    @ApiModelProperty("分页数量")
    protected int pageSize = 20;

    // 排序
    @ApiModelProperty("排序,list")
    protected List<String> sortList ; // 排序

    public List<String> getSortList() {
        return sortList;
    }

    public void setSortList(List<String> sortList) {
        this.sortList = sortList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public abstract void setWrap();


    @ApiModelProperty(hidden = true)
    protected QueryWrapper<T> wrapper = new QueryWrapper<>();

    @ApiModelProperty(hidden = true)
    protected BaseMapper<T> baseMapper;

    public AbsWrapper() {
        setMapper();
    }

    protected abstract void setMapper();

    @ApiModelProperty(hidden = true)
    public List<T> getList() {
        return baseMapper.selectList(wrapper);
    }

    @ApiModelProperty(hidden = true)
    public IPage<T> getPageList() {
        setWrap();
        return baseMapper.selectPage(getIPage(), wrapper);
    }

    @ApiModelProperty(hidden = true)
    public Page<T> getIPage() {
        return new Page<>(page, pageSize);
    }

    @ApiModelProperty(hidden = true)
    public QueryWrapper<T> getWrapper() {
        return wrapper;
    }
}
