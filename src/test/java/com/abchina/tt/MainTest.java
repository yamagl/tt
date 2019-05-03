package com.abchina.tt;

import com.abchina.tt.generated.tables.House;
import com.abchina.tt.generated.tables.records.HouseRecord;
import com.abchina.tt.service.GeoService;
import com.abchina.tt.service.HouseService;
import org.jooq.Cursor;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class MainTest {
    private static Logger logger = LoggerFactory.getLogger(MainTest.class);
    private static volatile AtomicInteger counter = new AtomicInteger(0);
    private final static ArrayBlockingQueue<HouseRecord> queue = new ArrayBlockingQueue<>(5000, false);

    @Test
    void test() throws InterruptedException {
        CountDown countDown = new CountDown(1000, "warning");
        new Thread(countDown).start();
        Thread.sleep(900);
//        countDown.stop();
        Thread.sleep(200);

    }


    @Test
    void updateLocation() throws InterruptedException {
        logger.info("abc");
        logger.warn("abc");
        logger.debug("abc");

        HouseService houseService = new HouseService();

//            HouseSubscriber s = new HouseSubscriber(create, counter);
//            Scheduler s = Schedulers.from(Executors.newFixedThreadPool(4));

//            Disposable d = null;

        logger.info("begin");

//            new Thread(() -> {
//                DSLContext create = new DefaultDSLContext(houseService.getHouseDao().configuration());
//                try (Cursor<HouseRecord> cursor = create.selectFrom(House.HOUSE_)
//                        .where(House.HOUSE_.ADDRESS.isNotNull().and(House.HOUSE_.LOCATION.eq(""))
//                        ).fetchLazy()) {
//                    while (queue.size() < 500) {
//                        if (cursor.hasNext()) {
//                            cursor.fetchNext(300).forEach(queue::offer);
//                        } else
//                            break;
//                        try {
//                            Thread.sleep(5000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    logger.info("producer exit");
//                }
//            }).join();

        for (int i = 0; i < 50; ++i) {
            new Thread(() -> {
                logger.info("customer start");
                DSLContext create = new DefaultDSLContext(houseService.getHouseDao().configuration());
                try {
                    while (true) {
                        HouseRecord record = queue.poll(10, TimeUnit.SECONDS);
                        if (record == null)
                            break;
                        GeoService
                                .queryGeo(record.getAddress())
                                .ifPresent(r -> {
                                    record.setLocation(
                                            r.getGeocodes().get(0).getLocation()
                                    );
                                    CountDown dbCountDown = new CountDown(60000, "block on update database");
                                    new Thread(dbCountDown).start();
                                    create.update(House.HOUSE_).set(record).where(House.HOUSE_.ID.eq(record.getId())).execute();
                                    dbCountDown.stop();
                                    int current = counter.incrementAndGet();
                                    if (current % 100 == 0)
                                        logger.info(String.format("already update %d locations", current));
//                                        create.close();
                                });
                    }
                    logger.info("customer exit");
                } catch (InterruptedException ignored) {
                }

            }).start();
        }

        boolean exitFlag = false;
        DSLContext create = new DefaultDSLContext(houseService.getHouseDao().configuration());
        try (Cursor<HouseRecord> cursor = create.selectFrom(House.HOUSE_)
                .where(House.HOUSE_.ADDRESS.isNotNull().and(House.HOUSE_.LOCATION.eq(""))
                ).fetchLazy()) {
            while (true) {
                if (queue.size() < 3000 && !exitFlag) {
                    if (cursor.hasNext())
                        cursor.fetchNext(3000).forEach(queue::offer);
                    else
                        exitFlag = true;
                }

                if (exitFlag && queue.isEmpty())
                    break;

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
            }
            logger.info("producer exit");
        }

//            while (cursor.hasNext())  {
//                 Single
//                        .fromCallable(cursor::fetchNext)
//                        .observeOn(s)
//                        .subscribe((record, t) -> {
//                            if (t != null)
//                                logger.error("something happened", t);
//                            GeoService
//                                    .queryGeo(record.getAddress())
//                                    .ifPresent(r -> {
//                                        record.setLocation(
//                                                r.getGeocodes().get(0).getLocation()
//                                        );
//                                        CountDown dbCountDown = new CountDown(60000, "block on update database");
//                                        new Thread(dbCountDown).start();
//                                        create.update(House.HOUSE_).set(record).where(House.HOUSE_.ID.eq(record.getId())).execute();
//                                        dbCountDown.stop();
//                                        int current = counter.incrementAndGet();
//                                        if (current % 100 == 0)
//                                            logger.info(String.format("already update %d locations", current));
//                                        create.close();
//                                    });
//                        });
//            }
//            if (Objects.nonNull(d))
//                d.dispose();
    }
}