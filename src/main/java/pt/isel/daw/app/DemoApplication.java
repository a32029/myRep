package pt.isel.daw.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import pt.isel.daw.business.interfaces.*;
import pt.isel.daw.business.logic.*;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TeacherService getTeacherService(DataSourceTransactionManager tm){
        return new TeacherServiceImpl(tm);
    }

	@Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public StudentService getStudentService(DataSourceTransactionManager tm){
        return new StudentServiceImpl(tm);
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CourseService getCourseService(DataSourceTransactionManager tm){
        return new CourseServiceImpl(tm);
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SemesterService getSemesterService(DataSourceTransactionManager tm){
        return new SemesterServiceImpl(tm);
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public ClassService getClassService(DataSourceTransactionManager tm){
        return new ClassServiceImpl(tm);
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public TeacherClassService getTeacherClassService(DataSourceTransactionManager tm){
        return new TeacherClassServiceImpl(tm);
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public StudentClassService getStudentClassService(DataSourceTransactionManager tm){
        return new StudentClassServiceImpl(tm);
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    public GroupClassService getGroupClassService(DataSourceTransactionManager tm){
        return new GroupClassServiceImpl(tm);
    }
}