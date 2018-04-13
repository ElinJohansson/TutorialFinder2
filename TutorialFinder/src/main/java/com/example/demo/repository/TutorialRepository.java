package com.example.demo.repository;

import com.example.demo.domain.Format;
import com.example.demo.domain.Language;
import com.example.demo.domain.Tutorial;
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
            //Not enought information was provided
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

    @Override
    public int getFormatId(String format) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM format WHERE name = ?")) {
            ps.setString(1, format);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt("id");
            }
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch formatid from database", e);
        }
        return -1;
    }

    @Override
    public int getLanguageId(String name) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM Language WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt("id");
            }
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch languageid from database", e);
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
            throw new TutorialRepositoryException("Unable to fetch tutorialid from database", e);
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
            throw new TutorialRepositoryException("Unable to fetch tagid from database", e);
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
            throw new TutorialRepositoryException("Unable to fetch tutorialid from database", e);
        }
        return -1;
    }

    @Override
    public List<Language> getLanguages() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name, id FROM Language")) {
            ResultSet results = ps.executeQuery();
            List<Language> languages = new ArrayList<>();
            while (results.next()) {
                languages.add(new Language(results.getString("name")));
            }
            return languages;
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch language from database", e);
        }
    }

    @Override
    public List<Tutorial> getTutorialsByLanguage(List<String> languages, List<String> formats, List<String> tags) {

        //Checks if lists are empty and concats a string of all languages/formats/tags that are to be placed in the query
        StringBuilder sb = new StringBuilder();
        if (languages != null) { //if it contains an empty string from js
            sb.append("(");
            for (int i = 0; i < languages.size(); i++) {
                if (i == 0) {
                    sb.append(" l.name = ?");
                } else {
                    sb.append(" or l.name = ?");
                }
            }
            sb.append(")");
        }
        String language = sb.toString();

        StringBuilder sb2 = new StringBuilder();
        if (formats != null) {
            if (languages != null) {
                sb2.append(" and ");
            }
            sb2.append("(");
            for (int i = 0; i < formats.size(); i++) {
                if (i == 0) {
                    sb2.append(" f.name = ?");
                } else {
                    sb2.append(" or f.name = ?");
                }
            }
            sb2.append(")");
        }
        String format = sb2.toString();

        StringBuilder sb3 = new StringBuilder();
        if (tags != null) {
            if (formats != null || languages != null) {
                sb3.append(" and ");
            }
            sb3.append("(");
            for (int i = 0; i < tags.size(); i++) {
                if (i == 0) {
                    sb3.append(" ta.name = ?");
                } else {
                    sb3.append(" or ta.name = ?");
                }
            }
            sb3.append(")");
        }
        String tag = sb3.toString();

        try (Connection conn = dataSource.getConnection();

             PreparedStatement ps = conn.prepareStatement("select *\n" +
                     "from (    \n" +
                     "    select t.id\n" +
                     "        ,t.title\n" +
                     "        ,descr = cast(t.descr as varchar(max))\n" +
                     "        ,t.format_id\n" +
                     "        ,t.language_id\n" +
                     "        ,t.url\n" +
                     "        ,f.name as format\n" +
                     "        ,l.name as language\n" +
                     "        ,t.creationDate\n" +
                     "        ,round(avg(cast(tr.rating as float)), 1) as avgRating\n" +
                     "        ,ROW_NUMBER() over(partition by t.id order by t.id ) thing\n" +
                     "        ,ta.name\n" +
                     "    from Tutorial as t\n" +
                     "        join Format as f\n" +
                     "            on t.format_id = f.id  \n" +
                     "        join Language as l\n" +
                     "            on t.language_id = l.id\n" +
                     "        left join Rating as tr\n" +
                     "            on t.id = tr.tutorial_id\n" +
                     "        join TutorialTags as tt\n" +
                     "            on t.id = tt.tutorial_id\n" +
                     "        join Tags as ta\n" +
                     "            on tt.tags_id = ta.id\n" +
                     "    where " + language + format + tag + "\n" +
                     "    group by t.id ,t.title, t.format_id, t.language_id, t.url, f.name, l.name, t.creationDate, cast(t.descr as varchar(max)), ta.name\n" +
                     "    ) a\n" +
                     "where thing = 1\n" +
                     "order by avgRating DESC")) {

            int startI = 0;
            if (languages != null) {
                startI = languages.size();
                for (int i = 0; i < languages.size(); i++) {
                    ps.setString(i + 1, languages.get(i));
                }
            }

            int startF = startI;
            if (formats != null) {
                startF += formats.size();
                for (int i = 0; i < formats.size(); i++) {
                    ps.setString(i + startI + 1, formats.get(i));
                }
            }

            if (tags != null) {
                for (int i = 0; i < tags.size(); i++) {
                    ps.setString(i + startF + 1, tags.get(i));
                }
            }

            //The method shouldn't run the query if no boxes are checked
            if (!(formats == null && languages == null && tags == null)) {

                ResultSet results = ps.executeQuery();
                List<Tutorial> tutorials = new ArrayList<>();

                while (results.next()) {
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
            }
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch tutorials from database", e);
        }
        return null;
    }

    @Override
    public List<Format> getFormats() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM Format")) {
            ResultSet results = ps.executeQuery();
            List<Format> formats = new ArrayList<>();
            while (results.next()) {
                formats.add(new Format(results.getString("name")));
            }
            return formats;
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch format from database", e);
        }
    }

    @Override
    public void addRatingToTutorial(String title, int rating) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO Rating (tutorial_id, rating)\n " +
                     "VALUES (?, ?)")) {
            int tutorialId = getTutorialId(title);
            if (tutorialId < 0) {
                throw new TutorialRepositoryException("Tutorial was not found");
            }
            ps.setInt(1, tutorialId);
            ps.setInt(2, rating);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add rating to tutorial", e);
        }
    }

    @Override
    public List<Tutorial> getToplist() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("select top (10)\n" +
                     "    t.id\n" +
                     "    ,t.title\n" +
                     "    ,descr = cast(t.descr as varchar(max))\n" +
                     "    ,t.format_id\n" +
                     "    ,t.language_id\n" +
                     "    ,t.url\n" +
                     "    ,f.name as format\n" +
                     "    ,t.creationDate" +
                     "    ,l.name as language\n" +
                     "    ,avg(tr.rating) as AvgRating\n" +
                     "from Tutorial as t\n" +
                     "    join Format as f\n" +
                     "        on t.format_id = f.id  \n" +
                     "    join Language as l\n" +
                     "        on t.language_id = l.id\n" +
                     "    left join Rating as tr\n" +
                     "        on t.id = tr.tutorial_id\n" +
                     "group by t.id ,t.title, t.format_id, t.language_id, t.url, f.name, t.creationDate, l.name, cast(t.descr as varchar(max))\n" +
                     "order by avgRating desc")) {
            ResultSet results = ps.executeQuery();

            List<Tutorial> tutorials = new ArrayList<>();
            System.out.println("hej");
            while (results.next()) {
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
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch toplist from database", e);
        }
    }

    @Override
    public List<String> getTags() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM Tags")) {
            ResultSet results = ps.executeQuery();
            List<String> tags = new ArrayList<>();
            while (results.next()) {
                tags.add(results.getString("name"));
            }
            return tags;
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to fetch format from database", e);
        }
    }
}

