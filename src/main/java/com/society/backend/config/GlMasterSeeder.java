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

            Long societyId = 1L;

            List<GlMaster> list = List.of(

                // ================= ASSETS =================
                create(1000, "Assets","ASSETS", null,false, societyId),  
                create(1001, "Cash in Hand","ASSETS", 1000,true, societyId)
                );

            repo.saveAll(list);
        };
    }

    private GlMaster create(Integer code, String accountName,String groupName, Integer parentGlCode, Boolean posting , Long societyId) {

        GlMaster g = new GlMaster();

        g.setGlCode(code);
        g.setAccountName(accountName);
        g.setGroupName(groupName);
        g.setParentGlCode(parentGlCode);
        g.setPosting(posting);
        g.setSocietyId(societyId);
        g.setIsActive(true);

        return g;
    }
}