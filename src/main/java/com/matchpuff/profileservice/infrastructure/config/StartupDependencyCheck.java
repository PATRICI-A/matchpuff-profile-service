package com.matchpuff.profileservice.infrastructure.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupDependencyCheck implements ApplicationRunner {

    private final MongoTemplate mongoTemplate;
    private final Cloudinary cloudinary;

    @Value("${app.startup.fail-fast:true}")
    private boolean failFast;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!failFast) {
            log.warn("Startup dependency checks are disabled (app.startup.fail-fast=false)");
            return;
        }

        checkMongo();
        checkCloudinary();
        log.info("Startup dependency checks passed: MongoDB and Cloudinary are reachable");
    }

    private void checkMongo() {
        try {
            mongoTemplate.executeCommand(new Document("ping", 1));
        } catch (Exception ex) {
            throw new IllegalStateException("Startup failed: cannot connect to MongoDB", ex);
        }
    }

    private void checkCloudinary() {
        try {
            cloudinary.api().ping(ObjectUtils.emptyMap());
        } catch (Exception ex) {
            throw new IllegalStateException("Startup failed: cannot connect to Cloudinary", ex);
        }
    }
}
