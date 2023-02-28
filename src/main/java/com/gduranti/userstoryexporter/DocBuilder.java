package com.gduranti.userstoryexporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gduranti.userstoryexporter.QueryList.QueryListItem;

public class DocBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocBuilder.class);

    private final Configurations config;
    private final File template;

    DocBuilder(Configurations config) {
        this.config = config;
        this.template = new File(config.getDocTemplatePath());
    }

    void buildDocs(QueryList resultList) {
        resultList.getValue().stream().forEach(i -> buildDoc(i));
    }

    private void buildDoc(QueryListItem workItem) {

        LOGGER.info("Generating doc for wokitem #{}", workItem.getId());

        String docName = config.getFileNameFormatter().apply(workItem);

        try {
            String itemFilePath = config.getTargetFolderPath() + docName + ".doc";
            FileUtils.copyFile(template, new File(itemFilePath));

            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(itemFilePath));
            HWPFDocument doc = new HWPFDocument(fs);
            replaceText(doc, workItem);
            saveDoc(itemFilePath, doc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HWPFDocument replaceText(HWPFDocument doc, QueryListItem workItem) {
        Range range = doc.getRange();

        for (int sectionIndex = 0; sectionIndex < range.numSections(); ++sectionIndex) {
            Section section = range.getSection(sectionIndex);
            for (int paragraphIndex = 0; paragraphIndex < section.numParagraphs(); paragraphIndex++) {
                Paragraph paragraph = section.getParagraph(paragraphIndex);
                for (int z = 0; z < paragraph.numCharacterRuns(); z++) {
                    CharacterRun run = paragraph.getCharacterRun(z);
                    config.getFieldToTemplateMap().forEach((fieldName, templateVariable) -> replaceField(fieldName, templateVariable, workItem, run));
                }
            }
        }
        return doc;
    }

    private void replaceField(String field, String templateVariable, QueryListItem workItem, CharacterRun run) {
        if (run.text().contains(templateVariable)) {
            run.replaceText(templateVariable, workItem.getFields().get(field));
        }
    }

    private void saveDoc(String filePath, HWPFDocument doc) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            doc.write(out);
        } finally {
            out.close();
        }
    }
}
