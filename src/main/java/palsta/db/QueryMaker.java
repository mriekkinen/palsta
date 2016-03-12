package palsta.db;

import java.sql.*;
import java.util.*;

public class QueryMaker<T> {

    private final Connection connection;
    private final Collector<T> collector;

    public QueryMaker(Connection connection, Collector<T> collector) {
        this.connection = connection;
        this.collector = collector;
    }

    public List<T> queryAndCollect(String query, Object... params) throws SQLException {
        List<T> rows = new ArrayList<>();
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            rows.add(collector.collect(rs));
        }

        rs.close();
        stmt.close();
        return rows;
    }

    public int queryInteger(String query, Object... params) throws SQLException {
        int result = 0;
        PreparedStatement stmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            result = rs.getInt(1);
        }

        rs.close();
        stmt.close();
        return result;
    }

    public int insert(String query, Object... params) throws SQLException {
        int generatedKey = -1;
        PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }

        stmt.executeUpdate();

        ResultSet rs = stmt.getGeneratedKeys();

        if (rs.next()) {
            generatedKey = rs.getInt(1);
        }

        stmt.close();
        return generatedKey;
    }

}
