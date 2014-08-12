package com.evcheung.notify.dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class GreenDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.evcheung.libs.notify.app.dao");

        Entity message = schema.addEntity("Message");
        message.addIdProperty();
        message.addStringProperty("title").notNull();
        message.addStringProperty("content");
        message.addDateProperty("created_at");

        new DaoGenerator().generateAll(schema, "app/src/main/java");
    }
}
