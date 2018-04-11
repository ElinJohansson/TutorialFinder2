package com.example.demo.repository;

import com.example.demo.domain.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
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
    public int createNewTag(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Tags (Name)\n" +
                     "VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int tagId = -1;
            while (rs.next()) {
                tagId = rs.getInt(1);
            }
            return tagId;

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
    public void addTagsToTutorial(List<String> tags, String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO TutorialTags (tags_id, tutorial_id)\n " +
                     "VALUES (?, ?)")) {

            int tutorialId = getTutorialId(title);
            if (tutorialId < 0) {
                throw new TutorialRepositoryException("Tutorial was not found");
            }

            int tagId;

            for (String tagName : tags) {
                tagId = getTagId(tagName);
                if (tagId < 0) {
                    tagId = createNewTag(tagName);
                } else {
                    if (getTutorialTagId(tagId, tutorialId) != -1) {
                        continue;
                    }
                }
                ps.setInt(1, tagId);
                ps.setInt(2, tutorialId);
                ps.executeUpdate();
            }

        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add tags to tutorial", e);
        }

    }

    private int getTutorialTagId(int tagId, int tutorialId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM TutorialTags " +
                     "WHERE tags_id = ? AND tutorial_id = ?")) {
            ps.setInt(1, tagId);
            ps.setInt(2, tutorialId);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt("id");
            }
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch id from database", e);
        }
        return -1;
    }

    private int getTagId(String tagName) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM Tags WHERE name = ?")) {
            ps.setString(1, tagName);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt("id");
            }
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch id from database", e);
        }
        return -1;
    }

    private int getTutorialId(String title) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM Tutorial WHERE title = ?")) {
            ps.setString(1, title);
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
    public List<Language> getLanguages() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM Language")) {
            ResultSet results = ps.executeQuery();

            List<Language> languages = new ArrayList<>();

            while (results.next()) {
                languages.add(new Language(results.getString("name")));
            }
            return languages;

        } catch (SQLException e)

        {
            throw new TutorialRepositoryException("Unable to fetch id from database", e);
        }
    }

    @Override
    public void addRatingtToTutorial(String title, int rating) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO TutorialRating (tutorial_id, rating_id)\n " +
                     "VALUES (?, ?)")) {

            int tutorialId = getTutorialId(title);
            if (tutorialId < 0) {
                throw new TutorialRepositoryException("Tutorial was not found");
            }

            //using superuser as default user for now
            int ratingId = getRatingId(rating, "superuser");
            if (ratingId < 0) {
                ratingId = createNewRating(rating, "superuser");
            }

            ps.setInt(1, tutorialId);
            ps.setInt(2, ratingId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add rating to tutorial", e);
        }

    }

    private int createNewRating(int rating, String user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Rating (value, username)\n" +
                     "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, rating);
            ps.setString(2, user);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int ratingId = -1;
            while (rs.next()) {
                ratingId = rs.getInt(1);
            }
            return ratingId;

        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add tag to database", e);
        }
    }

    private int getRatingId(int rating, String user) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM Rating WHERE username = ? and value = ?")) {
            ps.setString(1, user);
            ps.setInt(2, rating);
            ResultSet results = ps.executeQuery();

            if (results.next()) {
                return results.getInt("id");
            }


        } catch (SQLException e)

        {
            throw new TutorialRepositoryException("Unable to fetch id from database", e);
        }
        return -1;
    }
}

