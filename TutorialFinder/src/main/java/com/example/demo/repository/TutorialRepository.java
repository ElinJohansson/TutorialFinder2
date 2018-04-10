package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TutorialRepository implements Repository {

    @Autowired
    private DataSource dataSource;

    @Override
    public void createTutorial(String title, String descr, String language, String format, String url) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Tutorial] ([title],[descr],[creationDate],[language_id],[format_id],[url])\n" +
                     "VALUES (?,?,getdate(),?,?,?)")) {

            int languageId = getLanguageId(language);
            int formatId = getFormatId(format);

            //If languageId is less than zero the language wasnt found
            if (languageId < 0) {
                languageId = createNewLanguage(language);
            }
            //If formatId is less than zero the language wasnt found
            if (formatId < 0) {
                formatId = createNewFormat(format);
            }

            if (languageId < 0 || formatId < 0) {
                throw new TutorialRepositoryException("Unable to create language or format");
            }

            ps.setString(1, title);
            ps.setString(2, descr);
            ps.setInt(3, languageId);
            ps.setInt(4, formatId);
            ps.setString(5, url);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add tutorial to database", e);
        }
    }


    private int getFormatId(String format) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM format WHERE name = ?")) {
            ps.setString(1, format);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt("id");
            }
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch id from database", e);
        }
        return -1;
    }

    public int getLanguageId(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM Language WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt("id");
            }
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch id from database", e);
        }
        return -1;
    }

    @Override
    public int createNewLanguage(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Language (Name)\n" +
                     "VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int languageId = -1;
            while (rs.next()) {
                languageId = rs.getInt(1);
            }
            return languageId;
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add language to database", e);
        }
    }

    @Override
    public void createNewTag(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Tags (Name)\n" +
                     "VALUES (?)")) {
            ps.setString(1, name);
            ps.executeUpdate();


        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add tag to database", e);
        }
    }

    @Override
    public int createNewFormat(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Format (Name)\n" +
                     "VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int formatId = -1;
            while (rs.next()) {
                formatId = rs.getInt(1);
            }
            return formatId;
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add format to database", e);
        }
    }

    @Override
    public void addTags(List<String> tags, String title) {

    }


}
