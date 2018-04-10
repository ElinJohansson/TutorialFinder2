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


	    jdbcTemplate.execute("CREATE TABLE Users (\n" +
                "\tusername nvarchar(100) NOT NULL,\n" +
                "\tpassword nvarchar(100) NOT NULL,\n" +
                "\tPRIMARY KEY (username)\n" +
                ");");

	    jdbcTemplate.execute("CREATE TABLE Tutorial (\n" +
                "\tid int IDENTITY(1,1) NOT NULL,\n" +
                "\ttitle nvarchar(200) NOT NULL,\n" +
                "\tdescr text NOT NULL,\n" +
                "\tcreationDate date NOT NULL,\n" +
                "\tlanguage_id int NOT NULL,\n" +
                "\tformat_id int NOT NULL,\n" +
                "\tPRIMARY KEY (Id)\n" +
                ");");

	    jdbcTemplate.execute("CREATE TABLE Language (\n" +
                "\tid int IDENTITY(1,1) NOT NULL,\n" +
                "\tname nvarchar(100) NOT NULL,\n" +
                "\ttutorial_id int NOT NULL, \n" +
                "\tPRIMARY KEY (id),\n" +
                "\tCONSTRAINT FK_Tutorial_Language FOREIGN KEY (tutorial_id) REFERENCES Tutorial(id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");");
    }
}
