package ro.contezi.shopping.info;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/info")
public class InfoController {
    private final ApiInfo apiInfo;

    public InfoController(String facebookAppId, String graphApiVersion, String appVersion) {
        apiInfo = new ApiInfo(facebookAppId, graphApiVersion, appVersion);
    }

    @GetMapping
    @ResponseBody
    public ApiInfo getInfo() {
        return apiInfo;
    }
}
