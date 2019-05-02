package com.abchina.tt;

import com.abchina.tt.generated.tables.House;
import com.abchina.tt.generated.tables.records.HouseRecord;
import com.abchina.tt.service.GeoService;
import org.jooq.DSLContext;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public class HouseSubscriber implements Subscriber<HouseRecord> {
    private Subscription subscription;
    private DSLContext create;
    private AtomicInteger counter;
    private final static Logger logger = LoggerFactory.getLogger(HouseSubscriber.class);

    public HouseSubscriber(DSLContext create, AtomicInteger counter) {
        this.create = create;
        this.counter = counter;
    }

    @Override
    public void onSubscribe(Subscription s) {
        subscription = s;
        s.request(1L);
    }

    @Override
    public void onNext(HouseRecord houseRecord) {
//        CountDown httpCountDown = new CountDown(30000, "block on send http request");
//        new Thread(httpCountDown).run();
        GeoService
                .queryGeo(houseRecord.getAddress())
                .ifPresent(r -> {
//                    httpCountDown.stop();
                    houseRecord.setLocation(
                            r.getGeocodes().get(0).getLocation()
                    );
                    CountDown dbCountDown = new CountDown(60000, "block on update database");
                    new Thread(dbCountDown).start();
                    create.update(House.HOUSE_).set(houseRecord).where(House.HOUSE_.ID.eq(houseRecord.getId())).execute();
                    dbCountDown.stop();
                    int current = counter.incrementAndGet();
                    if (current % 100 == 0)
                        logger.info(String.format("already update %d locations", current));
                    create.close();
                });
    }

    @Override
    public void onError(Throwable t) {
        logger.error("something happened", t);
        subscription.request(1L);
    }

    @Override
    public void onComplete() {
        //do nothing
    }
}
