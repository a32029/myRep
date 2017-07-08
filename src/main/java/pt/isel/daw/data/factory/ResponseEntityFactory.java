package pt.isel.daw.data.factory;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

public class ResponseEntityFactory<T> {
    private Resource<T> resourceList;

    public ResponseEntityFactory(T content, Link selfLink) {
        resourceList = new Resource(content, selfLink);
    }

    public ResponseEntityFactory(T content, String selfHref) {
        resourceList = new Resource(content, new Link(selfHref).withSelfRel());
    }

    public Resource<T> getResourceList() {
        return resourceList;
    }

    public void add(Link link, boolean templated) {
        if (templated)
            link.isTemplated();
        resourceList.add(link);
    }

    public void add(String href, String rel, boolean templated) {
        Link link = new Link(href);
        if (templated)
            link.isTemplated();
        resourceList.add(link.withRel(rel));
    }
}