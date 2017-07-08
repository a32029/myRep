package pt.isel.daw.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import pt.isel.daw.business.interfaces.ConstantService;
import pt.isel.daw.business.logic.ConstantServiceImpl;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@PropertySources(value = {@PropertySource("classpath:constants.properties")})
public class MvcConfig extends WebMvcConfigurerAdapter {
    private ConstantService constantService;

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean(name = "constantSource")
    public ConstantService constantSource() {
        ConstantServiceImpl constantSource = new ConstantServiceImpl();
        constantSource.setBasename("constants");
        constantSource.setDefaultEncoding("UTF-8");
        this.constantService = constantSource;
        return constantSource;
    }

    @Bean(name = "dsUsers")
    public DataSource getDataSource() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        driverManagerDataSource.setUrl("jdbc:hsqldb:mem:testdb");//"jdbc:hsqldb:file:D:/db/testdb");//
        driverManagerDataSource.setUsername("SA");//"sa");//
        driverManagerDataSource.setPassword("");
        return driverManagerDataSource;
    }

    @Bean
    public JdbcTemplate getJdbcTemplate() {
        try {
            return new JdbcTemplate(new SingleConnectionDataSource(getDataSource().getConnection(), false));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController(constantService.getConstant("index.html")).setViewName("/html/interceptor");

        //--------------------------------------------------------------------------------------------------------------

        registry.addViewController("/docs/docs_teachers").setViewName("/docs/docs_teachers");
        registry.addViewController("/docs/docs_students").setViewName("/docs/docs_students");
        registry.addViewController("/docs/docs_courses").setViewName("/docs/docs_courses");
        registry.addViewController("/docs/docs_semesters").setViewName("/docs/docs_semesters");
        registry.addViewController("/docs/docs_classes").setViewName("/docs/docs_classes");
        registry.addViewController("/docs/docs_teacher_class").setViewName("/docs/docs_teacher_class");
        registry.addViewController("/docs/docs_student_class").setViewName("/docs/docs_student_class");
        registry.addViewController("/docs/docs_group_class").setViewName("/docs/docs_group_class");
    }
}