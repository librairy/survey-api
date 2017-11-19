package io.swagger.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "class io.swagger.codegen.languages.SpringCodegen", date = "2016-11-22T13:07:43.858Z")

@Configuration
public class SwaggerDocumentationConfig {


    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
            .title("librAIry Survey API")
            .description("Publish questions and collect results about using [librAIry](http://librairy.github.io/) as a recommender system")
            .license("Apache 2.0")
            .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
            .termsOfServiceUrl("")
            .version("1.0.0")
            .contact(new Contact("[OEG-UPM](http://www.oeg-upm.net/)","http://librairy.github.io/", "librairy.framework@gmail.com"))
            .build();
    }

    @Bean
    public Docket customImplementation(){

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.swagger.api"))
                .build()
                .enable(true)
                .apiInfo(apiInfo())
                .directModelSubstitute(org.joda.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(org.joda.time.DateTime.class, java.util.Date.class)
                .securitySchemes(newArrayList(basicAuth()))
                .securityContexts(newArrayList(securityContext()))
                ;
    }


    private <T> List<T> newArrayList(T something) {
        List<T> list = new ArrayList<T>();
        list.add(something);

        return list;
    }

    private BasicAuth basicAuth(){
        return new BasicAuth("basicAuth");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/*"))
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[0];
        return newArrayList(new SecurityReference("basicAuth", authorizationScopes));
    }

}
