package com.gduranti.userstoryexporter;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;

import com.gduranti.userstoryexporter.QueryList.QueryListItem;

public class Main {

    @SuppressWarnings("serial")
    public static void main(String[] args) throws Exception {

        Configurations config = Configurations.builder()
                .baseUrl("https://tfs.pro.intra.rs.gov.br/tfs/PROCERGSCollection/_apis/wit/workitems")
                .apiVersion("5.0")
                .docTemplatePath("C:\\Java\\Git\\UserStoryExporter\\src\\main\\resources\\us-template.doc")
                .targetFolderPath("C:\\Java\\Git\\UserStoryExporter\\src\\main\\resources\\target\\")
                .fileNameFormatter(Main::createFileNameFormatter)
                .fieldToTemplateMap(new HashMap<String, String>() {
                    {
                        put("System.IterationPath", "{SPRINT}");
                        put("System.CreatedDate", "{CREATED_DATE}");
                        put("System.Title", "{TITLE}");
                        put("System.Description", "{DESCRIPTION}");
                        put("Microsoft.VSTS.Common.AcceptanceCriteria", "{ACCEPTANCE_CRITERIA}");
                    }
                })
                .build();

        String ids = "360179,353669,360195,353975,353668";

        UserStoryExporter userStoryExporter = new UserStoryExporter(config);
        userStoryExporter.exportWorkItens(ids);
    }

    private static String createFileNameFormatter(QueryListItem workItem) {
        String name = StringUtils.stripAccents(workItem.getFields().get("System.Title").replace(" ", "_"));
        return name + "]" + name;
    }

}
