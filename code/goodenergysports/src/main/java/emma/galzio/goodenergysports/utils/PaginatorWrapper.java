package emma.galzio.goodenergysports.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * This class provide all the logic to manage the pagination on all the thymeleaf views
 *
 *
 * */
public class PaginatorWrapper {

    //Must be always odd!!
    private static final int MAX_PAGE_ITEM_DISPLAY = 5;

    private Logger logger = LoggerFactory.getLogger(PaginatorWrapper.class);

    private int numberOfPages;
    private int currentPage;
    private List<PageItem> pageItems;
    private String baseUrl;
    private int pageSize;
    private boolean hasPrevious;
    private boolean hasNext;
    private int previousPage;
    private int nextPage;

    public PaginatorWrapper(int numberOfPages, int currentPage, String baseUrl, int pageSize) {
        if(MAX_PAGE_ITEM_DISPLAY % 2 == 0) throw new IllegalArgumentException("El numero de páginas debe ser impar");

        this.numberOfPages = numberOfPages;
        this.currentPage = currentPage;
        this.baseUrl = baseUrl;
        this.pageSize = pageSize;
        this.innitPageWrapper();
    }

    private void innitPageWrapper(){

        pageItems = new ArrayList<>();
        int start, size;

        if(numberOfPages <= MAX_PAGE_ITEM_DISPLAY){
            start = 1;
            size = numberOfPages;

        } else if(currentPage <= MAX_PAGE_ITEM_DISPLAY - (MAX_PAGE_ITEM_DISPLAY/2)){
            //Si se encuentra entre las primeras páginas y en la primera mitad del paginador (ej 1, 2 o 3)
            start = 1;
            size = MAX_PAGE_ITEM_DISPLAY;
        } else if(currentPage >= numberOfPages - (MAX_PAGE_ITEM_DISPLAY/2)){
            //Si se encuentra entre las ultimas páginas, despues de la mitad del tamaño del paginador
            start = numberOfPages - MAX_PAGE_ITEM_DISPLAY + 1;
            size = MAX_PAGE_ITEM_DISPLAY;
        } else{
            //Si es cualquier numero que no sean ni los primeros ni los últimos
            //Por esto MAX_PAGE_ITEM_DISPLAY debe ser impar, la pagina actual siempre está al medio;
            start = currentPage - (MAX_PAGE_ITEM_DISPLAY/2);
            size = MAX_PAGE_ITEM_DISPLAY;
        }

        for(int i = 0; i < size; i++){

            PageItem pageItem = new PageItem(start+i, (start+i)==currentPage);
            pageItem.setFirst(currentPage == 1);
            pageItem.setLast(currentPage == numberOfPages);
            pageItems.add(pageItem);
        }
        hasNext = (currentPage != numberOfPages);
        logger.info("Tiene Próxima pagina: " + hasNext);
        hasPrevious = (currentPage != 1);
        logger.info("Tiene pagina anterior: " + hasPrevious);
        previousPage = (hasPrevious) ? currentPage-1 : currentPage;
        logger.info("Numero de pagina previa: " + previousPage);
        nextPage = (hasNext) ? currentPage + 1 : currentPage;
        logger.info("Numero de pagina siguiente: " + nextPage);

    }



    //<editor-fold desc="GETTERS and SETTERS">
    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }


    public List<PageItem> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<PageItem> pageItems) {
        this.pageItems = pageItems;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean getHasPrevious() {
        return hasPrevious;
    }

    public void setHasPrevious(boolean hasPrevious) {
        this.hasPrevious = hasPrevious;
    }

    public boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public int getPreviousPage() {
        return previousPage;
    }

    public void setPreviousPage(int previousPage) {
        this.previousPage = previousPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }
    //</editor-fold>
}
