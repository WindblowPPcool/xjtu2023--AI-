package com.xjtu.lzc_back.config;
// SwaggerConfig.java
//import .......

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author LZC
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig implements WebMvcConfigurer {
 /**
  * Docket类是Swagger的配置类，要自定义修改 Swagger 的默认配置信息，我们需要覆盖该对象
  */
 @Bean
 public Docket docket(){
  //1.以OAS_30标准构建Docket配置类
  return new Docket(DocumentationType.OAS_30)
          //2.配置Swagger接口文档基本信息apiInfo
          .apiInfo(apiInfo())
          //3.select方法开启配置扫描接口的Builder
          .select()
          //4.指定要扫描/维护接口文档的包（否则就全部扫描）
          .apis(RequestHandlerSelectors.basePackage("com.xjtu.lzc_back.controller"))
          //5.路径过滤：该Docket-UI展示时，只展示指定路径下的接口文档(any表示都展示)
          .paths(PathSelectors.any())
          .build();
 }

 /**
  * 配置 Swagger 接口文档的基本信息
  */
 private ApiInfo apiInfo(){
  return new ApiInfoBuilder()
          //接口文档标题
          .title("智慧云端知识分享平台接口文档")
          //接口文档描述内容
          .description("智慧云端知识分享平台")
          //项目文档迭代版本
          .version("1.0")
          //主要联系人信息（姓名name，个人主页url，邮箱email）
          .contact(new Contact("智慧云端知识分享平台接口文档","", ""))
          //lisence
          .license("智慧云端知识分享平台")
          //接口服务地址 根据自己的地址填写
          .termsOfServiceUrl("http://localhost:8088/doc.html")
          //返回构建的ApiInfo对象
          .build();
 }
}