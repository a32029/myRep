package pt.isel.daw.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PageListDTO<E> {
    @JsonProperty("page_number")
    private int pageNumber;
    @JsonProperty("pages_available")
    private int pagesAvailable;
    @JsonProperty("page_items")
    private List<E> pageItems;

    public PageListDTO(int pageNumber, int pagesAvailable, List<E> pageItems) {
        this.pageNumber = pageNumber;
        this.pagesAvailable = pagesAvailable;
        this.pageItems = pageItems;
    }

    public PageListDTO() {
        pageItems = new ArrayList<E>();
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPagesAvailable(int pagesAvailable) {
        this.pagesAvailable = pagesAvailable;
    }

    public void setPageItems(List<E> pageItems) {
        this.pageItems = pageItems;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPagesAvailable() {
        return pagesAvailable;
    }

    public boolean hasNextPage() {
        return pageNumber < pagesAvailable;
    }

    public boolean hasPrevPage() {
        return pageNumber > 1;
    }

    public List<E> getPageItems() {
        return pageItems;
    }
}