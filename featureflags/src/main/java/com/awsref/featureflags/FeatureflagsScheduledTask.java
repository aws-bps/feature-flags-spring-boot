package com.awsref.featureflags;

import java.io.UnsupportedEncodingException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.services.appconfig.model.ResourceNotFoundException;
import com.awsref.featureflags.config.AWSAppConfig;
import com.awsref.featureflags.feature.FeatureProperties;

@Configuration
@EnableScheduling
public class FeatureflagsScheduledTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeatureflagsScheduledTask.class);

    @Autowired
    private FeatureProperties featureProperties;

    @Autowired
    private AWSAppConfig appConfiguration;

    @Scheduled(fixedRate = 60000)
    public void pollConfiguration() throws UnsupportedEncodingException {
        LOGGER.info("polls configuration from aws app config");
        try {
            JSONObject externalizedConfig = appConfiguration.getConfiguration();
            featureProperties.setEnabled(externalizedConfig.getBoolean("enabled"));
            featureProperties.setLimit(externalizedConfig.getInt("limit"));
        } catch (ResourceNotFoundException e) {

        } catch (Exception e) {
            String message = String.format("stack: %s", e.getStackTrace());
            LOGGER.info(message);
        }
    }
}