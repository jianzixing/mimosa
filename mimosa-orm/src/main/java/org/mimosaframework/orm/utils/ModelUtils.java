package org.mimosaframework.orm.utils;

import org.mimosaframework.core.json.ModelObject;
import org.mimosaframework.core.utils.Finder;
import org.mimosaframework.orm.AutoResult;
import org.mimosaframework.orm.Paging;
import org.mimosaframework.orm.SessionTemplate;
import org.mimosaframework.orm.criteria.Criteria;
import org.mimosaframework.orm.criteria.Query;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by yangankang on 16/1/18.
 */
public abstract class ModelUtils {

    /**
     * 通过对比对象集合 id 和 pid 重新组合成一个链表的结构(树结构)
     *
     * @param objects
     * @param idKey
     * @param pidKey
     * @param childrenKey
     * @return
     */
    public static <T> List<T> getListToTree(List<T> objects, Object idKey, Object pidKey, Serializable childrenKey) {
        Map<String, T> map = new LinkedHashMap();
        if (objects != null) {
            for (T m : objects) {
                map.put(Finder.getStringValue(m, ModelObject.getKeyName(idKey)), m);
            }

            List<T> result = new ArrayList();
            List<T> removes = new ArrayList<>();

            Set<Map.Entry<String, T>> set = map.entrySet();
            for (Map.Entry<String, T> entry : set) {
                String pid = Finder.getStringValue(entry.getValue(), ModelObject.getKeyName(pidKey));
                Set<Map.Entry<String, T>> set2 = map.entrySet();
                for (Map.Entry<String, T> cen : set2) {
                    if (Finder.getStringValue(cen.getValue(), ModelObject.getKeyName(idKey)).equals(pid)) {
                        T object = cen.getValue();
                        List children = Finder.getArrayValue(object, childrenKey);
                        if (children == null) {
                            children = new ArrayList<ModelObject>();
                        }
                        children.add(entry.getValue());
                        Finder.setObjectValue(cen.getValue(), childrenKey, children);
                        removes.add(entry.getValue());
                    }
                }

                result.add(entry.getValue());
            }

            result.removeAll(removes);
            return result;
        }
        return null;
    }

    public static void removeValues(List<ModelObject> objects, Serializable... keys) {
        if (objects != null && keys != null) {
            for (ModelObject m : objects) {
                for (Object k : keys) {
                    m.remove(k);
                }
            }
        }
    }

    public static void removeValues(Paging paging, Serializable... keys) {
        List<ModelObject> objects = paging.getObjects();
        if (objects != null && keys != null) {
            for (ModelObject m : objects) {
                for (Object k : keys) {
                    m.remove(k);
                }
            }
        }
    }

    public static void removeValue(ModelObject object, Serializable... keys) {
        if (object != null && keys != null) {
            for (Object k : keys) {
                object.remove(k);
            }
        }
    }

    public static void setLikeUrlEncodeSearch(ModelObject search, Serializable key) {
        if (search != null && search.containsKey(key) && key != null && !search.isEmpty(key.toString())) {
            try {
                search.put(key, "%" + URLEncoder.encode(search.getString(key.toString()), "utf-8") + "%");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setEqSearch(ModelObject search, String key, Serializable tableKey, Query query) {
        if (search != null && search.containsKey(key) && !search.isEmpty(key) && query != null) {
            query.eq(tableKey, search.get(key));
        }
    }

    /**
     * SQL语句查询结果辅助查询
     *
     * @param name
     * @param sessionTemplate
     * @param search
     * @param query
     * @param key
     * @return
     */
    public static Paging getSearch(String name,
                                   SessionTemplate sessionTemplate,
                                   ModelObject search,
                                   Query query,
                                   Serializable key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                long count = AutoResult.setQueryCount(name + ".getSearchCount", sessionTemplate, search);
                if (count > 0) {
                    AutoResult.setQueryIn(name + ".getSearchIds", sessionTemplate, search, query, key);
                    Paging paging = sessionTemplate.paging(query);
                    return paging;
                }
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }

        if (runDefault) {
            Paging paging = sessionTemplate.paging(query);
            return paging;
        }
        return null;
    }

    public static List<ModelObject> getSearchByName(String name,
                                                    SessionTemplate sessionTemplate,
                                                    ModelObject search,
                                                    Query query,
                                                    Serializable key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                AutoResult.setQueryIn(name, sessionTemplate, search, query, key);
                List<ModelObject> objects = sessionTemplate.list(query);
                return objects;
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }
        if (runDefault) {
            List<ModelObject> objects = sessionTemplate.list(query);
            return objects;
        }
        return null;
    }

    /**
     * 结果完全依赖sql语句查询
     *
     * @param name
     * @param sessionTemplate
     * @param search
     * @param query
     * @param key
     * @return
     */
    public static Paging getRelySearch(String name,
                                       SessionTemplate sessionTemplate,
                                       ModelObject search,
                                       Query query,
                                       Serializable key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                long count = AutoResult.setQueryCount(name + ".getSearchCount", sessionTemplate, search);
                if (count > 0) {
                    AutoResult.setQueryIn(name + ".getSearchIds", sessionTemplate, search, query, key);
                    Paging paging = sessionTemplate.paging(query);
                    paging.setCount(count);
                    return paging;
                }
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }

        if (runDefault) {
            Paging paging = sessionTemplate.paging(query);
            return paging;
        }
        return null;
    }

