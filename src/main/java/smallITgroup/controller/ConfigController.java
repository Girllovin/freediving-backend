package smallITgroup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private Environment environment;

    @GetMapping("/info")
    public Map<String, Object> getConfigInfo() {
        Map<String, Object> configInfo = new HashMap<>();
        
        // Get active profiles
        String[] activeProfiles = environment.getActiveProfiles();
        configInfo.put("activeProfiles", activeProfiles);
        
        // Check if prod profile is active
        boolean isProdProfile = false;
        for (String profile : activeProfiles) {
            if ("prod".equals(profile)) {
                isProdProfile = true;
                break;
            }
        }
        configInfo.put("isProdProfile", isProdProfile);
        
        // Get some key configuration values
        configInfo.put("appName", environment.getProperty("spring.application.name"));
        configInfo.put("logLevel", environment.getProperty("logging.level.org.springframework.security"));
        configInfo.put("csrfEnabled", environment.getProperty("spring.security.csrf.enabled"));
        configInfo.put("mongodbUri", environment.getProperty("spring.data.mongodb.uri"));
        configInfo.put("mailHost", environment.getProperty("spring.mail.host"));
        
        return configInfo;
    }
} 