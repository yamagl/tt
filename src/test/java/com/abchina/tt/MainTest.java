package com.abchina.tt;

import com.abchina.tt.generated.tables.House;
import com.abchina.tt.generated.tables.records.HouseRecord;
import com.abchina.tt.service.HouseService;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

class MainTest {
    private static Logger logger = LoggerFactory.getLogger(MainTest.class);
    private static volatile AtomicInteger counter = new AtomicInteger(0);

    @Test
    void test() throws InterruptedException {
        CountDown countDown = new CountDown(1000, "warning");
        new Thread(countDown).start();
        Thread.sleep(900);
//        countDown.stop();
        Thread.sleep(200);

    }


    @Test
    void updateLocation() {
        logger.info("abc");
        logger.warn("abc");

        HouseService houseService = new HouseService();
        DSLContext create = new DefaultDSLContext(houseService.getHouseDao().configuration());
        try (Cursor<HouseRecord> cursor = create.selectFrom(House.HOUSE_)
                .where(House.HOUSE_.ADDRESS.isNotNull().and(House.HOUSE_.LOCATION.eq(""))
                ).fetchLazy()) {
            HouseSubscriber s = new HouseSubscriber(create, counter);
//            Scheduler scheduler = Schedulers.io();

            while(cursor.hasNext()) {
                Single.fromCallable(cursor::fetchNext).observeOn(Schedulers.from(Executors.newFixedThreadPool(4))).subscribe(s::onNext, s::onError);
            }
        }
    }
}