    public static List<ModelObject> getRelySearchByName(String name,
                                                        SessionTemplate sessionTemplate,
                                                        ModelObject search,
                                                        Query query,
                                                        Serializable key) {
        boolean runDefault = false;
        if (search != null) {
            search.clearEmpty();
            if (search.size() > 0) {
                boolean hasSet = AutoResult.setQueryIn(name, sessionTemplate, search, query, key);
                if (hasSet) {
                    List<ModelObject> objects = sessionTemplate.list(query);
                    return objects;
                }
            } else {
                runDefault = true;
            }
        } else {
            runDefault = true;
        }

        if (runDefault) {
            List<ModelObject> objects = sessionTemplate.list(query);
            return objects;
        }
        return null;
    }

    public static void setLikeSearch(ModelObject search, String key) {
        if (search != null && search.containsKey(key) && !search.isEmpty(key)) {
            search.put(key, "%" + search.getString(key) + "%");
        }
    }

    public static void setValue2Integer(ModelObject search, String key) {
        if (search != null && search.containsKey(key) && !search.isEmpty(key)) {
            search.put(key, Integer.parseInt(search.getString(key)));
        }
    }

    public static ModelObject queryModelObject(SessionTemplate sessionTemplate, ModelObject object, Object keyFrom, Class c, Serializable keyQuery) {
        if (sessionTemplate != null && object != null && keyFrom != null && c != null && keyQuery != null) {
            return sessionTemplate.get(Criteria.query(c).eq(keyQuery, object.get(keyFrom)));
        }
        return null;
    }

    public static ModelObject queryPKModelObject(SessionTemplate sessionTemplate, ModelObject object, Object keyFrom, Class c) {
        if (sessionTemplate != null && object != null && keyFrom != null && c != null) {
            return sessionTemplate.get(c, object.getString(ModelObject.getKeyName(keyFrom)));
        }
        return null;
    }

    /**
     * 判断数据库中是否存在某个对象
     *
     * @param sessionTemplate
     * @param object
     * @param keyFrom
     * @param c
     * @param keyQuery
     * @return
     */
    public static boolean hasModelObject(SessionTemplate sessionTemplate, ModelObject object, Object keyFrom, Class c, Serializable keyQuery) {
        ModelObject o = queryModelObject(sessionTemplate, object, keyFrom, c, keyQuery);
        if (o == null) {
            return false;
        }
        return true;
    }

    /**
     * 判断数据库中是否存在某个对象
     *
     * @param sessionTemplate
     * @param object
     * @param keyFrom
     * @param c
     * @return
     */
    public static boolean hasPKModelObject(SessionTemplate sessionTemplate, ModelObject object, Object keyFrom, Class c) {
        ModelObject o = queryPKModelObject(sessionTemplate, object, keyFrom, c);
        if (o == null) {
            return false;
        }
        return true;
    }

    /**
     * 通过keys对比两个列表中的差异，返回内容包含
     * 两个列表的交集和不同的集合
     *
     * @param exists
     * @param news
     * @param keys
     * @return
     */
    public static ModelDiffObject diffObjects(List<ModelObject> exists, List<ModelObject> news, String... keys) {
        if (exists != null && news != null && keys != null) {
            List<ModelObject> removedList;
            List<ModelObject> existList = new ArrayList<>();
            List<ModelObject> existList2 = new ArrayList<>();
            Map<ModelObject, ModelObject> map = new LinkedHashMap<>();
            List<ModelObject> insertList;
            for (ModelObject exist : exists) {
                for (ModelObject item : news) {
                    boolean isEqual = true;
                    for (String key : keys) {
                        String v1 = exist.getString(key);
                        String v2 = item.getString(key);
                        if (v1 != null && v2 != null && v1.equals(v2)) {

                        } else {
                            isEqual = false;
                        }
                    }
                    if (isEqual) {
                        existList.add(exist);
                        existList2.add(item);
                        map.put(exist, item);
                    }
                }
            }

            removedList = new ArrayList<>(exists);
            removedList.removeAll(existList);
            insertList = new ArrayList<>(news);
            insertList.removeAll(existList2);

            return new ModelDiffObject(removedList, existList, existList2, insertList, map);
        }
        return null;
    }

    public static ModelDiffObject diffObjects(List<ModelObject> exists, List<ModelObject> news, Enum... keys) {
        if (exists != null && news != null && keys != null) {
            String[] strings = new String[keys.length];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = keys[i].name();
            }
            return diffObjects(exists, news, strings);
        }
        return null;
    }
}
