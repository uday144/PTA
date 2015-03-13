package com.ptapp.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ptapp.bean.ParentBean;
import com.ptapp.io.model.Student;
import com.ptapp.dao.StudentDAO;
import com.ptapp.app.R;

import java.util.LinkedHashMap;

import static com.ptapp.utils.LogUtils.makeLogTag;

/**
 * Created by lifestyle on 05-11-14.
 */
public class EducatorCourseMessagesFragment extends Fragment {

    private static final String TAG = makeLogTag(EducatorCourseMessagesFragment.class);

    //class in which this course is
    private String classId = "";

    ListView lv1;

    public EducatorCourseMessagesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);    //necessary to show action menu from fragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_educator_course_messages, container, false);
        lv1 = (ListView) rootView.findViewById(R.id.list_students);
        TextView tv_Empty = (TextView) rootView.findViewById(R.id.tv_empty);

        classId = ((EducatorCourseActivity) getActivity()).classId;
        if (!classId.isEmpty()) {
            // Get class's students along with their parents
            //ArrayList<ParentBean> lstPar = new ArrayList<ParentBean>();
            //LinkeedHashMap retains the iteration order(in which items are put into it).
            LinkedHashMap<ParentBean, Student> hashPar = new LinkedHashMap<ParentBean, Student>();
            StudentDAO dcStu = new StudentDAO(getActivity());
            /*ArrayList<StudentBean> lstStu = dcStu.getStudentsOfClass(classId);

            if (lstStu.size() > 0) {
                for (StudentBean stu : lstStu) {
                    //find it's parents

                    ParentDAO dcPar = new ParentDAO((getActivity()));
                    ParentBean par1 = dcPar.getParent(stu.getParent1Id());
                    if (par1 != null) {
                        hashPar.put(par1, stu);
                        //lstPar.add(par1);
                    }
                    ParentBean par2 = dcPar.getParent(stu.getParent2Id());
                    if (par2 != null) {
                        hashPar.put(par2, stu);
                        //lstPar.add(par2);
                    }
                    //ParentBean gau = dcPar.getParent(stu.getGuardian_Id());


                }

                lv1.setAdapter(new EducatorCourseMsgsLvAdapter(getActivity(), hashPar));
                if (tv_Empty != null) {
                    tv_Empty.setText(null); // Set "no events found" text null.
                    tv_Empty.setVisibility(View.GONE);
                }
            } else {
                lv1.setAdapter(null);
                if (tv_Empty != null) {
                    tv_Empty.setText("nothing_found");
                    tv_Empty.setVisibility(View.VISIBLE);
                }
                Log.d(TAG, "Nothing found. students list= " + lstStu);
            }*/
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.educator_course_messages, menu);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.action_compose:
                Intent intent = new Intent(getActivity(),EducatorComposeMessage.class);
                intent.putExtra("classId",classId);
                getActivity().startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
