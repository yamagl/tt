package com.abchina.tt.service;

import com.abchina.tt.generated.tables.House;
import com.abchina.tt.generated.tables.daos.HouseDao;
import com.abchina.tt.generated.tables.records.HouseRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jooq.AggregateFunction;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDSLContext;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

class HouseServiceTest {

    @Test
    void loadHouseData() throws IOException {
        HouseService houseService = new HouseService();
        houseService.loadHouseData("data\\09-IDFB-ITPS_CMS_HOUSE-GG-20190430", "\\|!");
    }


//    @Test
//    void loadSingleHouseData() {
//        HouseService houseService = new HouseService();
//        HouseDao houseDao = houseService.getHouseDao();
//
//        House house = new House(2L, "友谊路1588弄3号702室", new BigDecimal("765599238.00"), new BigDecimal("81.65"), "2", "");
//
//        houseDao.delete(house);
//
//        houseService.save(house);
//        houseDao.fetchById(2L).forEach(System.out::println);
//    }


    @Test
    void exportData() throws IOException {
//        Map<String, MutableInt> counter = new HashMap<>();
        HouseService houseService = new HouseService();
        DSLContext create = new DefaultDSLContext(houseService.getHouseDao().configuration());
        try (Cursor<Record2<String, Integer>> cursor = create.select(House.HOUSE_.LOCATION, DSL.count())
                .from(House.HOUSE_)
                .where(House.HOUSE_.LOCATION.ne(""))
                .groupBy(House.HOUSE_.LOCATION).fetchLazy()) {

            BufferedWriter writer = new BufferedWriter(new FileWriter("html/shanghai.tsv", false));
            writer.write("lng\tlat\tcount\n");
            while (cursor.hasNext()) {
                Record2<String, Integer> r = cursor.fetchNext();
                writer.write(r.value1().replace(",", "\t"));
                writer.write("\t" + r.value2() + "\n");
//                String[] coordinate = r.value1().split(",");
//                MutableInt initVal = new MutableInt(r.value2());
//                MutableInt old = counter.put(r.value1(), initVal);
//                if (Objects.nonNull(old)) {
//                    initVal.add(old);
//                }
            }
        }
    }
}