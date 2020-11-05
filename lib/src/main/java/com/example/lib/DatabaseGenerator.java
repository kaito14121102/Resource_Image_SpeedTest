package com.example.lib;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class DatabaseGenerator {
    private static final String PROJECT_DIR = System.getProperty("user.dir");
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.dev.speedtest.database");
        createTables(schema);
        new DaoGenerator().generateAll(schema, PROJECT_DIR+"/app/src/main/java/");
        System.out.println("Schema name: "+schema.getName());
    }

    private static void createTables(Schema schema) {
        Entity result=schema.addEntity("Result");
        result.addIdProperty().autoincrement();
        result.addDoubleProperty("download");
        result.addDoubleProperty("upload");
        result.addDoubleProperty("ping");
        result.addStringProperty("time");
        result.addStringProperty("name");
        result.addStringProperty("typeNetwork");
    }
}
