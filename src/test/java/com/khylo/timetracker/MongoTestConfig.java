package com.khylo.timetracker;


import java.io.IOException;
import java.util.Date;

import cz.jirutka.spring.embedmongo.EmbeddedMongoFactoryBean;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.*;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
@Configuration
public class MongoTestConfig {
    private static final String MONGO_DB_URL = "localhost";
    private static final String MONGO_DB_NAME = "embeded_db";
   

    private void doItByHand() throws Exception{
    	MongodStarter starter = MongodStarter.getDefaultInstance();

    	String bindIp = "localhost";
    	int port = 12345;
    	IMongodConfig mongodConfig = new MongodConfigBuilder()
    		.version(Version.Main.PRODUCTION)
    		.net(new Net(bindIp, port, Network.localhostIsIPv6()))
    		.build();

    	MongodExecutable mongodExecutable = null;
    	try {
    		mongodExecutable = starter.prepare(mongodConfig);
    		MongodProcess mongod = mongodExecutable.start();

    		MongoClient mongo = new MongoClient(bindIp, port);
    		MongoDatabase db = mongo.getDatabase("test");
    		MongoCollection<Document> col = db.getCollection("testCol");
    		col.insertOne(new Document("testDoc", new Date()));

    	} finally {
    		if (mongodExecutable != null)
    			mongodExecutable.stop();
    	}
    }
    
    /*
     * Do it using plugin
     */
	@Bean
	public MongoTemplate mongoTemplate() throws IOException {
        EmbeddedMongoFactoryBean mongo = new EmbeddedMongoFactoryBean();
        mongo.setBindIp(MONGO_DB_URL);
        MongoClient mongoClient = mongo.getObject();
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient, MONGO_DB_NAME);
        return mongoTemplate;
    }
}
