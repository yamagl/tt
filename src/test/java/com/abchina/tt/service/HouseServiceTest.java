package com.abchina.tt.service;

import com.abchina.tt.generated.tables.daos.HouseDao;
import com.abchina.tt.generated.tables.pojos.House;
import com.abchina.tt.generated.tables.records.HouseRecord;
import org.jooq.meta.derby.sys.Sys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HouseServiceTest {

    @Test
    void loadHouseData() throws IOException {
        HouseService houseService = new HouseService();
        houseService.loadHouseData("data\\09-IDFB-ITPS_CMS_HOUSE-GG-20190430", "\\|!");
    }


    @Test
    void loadSingleHouseData() {
        HouseService houseService = new HouseService();
        HouseDao houseDao = houseService.getHouseDao();

        House house = new House(2L, "友谊路1588弄3号702室", new BigDecimal("765599238.00"), new BigDecimal("81.65"), "2", "");

        houseDao.delete(house);

        houseService.save(house);
        houseDao.fetchById(2L).forEach(System.out::println);
    }

}