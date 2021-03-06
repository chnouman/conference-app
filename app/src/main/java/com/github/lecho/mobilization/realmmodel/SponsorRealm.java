package com.github.lecho.mobilization.realmmodel;

import android.net.Uri;
import android.util.Log;

import com.github.lecho.mobilization.apimodel.SponsorApiModel;
import com.github.lecho.mobilization.viewmodel.SponsorViewModel;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Leszek on 2015-07-24.
 */
public class SponsorRealm extends RealmObject {

    public static final String TAG = RealmFacade.class.getSimpleName();
    @PrimaryKey
    private String name;
    private String logo;
    private String wwwPage;
    private String type;
    private int priority;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWwwPage() {
        return wwwPage;
    }

    public void setWwwPage(String wwwPage) {
        this.wwwPage = wwwPage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public static class SponsorApiConverter extends RealmFacade.ApiToRealmConverter<SponsorRealm, SponsorApiModel> {

        @Override
        public SponsorRealm convert(String key, SponsorApiModel apiModel) {
            SponsorRealm sponsorRealm = new SponsorRealm();
            sponsorRealm.setName(apiModel.name);
            sponsorRealm.setWwwPage(apiModel.link);
            sponsorRealm.setLogo(Uri.parse(apiModel.logoUrl).getLastPathSegment());
            SponsorType sponsorType = getSponsorType(apiModel.type);
            sponsorRealm.setType(sponsorType.name());
            sponsorRealm.setPriority(sponsorType.getPriority());
            return sponsorRealm;
        }

        private SponsorType getSponsorType(String sponsorTypeString) {
            try {
                return SponsorType.valueOf(sponsorTypeString.toUpperCase());
            } catch (IllegalArgumentException ex){
                Log.w(TAG, "Unknown sponsor type: " + sponsorTypeString);
                return SponsorType.OTHER;
            }
        }
    }

    public static class SponsorViewConverter extends RealmFacade.RealmToViewConverter<SponsorRealm, SponsorViewModel> {

        @Override
        public SponsorViewModel convert(SponsorRealm realmObject) {
            SponsorViewModel sponsorViewModel = new SponsorViewModel();
            sponsorViewModel.name = realmObject.getName();
            sponsorViewModel.wwwPage = realmObject.getWwwPage();
            sponsorViewModel.logo = realmObject.getLogo();
            //Realm doesn't support Enums
            sponsorViewModel.type = SponsorType.valueOf(realmObject.getType());
            return sponsorViewModel;
        }
    }
}
