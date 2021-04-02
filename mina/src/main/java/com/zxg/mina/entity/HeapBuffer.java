package com.zxg.mina.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zhou_xg
 * @Date: 2021/3/30
 * @Purpose:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeapBuffer {

    private Integer pos;

    private Integer lim;

    private String cap;

    @Override
    public String toString() {
        return "HeapBuffer{" +
                "pos=" + pos +
                ", lim=" + lim +
                ", cap='" + cap + '\'' +
                '}';
    }
}
