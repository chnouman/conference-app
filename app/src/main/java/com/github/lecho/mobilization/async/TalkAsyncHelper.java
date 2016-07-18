package com.github.lecho.mobilization.async;

import android.content.Context;
import android.os.AsyncTask;

import com.github.lecho.mobilization.realmmodel.RealmFacade;
import com.github.lecho.mobilization.ui.snackbar.SnackbarForTalkObserver;

/**
 * Created by Leszek on 2015-10-05.
 */
public class TalkAsyncHelper {

    public static void addTalk(Context context, String talkKey) {
        new AddTalkTask(context, talkKey).execute();
    }

    public static void removeTalk(Context context, String talkKey) {
        new RemoveTalkTask(context, talkKey).execute();
    }

    public static void removeTalkSilent(Context context, String talkKey) {
        new RemoveTalkTaskSilent(context, talkKey).execute();
    }

    public static void replaceTalk(Context context, String oldTalkKey, String newTalkKey) {
        new ReplaceTalkTask(context, oldTalkKey, newTalkKey).execute();
    }

    private static class AddTalkTask extends AsyncTask<Void, Void, Void> {

        private final Context context;
        private final String talkKey;

        private AddTalkTask(Context context, String talkKey) {
            this.context = context;
            this.talkKey = talkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.addTalkToMyAgenda(talkKey);
            return null;
        }
    }

    private static class RemoveTalkTask extends AsyncTask<Void, Void, Void> {

        private final Context context;
        private final String talkKey;

        private RemoveTalkTask(Context context, String talkKey) {
            this.context = context;
            this.talkKey = talkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.removeTalkFromMyAgenda(talkKey);
            return null;
        }
    }

    private static class RemoveTalkTaskSilent extends AsyncTask<Void, Void, Void> {

        private final Context context;
        private final String talkKey;

        private RemoveTalkTaskSilent(Context context, String talkKey) {
            this.context = context;
            this.talkKey = talkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.removeTalkFromMyAgendaSilent(talkKey);
            return null;
        }
    }

    private static class ReplaceTalkTask extends AsyncTask<Void, Void, Void> {

        private final Context context;
        private final String oldTalkKey;
        private final String newTalkKey;

        private ReplaceTalkTask(Context context, String oldTalkKey, String newTalkKey) {
            this.context = context;
            this.oldTalkKey = oldTalkKey;
            this.newTalkKey = newTalkKey;
        }

        @Override
        protected Void doInBackground(Void... params) {
            RealmFacade realmFacade = new RealmFacade();
            realmFacade.replaceTalk(oldTalkKey, newTalkKey);
            return null;
        }
    }
}
