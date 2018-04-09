package list.util.configurer;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CustomWebMvcConfigurerAdapter extends WebMvcConfigurerAdapter {
    /**
     * 添加资源映射地址
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //为swagger2添加资源映射
        registry.addResourceHandler("/lixue/**")
                .addResourceLocations("classpath:/templates/lixue/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("toLogin").setViewName("login");
        super.addViewControllers(registry);
    }

    /**
     * 拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new MyInterceptor()).addPathPatterns("*//**").excludePathPatterns("/toLogin", "/login");
        super.addInterceptors(registry);
    }


    /**
     * 允许跨域请求
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH");
    }
}
