package pt.isel.daw.business.entities;

import java.util.ArrayList;
import java.util.List;

public class PageListEntity<E> {
    private int pageNumber;
    private int pagesAvailable;
    private List<E> pageItems = new ArrayList<E>();

    public PageListEntity() {}

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