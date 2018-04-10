package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

//        jdbcTemplate.execute("CREATE TABLE Users (\n" +
//                "\tusername nvarchar(100) NOT NULL,\n" +
//                "\tpassword nvarchar(100) NOT NULL,\n" +
//                "\tPRIMARY KEY (username)\n" +
//                ")");
//        jdbcTemplate.execute("CREATE TABLE Tutorial (\n" +
//                "\tid int IDENTITY(1,1) NOT NULL,\n" +
//                "\ttitle nvarchar(200) NOT NULL,\n" +
//                "\tdescr text NOT NULL,\n" +
//                "\tcreationDate date NOT NULL,\n" +
//                "\tlanguage_id int NOT NULL,\n" +
//                "\tformat_id int NOT NULL,\n" +
//                "\turl nvarchar (2000), \n" +
//                "\tPRIMARY KEY (id)\n" +
//                ")");
//        jdbcTemplate.execute("CREATE TABLE Language (\n" +
//                "\tid int IDENTITY(1,1) NOT NULL,\n" +
//                "\tname nvarchar(100) NOT NULL,\n" +
//                "\tPRIMARY KEY (id)\n" +
//                "\n" +
//                ")");
//        jdbcTemplate.execute("CREATE TABLE Format (\n" +
//                "\tid int IDENTITY(1,1) NOT NULL,\n" +
//                "\tname nvarchar(100) NOT NULL,\n" +
//                "\tPRIMARY KEY (id)\n" +
//                "\t)");
//        jdbcTemplate.execute("CREATE TABLE Rating (\n" +
//                "id int IDENTITY(1,1) NOT NULL,\n" +
//                "value int NOT NULL CHECK (value >= 1 AND value <= 5),\n" +
//                "PRIMARY KEY (id)\n" +
//                ")");
//        jdbcTemplate.execute("CREATE TABLE Tags (\n" +
//                "id int IDENTITY(1,1) NOT NULL,\n" +
//                "name nvarchar(100) NOT NULL,\n" +
//                "PRIMARY KEY (id)\n" +
//                ")");
//        jdbcTemplate.execute("CREATE TABLE TutorialTags (\n" +
//                "id int IDENTITY(1,1) NOT NULL,\n" +
//                "tags_id int NOT NULL,\n" +
//                "tutorial_id int NOT NULL,\n" +
//                "PRIMARY KEY (id)\n" +
//                ")");
//        jdbcTemplate.execute("CREATE TABLE TutorialRating (\n" +
//                "id int IDENTITY(1,1) NOT NULL,\n" +
//                "tutorial_id int NOT NULL,\n" +
//                "rating_id int NOT NULL,\n" +
//                "PRIMARY KEY (id)\n" +
//                ")");
//        jdbcTemplate.execute("ALTER TABLE Tutorial\n" +
//                "ADD FOREIGN KEY (language_id) REFERENCES Language(id) ON DELETE CASCADE ON UPDATE CASCADE");
//        jdbcTemplate.execute("ALTER TABLE Tutorial\n" +
//                "ADD FOREIGN KEY (format_id) REFERENCES Format(id) ON DELETE CASCADE ON UPDATE CASCADE");
//        jdbcTemplate.execute("ALTER TABLE TutorialTags\n" +
//                "ADD FOREIGN KEY (tutorial_id) REFERENCES Tutorial(id) ON DELETE CASCADE ON UPDATE CASCADE");
//        jdbcTemplate.execute("ALTER TABLE TutorialTags\n" +
//                "ADD FOREIGN KEY (tags_id) REFERENCES Tags(id) ON DELETE CASCADE ON UPDATE CASCADE");
//        jdbcTemplate.execute("ALTER TABLE TutorialRating\n" +
//                "ADD FOREIGN KEY (tutorial_id) REFERENCES Tutorial(id) ON DELETE CASCADE ON UPDATE CASCADE");
//        jdbcTemplate.execute("ALTER TABLE TutorialRating\n" +
//                "ADD FOREIGN KEY (rating_id) REFERENCES Rating(id) ON DELETE CASCADE ON UPDATE CASCADE");
    }
}
