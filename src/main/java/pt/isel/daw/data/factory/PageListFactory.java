package pt.isel.daw.data.factory;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import pt.isel.daw.business.entities.PageListEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PageListFactory<E> {

    public PageListEntity<E> fetchPage(
            final JdbcTemplate jt,
            final String sqlCountRows,
            final String sqlFetchRows,
            final Object args[],
            final int pageNo,
            final int pageSize,
            final RowMapper<E> rowMapper) {

        // determine how many rows are available
        final int rowCount = (int) jt.queryForObject(sqlCountRows, args, Integer.class); //jt.queryForInt(sqlCountRows, args);

        // calculate the number of pages
        int pageCount = rowCount / pageSize;
        if (rowCount > pageSize * pageCount) {
            pageCount++;
        }

        // create the page object
        final PageListEntity<E> page = new PageListEntity<E>();
        page.setPageNumber(pageNo);
        page.setPagesAvailable(pageCount);

        // fetch a single page of results
        final int startRow = (pageNo - 1) * pageSize;
        jt.query(
                sqlFetchRows,
                args,
                new ResultSetExtractor() {
                    public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                        final List pageItems = page.getPageItems();
                        int currentRow = 0;
                        while (rs.next() && currentRow <startRow + pageSize) {
                            if (currentRow >= startRow) {
                                pageItems.add(rowMapper.mapRow(rs, currentRow));
                            }
                            currentRow++;
                        }
                        return page;
                    }
                });
        return page;
    }
}