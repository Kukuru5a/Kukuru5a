//package hexlet.code.repositories;
//
//import hexlet.code.models.UrlCheck;
//
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.HashMap;
//
//public class UrlCheckRepository extends BaseRepository {
//
//    public static void save(UrlCheck urlCheck) throws SQLException {
//        var sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at)"
//                + "VALUES (?, ?, ?, ?, ?, ?)";
//        try (var conn = dataSource.getConnection();
//             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//            preparedStatement.setInt(1, urlCheck.getStatusCode());
//            preparedStatement.setString(2, urlCheck.getTitle());
//            preparedStatement.setString(3, urlCheck.getH1());
//            preparedStatement.setString(4, urlCheck.getDescription());
//            preparedStatement.setLong(5, urlCheck.getUrlId());
//            preparedStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
//            preparedStatement.executeUpdate();
//            var generatedKeys = preparedStatement.getGeneratedKeys();
//            if (generatedKeys.next()) {
//                urlCheck.setId(generatedKeys.getLong(1));
//            } else {
//                throw new SQLException("Database didn't return id after saving urlCheck");
//            }
//        }
//    }
//
//    public static Optional<UrlCheck> find(Long id) throws SQLException {
//        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
//
//        try (var conn = dataSource.getConnection();
//             var stmt = conn.prepareStatement(sql)) {
//            stmt.setLong(1, id);
//            var resultSet = stmt.executeQuery();
//
//            if (resultSet.next()) {
//
//                var urlId = resultSet.getInt("url_id");
//                var statusCode = resultSet.getInt("status_code");
//                var title = resultSet.getString("title");
//                var h1 = resultSet.getString("h1");
//                var description = resultSet.getString("description");
//                var createdAt = resultSet.getTimestamp("created_at");
//                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
//
//                urlCheck.setUrlId(id);
//                urlCheck.setCreatedAt(createdAt);
//                return Optional.of(urlCheck);
//            }
//            return Optional.empty();
//        }
//    }
//
//    public static List<UrlCheck> getEntitiesByUrlId(long urlId) throws SQLException {
//        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
//
//        try (var conn = dataSource.getConnection();
//             var stmt = conn.prepareStatement(sql)) {
//            stmt.setLong(1, urlId);
//            var resultSet = stmt.executeQuery();
//            var result = new ArrayList<UrlCheck>();
//
//            while (resultSet.next()) {
//                var id = resultSet.getLong("id");
//                var statusCode = resultSet.getInt("status_code");
//                var title = resultSet.getString("title");
//                var h1 = resultSet.getString("h1");
//                var description = resultSet.getString("description");
//                var createdAt = resultSet.getTimestamp("created_at");
//                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
//
//                urlCheck.setId(id);
//                urlCheck.setCreatedAt(createdAt);
//                result.add(urlCheck);
//            }
//            return result;
//        }
//    }
//
//    public static List<UrlCheck> getEntitiesByUrl(long urlId) throws SQLException {
//        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
//
//        try (var conn = dataSource.getConnection();
//             var stmt = conn.prepareStatement(sql)) {
//            stmt.setLong(1, urlId);
//            var resultSet = stmt.executeQuery();
//            var result = new ArrayList<UrlCheck>();
//
//            while (resultSet.next()) {
//                var id = resultSet.getLong("id");
//                var statusCode = resultSet.getInt("status_code");
//                var title = resultSet.getString("title");
//                var h1 = resultSet.getString("h1");
//                var description = resultSet.getString("description");
//                var createdAt = resultSet.getTimestamp("created_at");
//                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
//
//                urlCheck.setId(id);
//                urlCheck.setCreatedAt(createdAt);
//                result.add(urlCheck);
//            }
//            return result;
//        }
//    }
//
//    public static Optional<UrlCheck> findLatestChecks() throws SQLException {
//        var sql = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
//        var conn = dataSource.getConnection();
//
//        try (var stmt = conn.prepareStatement(sql)) {
//            var resultSet = stmt.executeQuery();
//            var result = new HashMap<Long, UrlCheck>();
//
//            while (resultSet.next()) {
//                var id = resultSet.getLong("id");
//                var statusCode = resultSet.getInt("status_code");
//                var title = resultSet.getString("title");
//                var h1 = resultSet.getString("h1");
//                var description = resultSet.getString("description");
//                var urlId = resultSet.getLong("url_id");
//                var createdAt = resultSet.getTimestamp("created_at");
//                var check = new UrlCheck(statusCode, title, h1, description, urlId);
//
//                check.setId(id);
//                check.setCreatedAt(createdAt);
////                result.put(urlId, check);
//                return Optional.of(check);
//            }
//            return Optional.empty();
//        }
//    }
//}
//
package hexlet.code.repositories;

import hexlet.code.models.UrlCheck;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlCheckRepository extends BaseRepository {
    public static void save(UrlCheck urlCheck) throws SQLException {
        var sql = "INSERT INTO url_checks (status_code, title, h1, description, url_id, created_at)"
                + "VALUES (?, ?, ?, ?, ?, ?)";
        var createdAt = new Timestamp(System.currentTimeMillis());
        try (var conn = dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, urlCheck.getStatusCode());
            preparedStatement.setString(2, urlCheck.getTitle());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getDescription());
            preparedStatement.setLong(5, urlCheck.getUrlId());
            preparedStatement.setTimestamp(6, createdAt);
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
                urlCheck.setCreatedAt(createdAt);
            } else {
                throw new SQLException("Database didn't return id after saving urlCheck");
            }
        }
    }

    public static List<UrlCheck> getUrlChecks(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            var result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                result.add(urlCheck);
            }
            return result;
        }
    }

    public static Optional<UrlCheck> getLastCheck(Long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC LIMIT 1";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, urlId);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var id = resultSet.getLong("id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlCheck = new UrlCheck(statusCode, title, h1, description, urlId);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                return Optional.of(urlCheck);
            }
            return Optional.empty();
        }
    }
}
