package com.awsref.featureflags.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.appconfig.AmazonAppConfig;
import com.amazonaws.services.appconfig.AmazonAppConfigClient;
import com.amazonaws.services.appconfig.model.GetConfigurationRequest;
import com.amazonaws.services.appconfig.model.GetConfigurationResult;

import com.amazonaws.services.appconfigdata.AWSAppConfigData;
import com.amazonaws.services.appconfigdata.AWSAppConfigDataClient;
import com.amazonaws.services.appconfigdata.model.GetLatestConfigurationRequest;
import com.amazonaws.services.appconfigdata.model.GetLatestConfigurationResult;
import com.amazonaws.services.appconfigdata.model.StartConfigurationSessionRequest;
import com.amazonaws.services.appconfigdata.model.StartConfigurationSessionResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Configuration
public class AWSAppConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(AWSAppConfig.class);
    private final AWSAppConfigData appConfigData;

    public AWSAppConfig() {
        appConfigData = AWSAppConfigDataClient.builder()
            .withRegion("ap-northeast-2")
            .build();
    }

    public JSONObject getConfiguration() throws UnsupportedEncodingException {

        StartConfigurationSessionRequest sessionRequest = new StartConfigurationSessionRequest()
        .withApplicationIdentifier("order")
        .withConfigurationProfileIdentifier("milestone0-profile")
        .withEnvironmentIdentifier("dev")
        .withRequiredMinimumPollIntervalInSeconds(60);
        StartConfigurationSessionResult sessionResult = appConfigData.startConfigurationSession(sessionRequest);
        
        String configurationToken = sessionResult.getInitialConfigurationToken();

        GetLatestConfigurationRequest configurationRequest = new GetLatestConfigurationRequest()
            .withConfigurationToken(configurationToken);

        GetLatestConfigurationResult configurationResult = appConfigData.getLatestConfiguration(configurationRequest);
        
        String message = String.format("contentType: %s", configurationResult.getContentType());
        LOGGER.info(message);

        if (!Objects.equals("application/json", configurationResult.getContentType())) {
            throw new IllegalStateException("config is expected to be JSON");
        }

        String content = new String(configurationResult.getConfiguration().array(), StandardCharsets.US_ASCII);
        return new JSONObject(content).getJSONObject("feature");
    }
}