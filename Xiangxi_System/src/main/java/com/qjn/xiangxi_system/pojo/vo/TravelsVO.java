package com.qjn.xiangxi_system.pojo.vo;

import com.qjn.xiangxi_system.pojo.Travels;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TravelsVO extends Travels {
    private String tags;

    public String[] getTagArray() {
        return tags != null ? tags.split(",") : new String[0];
    }
}
