package list.dto;

import net.sf.json.JSONObject;

/**
 * <p> 分页入参 </p>
 *
 * @author jbhuang
 * @date 2017/9/29 09:40
 */
public class PageDTO {
    private int pageNum;
    private int pageSize;

    public PageDTO(){}

    public PageDTO(JSONObject jsonObject){
     this.pageNum= jsonObject.getInt("pageNum");
     this.pageSize= jsonObject.getInt("pageSize");
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
