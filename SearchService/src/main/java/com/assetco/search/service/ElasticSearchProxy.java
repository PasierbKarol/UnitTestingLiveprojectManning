package com.assetco.search.service;

public interface ElasticSearchProxy {
    String execute(String path, String query);
}
