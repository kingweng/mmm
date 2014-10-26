package com.oforsky.mmm.capture;

/**
 * Created by kingweng on 2014/8/5.
 */
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.schema.JsonSchema;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeElementsScanner;
import org.reflections.scanners.TypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import com.truetel.jcore.util.FileUtil;

public class SchemaGenerator {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            throw new Exception("invalid parameter length");
        }
        String packageName = args[0];
        String regExp = args[1];
        String rootDir = args[2];
        Boolean genAdditionalProperties = true;
        if (args.length == 4) {
            genAdditionalProperties = Boolean.valueOf(args[3]);
        }
        Pattern p = Pattern.compile(regExp);
        ObjectMapper mapper = new ObjectMapper();
        Set<Class> classes = getClasses(packageName);
        for (Class clazz : classes) {
            String clazzName = clazz.getName();
            String simpleClazzName = clazz.getSimpleName();
            // System.out.println("process file:" + clazzName);
            if (p.matcher(clazzName).matches()) {
                System.out.println("match file:" + clazzName);
                try {
                    JsonSchema jsonSchema = mapper.generateJsonSchema(clazz);
                    String json = jsonSchema.toString();
                    if (!genAdditionalProperties) {

                        json = json.replaceFirst("}$",
                                ",\"additionalProperties\":false}");
                    }
                    FileUtil.writeFile(json,
                            rootDir + "/" + simpleClazzName.replace(".", "/")
                                    + ".json");
                } catch (Throwable e) {
                    System.err.println(clazzName + "," + e.getMessage());
                    // e.printStackTrace();
                }
            }
        }
    }

    private static Set<Class> getClasses(String packageName)
            throws ClassNotFoundException, IOException {

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName)).setScanners(
                        new TypesScanner(), new SubTypesScanner(false),
                        new TypeElementsScanner()));

        Set allClasses = new HashSet();

        Set allClasses1 = reflections.getSubTypesOf(Object.class);
        merge(allClasses, allClasses1);

        Set allClasses2 = reflections.getSubTypesOf(Serializable.class);
        merge(allClasses, allClasses2);

        return allClasses;
    }

    public static void merge(Set allClasses, Set allClasses1) {
        if (allClasses1 != null) {
            for (Object o : allClasses1) {
                if (o != null) {
                    allClasses.add(o);
                }
            }
        }
    }

}
