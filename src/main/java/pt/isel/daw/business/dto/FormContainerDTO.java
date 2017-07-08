package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Resource;

import java.util.LinkedList;
import java.util.List;

public class FormContainerDTO<E> {
    @JsonProperty("auth_user")
    private Resource authUser;
    @JsonProperty("form")
    private E container;
    @JsonProperty("options")
    private List<E> options;
    @JsonProperty("method")
    private String method;

    public FormContainerDTO(Resource authUser, E container, String method) {
        this.authUser = authUser;
        this.container = container;
        this.options = new LinkedList<E>();
        this.method = method.toUpperCase();
    }

    public Resource getAuthUser() {
        return authUser;
    }

    public FormContainerDTO() {
        this.options = new LinkedList<E>();
    }

    public E getContainer() {
        return container;
    }

    public List<E> getOptions() {
        return options;
    }

    public String getMethod() {
        return method;
    }
}
