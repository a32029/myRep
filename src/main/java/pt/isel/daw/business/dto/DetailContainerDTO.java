package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Resource;

public class DetailContainerDTO<E> {
    @JsonProperty("auth_user")
    private Resource authUser;
    @JsonProperty("detail")
    private E container;

    public DetailContainerDTO(Resource authUser, E container) {
        this.authUser = authUser;
        this.container = container;
    }

    public DetailContainerDTO() {
    }

    public Resource getAuthUser() {
        return authUser;
    }

    public E getContainer() {
        return container;
    }
}
