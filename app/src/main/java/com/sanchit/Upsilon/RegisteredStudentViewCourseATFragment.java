package com.sanchit.Upsilon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sanchit.Upsilon.advancedTestData.TestListItem;
import com.sanchit.Upsilon.advancedTestData.TestStatus;
import com.sanchit.Upsilon.advancedTestData.TestsListAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisteredStudentViewCourseATFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisteredStudentViewCourseATFragment extends Fragment {
    private static final String TAG = "StudentAssignmentTests";

    private RecyclerView rvTests;
    private TestsListAdapter adapterTests;
    private RecyclerView rvAssignments; // non existent at the moment
    private ArrayList<TestListItem> testListItems;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisteredStudentViewCourseATFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisteredStudentViewCourseATFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisteredStudentViewCourseATFragment newInstance(String param1, String param2) {
        RegisteredStudentViewCourseATFragment fragment = new RegisteredStudentViewCourseATFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        testListItems = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_active_course_assignments_tests, container, false);
        rvTests = view.findViewById(R.id.list_tests);
        // get test list
        getTestList();
        Log.d(TAG, "onCreateView: list size:" + testListItems.size());
        adapterTests = new TestsListAdapter(testListItems, getContext());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext(), RecyclerView.VERTICAL, false);
        rvTests.setAdapter(adapterTests);
        rvTests.setLayoutManager(manager);
        rvTests.setItemAnimator(new DefaultItemAnimator());
        Log.d(TAG, "onCreateView: recycler displayed: " + rvTests.getChildCount() + " items");
//        adapterTests.notifyDataSetChanged();

//        rvTests.setAdapter(adapterTests);
        return view;
    }

    public void getTestList() {
        /* DEMO CODE START */
        TestListItem item = new TestListItem();
        item.setId("i");
        item.setName("Test " + 0);
        item.setTotalMarks(20);
        item.setStatus(TestStatus.GRADED);
        item.setMarksReceived(12);
        testListItems.add(item);
        item = new TestListItem();
        item.setId("ii");
        item.setName("Test " + 1);
        item.setTotalMarks(20);
        item.setStatus(TestStatus.GRADED);
        item.setMarksReceived(1 + 12);
        testListItems.add(item);
        item = new TestListItem();
        item.setId("iii");
        item.setName("Test " + 2);
        item.setTotalMarks(20);
        item.setStatus(TestStatus.COMPLETED_NOT_GRADED);
        testListItems.add(item);
        item = new TestListItem();
        item.setId("iv");
        item.setName("Test " + 3);
        item.setTotalMarks(20);
        item.setStatus(TestStatus.ONGOING);
        testListItems.add(item);
        item = new TestListItem();
        item.setId("v");
        item.setName("Test " + 4);
        item.setTotalMarks(20);
        item.setStatus(TestStatus.YET_TO_START);
        testListItems.add(item);
        /* DEMO CODE ENDS */
    }
}