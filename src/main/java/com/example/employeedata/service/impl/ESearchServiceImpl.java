package com.example.employeedata.service.impl;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.employeedata.document.*;
import com.example.employeedata.dto.ResultQueryDto;
import com.example.employeedata.helpers.*;
import com.example.employeedata.repository.elastic.*;
import com.example.employeedata.service.ESearchService;

@Service
public class ESearchServiceImpl implements ESearchService {

    private EmployeeESRepository employeeESRepo;
    private ProjectESRepository projectESRepo;

    @Value("${spring.data.elasticsearch.uri}")
    private String elasticSearchURI;

    @Value("${spring.data.elasticsearch.search}")
    private String elasticSearchSearchPrefix;

    public ESearchServiceImpl(
        EmployeeESRepository employeeESRepo,
        ProjectESRepository projectESRepo) {
        this.employeeESRepo = employeeESRepo;
        this.projectESRepo = projectESRepo;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ESearchService.class);
    
    @Override
    public Iterable<EmployeeDoc> getAllEmployees() {
        return employeeESRepo.findAll();
    }

    @Override
    public Iterable<ProjectDoc> getAllProjects() {
        return projectESRepo.findAll();
    }

    @Override
    public ResultQueryDto searchByQueryCustomFields(String[] fields, String query) throws IOException {
        String body = ESearchQuery.createMultiIndexMatchBody(fields, query);
        return executeHttpRequest("", body);
    }
    
    @Override
    public ResultQueryDto searchByQueryCustomFields(String index, String[] fields, String query) throws IOException {
        String body = ESearchQuery.createMultiIndexMatchBody(fields, query);
        return executeHttpRequest(index + "/", body);
    }

    private ResultQueryDto executeHttpRequest(String index, String body) throws IOException{
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            ResultQueryDto resultQuery = new ResultQueryDto();

            HttpPost httpPost = new HttpPost(
                ESearchQuery.createSearchURI(
                    elasticSearchURI,
                    index,
                    elasticSearchSearchPrefix
                )
            );

            httpPost.setHeader(Constants.CONTENT_ACCEPT, Constants.APP_TYPE);
            httpPost.setHeader(Constants.CONTENT_TYPE, Constants.APP_TYPE);

            try {
                httpPost.setEntity(new StringEntity(body, Constants.UTF8_ENCODING));
                HttpResponse response = httpClient.execute(httpPost);
                String responseMessage = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = new JSONObject(responseMessage);

                if(jsonObject.getJSONObject(Constants.HITS).getJSONObject(Constants.TOTAL_HITS).optInt(Constants.VALUE) != 0){
                    resultQuery
                            .setElements(jsonObject
                                .getJSONObject(Constants.HITS)
                                .getJSONArray(Constants.HITS)
                                .toString()
                            );
                    resultQuery.setNumberOfResults(jsonObject.getJSONObject(Constants.HITS).getJSONObject(Constants.TOTAL_HITS).optInt(Constants.VALUE));
                    resultQuery.setTookTime((float) ((double) jsonObject.getInt(Constants.TOOK) / Constants.MILLISECONDS));
                } else {
                    resultQuery.setTookTime((float) ((double) jsonObject.getInt(Constants.TOOK) / Constants.MILLISECONDS));
                }

            } catch (IOException | JSONException err) {
                LOGGER.error("Error occurred while connecting to elasticsearch: {}", err.getMessage());
                resultQuery.setNumberOfResults(0);
            }

            return resultQuery;
        }
    }
}
