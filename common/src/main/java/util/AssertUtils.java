package util;

import other.ResultException;

import java.util.List;

/**
 * @Author: zhou_xg
 * @Date: 2020/4/20
 * @Purpose:
 */

public class AssertUtils {

    /**
     * 条件为null报异常
     * @param o 条件
     * @param msg 不成立则返回信息
     */
    public static void isNull(Object o,String msg){
        if (o == null){
            throw new ResultException(msg);
        }
    }

    /**
     * 条件为null或空字符串报异常
     * @param o 条件
     * @param msg 不成立则返回信息
     */
    public static void isEmpty(Object o,String msg){
        if (o == null || o.equals("")){
            throw new ResultException(msg);
        }
    }

    /**
     * 条件都为null报异常
     * @param o1 o2 条件
     * @param msg 不成立则返回信息
     */
    public static void isAllNull(Object o1,Object o2,String msg){
        if (o1==null && o2==null){
            throw new RuntimeException(msg);
        }
    }

    /**
     * list中条件都为null则报异常
     * @param list 条件list
     * @param msg 不成立则返回信息
     */
    public static void isListAllNull(List<Object> list, String msg){
        if (ListUtil.ListIsNull(list)){
            int i = 0;
            for (Object o : list) {
                if (o == null) { i++; }
            }

            if (i==list.size()){
                throw new RuntimeException(msg);
            }
        }
    }

    /**
     * @param b 条件
     * @param msg 不成立则返回信息
     */
    public static void check(boolean b,String msg){
        if (!b){
            throw new RuntimeException(msg);
        }
    }

    /**
     * 不为空则返回原数据
     * @param o 条件
     */
    public static String checkEmptyBack(String o){
        if (o == null || o.equals("")){
            return "";
        }else {
            return o;
        }
    }

    /**
     * 不为空则返回原数据,否则返回msg
     * @param o 条件
     * @param msg 条件
     */
    public static String checkEmptyBack(String o,String msg){
        if (o == null || o.equals("")){
            return msg;
        }else {
            return o;
        }
    }

}
