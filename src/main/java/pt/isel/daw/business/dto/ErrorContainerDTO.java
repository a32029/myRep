package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Resource;

public class ErrorContainerDTO<E> {
    @JsonProperty("auth_user")
    private Resource authUser;
    @JsonProperty("error")
    private E container;

    public ErrorContainerDTO(Resource authUser, E container) {
        this.authUser = authUser;
        this.container = container;
    }

    public ErrorContainerDTO() {
    }

    public Resource getAuthUser() {
        return authUser;
    }

    public E getContainer() {
        return container;
    }
}
