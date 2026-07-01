package com.society.backend.config;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.repository.GlMasterRepository;

@Configuration
public class GlMasterSeeder {

    @Bean
    CommandLineRunner seedGlAccounts(GlMasterRepository repo) {
        return args -> {

            // prevent duplicate seeding
            if (repo.count() > 0) {
                return;
            }

            List<GlMaster> list = List.of(
                );

            repo.saveAll(list);
        };
    }


}