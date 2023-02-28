package com.gduranti.userstoryexporter;

import java.util.Map;
import java.util.function.Function;

import com.gduranti.userstoryexporter.QueryList.QueryListItem;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Configurations {

    private final String baseUrl;
    private final String docTemplatePath;
    private final String targetFolderPath;
    private final String apiVersion;

    private final Function<QueryListItem, String> fileNameFormatter;
    private final Map<String, String> fieldToTemplateMap;

}
