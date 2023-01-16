package edu.bbte.idde.frim1910.backend.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bbte.idde.frim1910.backend.dao.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ConfigBeanFactory {
    private static final ConfigBean CONFIG_BEAN;
    private static String PROFILE;
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigBeanFactory.class);

    static {
        PROFILE = System.getenv("profile");
        if (PROFILE == null) {
            PROFILE = "jdbc";
        }
        try (
                InputStream inputStream = ConfigBeanFactory.class.getResourceAsStream(
                        String.format("/config-%s.json", PROFILE)
                )
        ) {
            ObjectMapper objectMapper = new ObjectMapper();
            CONFIG_BEAN = objectMapper.readValue(inputStream, ConfigBean.class);
            LOGGER.info("Loaded {}", CONFIG_BEAN);
        } catch (IOException e) {
            LOGGER.error("Failed loading config", e);
            throw new DaoException("Failed loading config", e);
        }
    }

    public static ConfigBean getConfigBean() {
        return CONFIG_BEAN;
    }

    public static String getProfile() {
        return PROFILE;
    }
}
