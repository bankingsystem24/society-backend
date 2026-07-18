package com.society.backend.gl.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Service;

import com.society.backend.gl.entity.GlMapping;
import com.society.backend.gl.entity.GlMaster;
import com.society.backend.gl.repository.GlMappingRepository;
import com.society.backend.gl.repository.GlMasterRepository;

@Service
public class GlMasterService {

    private final GlMasterRepository glMasterRepository;
    private final GlMappingRepository glMappingRepository;

    public GlMasterService(GlMasterRepository glMasterRepository, GlMappingRepository glMappingRepository) {
        this.glMasterRepository = glMasterRepository;
        this.glMappingRepository = glMappingRepository;
    };

    public List<GlMaster> getAllBySociety(Long societyId) {
        return glMasterRepository.findBySocietyIdOrderByGlCodeAsc(societyId);
    }

    public List<GlMaster> getById(Long id) {
        return glMasterRepository.findById(id)
                .map(List::of)
                .orElse(Collections.emptyList());
    }


    public GlMaster save(GlMaster glMaster) {
        return glMasterRepository.save(glMaster);
    }

    public GlMaster update(
            Integer glCode,
            Long societyId,
            GlMaster glMaster) {

        GlMaster existing = glMasterRepository.findByGlCodeAndSocietyId(
                glCode,
                societyId);

        if (existing == null) {
            throw new RuntimeException("GL Master not found");
        }

        existing.setAccountName(glMaster.getAccountName());
        existing.setGroupName(glMaster.getGroupName());
        existing.setParentGlCode(glMaster.getParentGlCode());
        existing.setIsActive(glMaster.getIsActive());

        return glMasterRepository.save(existing);
    }

    public void delete(
            Integer glCode,
            Long societyId) {

        GlMaster existing = glMasterRepository.findByGlCodeAndSocietyId(
                glCode,
                societyId);

        if (existing == null) {
            throw new RuntimeException("GL Master not found");
        }

        glMasterRepository.delete(existing);
    }

    public List<GlMaster> getCashBankAccounts(Long societyId) {
        return glMasterRepository.findCashBankAccounts(societyId);
    }
    public GlMapping save(GlMapping glMapping) {
        return glMappingRepository.save(glMapping);
    }

    public List<GlMapping> getMappingBySociety(Long societyId) {
        return glMappingRepository.findBySocietyId(societyId);
    }

