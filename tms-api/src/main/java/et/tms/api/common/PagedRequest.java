package et.tms.api.common;


public record PagedRequest(Integer page, Integer pageSize, String search,
                           String orderBy, Boolean descending) {

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int MAX_PAGE_SIZE = 50;
    public PagedRequest { // compact constructor
        page = (page == null || page < 1) ? 1 : page;

        pageSize = (pageSize == null || pageSize < 1) ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);

        search = (search == null) ? "" : search.trim();

        orderBy = (orderBy == null || orderBy.isBlank()) ? "title" : orderBy.trim();

        descending = Boolean.TRUE.equals(descending);
    }
}