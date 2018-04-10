package com.example.demo.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class TutorialRepository implements Repository {

    @Autowired
    private DataSource dataSource;

    @Override
    public void createTutorial(String title, String descr, LocalDateTime creationDate, String language, String format, String url, List<String> tags) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("")) {

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new TutorialRepositoryException("Unable to add tutorial to database",e);
        }
    }
}
