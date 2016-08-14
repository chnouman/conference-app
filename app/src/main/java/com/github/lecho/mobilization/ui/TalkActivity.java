package com.github.lecho.mobilization.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lecho.mobilization.R;
import com.github.lecho.mobilization.async.TalkAsyncHelper;
import com.github.lecho.mobilization.ui.loader.TalkLoader;
import com.github.lecho.mobilization.ui.snackbar.SnackbarForTalkHelper;
import com.github.lecho.mobilization.ui.view.SpeakerForTalkLayout;
import com.github.lecho.mobilization.util.Optional;
import com.github.lecho.mobilization.util.Utils;
import com.github.lecho.mobilization.viewmodel.SlotViewModel;
import com.github.lecho.mobilization.viewmodel.SpeakerViewModel;
import com.github.lecho.mobilization.viewmodel.TalkViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TalkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Optional<TalkViewModel>> {

    private static final String TAG = TalkActivity.class.getSimpleName();
    private static final String ARG_TALK_KEY = "talk-key";
    private static final int LOADER_ID = 0;
    private String talkKey;
    private FABController fabController;
    private HeaderController headerController;
    private InfoCardController infoCardController;
    private SpeakersCardController speakersCardController;
    private SnackbarForTalkHelper snackbarForTalkHelper;

    @BindView(R.id.main_container)
    View mainContainerView;

    @BindView(R.id.toolbar)
    Toolbar toolbarView;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    public static void startActivity(@NonNull Activity activity, @NonNull String talkKey) {
        Intent intent = new Intent(activity, TalkActivity.class);
        intent.putExtra(ARG_TALK_KEY, talkKey);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);
        ButterKnife.bind(this);

        fabController = new FABController(mainContainerView);
        headerController = new HeaderController(mainContainerView);
        headerController.bindHeaderImage();
        infoCardController = new InfoCardController(mainContainerView);
        speakersCardController = new SpeakersCardController(mainContainerView);
        snackbarForTalkHelper = new SnackbarForTalkHelper(getApplicationContext(), toolbarView);

        setSupportActionBar(toolbarView);
        collapsingToolbarLayout.setTitleEnabled(false);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(null);
        }

        talkKey = getIntent().getStringExtra(ARG_TALK_KEY);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        snackbarForTalkHelper.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        snackbarForTalkHelper.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Workaround:/ https://code.google.com/p/android/issues/detail?id=183334
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Optional<TalkViewModel>> onCreateLoader(int id, Bundle args) {
        if (id == LOADER_ID) {
            Log.w(TAG, "Create talk loader for key: " + talkKey);
            return TalkLoader.getLoader(this, talkKey);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Optional<TalkViewModel>> loader, Optional<TalkViewModel> data) {
        if (loader.getId() == LOADER_ID) {
            Log.w(TAG, "Loaded talk data: " + talkKey);
            if (!data.isPresent()) {
                Log.w(TAG, "Talk data is null for talk-key: " + talkKey);
                return;
            }
            TalkViewModel talkViewModel = data.get();
            fabController.bind(talkViewModel);
            headerController.bind(talkViewModel);
            infoCardController.bind(talkViewModel);
            speakersCardController.bind(talkViewModel);

        }
    }

    @Override
    public void onLoaderReset(Loader<Optional<TalkViewModel>> loader) {
    }

    protected class FABController {

        @BindView(R.id.button_add_to_my_agenda)
        FloatingActionButton addToMyAgendaButton;

        public FABController(View mainContainer) {
            ButterKnife.bind(this, mainContainer);
        }

        public void bind(TalkViewModel talkViewModel) {
            if (talkViewModel.isInMyAgenda) {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_24);
            } else {
                addToMyAgendaButton.setImageResource(R.drawable.ic_star_border_24);
            }
            addToMyAgendaButton.setOnClickListener(new AddToMyAgendaClickListener(talkViewModel));
            addToMyAgendaButton.show();
        }
    }

    protected class HeaderController {

        private static final String TALK_HEADER_IMAGE = "talk_header.jpg";

        @BindView(R.id.header_image)
        ImageView headerImageView;

        @BindView(R.id.text_talk_title)
        TextView talkTitleView;

        @BindView(R.id.text_venue)
        TextView talkVenueView;

        @BindView(R.id.text_language)
        TextView talkLanguageView;

        public HeaderController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bindHeaderImage() {
            Utils.loadHeaderImage(getApplicationContext(), TALK_HEADER_IMAGE, headerImageView);
        }

        public void bind(TalkViewModel talkViewModel) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                SlotViewModel.SlotInTimeZone slotInTimeZone = SlotViewModel.SlotInTimeZone.getSlotInTimezone(talkViewModel
                        .slot);
                actionBar.setTitle(slotInTimeZone.getTimeSlotText());
            }
            talkTitleView.setText(talkViewModel.title);
            talkVenueView.setText(talkViewModel.venue.getVenueText(getApplicationContext()));
            talkLanguageView.setText(talkViewModel.getLanguageLong(getApplicationContext()));
        }
    }

    protected class InfoCardController {

        @BindView(R.id.text_info)
        TextView talkInfoView;

        public InfoCardController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewModel talkViewModel) {
            talkInfoView.setText(Html.fromHtml(talkViewModel.description));
        }
    }

    protected class SpeakersCardController {

        @BindView(R.id.speakers_layout)
        LinearLayout speakersLayout;

        public SpeakersCardController(View view) {
            ButterKnife.bind(this, view);
        }

        public void bind(TalkViewModel talkViewModel) {
            speakersLayout.removeAllViews();
            for (SpeakerViewModel speakerViewModel : talkViewModel.speakers) {
                SpeakerForTalkLayout speakerForTalkLayout = new SpeakerForTalkLayout(TalkActivity.this, speakerViewModel);
                speakerForTalkLayout.bind();
                speakersLayout.addView(speakerForTalkLayout);
            }
        }
    }

    private class AddToMyAgendaClickListener implements View.OnClickListener {

        private TalkViewModel talkViewModel;

        public AddToMyAgendaClickListener(TalkViewModel talkViewModel) {
            this.talkViewModel = talkViewModel;
        }

        @Override
        public void onClick(View v) {
            FloatingActionButton floatingActionButton = (FloatingActionButton) v;
            if (talkViewModel.isInMyAgenda) {
                floatingActionButton.setImageResource(R.drawable.ic_star_border_24);
                talkViewModel.isInMyAgenda = false;
                TalkAsyncHelper.removeTalk(talkViewModel.key);
            } else {
                if (Utils.checkSlotConflict(TalkActivity.this, talkViewModel.key)) {
                    Log.d(TAG, "Slot conflict for talk with key: " + talkViewModel.key);
                    return;
                }
                floatingActionButton.setImageResource(R.drawable.ic_star_24);
                talkViewModel.isInMyAgenda = true;
                TalkAsyncHelper.addTalk(talkViewModel.key);
            }
        }
    }
}
