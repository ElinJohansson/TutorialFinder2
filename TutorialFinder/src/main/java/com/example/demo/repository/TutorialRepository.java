package com.example.demo.repository;

import com.example.demo.domain.Format;
import com.example.demo.domain.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.domain.Tutorial;

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
                     "VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            int tutorialId = getTutorialId(title);
            System.out.println(tutorialId);
            if(tutorialId < 0){
                throw new TutorialRepositoryException("Tutorial was not found");
            }

            int tagId = 0;

            for(String tagName : tags){
                tagId = getTagId(tagName);
                System.out.println("tagid"+tagId);
                if(tagId < 0){
                    tagId = createNewTag(tagName);
                }
                ps.setInt(1, tagId);
                ps.setInt(2, tutorialId);
                ps.executeUpdate();
            }

//            ResultSet rs = ps.getGeneratedKeys();
//            int formatId = -1;
//            while (rs.next()) {
//                formatId = rs.getInt(1);
//            }
//            return formatId;
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add format to database", e);
        }

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

            while (results.next()){
                languages.add(new Language (results.getString("name")));
            }
            return languages;

        } catch (SQLException e)

        {
            throw new TutorialRepositoryException("Unable to fetch id from database", e);
        }
    }
    @Override
    public List<Tutorial> getTutorials() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("select\n" +
                     "    t.id\n" +
                     "    ,t.title\n" +
                     "    ,descr = cast(t.descr as varchar(max))\n" +
                     "    ,t.format_id\n" +
                     "    ,t.language_id\n" +
                     "    ,t.url\n" +
                     "    ,f.name as format\n" +
                     "    ,l.name as language\n" +
                     "    ,t.creationDate"+
                     "    ,avg(tr.rating_id) as avgRating\n" +
                     "from Tutorial as t\n" +
                     "    join Format as f\n" +
                     "        on t.format_id = f.id  \n" +
                     "    join Language as l\n" +
                     "        on t.language_id = l.id\n" +
                     "    left join TutorialRating as tr\n" +
                     "        on t.id = tr.tutorial_id\n" +
                     "group by t.id ,t.title, t.format_id, t.language_id, t.url, f.name, l.name, t.creationDate, cast(t.descr as varchar(max)) ")) {
            ResultSet results = ps.executeQuery();

            List<Tutorial> tutorials = new ArrayList<>();
            System.out.println("hej");
            while (results.next()){
                tutorials.add(new Tutorial(
                        results.getLong("id"),
                        results.getString("title"),
                        results.getString("language"),
                        results.getString("format"),
                        results.getDouble("avgRating"),
                        results.getTimestamp("creationDate").toLocalDateTime(),
                        results.getString("descr"),
                        results.getString("url")));
            }
            return tutorials;

        } catch (SQLException e)

        {
            throw new TutorialRepositoryException("Unable to fetch id from database1", e);
        }
    }
}