    public List<GlMaster> createDefaultGL(Long societyId) {

        List<GlMaster> glList = new ArrayList<>();

        GlMaster gl1 = new GlMaster();
        gl1.setSocietyId(societyId);
        gl1.setGlCode(1000);
        gl1.setAccountName("Assets");
        gl1.setGroupName("ASSETS");
        gl1.setParentGlCode(null);
        gl1.setIsActive(true);
        glList.add(gl1);

        GlMaster gl2 = new GlMaster();
        gl2.setSocietyId(societyId);
        gl2.setGlCode(1001);
        gl2.setAccountName("Cash in Hand");
        gl2.setGroupName("ASSETS");
        gl2.setParentGlCode(1000);
        gl2.setIsActive(true);
        glList.add(gl2);

        GlMaster gl3 = new GlMaster();
        gl3.setSocietyId(societyId);
        gl3.setGlCode(1002);
        gl3.setAccountName("Bank Account");
        gl3.setGroupName("ASSETS");
        gl3.setParentGlCode(1000);
        gl3.setIsActive(true);
        glList.add(gl3);

        GlMaster gl4 = new GlMaster();
        gl4.setSocietyId(societyId);
        gl4.setGlCode(1200);
        gl4.setAccountName("Receivables");
        gl4.setGroupName("ASSETS");
        gl4.setParentGlCode(null);
        gl4.setIsActive(true);
        glList.add(gl4);

        GlMaster gl5 = new GlMaster();
        gl5.setSocietyId(societyId);
        gl5.setGlCode(1201);
        gl5.setAccountName("Maintenance Receivables");
        gl5.setGroupName("ASSETS");
        gl5.setParentGlCode(1200);
        gl5.setIsActive(true);
        glList.add(gl5);

        GlMaster gl6 = new GlMaster();
        gl6.setSocietyId(societyId);
        gl6.setGlCode(1202);
        gl6.setAccountName("Member Receivables");
        gl6.setGroupName("ASSETS");
        gl6.setParentGlCode(1200);
        gl6.setIsActive(true);
        glList.add(gl6);

        GlMaster gl7 = new GlMaster();
        gl7.setSocietyId(societyId);
        gl7.setGlCode(1203);
        gl7.setAccountName("GST Receivables");
        gl7.setGroupName("ASSETS");
        gl7.setParentGlCode(1200);
        gl7.setIsActive(true);
        glList.add(gl7);

        GlMaster gl8 = new GlMaster();
        gl8.setSocietyId(societyId);
        gl8.setGlCode(1204);
        gl8.setAccountName("Sinking Fund Receivables");
        gl8.setGroupName("ASSETS");
        gl8.setParentGlCode(1200);
        gl8.setIsActive(true);
        glList.add(gl8);

        GlMaster gl8a = new GlMaster();
        gl8a.setSocietyId(societyId);
        gl8a.setGlCode(1205);
        gl8a.setAccountName("Painting Contribution Receivables");
        gl8a.setGroupName("ASSETS");
        gl8a.setParentGlCode(1200);
        gl8a.setIsActive(true);
        glList.add(gl8a);

        GlMaster gl9 = new GlMaster();
        gl9.setSocietyId(societyId);
        gl9.setGlCode(1500);
        gl9.setAccountName("Fixed Assets");
        gl9.setGroupName("ASSETS");
        gl9.setParentGlCode(null);
        gl9.setIsActive(true);
        glList.add(gl9);

        GlMaster gl10 = new GlMaster();
        gl10.setSocietyId(societyId);
        gl10.setGlCode(1501);
        gl10.setAccountName("Building");
        gl10.setGroupName("ASSETS");
        gl10.setParentGlCode(1500);
        gl10.setIsActive(true);
        glList.add(gl10);

        GlMaster gl11 = new GlMaster();
        gl11.setSocietyId(societyId);
        gl11.setGlCode(1502);
        gl11.setAccountName("Lift");
        gl11.setGroupName("ASSETS");
        gl11.setParentGlCode(1500);
        gl11.setIsActive(true);
        glList.add(gl11);

        GlMaster gl12 = new GlMaster();
        gl12.setSocietyId(societyId);
        gl12.setGlCode(1503);
        gl12.setAccountName("Shed Contribution");
        gl12.setGroupName("ASSETS");
        gl12.setParentGlCode(1500);
        gl12.setIsActive(true);
        glList.add(gl12);

        GlMaster gl13 = new GlMaster();
        gl13.setSocietyId(societyId);
        gl13.setGlCode(1504);
        gl13.setAccountName("Generator");
        gl13.setGroupName("ASSETS");
        gl13.setParentGlCode(1500);
        gl13.setIsActive(true);
        glList.add(gl13);

        GlMaster gl14 = new GlMaster();
        gl14.setSocietyId(societyId);
        gl14.setGlCode(1505);
        gl14.setAccountName("Furniture & Fixtures");
        gl14.setGroupName("ASSETS");
        gl14.setParentGlCode(1500);
        gl14.setIsActive(true);
        glList.add(gl14);

        GlMaster gl15 = new GlMaster();
        gl15.setSocietyId(societyId);
        gl15.setGlCode(2000);
        gl15.setAccountName("Liabilities");
        gl15.setGroupName("LIABILITIES");
        gl15.setParentGlCode(null);
        gl15.setIsActive(true);
        glList.add(gl15);

        GlMaster gl16 = new GlMaster();
        gl16.setSocietyId(societyId);
        gl16.setGlCode(2001);
        gl16.setAccountName("Vendor Payable");
        gl16.setGroupName("LIABILITIES");
        gl16.setParentGlCode(2000);
        gl16.setIsActive(true);
        glList.add(gl16);

        GlMaster gl17 = new GlMaster();
        gl17.setSocietyId(societyId);
        gl17.setGlCode(2002);
        gl17.setAccountName("Electricity Charges Payable");
        gl17.setGroupName("LIABILITIES");
        gl17.setParentGlCode(2000);
        gl17.setIsActive(true);
        glList.add(gl17);

        GlMaster gl18 = new GlMaster();
        gl18.setSocietyId(societyId);
        gl18.setGlCode(2003);
        gl18.setAccountName("Water Charges Payable");
        gl18.setGroupName("LIABILITIES");
        gl18.setParentGlCode(2000);
        gl18.setIsActive(true);
        glList.add(gl18);

        GlMaster gl19 = new GlMaster();
        gl19.setSocietyId(societyId);
        gl19.setGlCode(2004);
        gl19.setAccountName("Salary Payable");
        gl19.setGroupName("LIABILITIES");
        gl19.setParentGlCode(2000);
        gl19.setIsActive(true);
        glList.add(gl19);

        GlMaster gl20 = new GlMaster();
        gl20.setSocietyId(societyId);
        gl20.setGlCode(2005);
        gl20.setAccountName("Audit Fees Payable");
        gl20.setGroupName("LIABILITIES");
        gl20.setParentGlCode(2000);
        gl20.setIsActive(true);
        glList.add(gl20);

        GlMaster gl21 = new GlMaster();
        gl21.setSocietyId(societyId);
        gl21.setGlCode(2006);
        gl21.setAccountName("TDS Payable");
        gl21.setGroupName("LIABILITIES");
        gl21.setParentGlCode(2000);
        gl21.setIsActive(true);
        glList.add(gl21);

        GlMaster gl22 = new GlMaster();
        gl22.setSocietyId(societyId);
        gl22.setGlCode(2007);
        gl22.setAccountName("GST Payable");
        gl22.setGroupName("LIABILITIES");
        gl22.setParentGlCode(2000);
        gl22.setIsActive(true);
        glList.add(gl22);

        GlMaster gl23 = new GlMaster();
        gl23.setSocietyId(societyId);
        gl23.setGlCode(3000);
        gl23.setAccountName("Reserves / Funds");
        gl23.setGroupName("RESERVES");
        gl23.setParentGlCode(null);
        gl23.setIsActive(true);
        glList.add(gl23);

        GlMaster gl24 = new GlMaster();
        gl24.setSocietyId(societyId);
        gl24.setGlCode(3001);
        gl24.setAccountName("Sinking Fund");
        gl24.setGroupName("RESERVES");
        gl24.setParentGlCode(3000);
        gl24.setIsActive(true);
        glList.add(gl24);

        GlMaster gl25 = new GlMaster();
        gl25.setSocietyId(societyId);
        gl25.setGlCode(3002);
        gl25.setAccountName("Repair Fund");
        gl25.setGroupName("RESERVES");
        gl25.setParentGlCode(3000);
        gl25.setIsActive(true);
        glList.add(gl25);

        GlMaster gl26 = new GlMaster();
        gl26.setSocietyId(societyId);
        gl26.setGlCode(3003);
        gl26.setAccountName("Parking Fund");
        gl26.setGroupName("RESERVES");
        gl26.setParentGlCode(3000);
        gl26.setIsActive(true);
        glList.add(gl26);

        GlMaster gl27 = new GlMaster();
        gl27.setSocietyId(societyId);
        gl27.setGlCode(3004);
        gl27.setAccountName("Building Maintenance Fund");
        gl27.setGroupName("RESERVES");
        gl27.setParentGlCode(3000);
        gl27.setIsActive(true);
        glList.add(gl27);

        GlMaster gl28 = new GlMaster();
        gl28.setSocietyId(societyId);
        gl28.setGlCode(3005);
        gl28.setAccountName("Emergency Fund");
        gl28.setGroupName("RESERVES");
        gl28.setParentGlCode(3000);
        gl28.setIsActive(true);
        glList.add(gl28);

        GlMaster gl29 = new GlMaster();
        gl29.setSocietyId(societyId);
        gl29.setGlCode(3006);
        gl29.setAccountName("Shed Contribution Fund");
        gl29.setGroupName("RESERVES");
        gl29.setParentGlCode(3000);
        gl29.setIsActive(true);
        glList.add(gl29);

        GlMaster gl30 = new GlMaster();
        gl30.setSocietyId(societyId);
        gl30.setGlCode(3007);
        gl30.setAccountName("General Reserves");
        gl30.setGroupName("RESERVES");
        gl30.setParentGlCode(3000);
        gl30.setIsActive(true);
        glList.add(gl30);

        GlMaster gl31 = new GlMaster();
        gl31.setSocietyId(societyId);
        gl31.setGlCode(3008);
        gl31.setAccountName("Accumulated Surplus");
        gl31.setGroupName("RESERVES");
        gl31.setParentGlCode(3000);
        gl31.setIsActive(true);
        glList.add(gl31);

        GlMaster gl31a = new GlMaster();
        gl31a.setSocietyId(societyId);
        gl31a.setGlCode(3009);
        gl31a.setAccountName("Painting Fund");
        gl31a.setGroupName("RESERVES");
        gl31a.setParentGlCode(3000);
        gl31a.setIsActive(true);
        glList.add(gl31a);

        GlMaster gl32 = new GlMaster();
        gl32.setSocietyId(societyId);
        gl32.setGlCode(4000);
        gl32.setAccountName("Income");
        gl32.setGroupName("INCOME");
        gl32.setParentGlCode(null);
        gl32.setIsActive(true);
        glList.add(gl32);

        GlMaster gl33 = new GlMaster();
        gl33.setSocietyId(societyId);
        gl33.setGlCode(4001);
        gl33.setAccountName("Monthly Maintenance Income");
        gl33.setGroupName("INCOME");
        gl33.setParentGlCode(4000);
        gl33.setIsActive(true);
        glList.add(gl33);

        GlMaster gl34 = new GlMaster();
        gl34.setSocietyId(societyId);
        gl34.setGlCode(4002);
        gl34.setAccountName("Interest Income");
        gl34.setGroupName("INCOME");
        gl34.setParentGlCode(4000);
        gl34.setIsActive(true);
        glList.add(gl34);

        GlMaster gl35 = new GlMaster();
        gl35.setSocietyId(societyId);
        gl35.setGlCode(4004);
        gl35.setAccountName("Parking Charges Income");
        gl35.setGroupName("INCOME");
        gl35.setParentGlCode(4000);
        gl35.setIsActive(true);
        glList.add(gl35);

        GlMaster gl36 = new GlMaster();
        gl36.setSocietyId(societyId);
        gl36.setGlCode(4005);
        gl36.setAccountName("Club House Income");
        gl36.setGroupName("INCOME");
        gl36.setParentGlCode(4000);
        gl36.setIsActive(true);
        glList.add(gl36);

        GlMaster gl37 = new GlMaster();
        gl37.setSocietyId(societyId);
        gl37.setGlCode(4006);
        gl37.setAccountName("Advertisement Income");
        gl37.setGroupName("INCOME");
        gl37.setParentGlCode(4000);
        gl37.setIsActive(true);
        glList.add(gl37);

        GlMaster gl38 = new GlMaster();
        gl38.setSocietyId(societyId);
        gl38.setGlCode(4007);
        gl38.setAccountName("Transfer Fee Income");
        gl38.setGroupName("INCOME");
        gl38.setParentGlCode(4000);
        gl38.setIsActive(true);
        glList.add(gl38);

        GlMaster gl39 = new GlMaster();
        gl39.setSocietyId(societyId);
        gl39.setGlCode(4008);
        gl39.setAccountName("Rental Income");
        gl39.setGroupName("INCOME");
        gl39.setParentGlCode(4000);
        gl39.setIsActive(true);
        glList.add(gl39);

        GlMaster gl40 = new GlMaster();
        gl40.setSocietyId(societyId);
        gl40.setGlCode(4009);
        gl40.setAccountName("Event Contribution Income");
        gl40.setGroupName("INCOME");
        gl40.setParentGlCode(4000);
        gl40.setIsActive(true);
        glList.add(gl40);

        GlMaster gl41 = new GlMaster();
        gl41.setSocietyId(societyId);
        gl41.setGlCode(4010);
        gl41.setAccountName("Miscellaneous Income");
        gl41.setGroupName("INCOME");
        gl41.setParentGlCode(4000);
        gl41.setIsActive(true);
        glList.add(gl41);

        GlMaster gl42 = new GlMaster();
        gl42.setSocietyId(societyId);
        gl42.setGlCode(5000);
        gl42.setAccountName("Expenses");
        gl42.setGroupName("EXPENSES");
        gl42.setParentGlCode(null);
        gl42.setIsActive(true);
        glList.add(gl42);

        GlMaster gl43 = new GlMaster();
        gl43.setSocietyId(societyId);
        gl43.setGlCode(5001);
        gl43.setAccountName("Discount");
        gl43.setGroupName("EXPENSES");
        gl43.setParentGlCode(5000);
        gl43.setIsActive(true);
        glList.add(gl43);

        GlMaster gl44 = new GlMaster();
        gl44.setSocietyId(societyId);
        gl44.setGlCode(5002);
        gl44.setAccountName("Electricity Charges");
        gl44.setGroupName("EXPENSES");
        gl44.setParentGlCode(5000);
        gl44.setIsActive(true);
        glList.add(gl44);

        GlMaster gl45 = new GlMaster();
        gl45.setSocietyId(societyId);
        gl45.setGlCode(5003);
        gl45.setAccountName("Water Charges");
        gl45.setGroupName("EXPENSES");
        gl45.setParentGlCode(5000);
        gl45.setIsActive(true);
        glList.add(gl45);

        GlMaster gl46 = new GlMaster();
        gl46.setSocietyId(societyId);
        gl46.setGlCode(5004);
        gl46.setAccountName("Security Guard Salary");
        gl46.setGroupName("EXPENSES");
        gl46.setParentGlCode(5000);
        gl46.setIsActive(true);
        glList.add(gl46);

        GlMaster gl47 = new GlMaster();
        gl47.setSocietyId(societyId);
        gl47.setGlCode(5005);
        gl47.setAccountName("Housekeeping Charges");
        gl47.setGroupName("EXPENSES");
        gl47.setParentGlCode(5000);
        gl47.setIsActive(true);
        glList.add(gl47);

        GlMaster gl48 = new GlMaster();
        gl48.setSocietyId(societyId);
        gl48.setGlCode(5006);
        gl48.setAccountName("Repairs & Maintenance");
        gl48.setGroupName("EXPENSES");
        gl48.setParentGlCode(5000);
        gl48.setIsActive(true);
        glList.add(gl48);

        GlMaster gl49 = new GlMaster();
        gl49.setSocietyId(societyId);
        gl49.setGlCode(5007);
        gl49.setAccountName("Lift Maintenance");
        gl49.setGroupName("EXPENSES");
        gl49.setParentGlCode(5000);
        gl49.setIsActive(true);
        glList.add(gl49);

        GlMaster gl50 = new GlMaster();
        gl50.setSocietyId(societyId);
        gl50.setGlCode(5008);
        gl50.setAccountName("Garden Maintenance");
        gl50.setGroupName("EXPENSES");
        gl50.setParentGlCode(5000);
        gl50.setIsActive(true);
        glList.add(gl50);

        GlMaster gl51 = new GlMaster();
        gl51.setSocietyId(societyId);
        gl51.setGlCode(5009);
        gl51.setAccountName("Office & Admin Expenses");
        gl51.setGroupName("EXPENSES");
        gl51.setParentGlCode(5000);
        gl51.setIsActive(true);
        glList.add(gl51);

        GlMaster gl52 = new GlMaster();
        gl52.setSocietyId(societyId);
        gl52.setGlCode(5010);
        gl52.setAccountName("Printing & Stationary");
        gl52.setGroupName("EXPENSES");
        gl52.setParentGlCode(5000);
        gl52.setIsActive(true);
        glList.add(gl52);

        GlMaster gl53 = new GlMaster();
        gl53.setSocietyId(societyId);
        gl53.setGlCode(5011);
        gl53.setAccountName("Audit Fees");
        gl53.setGroupName("EXPENSES");
        gl53.setParentGlCode(5000);
        gl53.setIsActive(true);
        glList.add(gl53);

        GlMaster gl54 = new GlMaster();
        gl54.setSocietyId(societyId);
        gl54.setGlCode(5012);
        gl54.setAccountName("Legal & Professional Fees");
        gl54.setGroupName("EXPENSES");
        gl54.setParentGlCode(5000);
        gl54.setIsActive(true);
        glList.add(gl54);

        GlMaster gl55 = new GlMaster();
        gl55.setSocietyId(societyId);
        gl55.setGlCode(5013);
        gl55.setAccountName("Bank Charges");
        gl55.setGroupName("EXPENSES");
        gl55.setParentGlCode(5000);
        gl55.setIsActive(true);
        glList.add(gl55);

        GlMaster gl56 = new GlMaster();
        gl56.setSocietyId(societyId);
        gl56.setGlCode(5014);
        gl56.setAccountName("Society Event Expenses");
        gl56.setGroupName("EXPENSES");
        gl56.setParentGlCode(5000);
        gl56.setIsActive(true);
        glList.add(gl56);

        GlMaster gl57 = new GlMaster();
        gl57.setSocietyId(societyId);
        gl57.setGlCode(5015);
        gl57.setAccountName("Insurance Expenses");
        gl57.setGroupName("EXPENSES");
        gl57.setParentGlCode(5000);
        gl57.setIsActive(true);
        glList.add(gl57);

        GlMaster gl58 = new GlMaster();
        gl58.setSocietyId(societyId);
        gl58.setGlCode(5016);
        gl58.setAccountName("AMC Charges");
        gl58.setGroupName("EXPENSES");
        gl58.setParentGlCode(5000);
        gl58.setIsActive(true);
        glList.add(gl58);

        GlMaster gl59 = new GlMaster();
        gl59.setSocietyId(societyId);
        gl59.setGlCode(5017);
        gl59.setAccountName("Pest Control Charges");
        gl59.setGroupName("EXPENSES");
        gl59.setParentGlCode(5000);
        gl59.setIsActive(true);
        glList.add(gl59);

        GlMaster gl60 = new GlMaster();
        gl60.setSocietyId(societyId);
        gl60.setGlCode(5018);
        gl60.setAccountName("Generator Diesel Expenses");
        gl60.setGroupName("EXPENSES");
        gl60.setParentGlCode(5000);
        gl60.setIsActive(true);
        glList.add(gl60);

        GlMaster gl61 = new GlMaster();
        gl61.setSocietyId(societyId);
        gl61.setGlCode(5019);
        gl61.setAccountName("Internel & Software Expenses");
        gl61.setGroupName("EXPENSES");
        gl61.setParentGlCode(5000);
        gl61.setIsActive(true);
        glList.add(gl61);

        GlMaster gl62 = new GlMaster();
        gl62.setSocietyId(societyId);
        gl62.setGlCode(5020);
        gl62.setAccountName("Depreciation - Building");
        gl62.setGroupName("EXPENSES");
        gl62.setParentGlCode(5000);
        gl62.setIsActive(true);
        glList.add(gl62);

        GlMaster gl63 = new GlMaster();
        gl63.setSocietyId(societyId);
        gl63.setGlCode(5021);
        gl63.setAccountName("Depreciation - Lift");
        gl63.setGroupName("EXPENSES");
        gl63.setParentGlCode(5000);
        gl63.setIsActive(true);
        glList.add(gl63);

        GlMaster gl64 = new GlMaster();
        gl64.setSocietyId(societyId);
        gl64.setGlCode(5022);
        gl64.setAccountName("Depreciation - Generator");
        gl64.setGroupName("EXPENSES");
        gl64.setParentGlCode(5000);
        gl64.setIsActive(true);
        glList.add(gl64);

        GlMaster gl65 = new GlMaster();
        gl65.setSocietyId(societyId);
        gl65.setGlCode(5023);
        gl65.setAccountName("Depreciation - Furniture");
        gl65.setGroupName("EXPENSES");
        gl65.setParentGlCode(5000);
        gl65.setIsActive(true);
        glList.add(gl65);

        List<GlMapping> glMappingList = new ArrayList<>();

        GlMapping glm1 = new GlMapping();
        glm1.setSocietyId(societyId);
        glm1.setDescription("Monthly Maintenance");
        glm1.setGl_credit_account(4001);
        glm1.setGl_receivable(1201);
        glMappingList.add(glm1);

        GlMapping glm2 = new GlMapping();
        glm2.setSocietyId(societyId);
        glm2.setDescription("Sinking Fund Receivable");
        glm2.setGl_credit_account(3001);
        glm2.setGl_receivable(1204);
        glMappingList.add(glm2);

        GlMapping glm3 = new GlMapping();
        glm3.setSocietyId(societyId);
        glm3.setDescription("Paintaining Contribution Receivable");
        glm3.setGl_credit_account(3009);
        glm3.setGl_receivable(1205);
        glMappingList.add(glm3);

        GlMapping glm4 = new GlMapping();
        glm4.setSocietyId(societyId);
        glm4.setDescription("Cash in Hand");
        glm4.setGl_credit_account(null);
        glm4.setGl_receivable(1001);
        glMappingList.add(glm4);

        GlMapping glm5 = new GlMapping();
        glm5.setSocietyId(societyId);
        glm5.setDescription("Bank Account");
        glm5.setGl_credit_account(null);
        glm5.setGl_receivable(1002);
        glMappingList.add(glm5);

        GlMapping glm6 = new GlMapping();
        glm6.setSocietyId(societyId);
        glm6.setDescription("Interest Income");
        glm6.setGl_credit_account(null);
        glm6.setGl_receivable(4002);
        glMappingList.add(glm6);

        GlMapping glm7 = new GlMapping();
        glm7.setSocietyId(societyId);
        glm7.setDescription("Discount");
        glm7.setGl_credit_account(null);
        glm7.setGl_receivable(5001);
        glMappingList.add(glm7);


        glMappingRepository.saveAll(glMappingList);

        return glMasterRepository.saveAll(glList);
    }

}