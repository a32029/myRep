package pt.isel.daw.business.dto;

import org.springframework.http.HttpStatus;

public class ControllerExceptionDTO extends RuntimeException {
    private String type;
    private String title;
    private HttpStatus status;
    private String detail;
    private String instance;

    public ControllerExceptionDTO(String type, String title, HttpStatus status, String detail, String instance) {
        super(title);
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }

    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getDetail() {
        return detail;
    }

    public String getInstance() {
        return instance;
    }
}
