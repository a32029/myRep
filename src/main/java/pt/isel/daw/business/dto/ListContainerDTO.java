package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Resource;

public class ListContainerDTO<E> {
    @JsonProperty("auth_user")
    private Resource authUser;
    @JsonProperty("list")
    private E container;

    public ListContainerDTO(Resource authUser, E container) {
        this.authUser = authUser;
        this.container = container;
    }

    public ListContainerDTO() {
    }

    public Resource getAuthUser() {
        return authUser;
    }

    public E getContainer() {
        return container;
    }
}
