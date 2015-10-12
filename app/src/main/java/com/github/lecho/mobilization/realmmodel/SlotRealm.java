package com.github.lecho.mobilization.realmmodel;


import android.util.Log;

import com.github.lecho.mobilization.apimodel.SlotApiDto;
import com.github.lecho.mobilization.viewmodel.SlotViewDto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SlotRealm extends RealmObject {

    @PrimaryKey
    private String key;
    private String from;
    private String to;
    private long fromInMilliseconds;
    private long toInMilliseconds;
    private boolean isInMyAgenda;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getFromInMilliseconds() {
        return fromInMilliseconds;
    }

    public void setFromInMilliseconds(long fromInMilliseconds) {
        this.fromInMilliseconds = fromInMilliseconds;
    }

    public long getToInMilliseconds() {
        return toInMilliseconds;
    }

    public void setToInMilliseconds(long toInMilliseconds) {
        this.toInMilliseconds = toInMilliseconds;
    }

    public boolean isInMyAgenda() {
        return isInMyAgenda;
    }

    public void setIsInMyAgenda(boolean isInMyAgenda) {
        this.isInMyAgenda = isInMyAgenda;
    }

    public static class SlotApiConverter extends RealmFacade.ApiToRealmConverter<SlotRealm, SlotApiDto> {
        private static final String TAG = SlotApiConverter.class.getSimpleName();
        private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        private static final String LOCAL_TIMEZONE = "Poland";

        @Override
        public SlotRealm convert(String key, SlotApiDto apiDto) {
            SlotRealm slotRealm = new SlotRealm();
            slotRealm.setKey(key);
            slotRealm.setFrom(apiDto.from);
            slotRealm.setTo(apiDto.to);
            timeToMilliseconds(slotRealm);
            slotRealm.setIsInMyAgenda(false);
            return slotRealm;
        }

        public void timeToMilliseconds(SlotRealm slotRealm) {
            try {
                dateFormat.setTimeZone(TimeZone.getTimeZone(LOCAL_TIMEZONE));
                slotRealm.setFromInMilliseconds(dateFormat.parse(slotRealm.getFrom()).getTime());
                slotRealm.setToInMilliseconds(dateFormat.parse(slotRealm.getTo()).getTime());
            } catch (ParseException e) {
                Log.e(TAG, "Could not parse Slots From and To values", e);
            }
        }
    }

    public static class SlotViewConverter extends RealmFacade.RealmToViewConverter<SlotRealm, SlotViewDto> {

        @Override
        public SlotViewDto convert(SlotRealm realmObject) {
            SlotViewDto slotViewDto = new SlotViewDto();
            slotViewDto.key = realmObject.getKey();
            slotViewDto.from = realmObject.getFrom();
            slotViewDto.to = realmObject.getTo();
            slotViewDto.fromInMilliseconds = realmObject.getFromInMilliseconds();
            slotViewDto.toInMilliseconds = realmObject.getToInMilliseconds();
            slotViewDto.isEmpty = realmObject.isInMyAgenda();
            return slotViewDto;
        }
    }
}
