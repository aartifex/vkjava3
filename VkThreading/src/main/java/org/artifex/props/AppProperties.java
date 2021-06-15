package org.artifex.props;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties
{
    private static AppProperties instance;


    private AppProperties(){
        Properties props = new Properties();
        try(InputStream is = AppProperties.class.getResourceAsStream("/" + FILENAME)){
            props.load(is);
            ups = Integer.parseInt(props.getOrDefault("ups",DEFAULT_UPS).toString());
            validate = Boolean.parseBoolean(props.getOrDefault("vkValidate",false).toString());
            requestedImages = Integer.parseInt(props.getOrDefault("requestedImages",2).toString());
            vsync = Boolean.parseBoolean(props.getOrDefault("vsync",true).toString());
            shaderRecompilation = Boolean.parseBoolean(props.getOrDefault("shaderRecompilation",false).toString());
        }catch (IOException ioe){
            LOGGER.debug("Could not read [" + FILENAME + "] properties file",ioe);
        }
    }

    public static AppProperties getInstance() {
        return (instance == null)  ? ( instance = new AppProperties()) : instance;
    }

    public int getRequestedImages() {
        return requestedImages;
    }

    public boolean isVsync() {
        return vsync;
    }

    public boolean isShaderRecompilation() {
        return shaderRecompilation;
    }

    public double getUps() {
        return ups;
    }
    public boolean isValidate() {
        return validate;
    }
    private static final int DEFAULT_UPS = 30;
    private static final String FILENAME = "app.properties";
    private static final Logger LOGGER = LogManager.getLogger(AppProperties.class);
    private int ups = DEFAULT_UPS;
    private boolean validate;
    private int requestedImages;
    private boolean vsync;
    private boolean shaderRecompilation;
}
