package test.jsp.org.testgenerator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jsp.generator.Field;
import org.jsp.generator.FieldTypes;
import org.jsp.generator.FindViewById;

@Field(layout = {"activity_main", "activity_main"}, fieldType = FieldTypes.Protected)
public class MainActivity extends AppCompatActivity {


	/*Declaration*/
	protected RelativeLayout rlMain;
	protected TextView tvTime;
    /*EndDeclaration*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @FindViewById
    private void init(View view) {


		/*Initialization*/
		rlMain = (RelativeLayout) findViewById(R.id.rlMain);
		tvTime = (TextView) findViewById(R.id.tvTime);
        /*EndInitialization*/
    }
}
