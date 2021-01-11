package book.helper;

public class PageRequest {
    private final int pageNumber;
    private final int maximumDataPerPage;

    public static PageRequest of(int pageNumber, int maximumDataPerPage) {
        return new PageRequest(pageNumber,maximumDataPerPage);
    }

    private PageRequest(int _pageNumber, int _maximumDataPerPage) {
       this.pageNumber=_pageNumber;
       this.maximumDataPerPage=_maximumDataPerPage;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getMaximumDataPerPage() {
        return maximumDataPerPage;
    }

    public int getStartOffset() {
        return (pageNumber==1)?0:(pageNumber-1)*maximumDataPerPage;
    }
}
