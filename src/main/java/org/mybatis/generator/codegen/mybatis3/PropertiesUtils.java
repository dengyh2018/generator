package org.mybatis.generator.codegen.mybatis3;

import org.mybatis.generator.modules.utils.MapStorage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class PropertiesUtils {
    private static Map<String, String> map = new HashMap<String, String>();

    private static Map<String, String> init() {

        /*Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(ShellRunerConfig.getPropFilePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Entry<Object, Object>> set = pro.entrySet();
        Iterator<Entry<Object, Object>> it = set.iterator();

        while (it.hasNext()) {
            Map.Entry<Object, Object> entry = it.next();
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }*/

        String models = MapStorage.datasourceMap.get("models");
        String[] model = models.split(",");

        for (String m : model) {
            map.put(m + "Base", m);
        }

        return map;
    }

    public static String parseValue(String keys) {
        Iterator<Entry<String, String>> IT = PropertiesUtils.init().entrySet().iterator();
        while (IT.hasNext()) {
            Map.Entry<String, String> entry = IT.next();
            if (entry.getKey().contains(".") && !entry.getKey().contains("..")) {
                if (keys.startsWith(entry.getKey() + ".")) {
                    keys = keys.replace(entry.getKey() + ".", entry.getValue() + ".");
                }
            } else {
                String[] str = new String[]{"Dao", "Service", "ServiceImpl", "Mapper"};

                for (String stri : str) {
                    if (keys.endsWith(stri)) {
                        if (keys.indexOf("." + entry.getKey()) != -1) {
                            keys = keys.replace("." + entry.getKey(), "." + entry.getValue());
                            break;
                        }
                    }
                }
                if (keys.indexOf("." + entry.getKey()) != -1) {
                    keys = keys.replace("." + entry.getKey(), "." + entry.getValue());
                } else if (keys.indexOf(entry.getKey()) != -1) {
                    keys = keys.replace(entry.getKey(), entry.getValue());
                }
                if (keys.indexOf("/" + entry.getKey()) != -1) {
                    keys = keys.replace("/" + entry.getKey(), "/" + entry.getValue());
                }
            }

        }
        return keys;
    }

}
