package test.jsp.org.testgenerator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsp.generator.Field;
import org.jsp.generator.FieldTypes;
import org.jsp.generator.FindViewById;

@Field(layout = "fragment_my", fieldType = FieldTypes.Public)
public class MyFragment extends Fragment {


	/*Declaration*/
	public TextView tvTest;
    /*EndDeclaration*/

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        init(view);
        return view;
    }

    @FindViewById
    private void init(View view) {


		/*Initialization*/
		tvTest = (TextView) view.findViewById(R.id.tvTest);
        /*EndInitialization*/

    }

}
