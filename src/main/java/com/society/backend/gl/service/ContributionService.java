package com.society.backend.gl.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.society.backend.dto.CompulsoryContributionRequest;
import com.society.backend.entity.Flat;
import com.society.backend.entity.Member;
import com.society.backend.gl.dto.ContributionResponse;
import com.society.backend.gl.entity.Contribution;
import com.society.backend.gl.repository.ContributionRepository;
import com.society.backend.repository.FlatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ContributionService {

    private final ContributionRepository contributionRepository;
    private final FlatRepository flatRepository;
    private final JournalService journalService;

public List<ContributionResponse> getCompulsoryContributions(Long societyId) {

    List<Contribution> list =
            contributionRepository.findBySocietyIdAndType(societyId, "COMPULSORY");

    return list.stream().map(c -> {

        ContributionResponse dto = new ContributionResponse();

        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setAmount(c.getAmount());
        dto.setMode(c.getMode());
        dto.setDate(c.getDate());
        dto.setDueDate(c.getDueDate());
        dto.setStatus(c.getStatus().name());
        dto.setSocietyId(c.getSocietyId());
        dto.setMemberId(c.getMemberId());

        if (c.getFlat() != null) {
            dto.setFlatNo(c.getFlat().getFlatNo());
            dto.setAreaSqFt(c.getFlat().getAreaSqFt());
        }

        return dto;

    }).toList();
}

    public void createCompulsoryContribution(Long societyId, CompulsoryContributionRequest req) {

        List<Flat> flats = flatRepository.findBySociety_Id(societyId);

        List<Contribution> contributions = new ArrayList<>();

        for (Flat m : flats) {

            double amount;

            if ("FLAT".equalsIgnoreCase(req.getMode())) {
                amount = req.getFlatAmount();
            } else {
                amount = m.getAreaSqFt() * req.getRate();
            }

            Contribution c = new Contribution();
            c.setSocietyId(societyId);
            c.setMemberId(m.getId());
            c.setName(req.getName());
            c.setType("COMPULSORY");
            c.setMode(req.getMode());
            c.setFlat(m);
            c.setAmount(amount);
            c.setDueDate(req.getDueDate());
            c.setDate(req.getDate());
            c.setDescription(req.getDescription());
            c.setCreatedBy(req.getUserId());

            contributions.add(c);
        }

        // ✅ SAVE ONLY ONCE
        List<Contribution> savedContributions = contributionRepository.saveAll(contributions);

        // ✅ JOURNAL POSTING
        for (Contribution c : savedContributions) {

            Member member = c.getFlat().getOwner();

            try {

                Long journalId = journalService.createContributionEntry(
                        c.getId(),
                        member,
                        c.getAmount(),
                        societyId,
                        c.getCreatedBy(),
                        c.getFlat().getId());

                if (journalId == null) {
                    throw new RuntimeException(
                            "Journal not created for contribution " + c.getId());
                }

            } catch (Exception e) {

                throw new RuntimeException(
                        "Journal failed for contributionId="
                                + c.getId()
                                + " -> " + e.getMessage());
            }
        }
    }

}
