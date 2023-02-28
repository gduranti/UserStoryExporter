package com.gduranti.userstoryexporter;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryList {

    private Integer count;
    private List<QueryListItem> value;

    @Data
    @ToString
    public class QueryListItem {

        private Integer id;
        private Integer rev;
        private Map<String, String> fields;

    }

}
