package org.artifex.props;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.lwjgl.vulkan.VK10.VK_API_VERSION_1_0;
import static org.lwjgl.vulkan.VK10.VK_MAKE_VERSION;
import static org.lwjgl.vulkan.VK11.VK_API_VERSION_1_1;

public class AppInfoProps
{
    private static final AppInfoProps instance = new AppInfoProps();


    private AppInfoProps(){
        props = new Properties();
        try(InputStream is = AppProperties.class.getResourceAsStream("/" + FILENAME)){
            props.load(is);
            int ma,mi,p;
            ma = Integer.parseInt(props.getOrDefault("app.ver.major",1).toString());
            mi = Integer.parseInt(props.getOrDefault("app.ver.minor",0).toString());
            p = Integer.parseInt(props.getOrDefault("app.ver.patch",0).toString());
            appVersion = VK_MAKE_VERSION(ma,mi,p);

            ma = Integer.parseInt(props.getOrDefault("engine.ver.major",1).toString());
            mi = Integer.parseInt(props.getOrDefault("engine.ver.minor",0).toString());
            p = Integer.parseInt(props.getOrDefault("engine.ver.patch",0).toString());

            engineVersion = VK_MAKE_VERSION(ma,mi,p);


            appName = props.getOrDefault("app.name","Application").toString();
            engineName = props.getOrDefault("engine.name","NoEngine").toString();

            int ver = Integer.parseInt(props.getOrDefault("vk.api",11).toString());
            if(ver==10) vkVersion = VK_API_VERSION_1_0;
            else if (ver==11) vkVersion = VK_API_VERSION_1_1;
        }catch (IOException ioe){
            LOGGER.debug("Could not read [" + FILENAME + "] properties file",ioe);
        }
    }


    public static AppInfoProps getInstance() {
        return instance;
    }

    public int getAppVersion() {
        return appVersion;
    }
    public int getEngineVersion() {
        return engineVersion;
    }

    public int getVkVersion() {
        return vkVersion;
    }

    public String getAppName() {
        return appName;
    }

    public String getEngineName() {
        return engineName;
    }

    private static Properties props;
    private static final Logger LOGGER = LogManager.getLogger(AppInfoProps.class);
    private static final String FILENAME = "appinfo.properties";


    private int appVersion= 0x1;
    private int engineVersion = 0x1;

    private String appName = "";
    private String engineName = "";

    private int vkVersion = VK_API_VERSION_1_1;

}
