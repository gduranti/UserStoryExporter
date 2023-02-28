package com.gduranti.userstoryexporter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserStoryExporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserStoryExporter.class);

    private final Configurations config;

    public UserStoryExporter(Configurations config) {
        this.config = config;
    }

    public void exportWorkItens(String workItemIds) throws IOException {

        LOGGER.info("Starting...");

        WorkitemLoader workitemLoader = new WorkitemLoader(config);
        QueryList resultList = workitemLoader.load(workItemIds);

        DocBuilder docBuilder = new DocBuilder(config);
        docBuilder.buildDocs(resultList);

        LOGGER.info("Completed!");
    }

}
