package pt.isel.daw.app;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import pt.isel.daw.app.controllers.MainController;

@Configuration
public class ErrorConfig implements EmbeddedServletContainerCustomizer {
    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, MainController.ERROR_INTERNAL_ERROR_PATH));
        container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, MainController.ERROR_NOT_FOUND_PATH));
        container.addErrorPages(new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, MainController.ERROR_METHOD_NOT_ALLOWED_PATH));
        container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, MainController.ERROR_FORBIDDEN_PATH));
        container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, MainController.ERROR_UNAUTHORIZED_PATH));
        container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, MainController.ERROR_BAD_REQUEST_PATH));
    }
}