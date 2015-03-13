package com.ptapp.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ptapp.adapter.GroupChatLvAdapter;
import com.ptapp.dao.EducatorDAO;
import com.ptapp.app.R;

import java.util.ArrayList;

import static com.ptapp.utils.LogUtils.makeLogTag;

public class EducatorGroupMsgActivity extends Activity {

    private static final String TAG = makeLogTag(EducatorGroupMsgActivity.class);
    ListView lst_msg;
    EditText msgTyped;
    LinearLayout sendToBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educator_group_msg);

        lst_msg = (ListView) findViewById(R.id.list_msg);
        msgTyped = (EditText) findViewById(R.id.type_msg);
        sendToBar = (LinearLayout) findViewById(R.id.send_to_bar);

        //show send To bar on touch of the type message edit text
        msgTyped.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                sendToBar.setVisibility(View.VISIBLE);
                return false;
            }
        });


        ArrayList<String> msgs = new ArrayList<String>();
        msgs.add("Donnez-moi une suite au Ritz, je n'en veux pas des bijoux de chez Chanel, je n'en veux pas, Donnez-moi une limousine, j'en ferais quoi ? Offrez-moi du personnel, j'en ferais quoi ? un manoir à Neuchâtel, ce n'est pas pour moi, Offrez-moi la tour Eiffel, j'en ferais quoi ?");
        msgs.add("Refrain Je veux d'l'amour, d'la joie, de la bonne humeur, ce n'est pas votre argent qui fera mon bonheur moi, j'veux crever la main sur le cœur. Allons ensemble découvrir ma liberté, oubliez donc tous vos clichés, bienvenue dans ma réalité.");
        msgs.add("J'en ai marre de vos bonnes manières, c'est trop pour moi. moi, je mange avec les mains ");
        msgs.add("et j'suis comme ça j'parle fort et je suis franche, excusez-moi. Fini l'hypocrisie, moi, ");
        msgs.add("j'suis comme jnjnjnjnjnjnjnj j jnjnjnjn jnjnjn ça !");
        msgs.add("Refrain Je veux d'l'amour, d'la joie, de la bonne humeur, ce n'est pas votre argent qui fera mon bonheur moi, j'veux crever la main sur le cœur. Allons ensemble découvrir ma liberté, oubliez donc tous vos clichés, bienvenue dans ma réalité.");
        msgs.add("j'me casse de là j'en ai marre des langues de bois regardez moi, toute manière J'vous en ");

        if (msgs.size() > 0) {
            lst_msg.setAdapter(new GroupChatLvAdapter(EducatorGroupMsgActivity.this, msgs));
        }

        //Action bar
        ActionBar ab = getActionBar();
        if (ab != null) {
            // no title (to make more room for navigation and actions)
            // unless Nav Drawer opens
            //ab.setTitle("#rd B : Math1");
            ab.setDisplayShowTitleEnabled(true);

            View imgContainer = LayoutInflater.from(getActionBar().getThemedContext())
                    .inflate(R.layout.actionbar_image_title, null);
            ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            //Add teacher image & spinner in the Action bar
            ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
            ab.setCustomView(imgContainer, lp);

            TextView tvClass = (TextView) imgContainer.findViewById(R.id.actionbar_class_subj);
            tvClass.setText("3rd : Math1");
        /*ImageView ivTeacher = (ImageView)ab.getCustomView().findViewById(R.id.actionbar_edu_img);*/
            ImageView ivTeacher = (ImageView) imgContainer.findViewById(R.id.actionbar_edu_img);
            showActionBarTeacherImage(ivTeacher);
        }
    }

    private void showActionBarTeacherImage(ImageView ivTeacher) {
        //Log.d(TAG, "teacherId: " + SharedPrefUtil.getPrefTeacherUserId(EducatorGroupMsgActivity.this));
        //Get image path of the Logged-in user(teacher)
        EducatorDAO daoEdu = new EducatorDAO(EducatorGroupMsgActivity.this);
        /*Educator teacher = daoEdu.getEducator(SharedPrefUtil.getPrefTeacherUserId(EducatorGroupMsgActivity.this));
        if (teacher != null) {
            Picasso.with(EducatorGroupMsgActivity.this)
                    .load(teacher.getImagePath()) //
                    .placeholder(CommonConstants.LOADING) //
                    .error(CommonConstants.NO_PHOTO_AVAILABLE) //
                    .fit() //
                    .into(ivTeacher);
        } else {
            ivTeacher.setImageResource(R.drawable.nophotoavailable);
            Log.w(TAG, "Couldn't load image because no Educator found.");
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.educator_group_msg, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
