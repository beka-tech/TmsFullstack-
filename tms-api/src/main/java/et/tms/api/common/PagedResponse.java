package et.tms.api.common;
import java.util.List;
public record PagedResponse<T>(List<T> items, long totalCount, int page,
                               int pageSize, int totalPages,
                               boolean hasPrevious, boolean hasNext) {
    public static <T> PagedResponse<T> of(List<T> items, long totalCount,
                                          int page, int pageSize) {

        int totalPages = (int) Math.ceilDiv(totalCount, pageSize);

        return new PagedResponse<>(items, totalCount, page, pageSize,
                totalPages, page > 1, page < totalPages);
    }
}
