package com.gduranti.userstoryexporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gduranti.userstoryexporter.QueryList.QueryListItem;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

public class WorkitemLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkitemLoader.class);

    private final Configurations config;

    WorkitemLoader(Configurations config) {
        this.config = config;
    }

    QueryList load(String workItemsIds) throws IOException {
        InputStream is = connect(workItemsIds);
        QueryList result = readResponse(is);
        logResult(result);
        return result;
    }

    private InputStream connect(String workItemsIds) throws IOException {

        StringBuilder url = new StringBuilder()
                .append(config.getBaseUrl())
                .append("?ids=").append(workItemsIds)
                .append("&api-version=").append(config.getApiVersion())
                .append("&fields=").append(StringUtils.join(config.getFieldToTemplateMap().keySet(), ","));

        LOGGER.info("Connecting to Azure Devops API...");
        LOGGER.info("URL: {}", url);

        InputStream inputStream = new URL(url.toString()).openStream();

        LOGGER.info("Connected!");

        return inputStream;
    }

    private QueryList readResponse(InputStream inputStream) throws UnsupportedEncodingException, IOException {

        LOGGER.info("Reading response...");

        BufferedReader streamReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder responseBuilder = new StringBuilder();

        String inputStr;
        while ((inputStr = streamReader.readLine()) != null) {
            responseBuilder.append(inputStr);
        }

        LOGGER.info("Parsing json...");

        return new Gson().fromJson(JsonParser.parseString(responseBuilder.toString()), QueryList.class);
    }


    private void logResult(QueryList resultList) {

        for (QueryListItem item : resultList.getValue()) {

            LOGGER.trace("-----");
            LOGGER.trace(item.getId().toString());
            LOGGER.trace("Properties:");

            for (Entry<String, String> entry : item.getFields().entrySet()) {
                LOGGER.trace("-----");
                LOGGER.trace(entry.getKey());
                LOGGER.trace(entry.getValue());
                LOGGER.trace("-----");
            }

        }
    }

}
