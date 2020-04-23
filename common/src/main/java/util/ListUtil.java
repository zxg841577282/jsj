package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhou_xg
 * @Date: 2019/8/26 0026
 * @Purpose:
 */

public class ListUtil {

    /**
     * 判断List是否为NULL
     * @param list
     * @return
     */
    public static Boolean ListIsNull(List<?> list){
        return list != null && !list.isEmpty();
    }


    public static Map<String,List<Integer>> removeDuplicateToInteger(List<Integer> old,List<Integer> now){
        if (old.size()== now.size() && old.containsAll(now)){return new HashMap<>();}

        List<Integer> delete = new ArrayList<>(old);
        delete.removeAll(now);

        Map<String,List<Integer>> map = new HashMap<>();
        map.put("delete",delete);

        now.removeAll(old);
        map.put("add",now);

        return map;
    }

    public static Map<String,List<String>> removeDuplicateToString(List<String> old,List<String> now){
        if (old.containsAll(now)){return null;}

        List<String> delete = new ArrayList<>(old);
        delete.removeAll(now);

        Map<String,List<String>> map = new HashMap<>();
        map.put("delete",delete);

        now.removeAll(old);
        map.put("add",now);

        return map;
    }

}
